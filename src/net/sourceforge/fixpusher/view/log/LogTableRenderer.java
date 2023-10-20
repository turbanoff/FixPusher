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

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;

import net.sourceforge.fixpusher.model.log.LogTableModel;

/**
 * The Class LogTableRenderer.
 */
public class LogTableRenderer implements TableCellRenderer {

	private LogTableModel logTableModel = null;

	/**
	 * Instantiates a new log table renderer.
	 *
	 * @param logTableModel the log table model
	 */
	public LogTableRenderer(final LogTableModel logTableModel) {

		super();
		
		this.logTableModel = logTableModel;
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	@Override
	public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected, final boolean hasFocus, final int row,
			final int column) {

		JComponent component = new JLabel(value.toString());
		
		if (value instanceof MessageText)
			component = ((MessageText) value).getText(row);
		
		else {
			
			component.setFont(new Font("Dialog", Font.PLAIN, 12));
			component.setBorder(new EmptyBorder(5, 5, 5, 5));
		}
		
		if (row == logTableModel.getMouseOverRow())
			component.setBackground(new Color(179, 198, 255));
		
		else if (row % 2 == 0)
			component.setBackground(new Color(255, 243, 204));
		
		else
			component.setBackground(new Color(255, 236, 179));
		
		component.setOpaque(true);
		
		return component;
	}

}
