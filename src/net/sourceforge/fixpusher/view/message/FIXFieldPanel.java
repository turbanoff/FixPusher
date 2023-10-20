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
package net.sourceforge.fixpusher.view.message;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import net.sourceforge.fixpusher.model.FIXMessageFilter;
import net.sourceforge.fixpusher.model.message.AbstractFIXElement.FieldType;
import net.sourceforge.fixpusher.model.message.FIXField;
import net.sourceforge.fixpusher.model.message.FIXField.Type;
import net.sourceforge.fixpusher.model.message.FIXFieldValue;
import net.sourceforge.fixpusher.view.dialog.ExceptionDialog;
import net.sourceforge.fixpusher.view.document.CharDocument;
import net.sourceforge.fixpusher.view.document.DoubleDocument;
import net.sourceforge.fixpusher.view.document.IntegerDocument;

import org.japura.gui.CheckComboBox.CheckState;
import org.japura.gui.event.ListCheckListener;
import org.japura.gui.event.ListEvent;
import org.japura.gui.renderer.CheckListRenderer;

import quickfix.FieldMap;
import quickfix.FieldNotFound;

import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JTextFieldDateEditor;

/**
 * The Class FIXFieldPanel.
 */
public class FIXFieldPanel extends AbstractFIXPanel {

	private static final long serialVersionUID = 1L;

	private MultipleValueStringCombo checkCombo = null;

	private JComboBox comboBox = null;

	private JDateChooser jDateChooser = null;

	private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");

	private final SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyyMMdd-HH:mm:ss.SSS");

	private final SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat("yyyyMMdd-HH:mm:ss");

	private final SimpleDateFormat simpleDateFormat4 = new SimpleDateFormat("HH:mm:ss.SSS");

	private final SimpleDateFormat simpleDateFormat5 = new SimpleDateFormat("HH:mm:ss");

	private JTextField textField = null;

