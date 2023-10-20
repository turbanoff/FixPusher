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
package net.sourceforge.fixpusher.view.log;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.TableCellRenderer;

import net.sourceforge.fixpusher.view.GradientLabel;

/**
 * The Class LogTableHeaderRenderer.
 */
public class LogTableHeaderRenderer extends JLabel implements TableCellRenderer {

	private static final long serialVersionUID = 1L;

	/* (non-Javadoc)
	 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	@Override
	public Component getTableCellRendererComponent(final JTable jTable, final Object value, final boolean isSelected, final boolean hasFocus, final int row, final int column) {

		final JLabel label = new GradientLabel(value.toString()) ;
		label.setFont(new Font("Dialog", Font.PLAIN, 12));
		label.setBorder(new CompoundBorder(new MatteBorder(new Insets(0, 0, 0, 1), Color.BLACK), new EmptyBorder(5, 5, 5, 0)));
		label.setOpaque(true);
		label.setForeground(Color.WHITE);
		label.setBackground(Color.GRAY);
		
		return label;
	}

}
