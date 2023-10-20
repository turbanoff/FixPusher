/**
 * Copyright (C) 2012 Alexander Pinnow
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Library General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Library General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Library General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 * 
 **/
package net.sourceforge.fixpusher.model.log;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.geom.Rectangle2D;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import net.sourceforge.fixpusher.model.FIXMessageFilterListener;
import net.sourceforge.fixpusher.model.FIXProperties;
import net.sourceforge.fixpusher.model.FIXPropertyListener;
import net.sourceforge.fixpusher.model.message.AbstractFIXElement;
import net.sourceforge.fixpusher.model.message.FIXComponent;
import net.sourceforge.fixpusher.model.message.FIXField;
import net.sourceforge.fixpusher.model.message.FIXGroup;
import net.sourceforge.fixpusher.model.message.FIXMessage;
import net.sourceforge.fixpusher.view.dialog.ExceptionDialog;
import net.sourceforge.fixpusher.view.log.MessageText;
import net.sourceforge.fixpusher.view.message.FIXMainMessagePanel;
import quickfix.FieldMap;
import quickfix.FieldNotFound;
import quickfix.Group;
import quickfix.Message;

/**
 * The Class LogTableModel.
 */
public class LogTableModel extends AbstractTableModel implements FIXMessageFilterListener, FIXPropertyListener {

	private static final long serialVersionUID = 1L;

	private final List<Message> filteredMessages = new ArrayList<Message>();

	private FIXMainMessagePanel fixMainMessagePanel = null;

	private FIXProperties fixProperties = null;

	private final Font font = new Font("Dialog", Font.PLAIN, 12);;

	private String longestMessage = new String("Detail");

	private final List<Message> messages = new ArrayList<Message>();

	private int minWidth = 0;

	private int mouseOverRow = -1;

	private JTable table = null;

	/**
	 * Instantiates a new log table model.
	 * 
	 * @param fixProperties
	 *            the fix properties
	 */
	public LogTableModel(final FIXProperties fixProperties) {

		super();

		this.fixProperties = fixProperties;

		fixProperties.getFixMessageFilter().addFIXMessageFilterListener(this);
		fixProperties.addFIXPropertyListener(this);
		fixProperties.init();
	}

	private void addFilteredMessage(final Message message) {

		try {

			if (fixProperties.getFixMessageFilter().isHideReceived() && message.getHeader().getString(49).equals(fixProperties.getTargetCompID()))
				return;

			if (fixProperties.getFixMessageFilter().isHideSent() && message.getHeader().getString(49).equals(fixProperties.getSenderCompID()))
				return;

			if (fixProperties.getFixMessageFilter().isHideHeartbeats() && message.getHeader().getString(35).equals("0"))
				return;
		}
		catch (final FieldNotFound e) {

			ExceptionDialog.showException(e);
		}

		filteredMessages.add(0, message);

		if (message.toString().length() > longestMessage.length()) {

			longestMessage = message.toString();

			if (table != null)
				setTableWidth();
		}

		if (fixMainMessagePanel == null)
			return;

		if (fixMainMessagePanel.getMessage() == message)
			fixMainMessagePanel.updateFields();
	}

	/**
	 * Adds the message.
	 * 
	 * @param message
	 *            the message
	 */
	public void addMessage(final Message message) {

		if (messages.size() > 30000)
			messages.remove(messages.size() - 1);

		messages.add(0, message);

		addFilteredMessage(message);
		fireTableDataChanged();
	}

	/**
	 * Adopt values.
	 */
	public void adoptValues() {

		if (fixMainMessagePanel == null)
			return;

		final Message message = fixMainMessagePanel.getMessage();
		final FIXMessage fixMessage = fixMainMessagePanel.getFIXMessage();
		final Message message2 = filteredMessages.get(mouseOverRow);

		for (final AbstractFIXElement abstractFIXElement : fixMessage.getAbstractFIXElements())
			copy(abstractFIXElement, message2, message);

		fixMainMessagePanel.updateFields();
	}

	/**
	 * Clear.
	 */
	public void clear() {

		filteredMessages.clear();
		messages.clear();

		fireTableDataChanged();
	}

