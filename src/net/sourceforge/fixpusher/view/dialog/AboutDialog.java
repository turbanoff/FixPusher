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
package net.sourceforge.fixpusher.view.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URI;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;

import net.sourceforge.fixpusher.view.FIXPusher;

/**
 * The Class AboutDialog.
 */
public class AboutDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	private final ImageIcon backImage = new ImageIcon(FIXPusher.class.getResource("/net/sourceforge/fixpusher/view/images/about.png"));

	private final JPanel contentPanel = new JPanel();

	private final Image image = backImage.getImage();

	/**
	 * Instantiates a new about dialog.
	 */
	public AboutDialog() {

		setTitle("About FIX Pusher");
		setIconImage(new ImageIcon(FIXPusher.class.getResource("/net/sourceforge/fixpusher/view/images/16x16/f-logo.png")).getImage());
		setBounds(100, 100, 632, 480);
		setResizable(false);

		final JPanel contentPane = new JPanel() {

			private static final long serialVersionUID = 1L;

			@Override
			public void paintComponent(final Graphics g) {

				super.paintComponent(g);

				for (int i = 0; i < g.getClipBounds().getWidth(); i = i + backImage.getIconWidth())
					g.drawImage(image, i, 0, this);
			}

		};

		setContentPane(contentPane);
		getContentPane().setLayout(new BorderLayout());

		contentPanel.setOpaque(false);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		final GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[] { 0, 0 };
		gbl_contentPanel.rowHeights = new int[] { 0, 0, 0, 0 };
		gbl_contentPanel.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gbl_contentPanel.rowWeights = new double[] { 0.0, 0.0, 1.0, Double.MIN_VALUE };
		contentPanel.setLayout(gbl_contentPanel);

		final JLabel copyrightLabel = new JLabel("Version " + FIXPusher.version + "  -  Copyright (C) 2012  Alexander Pinnow");
		copyrightLabel.setForeground(Color.WHITE);
		copyrightLabel.setFont(new Font("Dialog", Font.BOLD, 12));
		final GridBagConstraints gbc_copyrightLabel = new GridBagConstraints();
		gbc_copyrightLabel.insets = new Insets(100, 0, 5, 0);
		gbc_copyrightLabel.gridx = 0;
		gbc_copyrightLabel.gridy = 0;
		contentPanel.add(copyrightLabel, gbc_copyrightLabel);

		final JLabel contactLabel = new JLabel("Contact: alexander.pinnow@googlemail.com");
		contactLabel.setIcon(new ImageIcon(FIXPusher.class.getResource("/net/sourceforge/fixpusher/view/images/22x22/mail_new3.png")));
		contactLabel.setForeground(Color.WHITE);
		contactLabel.setFont(new Font("Dialog", Font.BOLD, 12));
		final GridBagConstraints gbc_contactLabel = new GridBagConstraints();
		gbc_contactLabel.insets = new Insets(5, 0, 0, 0);
		gbc_contactLabel.gridx = 0;
		gbc_contactLabel.gridy = 1;
		contentPanel.add(contactLabel, gbc_contactLabel);

		contactLabel.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(final MouseEvent e) {

				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(final MouseEvent e) {

				contactLabel.setForeground(new Color(204, 216, 255));

			}

			@Override
			public void mouseExited(final MouseEvent e) {

				contactLabel.setForeground(Color.WHITE);

			}

			@Override
			public void mousePressed(final MouseEvent e) {

				try {
					Desktop.getDesktop().mail(new URI("mailto:alexander.pinnow@googlemail.com?subject=FIX%20Pusher&body=Hi%20Alex,"));
				}
				catch (final Exception e1) {
					
					ExceptionDialog.showException(e1);
				}

			}

			@Override
			public void mouseReleased(final MouseEvent e) {

			}
		});

		final JScrollPane scrollPane = new JScrollPane();
		final GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.insets = new Insets(25, 25, 20, 25);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 2;
		contentPanel.add(scrollPane, gbc_scrollPane);

		final StringBuffer stringBuffer = new StringBuffer();
		stringBuffer
				.append("FIX Pusher is free software; you can redistribute it and/or modify it under the terms of the GNU Library General Public License as published by the Free Software Foundation; either version 2.1 of the License, or (at your option) any later version.");
		stringBuffer.append("\n\n");
		stringBuffer
				.append("FIX Pusher is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Library General Public License for more details.");
		stringBuffer.append("\n\n");
		stringBuffer
				.append("You should have received a copy of the GNU Library General Public License along with this library; if not, write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.");

		final JTextPane licensePane = new JTextPane();
		licensePane.setEditable(false);
		licensePane.setBackground(new Color(255, 243, 204));
		licensePane.setText(stringBuffer.toString());
		scrollPane.setViewportView(licensePane);

		final JPanel buttonPane = new JPanel();
		buttonPane.setBorder(new EmptyBorder(0, 0, 25, 25));
		buttonPane.setOpaque(false);
		final FlowLayout fl_buttonPane = new FlowLayout(FlowLayout.RIGHT);
		buttonPane.setLayout(fl_buttonPane);
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		final JButton closeButton = new JButton("Close");
		closeButton.setIcon(new ImageIcon(FIXPusher.class.getResource("/net/sourceforge/fixpusher/view/images/16x16/fileclose.png")));
		closeButton.setActionCommand("Cancel");
		buttonPane.add(closeButton);

		closeButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {

				AboutDialog.this.setVisible(false);

			}
		});

	}

}
