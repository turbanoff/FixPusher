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

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JWindow;


/**
 * The Class SplashScreen.
 */
public class SplashScreen extends JWindow {

	private static final long serialVersionUID = 1L;

	private final String[] licenseText = new String[] {
			"FIX Pusher is free software; you can redistribute it and/or modify it under the terms of the GNU Library General Public License",
			"as published by the Free Software Foundation; either version 2.1 of the License, or (at your option) any later version.",

			"FIX Pusher is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of",
			"MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Library General Public License for more details.",

			"You should have received a copy of the GNU Library General Public License along with this library; if not, write to the",
			"Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA." };
	
	
	/**
	 * Instantiates a new splash screen.
	 */
	public SplashScreen() {

		super();
		
		final ImageIcon backImage = new ImageIcon(FIXPusher.class.getResource("/net/sourceforge/fixpusher/view/images/splash.png"));
		final Image image = backImage.getImage();
		
		final String text = "Version " + FIXPusher.version + "  -  Copyright (C) 2012  Alexander Pinnow";

		FontMetrics fontMetrics = getFontMetrics(new Font("Dialog", Font.BOLD, 12));
		final int topWidth = fontMetrics.stringWidth(text);

		fontMetrics = getFontMetrics(new Font("Dialog", Font.BOLD, 8));

		int tmpWidth = 0;

		for (final String line : licenseText) {
			
			final int nextWidth = fontMetrics.stringWidth(line);
			
			if (nextWidth > tmpWidth)
				tmpWidth = nextWidth;
		}

		final int width = tmpWidth;

		final JPanel jPanel = new JPanel() {

			private static final long serialVersionUID = 1L;
			
			@Override
			public void paintComponent(final Graphics graphics) {

				super.paintComponent(graphics);
				
				final Graphics2D graphics2d = (Graphics2D) graphics;
				graphics2d.drawImage(image, 0, 0, this);
				graphics2d.setFont(new Font("Dialog", Font.BOLD, 12));
				graphics2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
				graphics2d.setPaintMode();
				graphics2d.setColor(Color.WHITE);
				graphics2d.drawString(text, (600 - topWidth) / 2, 100);
				graphics2d.setFont(new Font("Dialog", Font.BOLD, 8));
				
				int y = 130;

				for (int i = 0; i < licenseText.length; i++) {
					
					graphics2d.drawString(licenseText[i], (600 - width) / 2, y);
					y += 10 + i % 2 * 5;
				}

			}
		};
		
		jPanel.setOpaque(true);
		setContentPane(jPanel);
		setBounds(0, 0, 600, 225);

		final GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		final Rectangle r = ge.getDefaultScreenDevice().getDefaultConfiguration().getBounds();

		setLocation(new Point((int) (r.getWidth() - 600) / 2, (int) (r.getHeight() - 225) / 2));
		
	}

}