	private void copy(final AbstractFIXElement abstractFIXElement, final FieldMap source, final FieldMap destination) {

		if (abstractFIXElement instanceof FIXField) {

			if (source.isSetField(abstractFIXElement.getNumber()))
				try {

					destination.setString(abstractFIXElement.getNumber(), source.getString(abstractFIXElement.getNumber()));
				}
				catch (final FieldNotFound e) {

					ExceptionDialog.showException(e);
				}
		}

		else if (abstractFIXElement instanceof FIXComponent)
			for (final AbstractFIXElement abstractFIXElement2 : abstractFIXElement.getAbstractFIXElements())
				copy(abstractFIXElement2, source, destination);

		else if (abstractFIXElement instanceof FIXGroup)
			if (source.hasGroup(abstractFIXElement.getNumber())) {

				final int j = source.getGroupCount(abstractFIXElement.getNumber());

				Exception exception = null;

				for (int k = 1; k <= j; k++)
					try {

						final Group sourceGroup = source.getGroup(k, abstractFIXElement.getNumber());
						final Group destinationGroup = new Group(abstractFIXElement.getNumber(), abstractFIXElement.getAbstractFIXElements().get(0).getNumber(), ((FIXGroup)abstractFIXElement).getFieldOrder());

						for (final AbstractFIXElement abstractFIXElement2 : abstractFIXElement.getAbstractFIXElements())
							copy(abstractFIXElement2, sourceGroup, destinationGroup);

						destination.addGroup(destinationGroup);
					}
					catch (final FieldNotFound e) {

						exception = e;
					}

				if (exception != null)
					ExceptionDialog.showException(exception);
			}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sourceforge.fixpusher.model.FIXMessageFilterListener#
	 * fixMessageFilterChanged()
	 */
	@Override
	public void fixMessageFilterChanged() {

		resetFilter();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.sourceforge.fixpusher.model.FIXPropertyListener#fixPropertyChanged
	 * (java.util.List)
	 */
	@Override
	public void fixPropertyChanged(final List<Message> messages) {

		clear();

		for (final Message message : messages) {

			this.messages.add(0, message);
			addFilteredMessage(message);
		}

		fireTableDataChanged();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	@Override
	public int getColumnCount() {

		return 4;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.AbstractTableModel#getColumnName(int)
	 */
	@Override
	public String getColumnName(final int column) {

		switch (column) {

			case 0:
				return "Time";
			case 1:
				return "Direction";
			case 2:
				return "FIX Message";
			default:
				return "Detail";
		}
	}

	/**
	 * Gets the fIX main message panel.
	 * 
	 * @return the fIX main message panel
	 */
	public FIXMainMessagePanel getFIXMainMessagePanel() {

		return fixMainMessagePanel;
	}

	/**
	 * Gets the message.
	 * 
	 * @param row
	 *            the row
	 * @return the message
	 */
	public Message getMessage(final int row) {

		return filteredMessages.get(row);
	}

	/**
	 * Gets the mouse over row.
	 * 
	 * @return the mouse over row
	 */
	public int getMouseOverRow() {

		return mouseOverRow;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	@Override
	public int getRowCount() {

		return filteredMessages.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	@Override
	public Object getValueAt(final int rowIndex, final int columnIndex) {

		final Message message = filteredMessages.get(rowIndex);
		switch (columnIndex) {
			
			case 0:
				try {
					
					return DateFormat.getTimeInstance().format(message.getHeader().getUtcTimeStamp(52)) + " "
							+ DateFormat.getDateInstance(DateFormat.SHORT).format(message.getHeader().getUtcTimeStamp(52));
				}
				catch (final FieldNotFound e) {
					
					return "?";
				}
				
			case 1:
				try {
					
					if (message.getHeader().getString(49).equals(fixProperties.getSenderCompID()))
						return "Sent";
					
					if (message.getHeader().getString(49).equals(fixProperties.getTargetCompID()))
						return "Received";
					
					return "?";
				}
				catch (final FieldNotFound e) {
					
					return "?";
				}
				
			case 2:
				try {
					
					return fixProperties.getDictionaryParser().getMessageName(message.getHeader().getString(35));
				}
				catch (final Exception e) {
					
					return "?";
				}
				
			case 3:
				try {
					
					final MessageText messageText = new MessageText(fixProperties.getDictionaryParser().getFIXMessage(message.getHeader().getString(35)),
							message);
					
					return messageText;
				}
				catch (final FieldNotFound e) {
					
					return "?";
				}
				
			default:
				return "";
		}
	}

	/**
	 * Reset filter.
	 */
	public void resetFilter() {

		filteredMessages.clear();
		
		for (final Message message : messages)
			addFilteredMessage(message);
		
		Collections.reverse(filteredMessages);
		fireTableDataChanged();
	}

	/**
	 * Sets the fIX main message panel.
	 * 
	 * @param component
	 *            the new fIX main message panel
	 */
	public void setFIXMainMessagePanel(final FIXMainMessagePanel component) {

		fixMainMessagePanel = component;

	}

	/**
	 * Sets the min width.
	 * 
	 * @param minWidth
	 *            the new min width
	 */
	public void setMinWidth(final int minWidth) {

		this.minWidth = minWidth;
		setTableWidth();
	}

	/**
	 * Sets the mouse over row.
	 * 
	 * @param y
	 *            the new mouse over row
	 */
	public void setMouseOverRow(final int y) {

		mouseOverRow = y;
	}

	/**
	 * Sets the table.
	 * 
	 * @param table
	 *            the new table
	 */
	public void setTable(final JTable table) {

		this.table = table;
		setTableWidth();
	}

	/**
	 * Sets the table width.
	 */
	public void setTableWidth() {

		final FontMetrics fontMetrics = table.getFontMetrics(font);
		final Rectangle2D r = fontMetrics.getStringBounds(longestMessage, table.getGraphics());

		int width = (int) r.getWidth() + 10 + longestMessage.split(Character.toString((char) 1)).length*10;

		if (width < minWidth)
			width = minWidth;

		table.getColumnModel().getColumn(3).setPreferredWidth(width);
	}

}
