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
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import net.sourceforge.fixpusher.model.FIXMessageFilter;
import net.sourceforge.fixpusher.model.message.FIXField;
import net.sourceforge.fixpusher.view.FIXPusher;
import quickfix.FieldMap;

/**
 * The Class FIXUnknownFieldPanel.
 */
public class FIXUnknownFieldPanel extends AbstractFIXPanel {

	private static final long serialVersionUID = 1L;

	private JLabel valueLabel = null;

	/**
	 * Instantiates a new fIX unknown field panel.
	 *
	 * @param parent the parent
	 * @param value the value
	 * @param abstractFIXElement the abstract fix element
	 * @param fieldMap the field map
	 * @param enabled the enabled
	 * @param depth the depth
	 * @param fixMessageFilter the fix message filter
	 */
	public FIXUnknownFieldPanel(final AbstractFIXPanel parent, final String value, final FIXField abstractFIXElement, final FieldMap fieldMap,
			final boolean enabled, final int depth, final FIXMessageFilter fixMessageFilter) {

		super(parent, abstractFIXElement, fieldMap, enabled, depth, fixMessageFilter);

		dataTypeLabel.setText("");
		requiredLabel.setText("Unknown");

		warningIconLabel.setIcon(new ImageIcon(FIXPusher.class.getResource("/net/sourceforge/fixpusher/view/images/16x16/status_unknown.png")));
		warningIconLabel.setToolTipText(abstractFIXElement.getName() + " field is not defined in the data dictionary.");

		valueLabel = new JLabel(value);
		valueLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
		valueLabel.setMinimumSize(new Dimension(300, 25));
		valueLabel.setPreferredSize(new Dimension(300, 25));
		valueLabel.setEnabled(enabled);
		final GridBagConstraints gbc_valueLabel = new GridBagConstraints();
		gbc_valueLabel.anchor = GridBagConstraints.WEST;
		gbc_valueLabel.insets = new Insets(0, 0, 0, 5);
		gbc_valueLabel.gridx = 0;
		gbc_valueLabel.gridy = 0;
		lastPanel.add(valueLabel, gbc_valueLabel);

	}

	/* (non-Javadoc)
	 * @see net.sourceforge.fixpusher.view.message.AbstractFIXPanel#getPanelBackground()
	 */
	@Override
	protected Color getPanelBackground() {

		return new Color(255, 198, 179);
	}

	/* (non-Javadoc)
	 * @see java.awt.Component#toString()
	 */
	@Override
	public String toString() {

		return "Field";
	}

}
