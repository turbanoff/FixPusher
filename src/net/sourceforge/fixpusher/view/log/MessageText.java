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
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import net.sourceforge.fixpusher.model.message.FIXMessage;
import quickfix.Message;

/**
 * The Class MessageText.
 */
public class MessageText {

	private FIXMessage fixMessage = null;

	private Message message = null;

	/**
	 * Instantiates a new message text.
	 *
	 * @param fixMessage the fix message
	 * @param message the message
	 */
	public MessageText(final FIXMessage fixMessage, final Message message) {

		super();
		
		this.fixMessage = fixMessage;
		this.message = message;
	}

	/**
	 * Gets the text.
	 *
	 * @param row the row
	 * @return the text
	 */
	public JPanel getText(final int row) {

		final JPanel textPanel = new JPanel();
		
		final FlowLayout flowLayout = new FlowLayout(SwingConstants.HORIZONTAL);
		flowLayout.setHgap(0);
		flowLayout.setVgap(0);
		textPanel.setLayout(flowLayout);

		final String[] tags = message.toString().split("\u0001");

		final List<String> fieldCheckResult = fixMessage.getFieldCheckResult(message);

		for (final String tag : tags) {
			
			final JLabel label = new JLabel(tag.toString());
			label.setFont(new Font("Dialog", Font.PLAIN, 12));
			label.setBorder(new EmptyBorder(3, 5, 0, 5));
			label.setOpaque(false);
			
			if (fieldCheckResult.contains(tag)) {
				label.setForeground(new Color(255, 0, 0));
				label.setFont(new Font("Dialog", Font.BOLD, 12));
			}
			
			textPanel.add(label);
		}

		return textPanel;
	}

}
