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

import java.awt.Font;
import java.util.List;

import net.sourceforge.fixpusher.model.message.FIXFieldValue;

import org.japura.gui.CheckComboBox;

/**
 * The Class MultipleValueStringCombo.
 */
public class MultipleValueStringCombo extends CheckComboBox {

	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new multiple value string combo.
	 */
	public MultipleValueStringCombo() {

		super();
		
		getComboBox().setFont(new Font("Dialog", Font.PLAIN, 12));
	}

	/**
	 * Gets the text.
	 *
	 * @return the text
	 */
	public String getText() {

		final StringBuffer buffer = new StringBuffer();
		
		for (final Object object : getModel().getCheckeds())
			if (object instanceof FIXFieldValue) {
				
				buffer.append(((FIXFieldValue) object).getValue());
				buffer.append(" ");
			}
		
		return buffer.toString().trim();

	}

	/* (non-Javadoc)
	 * @see org.japura.gui.CheckComboBox#updateComboBox()
	 */
	@Override
	protected void updateComboBox() {

		getComboBox().removeAllItems();
		
		final List<Object> checkeds = getModel().getCheckeds();
		
		if (checkeds.size() == 1)
			getComboBox().addItem(((FIXFieldValue) checkeds.get(0)).getValue());
		else
			super.updateComboBox();
	}

}
