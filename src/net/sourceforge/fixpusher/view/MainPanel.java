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
package net.sourceforge.fixpusher.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import net.sourceforge.fixpusher.control.FIXConnectionListener.Status;
import net.sourceforge.fixpusher.model.FIXMessageFilterListener;
import net.sourceforge.fixpusher.model.FIXProperties;
import net.sourceforge.fixpusher.model.log.LogTableModel;
import net.sourceforge.fixpusher.model.message.FIXMessage;
import net.sourceforge.fixpusher.view.dialog.ExceptionDialog;
import net.sourceforge.fixpusher.view.message.FIXMainMessagePanel;
import quickfix.FieldNotFound;
import quickfix.Message;

/**
 * The Class MainPanel.
 */
public class MainPanel extends JPanel implements TableModelListener, FIXMessageFilterListener {

	private static final long serialVersionUID = 1L;
	
	private AbstractMainPanelContent content = null;
	
	private FIXProperties fixProperties = null;
	
	private final LogTableModel logTableModel;
	
	private final Vector<MainPanelListener> mainPanelListeners = new Vector<MainPanelListener>();

	/**
	 * Instantiates a new main panel.
	 *
	 * @param fixProperties the fix properties
	 * @param logTableModel the log table model
	 */
	public MainPanel(final FIXProperties fixProperties, final LogTableModel logTableModel) {

		super();
		
		setLayout(new BorderLayout(0, 0));
		
		this.fixProperties = fixProperties;
		this.logTableModel = logTableModel;
		
		logTableModel.addTableModelListener(this);
		fixProperties.getFixMessageFilter().addFIXMessageFilterListener(this);
		
		showSettingsPanel(Status.DISCONNECTED);
		
	}

	/**
	 * Adds the main panel listener.
	 *
	 * @param mainPanelListener the main panel listener
	 */
	public void addMainPanelListener(final MainPanelListener mainPanelListener) {

		mainPanelListeners.add(mainPanelListener);
	}

	private boolean discard() {

		if (content != null)
			if (content.isDirty()) {
				final Object[] options = { "Yes", "No" };
				final int n = JOptionPane.showOptionDialog(this, "Discard modified values?", "Discard", JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE,
						new ImageIcon(FIXPusher.class.getResource("/net/sourceforge/fixpusher/view/images/22x22/messagebox_warning.png")), options, options[1]);
				if (n == 1)
					return false;
			}
		return true;
	}

	/**
	 * Fire main panel listener.
	 */
	public void fireMainPanelListener() {

		for (final MainPanelListener mainPanelListener : mainPanelListeners)
			mainPanelListener.onMainPanelChanged();
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.fixpusher.model.FIXMessageFilterListener#fixMessageFilterChanged()
	 */
	@Override
	public void fixMessageFilterChanged() {

		content.fixMessageFilterChanged();

	}

	/**
	 * Gets the content.
	 *
	 * @return the content
	 */
	public Component getContent() {

		return content;
	}

	/**
	 * Gets the fix properties.
	 *
	 * @return the fix properties
	 */
	public FIXProperties getFixProperties() {

		return fixProperties;
	}

	/**
	 * Checks if is consistent.
	 *
	 * @return true, if is consistent
	 */
	public boolean isConsistent() {

		return content.isConsistent();
	}

	/**
	 * Checks if is dirty.
	 *
	 * @return true, if is dirty
	 */
	public boolean isDirty() {

		return content.isDirty();
	}

	/**
	 * Checks if is message.
	 *
	 * @return true, if is message
	 */
	public boolean isMessage() {

		return content.isMessage();
	}

	/**
	 * Removes the main panel listener.
	 *
	 * @param mainPanelListener the main panel listener
	 */
	public void removeMainPanelListener(final MainPanelListener mainPanelListener) {

		mainPanelListeners.remove(mainPanelListener);
	}

	/**
	 * Save.
	 */
	public void save() {

		content.save();
	}

	private void setContent(final AbstractMainPanelContent component) {

		if (this.content != null)
			remove(this.content);
	
		this.content = component;
		
		if (component instanceof FIXMainMessagePanel)
			logTableModel.setFIXMainMessagePanel((FIXMainMessagePanel) component);
		
		else
			logTableModel.setFIXMainMessagePanel(null);
		
		removeAll();
		add(component, BorderLayout.CENTER);
		validate();
		
		if (getParent() != null)
			getParent().validate();
		
		fireMainPanelListener();
	}

	/**
	 * Sets the status.
	 *
	 * @param status the new status
	 */
	public void setStatus(final Status status) {

		content.setStatus(status);

	}

	/**
	 * Sets the status info.
	 *
	 * @param imageIcon the image icon
	 * @param text the text
	 */
	public void setStatusInfo(final ImageIcon imageIcon, final String text) {

		for (final MainPanelListener mainPanelListener : mainPanelListeners)
			mainPanelListener.setStatusInfo(imageIcon, text);

	}

	/**
	 * Show data dictionary panel.
	 *
	 * @param status the status
	 */
	public void showDataDictionaryPanel(final Status status) {

		final Thread thread = new Thread() {

			@Override
			public void run() {

				if (!discard())
					return;
				
				setContent(new DataDictionaryPanel(MainPanel.this, status == Status.DISCONNECTED));
			}
		};

		thread.start();
	}

	/**
	 * Show fix chart panel.
	 */
	public void showFIXChartPanel() {

		final Thread thread = new Thread() {

			@Override
			public void run() {

				if (!discard())
					return;
				
				setContent(new FIXChartPanel(MainPanel.this, logTableModel));
			}
		};

		thread.start();
	}

	/**
	 * Show fix message panel.
	 *
	 * @param message the message
	 * @return true, if successful
	 */
	public boolean showFIXMessagePanel(final Message message) {

		if (!discard() || !content.isConsistent())
			return false;
		
		try {
			
			final FIXMessage fixMessage = fixProperties.getDictionaryParser().getFIXMessage(message.getHeader().getString(35));
			final FIXMainMessagePanel fixMessagePanel = new FIXMainMessagePanel(fixMessage, message, this);
			
			setContent(fixMessagePanel);
			
			return true;
		}
		catch (final FieldNotFound e) {
			
			ExceptionDialog.showException(e);
			
			return false;
		}
	}

	/**
	 * Show settings panel.
	 *
	 * @param status the status
	 */
	public void showSettingsPanel(final Status status) {

		if (!discard())
			return;
		
		final SettingsPanel settingsPanel = new SettingsPanel(this);
		settingsPanel.setStatus(status);
		setContent(settingsPanel);
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.TableModelListener#tableChanged(javax.swing.event.TableModelEvent)
	 */
	@Override
	public void tableChanged(final TableModelEvent e) {

		if (content instanceof FIXChartPanel)
			showFIXChartPanel();

	}

}
