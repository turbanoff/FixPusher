package net.sourceforge.fixpusher.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;
import javax.swing.UIManager;

public class ColoredCheckBoxIcon implements Icon {

	private Color color = null;

	private int width = 0;
	private int height = 0;

	private Icon icon;

	public ColoredCheckBoxIcon(Color iconColor) {

		super();

		color = iconColor;

		icon = UIManager.getIcon("CheckBox.icon");

		width = icon.getIconWidth();

		height = icon.getIconHeight();

	}

	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
		
		g.setColor(color);

		g.fillRect(x, y, width, height);
		
		icon.paintIcon(c, g, x, y);
		
		

	}

	@Override
	public int getIconWidth() {

		return width;
	}

	@Override
	public int getIconHeight() {

		return height;
	}

}
