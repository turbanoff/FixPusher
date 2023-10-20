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

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;

import net.sourceforge.fixpusher.control.FIXConnectionListener.Status;

/**
 * The Class StatusPanel.
 */
public class StatusPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private final ImageIcon greenLED = new ImageIcon(FIXPusher.class.getResource("/net/sourceforge/fixpusher/view/images/16x16/greenled.png"));
	private final JLabel infoIconLabel;
	private final JLabel infoTextLabel;
	private final JLabel connectionLabel;
	private final JLabel connectionIconLabel;

	private final ImageIcon redLED = new ImageIcon(FIXPusher.class.getResource("/net/sourceforge/fixpusher/view/images/16x16/redled.png"));

	private final ImageIcon yellowLED = new ImageIcon(FIXPusher.class.getResource("/net/sourceforge/fixpusher/view/images/16x16/yellowled.png"));

	/**
	 * Instantiates a new status panel.
	 */
	public StatusPanel() {

		super();
		
		setPreferredSize(new Dimension(10, 24));
		setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		
		final GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWeights = new double[] { 0.0, 1.0, 0.0, 0.0 };
		gridBagLayout.rowWeights = new double[] { 0.0 };
		setLayout(gridBagLayout);

		infoIconLabel = new JLabel();
		final GridBagConstraints gbc_infoIconLabel = new GridBagConstraints();
		gbc_infoIconLabel.insets = new Insets(2, 2, 2, 5);
		gbc_infoIconLabel.gridx = 0;
		gbc_infoIconLabel.gridy = 0;
		add(infoIconLabel, gbc_infoIconLabel);

		infoTextLabel = new JLabel();
		infoTextLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
		final GridBagConstraints gbc_infoTextLabel = new GridBagConstraints();
		gbc_infoTextLabel.anchor = GridBagConstraints.WEST;
		gbc_infoTextLabel.insets = new Insets(2, 2, 2, 5);
		gbc_infoTextLabel.gridx = 1;
		gbc_infoTextLabel.gridy = 0;
		add(infoTextLabel, gbc_infoTextLabel);

		connectionLabel = new JLabel("Disconnected");
		connectionLabel.setBorder(new EmptyBorder(2, 5, 2, 5));
		connectionLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		connectionLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
		final GridBagConstraints gbc_connectionLabel = new GridBagConstraints();
		gbc_connectionLabel.anchor = GridBagConstraints.EAST;
		gbc_connectionLabel.insets = new Insets(2, 2, 2, 5);
		gbc_connectionLabel.gridx = 2;
		gbc_connectionLabel.gridy = 0;
		add(connectionLabel, gbc_connectionLabel);

		connectionIconLabel = new JLabel();
		connectionIconLabel.setIcon(redLED);
		final GridBagConstraints gbc_connectionIconLabel = new GridBagConstraints();
		gbc_connectionIconLabel.anchor = GridBagConstraints.WEST;
		gbc_connectionIconLabel.fill = GridBagConstraints.VERTICAL;
		gbc_connectionIconLabel.gridx = 3;
		gbc_connectionIconLabel.gridy = 0;
		add(connectionIconLabel, gbc_connectionIconLabel);
		
		setStatusInfo(new ImageIcon(FIXPusher.class.getResource("/net/sourceforge/fixpusher/view/images/16x16/documentinfo.png")),
				"Hit the green button or Ctrl-F6 to start the session.");
	}

	/**
	 * Sets the status.
	 *
	 * @param status the status
	 * @param text the text
	 */
	public void setStatus(final Status status, final String text) {

		connectionLabel.setText(text);
		
		switch (status) {
			
			case CONNECTED:
				connectionIconLabel.setIcon(greenLED);
				break;
				
			case PENDING:
				connectionIconLabel.setIcon(yellowLED);
				break;
				
			case DISCONNECTED:
				connectionIconLabel.setIcon(redLED);
				break;
		}
		
		validate();
	}

	/**
	 * Sets the status info.
	 *
	 * @param imageIcon the image icon
	 * @param text the text
	 */
	public void setStatusInfo(final ImageIcon imageIcon, final String text) {

		infoIconLabel.setIcon(imageIcon);
		infoTextLabel.setText(text);
	}

}