	/**
	 * Instantiates a new fIX field panel.
	 * 
	 * @param parent
	 *            the parent
	 * @param abstractFIXElement
	 *            the abstract fix element
	 * @param fieldMap
	 *            the field map
	 * @param enabled
	 *            the enabled
	 * @param depth
	 *            the depth
	 * @param fixMessageFilter
	 *            the fix message filter
	 */
	public FIXFieldPanel(final AbstractFIXPanel parent, final FIXField abstractFIXElement, final FieldMap fieldMap, final boolean enabled, final int depth,
			final FIXMessageFilter fixMessageFilter) {

		super(parent, abstractFIXElement, fieldMap, enabled, depth, fixMessageFilter);
		
		dataTypeLabel.setText(abstractFIXElement.getType().toString());

		if (abstractFIXElement.getValueEnum().size() == 0) {

			if (abstractFIXElement.getType() == Type.LOCALMKTDATE || abstractFIXElement.getType() == Type.UTCDATEONLY || abstractFIXElement.getType() == Type.DATE) {

				jDateChooser = new JDateChooser();
				final GridBagConstraints gbc_jDateChooser = new GridBagConstraints();
				gbc_jDateChooser.anchor = GridBagConstraints.WEST;
				gbc_jDateChooser.insets = new Insets(0, 0, 0, 5);
				gbc_jDateChooser.gridx = 0;
				gbc_jDateChooser.gridy = 0;
				jDateChooser.setFont(new Font("Dialog", Font.PLAIN, 12));
				jDateChooser.setDateFormatString("yyyyMMdd");
				jDateChooser.setMinimumSize(new Dimension(300, 25));
				jDateChooser.setPreferredSize(new Dimension(300, 25));
				jDateChooser.setEnabled(enabled);
				lastPanel.add(jDateChooser, gbc_jDateChooser);

				if (enabled)
					jDateChooser.setBackground(getBackground());

				else
					jDateChooser.setBackground(Color.LIGHT_GRAY);

				jDateChooser.getDateEditor().addPropertyChangeListener(new PropertyChangeListener() {

					@Override
					public void propertyChange(final PropertyChangeEvent evt) {

						if (evt.getPropertyName().equals("date")) {

							fieldMap.setString(abstractFIXElement.getNumber(), simpleDateFormat.format((Date) evt.getNewValue()));
							fieldValueChanged();

						}

					}
				});

				if (fieldMap.isSetField(abstractFIXElement.getNumber()))
					try {

						jDateChooser.setDate(simpleDateFormat.parse(fieldMap.getString(abstractFIXElement.getNumber())));

					}
					catch (final Exception e) {

						ExceptionDialog.showException(e);
					}

			}
			else if (abstractFIXElement.getType() == Type.UTCTIMESTAMP || abstractFIXElement.getType() == Type.TZTIMESTAMP) {

				jDateChooser = new JDateChooser();
				jDateChooser.setFont(new Font("Dialog", Font.PLAIN, 12));
				jDateChooser.setDateFormatString("yyyyMMdd-HH:mm:ss.SSS");
				if (abstractFIXElement.getType() == Type.TZTIMESTAMP)
					jDateChooser.setDateFormatString("yyyyMMdd-HH:mm:ss.SSSZ");
				jDateChooser.setMinimumSize(new Dimension(300, 25));
				jDateChooser.setPreferredSize(new Dimension(300, 25));
				jDateChooser.setEnabled(enabled);
				final GridBagConstraints gbc_jDateChooser = new GridBagConstraints();
				gbc_jDateChooser.anchor = GridBagConstraints.WEST;
				gbc_jDateChooser.insets = new Insets(0, 0, 0, 5);
				gbc_jDateChooser.gridx = 0;
				gbc_jDateChooser.gridy = 0;
				lastPanel.add(jDateChooser, gbc_jDateChooser);

				if (enabled)
					jDateChooser.setBackground(getBackground());

				else
					jDateChooser.setBackground(Color.LIGHT_GRAY);

				jDateChooser.getDateEditor().addPropertyChangeListener(new PropertyChangeListener() {

					@Override
					public void propertyChange(final PropertyChangeEvent evt) {

						if (evt.getPropertyName().equals("date")) {

							fieldMap.setString(abstractFIXElement.getNumber(), simpleDateFormat2.format((Date) evt.getNewValue()));
							fieldValueChanged();

						}

					}
				});

				if (fieldMap.isSetField(abstractFIXElement.getNumber()))
					try {
						String value = fieldMap.getString(abstractFIXElement.getNumber());
						if (value.contains("."))
							jDateChooser.setDate(simpleDateFormat2.parse(value));
						else
							jDateChooser.setDate(simpleDateFormat3.parse(value));

					}
					catch (final Exception e) {

						ExceptionDialog.showException(e);
					}

			}
			else if (abstractFIXElement.getType() == Type.UTCTIMEONLY || abstractFIXElement.getType() == Type.TZTIMEONLY) {

				jDateChooser = new JDateChooser();
				jDateChooser.getCalendarButton().setVisible(false);
				jDateChooser.setFont(new Font("Dialog", Font.PLAIN, 12));
				jDateChooser.setDateFormatString("HH:mm:ss.SSS");
				if (abstractFIXElement.getType() == Type.TZTIMEONLY)
					jDateChooser.setDateFormatString("HH:mm:ss.SSSZ");
				jDateChooser.setMinimumSize(new Dimension(300, 25));
				jDateChooser.setPreferredSize(new Dimension(300, 25));
				jDateChooser.setEnabled(enabled);
				final GridBagConstraints gbc_jDateChooser = new GridBagConstraints();
				gbc_jDateChooser.anchor = GridBagConstraints.WEST;
				gbc_jDateChooser.insets = new Insets(0, 0, 0, 5);
				gbc_jDateChooser.gridx = 0;
				gbc_jDateChooser.gridy = 0;
				lastPanel.add(jDateChooser, gbc_jDateChooser);

				if (enabled)
					jDateChooser.setBackground(getBackground());

				else
					jDateChooser.setBackground(Color.LIGHT_GRAY);

				jDateChooser.getDateEditor().addPropertyChangeListener(new PropertyChangeListener() {

					@Override
					public void propertyChange(final PropertyChangeEvent evt) {

						if (evt.getPropertyName().equals("date")) {

							fieldMap.setString(abstractFIXElement.getNumber(), simpleDateFormat4.format((Date) evt.getNewValue()));
							fieldValueChanged();

						}

					}
				});

				if (fieldMap.isSetField(abstractFIXElement.getNumber()))
					try {
						String value = fieldMap.getString(abstractFIXElement.getNumber());
						if (value.contains("."))
							jDateChooser.setDate(simpleDateFormat4.parse(value));
						else
							jDateChooser.setDate(simpleDateFormat5.parse(value));

					}
					catch (final Exception e) {

						ExceptionDialog.showException(e);
					}

			}
			else if (abstractFIXElement.getType() == Type.BOOLEAN) {

				comboBox = new JComboBox();
				comboBox.setBackground(Color.WHITE);
				comboBox.setFont(new Font("Dialog", Font.PLAIN, 12));
				comboBox.setMinimumSize(new Dimension(300, 25));
				comboBox.setPreferredSize(new Dimension(300, 25));
				comboBox.addItem("Not initialized");
				comboBox.addItem(new Boolean(true));
				comboBox.addItem(new Boolean(false));
				comboBox.setEnabled(enabled);
				final GridBagConstraints gbc_comboBox = new GridBagConstraints();
				gbc_comboBox.anchor = GridBagConstraints.WEST;
				gbc_comboBox.insets = new Insets(0, 0, 0, 5);
				gbc_comboBox.gridx = 0;
				gbc_comboBox.gridy = 0;
				lastPanel.add(comboBox, gbc_comboBox);

				comboBox.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(final ActionEvent e) {

						if (!(abstractFIXElement.getFieldType() == FieldType.REQUIRED || abstractFIXElement.getFieldType() == FieldType.CONDITIONALLY)
								&& comboBox.getSelectedIndex() == 0)
							fieldMap.removeField(abstractFIXElement.getNumber());

						else
							fieldMap.setBoolean(abstractFIXElement.getNumber(), (Boolean) comboBox.getSelectedItem());

						fieldValueChanged();
					}
				});

				if (fieldMap.isSetField(abstractFIXElement.getNumber())) {

					int index = 2;

					try {

						if (fieldMap.getBoolean(abstractFIXElement.getNumber()))
							index = 1;

					}
					catch (final FieldNotFound e1) {

						ExceptionDialog.showException(e1);
					}

					comboBox.setSelectedIndex(index);
				}

			}
			else {

				textField = new JTextField();
				textField.setFont(new Font("Dialog", Font.PLAIN, 12));
				textField.setMinimumSize(new Dimension(300, 25));
				textField.setPreferredSize(new Dimension(300, 25));
				textField.setEnabled(enabled);

				switch (abstractFIXElement.getType()) {

					case AMT:
						textField.setDocument(new DoubleDocument());
						break;

					case DAYOFMONTH:
						textField.setDocument(new IntegerDocument());
						break;

					case FLOAT:
						textField.setDocument(new DoubleDocument());
						break;

					case INT:
						textField.setDocument(new IntegerDocument());
						break;

					case LENGTH:
						textField.setDocument(new IntegerDocument());
						break;

					case NUMINGROUP:
						textField.setDocument(new IntegerDocument());
						break;

					case PERCENTAGE:
						textField.setDocument(new DoubleDocument());
						break;

					case PRICE:
						textField.setDocument(new DoubleDocument());
						break;

					case PRICEOFFSET:
						textField.setDocument(new DoubleDocument());
						break;

					case QTY:
						textField.setDocument(new DoubleDocument());
						break;

					case SEQNUM:
						textField.setDocument(new IntegerDocument());
						break;

					case CHAR:
						textField.setDocument(new CharDocument());
						break;
				}

				final GridBagConstraints gbc_textField = new GridBagConstraints();
				gbc_textField.anchor = GridBagConstraints.WEST;
				gbc_textField.insets = new Insets(0, 0, 0, 5);
				gbc_textField.gridx = 0;
				gbc_textField.gridy = 0;
				lastPanel.add(textField, gbc_textField);

				final DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
				decimalFormatSymbols.setDecimalSeparator('.');
				decimalFormatSymbols.setGroupingSeparator(',');

				if (fieldMap.isSetField(abstractFIXElement.getNumber()))
					if (textField.getDocument() instanceof DoubleDocument)
						try {

							final DecimalFormat decimalFormat = new DecimalFormat("##0.0#########");
							decimalFormat.setDecimalFormatSymbols(decimalFormatSymbols);
							textField.setText(decimalFormat.format(fieldMap.getDouble(abstractFIXElement.getNumber())));

						}
						catch (final Exception e1) {

							ExceptionDialog.showException(e1);
						}
					else if (textField.getDocument() instanceof IntegerDocument)
						try {

							final DecimalFormat decimalFormat = new DecimalFormat("##0");
							textField.setText(decimalFormat.format(fieldMap.getInt(abstractFIXElement.getNumber())));

						}
						catch (final Exception e1) {

							ExceptionDialog.showException(e1);
						}
					else if (textField.getDocument() instanceof CharDocument)
						try {

							textField.setText(Character.toString(fieldMap.getChar(abstractFIXElement.getNumber())));

						}
						catch (final Exception e1) {

							ExceptionDialog.showException(e1);
						}
					else
						try {

							textField.setText(fieldMap.getString(abstractFIXElement.getNumber()));

						}
						catch (final Exception e1) {

							ExceptionDialog.showException(e1);
						}

				textField.getDocument().addDocumentListener(new DocumentListener() {

					@Override
					public void changedUpdate(final DocumentEvent e) {

						doTextFieldCheck();
					}

					@Override
					public void insertUpdate(final DocumentEvent e) {

						doTextFieldCheck();

					}

					@Override
					public void removeUpdate(final DocumentEvent e) {

						doTextFieldCheck();

					}
				});
			}
		}
		else if (abstractFIXElement.getType() == Type.MULTIPLEVALUESTRING || abstractFIXElement.getType() == Type.MULTIPLESTRINGVALUE
				|| abstractFIXElement.getType() == Type.MULTIPLECHARVALUE) {

			checkCombo = new MultipleValueStringCombo();
			checkCombo.setBackground(Color.WHITE);
			checkCombo.setFont(new Font("Dialog", Font.PLAIN, 12));
			checkCombo.setMinimumSize(new Dimension(300, 25));
			checkCombo.setPreferredSize(new Dimension(300, 25));
			checkCombo.setTextFor(CheckState.NONE, "Not initialized");
			checkCombo.setEnabled(enabled);
			final GridBagConstraints gbc_checkCombo = new GridBagConstraints();
			gbc_checkCombo.anchor = GridBagConstraints.WEST;
			gbc_checkCombo.insets = new Insets(0, 0, 0, 5);
			gbc_checkCombo.gridx = 0;
			gbc_checkCombo.gridy = 0;

			String[] values = new String[] {};

			if (fieldMap.isSetField(abstractFIXElement.getNumber()))
				try {

					values = fieldMap.getString(abstractFIXElement.getNumber()).split(" ");

				}
				catch (final FieldNotFound e) {

					ExceptionDialog.showException(e);
				}

			for (final FIXFieldValue fixFieldValue : abstractFIXElement.getValueEnum()) {

				checkCombo.getModel().addElement(fixFieldValue);

				for (final String value : values)
					if (fixFieldValue.getValue().equals(value.toString()))
						checkCombo.getModel().addCheck(fixFieldValue);

				checkCombo.setTextFor(CheckState.MULTIPLE, checkCombo.getText());
			}

			checkCombo.getModel().addListCheckListener(new ListCheckListener() {

				@Override
				public void addCheck(final ListEvent event) {

					checkCombo.setTextFor(CheckState.MULTIPLE, checkCombo.getText());
					fieldMap.setString(abstractFIXElement.getNumber(), checkCombo.getText());
					fieldValueChanged();
				}

				@Override
				public void removeCheck(final ListEvent event) {

					checkCombo.setTextFor(CheckState.MULTIPLE, checkCombo.getText());

					if (abstractFIXElement.getFieldType() == FieldType.OPTIONAL && checkCombo.getModel().getCheckeds().size() == 0)
						fieldMap.removeField(abstractFIXElement.getNumber());
					else
						fieldMap.setString(abstractFIXElement.getNumber(), checkCombo.getText());

					fieldValueChanged();
				}
			});

			checkCombo.setRenderer(new CheckListRenderer() {

				private static final long serialVersionUID = 1L;

				@Override
				public Component getListCellRendererComponent(final JList arg0, final Object arg1, final int arg2, final boolean arg3, final boolean arg4) {

					final Component component = super.getListCellRendererComponent(arg0, arg1, arg2, arg3, arg4);
					component.setFont(new Font("Dialog", Font.PLAIN, 12));
					return component;
				}

			});

			lastPanel.add(checkCombo, gbc_checkCombo);
		}
		else {

			comboBox = new JComboBox();
			comboBox.setBackground(Color.WHITE);
			comboBox.setFont(new Font("Dialog", Font.PLAIN, 12));
			comboBox.setMinimumSize(new Dimension(300, 25));
			comboBox.setPreferredSize(new Dimension(300, 25));
			comboBox.addItem("Not initialized");
			comboBox.setEnabled(enabled);
			final GridBagConstraints gbc_comboBox = new GridBagConstraints();
			gbc_comboBox.anchor = GridBagConstraints.WEST;
			gbc_comboBox.insets = new Insets(0, 0, 0, 5);
			gbc_comboBox.gridx = 0;
			gbc_comboBox.gridy = 0;

			String value = null;

			if (fieldMap.isSetField(abstractFIXElement.getNumber()))
				try {

					value = fieldMap.getString(abstractFIXElement.getNumber());

				}
				catch (final FieldNotFound e) {

					ExceptionDialog.showException(e);
				}

			for (final FIXFieldValue fixFieldValue : abstractFIXElement.getValueEnum()) {

				comboBox.addItem(fixFieldValue);

				if (fixFieldValue.getValue().equals(value))
					comboBox.setSelectedItem(fixFieldValue);
			}

			comboBox.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(final ActionEvent e) {

					if (comboBox.getSelectedIndex() == 0)
						fieldMap.removeField(abstractFIXElement.getNumber());

					else
						fieldMap.setString(abstractFIXElement.getNumber(), ((FIXFieldValue) comboBox.getSelectedItem()).getValue());

					fieldValueChanged();
				}
			});

			lastPanel.add(comboBox, gbc_comboBox);
		}

		if (textField != null)
			valueComponent = textField;

		else if (comboBox != null)
			valueComponent = comboBox;

		else if (jDateChooser != null)
			valueComponent = (JTextFieldDateEditor) jDateChooser.getDateEditor();

		else if (checkCombo != null)
			valueComponent = checkCombo;

		fieldValueCheck();
	}

	private void doTextFieldCheck() {

		if (textField.getText() == null || textField.getText().length() == 0)
			fieldMap.removeField(abstractFIXElement.getNumber());

		else if (textField.getDocument() instanceof DoubleDocument)
			fieldMap.setDouble(abstractFIXElement.getNumber(), Double.parseDouble(textField.getText()));

		else if (textField.getDocument() instanceof IntegerDocument)
			fieldMap.setInt(abstractFIXElement.getNumber(), Integer.parseInt(textField.getText()));

		else if (textField.getDocument() instanceof CharDocument)
			fieldMap.setChar(abstractFIXElement.getNumber(), textField.getText().charAt(0));

		else
			fieldMap.setString(abstractFIXElement.getNumber(), textField.getText());

		fieldValueChanged();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.sourceforge.fixpusher.view.message.AbstractFIXPanel#getPanelBackground
	 * ()
	 */
	@Override
	protected Color getPanelBackground() {

		return new Color(255, 243, 204);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Component#toString()
	 */
	@Override
	public String toString() {

		return "Field";
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.fixpusher.view.message.AbstractFIXPanel#dispose()
	 */
	public void dispose() {

		valueComponent=null;
		
		if (jDateChooser != null) {
			lastPanel.remove(jDateChooser);
			jDateChooser.cleanup();
		}
		
		super.dispose();

	}


}
