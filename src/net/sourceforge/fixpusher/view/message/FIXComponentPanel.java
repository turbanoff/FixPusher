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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.border.EmptyBorder;

import net.sourceforge.fixpusher.model.FIXMessageFilter;
import net.sourceforge.fixpusher.model.message.AbstractFIXElement;
import net.sourceforge.fixpusher.model.message.AbstractFIXElement.FieldType;
import net.sourceforge.fixpusher.model.message.FIXComponent;
import net.sourceforge.fixpusher.model.message.FIXField;
import net.sourceforge.fixpusher.model.message.FIXGroup;
import quickfix.FieldMap;
import quickfix.Message.Header;
import quickfix.Message.Trailer;

/**
 * The Class FIXComponentPanel.
 */
public class FIXComponentPanel extends AbstractFIXPanel {

	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new fIX component panel.
	 *
	 * @param parent the parent
	 * @param abstractFIXElement the abstract fix element
	 * @param fieldMap the field map
	 * @param enabled the enabled
	 * @param depth the depth
	 * @param fixMessageFilter the fix message filter
	 */
	public FIXComponentPanel(final AbstractFIXPanel parent, final FIXComponent abstractFIXElement, final FieldMap fieldMap, final boolean enabled,
			final int depth, final FIXMessageFilter fixMessageFilter) {

		super(parent, abstractFIXElement, fieldMap, enabled, depth, fixMessageFilter);

		numberLabel.setText("");

		if (abstractFIXElement.getFieldType() == FieldType.OPTIONAL) {

			lastPanel.add(addButton, gbc_addButton);
			lastPanel.add(removeButton, gbc_removeButton);

			addButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(final ActionEvent e) {

					childPanel.removeAll();
					addButton.setEnabled(false);
					removeButton.setEnabled(true);
					
					initComponent(fieldMap, true);
					fieldValueChanged();
				}
			});

			removeButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(final ActionEvent e) {

					childPanel.removeAll();
					addButton.setEnabled(true && !fixMessageFilter.isHideEmptyFields());
					removeButton.setEnabled(false);
					
					abstractFIXElement.removeAll(fieldMap);
					initComponent(fieldMap, false);
					fieldValueChanged();
				}
			});

			final boolean atLeastOneSet = abstractFIXElement.atLeastOneSet(fieldMap);

			if (!fixMessageFilter.isHideEmptyFields() && !fixMessageFilter.isHideOptionalFields() && atLeastOneSet)
				removeButton.setEnabled(true);
			
			else
				removeButton.setEnabled(false);
			
			if (!atLeastOneSet) {
				
				addButton.setEnabled(true && enabled && !fixMessageFilter.isHideEmptyFields());
				initComponent(fieldMap, false);
			}
			else {
				
				addButton.setEnabled(false);
				initComponent(fieldMap, true);
			}

		}
		else
			initComponent(fieldMap, true && enabled);

		fieldValueCheck();

	}

	/* (non-Javadoc)
	 * @see net.sourceforge.fixpusher.view.message.AbstractFIXPanel#getPanelBackground()
	 */
	@Override
	protected Color getPanelBackground() {

		return new Color(255, 229, 143);

	}

	private void initComponent(final FieldMap fieldMap, final boolean enabled) {

		int i = 0;

		for (final AbstractFIXElement abstractFIXElement2 : abstractFIXElement.getAbstractFIXElements())
			if (abstractFIXElement2.getFieldType() == FieldType.REQUIRED || abstractFIXElement2.getFieldType() == FieldType.CONDITIONALLY
					|| !fixMessageFilter.isHideOptionalFields()) {

				if (abstractFIXElement2 instanceof FIXField) {

					final FIXField fixField = (FIXField) abstractFIXElement2;

					if (fieldMap.isSetField(fixField.getNumber()) || !fixMessageFilter.isHideEmptyFields()) {

						final FIXFieldPanel panel_3 = new FIXFieldPanel(this, fixField, fieldMap, enabled, depth - 1, fixMessageFilter);
						final GridBagConstraints gbc_panel_3 = new GridBagConstraints();
						gbc_panel_3.anchor = GridBagConstraints.WEST;
						gbc_panel_3.fill = GridBagConstraints.HORIZONTAL;
						gbc_panel_3.gridx = 0;
						gbc_panel_3.gridy = i;
						i++;
						children.add(panel_3);
						childPanel.add(panel_3, gbc_panel_3);

					}
				}

				else if (abstractFIXElement2 instanceof FIXComponent) {

					final FIXComponent fixComponent2 = (FIXComponent) abstractFIXElement2;
					final boolean atLeastOneSet = fixComponent2.atLeastOneSet(fieldMap);

					if (atLeastOneSet || !fixMessageFilter.isHideEmptyFields()) {

						final FIXComponentPanel panel_3 = new FIXComponentPanel(this, fixComponent2, fieldMap, enabled, depth - 1, fixMessageFilter);
						final GridBagConstraints gbc_panel_3 = new GridBagConstraints();
						gbc_panel_3.anchor = GridBagConstraints.WEST;
						gbc_panel_3.fill = GridBagConstraints.HORIZONTAL;
						gbc_panel_3.gridx = 0;
						gbc_panel_3.gridy = i;
						i++;
						children.add(panel_3);
						childPanel.add(panel_3, gbc_panel_3);

					}

				}

				else if (abstractFIXElement2 instanceof FIXGroup) {

					final FIXGroup fixGroup = (FIXGroup) abstractFIXElement2;

					if (fieldMap.hasGroup(fixGroup.getNumber()) && fieldMap.getGroupCount(fixGroup.getNumber()) > 0 || !fixMessageFilter.isHideEmptyFields()) {

						final FIXGroupPanel panel_3 = new FIXGroupPanel(this, fixGroup, fieldMap, enabled, depth - 1, fixMessageFilter);
						final GridBagConstraints gbc_panel_3 = new GridBagConstraints();
						gbc_panel_3.anchor = GridBagConstraints.WEST;
						gbc_panel_3.fill = GridBagConstraints.HORIZONTAL;
						gbc_panel_3.gridx = 0;
						gbc_panel_3.gridy = i;
						i++;
						children.add(panel_3);
						childPanel.add(panel_3, gbc_panel_3);

					}
				}

			}

		if (i > 0) {

			topPanel.setBorder(new EmptyBorder(5, 25 * depth, 5, 10));
			add(leftPanel, BorderLayout.WEST);
			add(childPanel, BorderLayout.CENTER);

		}
	}

	/* (non-Javadoc)
	 * @see java.awt.Component#toString()
	 */
	@Override
	public String toString() {

		if (fieldMap instanceof Header || fieldMap instanceof Trailer)
			return "";

		return "Component";
	}

}
