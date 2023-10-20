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
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import net.sourceforge.fixpusher.model.FIXMessageFilter;
import net.sourceforge.fixpusher.model.message.AbstractFIXElement;
import net.sourceforge.fixpusher.model.message.AbstractFIXElement.FieldType;
import net.sourceforge.fixpusher.model.message.FIXComponent;
import net.sourceforge.fixpusher.model.message.FIXField;
import net.sourceforge.fixpusher.model.message.FIXGroup;
import net.sourceforge.fixpusher.view.dialog.ExceptionDialog;
import quickfix.FieldMap;
import quickfix.FieldNotFound;
import quickfix.Group;

/**
 * The Class FIXGroupPanel.
 */
public class FIXGroupPanel extends AbstractFIXPanel {

	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new fIX group panel.
	 *
	 * @param parent the parent
	 * @param fixGroup the abstract fix element
	 * @param fieldMap the field map
	 * @param enabled the enabled
	 * @param depth the depth
	 * @param fixMessageFilter the fix message filter
	 */
	public FIXGroupPanel(final AbstractFIXPanel parent, final FIXGroup fixGroup, final FieldMap fieldMap, final boolean enabled, final int depth,
			final FIXMessageFilter fixMessageFilter) {

		super(parent, fixGroup, fieldMap, enabled, depth, fixMessageFilter);
		
		lastPanel.add(addButton, gbc_addButton);
		lastPanel.add(removeButton, gbc_removeButton);

		addButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {

				childPanel.removeAll();
				final Group group = new Group(fixGroup.getNumber(), fixGroup.getAbstractFIXElements().get(0).getNumber(), fixGroup.getFieldOrder());
				fieldMap.addGroup(group);
				initGroups(enabled);
				fieldValueChanged();
			}
		});

		removeButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {

				childPanel.removeAll();
				final List<Group> groups = fieldMap.getGroups(fixGroup.getNumber());

				if (groups.size() > 0)
					fieldMap.removeGroup(groups.size(), groups.get(groups.size() - 1));
				initGroups(enabled);
				fieldValueChanged();
			}
		});

		if (initGroups(enabled)) {

			topPanel.setBorder(new EmptyBorder(5, 25 * depth, 5, 10));

			if (enabled)
				if (!fixMessageFilter.isHideEmptyFields() && !fixMessageFilter.isHideOptionalFields())
					addButton.setEnabled(true);

			add(leftPanel, BorderLayout.WEST);
			add(childPanel, BorderLayout.CENTER);
			
		}

		fieldValueCheck();

	}

	/* (non-Javadoc)
	 * @see net.sourceforge.fixpusher.view.message.AbstractFIXPanel#getPanelBackground()
	 */
	@Override
	protected Color getPanelBackground() {

		return new Color(204, 241, 255);

	}

	private boolean initGroup(final Group group, final int row, final boolean enabled) {

		final JPanel groupEntriesPanel = new JPanel();
		final GridBagLayout gbl_groupEntryPanel = new GridBagLayout();
		gbl_groupEntryPanel.columnWeights = new double[] { 1.0 };
		groupEntriesPanel.setLayout(gbl_groupEntryPanel);
		groupEntriesPanel.setOpaque(false);

		int i = 0;

		for (final AbstractFIXElement abstractFIXElement2 : abstractFIXElement.getAbstractFIXElements())
			if (!(abstractFIXElement2.getFieldType() == FieldType.OPTIONAL) || !fixMessageFilter.isHideOptionalFields()) {

				if (abstractFIXElement2 instanceof FIXField) {

					final FIXField fixField = (FIXField) abstractFIXElement2;

					if (group.isSetField(fixField.getNumber()) || !fixMessageFilter.isHideEmptyFields()) {

						final FIXFieldPanel fieldPanel = new FIXFieldPanel(this, fixField, group, enabled, depth - 2, fixMessageFilter);
						final GridBagConstraints gbc_fieldPanel = new GridBagConstraints();
						gbc_fieldPanel.anchor = GridBagConstraints.WEST;
						gbc_fieldPanel.fill = GridBagConstraints.HORIZONTAL;
						gbc_fieldPanel.gridx = 0;
						gbc_fieldPanel.gridy = i;
						children.add(fieldPanel);
						groupEntriesPanel.add(fieldPanel, gbc_fieldPanel);

						i++;
					}
				}

				else if (abstractFIXElement2 instanceof FIXComponent) {

					final FIXComponent fixComponent = (FIXComponent) abstractFIXElement2;

					final boolean atLeastOneSet = fixComponent.atLeastOneSet(group);

					if (atLeastOneSet || !fixMessageFilter.isHideEmptyFields()) {

						final FIXComponentPanel componentPanel = new FIXComponentPanel(this, fixComponent, group, enabled, depth - 2, fixMessageFilter);
						final GridBagConstraints gbc_componentPanel = new GridBagConstraints();
						gbc_componentPanel.anchor = GridBagConstraints.WEST;
						gbc_componentPanel.fill = GridBagConstraints.HORIZONTAL;
						gbc_componentPanel.gridx = 0;
						gbc_componentPanel.gridy = i;
						children.add(componentPanel);
						groupEntriesPanel.add(componentPanel, gbc_componentPanel);

						i++;
					}
				}

				else if (abstractFIXElement2 instanceof FIXGroup) {

					final FIXGroup abstractFIXElement = (FIXGroup) abstractFIXElement2;

					if (group.hasGroup(abstractFIXElement.getNumber()) && group.getGroupCount(abstractFIXElement.getNumber()) > 0
							|| !fixMessageFilter.isHideEmptyFields()) {

						final FIXGroupPanel groupPanel = new FIXGroupPanel(this, abstractFIXElement, group, enabled, depth - 2, fixMessageFilter);
						final GridBagConstraints gbc_groupPanel = new GridBagConstraints();
						gbc_groupPanel.anchor = GridBagConstraints.WEST;
						gbc_groupPanel.fill = GridBagConstraints.HORIZONTAL;
						gbc_groupPanel.gridx = 0;
						gbc_groupPanel.gridy = i;
						children.add(groupPanel);
						groupEntriesPanel.add(groupPanel, gbc_groupPanel);

						i++;
					}
				}

			}

		if (i > 0) {

			final JPanel groupPanel = new JPanel();
			groupPanel.setOpaque(!enabled);
			groupPanel.setBackground(Color.LIGHT_GRAY);
			groupPanel.setLayout(new BorderLayout(0, 0));
			final GridBagConstraints gbc_groupPanel = new GridBagConstraints();
			gbc_groupPanel.anchor = GridBagConstraints.WEST;
			gbc_groupPanel.fill = GridBagConstraints.HORIZONTAL;
			gbc_groupPanel.gridx = 0;
			gbc_groupPanel.gridy = row;
			childPanel.add(groupPanel, gbc_groupPanel);

			final JPanel groupEntriesNorthPanel = new JPanel();
			groupEntriesNorthPanel.setOpaque(false);
			groupEntriesNorthPanel.setLayout(new BorderLayout(0, 0));
			groupEntriesNorthPanel.add(groupEntriesPanel, BorderLayout.NORTH);
			groupPanel.add(groupEntriesNorthPanel, BorderLayout.CENTER);

			final JPanel groupEntrySeparatorPanel = new JPanel();
			groupEntrySeparatorPanel.setBorder(new MatteBorder(0, 0, 1, 1, new Color(0, 0, 0)));
			groupEntrySeparatorPanel.setOpaque(false);
			groupEntrySeparatorPanel.setMinimumSize(new Dimension(25, 10));
			groupEntrySeparatorPanel.setPreferredSize(new Dimension(25, 10));
			groupPanel.add(groupEntrySeparatorPanel, BorderLayout.WEST);

			return true;
		}
		return false;
	}

	private boolean initGroups(final boolean enabled) {
		
		if ((!fieldMap.hasGroup(abstractFIXElement.getNumber()) || fieldMap.getGroupCount(abstractFIXElement.getNumber()) == 0) && enabled
				&& !(abstractFIXElement.getFieldType() == FieldType.OPTIONAL)) {

			final Group group = new Group(abstractFIXElement.getNumber(), abstractFIXElement.getAbstractFIXElements().get(0).getNumber(), ((FIXGroup)abstractFIXElement).getFieldOrder());
			fieldMap.addGroup(group);
		}
		if (!fieldMap.hasGroup(abstractFIXElement.getNumber()) || fieldMap.getGroupCount(abstractFIXElement.getNumber()) == 0) {

			removeButton.setEnabled(false);
			final Group group2 = new Group(abstractFIXElement.getNumber(), abstractFIXElement.getAbstractFIXElements().get(0).getNumber(), ((FIXGroup)abstractFIXElement).getFieldOrder());

			return initGroup(group2, 0, false);

		}
		else {

			if (!fixMessageFilter.isHideEmptyFields() && !fixMessageFilter.isHideOptionalFields())
				removeButton.setEnabled(true);

			int i = 0;
			final int j = fieldMap.getGroupCount(abstractFIXElement.getNumber());

			if (j == 1 && !(abstractFIXElement.getFieldType() == FieldType.OPTIONAL))
				removeButton.setEnabled(false);

			for (int k = 1; k <= j; k++)
				try {
					final Group group = fieldMap.getGroup(k, abstractFIXElement.getNumber());
					if (initGroup(group, i, true))
						i++;
				}
				catch (final FieldNotFound e) {
					
					ExceptionDialog.showException(e);
				}
			return i > 0;
		}

	}

	/* (non-Javadoc)
	 * @see java.awt.Component#toString()
	 */
	@Override
	public String toString() {

		return "Group";
	}

}
