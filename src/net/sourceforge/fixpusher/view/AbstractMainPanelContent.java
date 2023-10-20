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

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import net.sourceforge.fixpusher.control.FIXConnectionListener.Status;

/**
 * The Class AbstractMainPanelContent.
 */
public abstract class AbstractMainPanelContent extends JPanel {

	private static final long serialVersionUID = 1L;

	private boolean dirty = false;

	private MainPanel mainPanel = null;

	/**
	 * Instantiates a new abstract main panel content.
	 *
	 * @param mainPanel the main panel
	 */
	public AbstractMainPanelContent(final MainPanel mainPanel) {

		super();
		this.mainPanel = mainPanel;
	}

	/**
	 * Fix message filter changed.
	 */
	public void fixMessageFilterChanged() {

	}

	/**
	 * Checks if is consistent.
	 *
	 * @return true, if is consistent
	 */
	public boolean isConsistent() {

		return true;
	}

	/**
	 * Checks if is dirty.
	 *
	 * @return true, if is dirty
	 */
	public boolean isDirty() {

		return dirty;
	}

	/**
	 * Checks if is message.
	 *
	 * @return true, if is message
	 */
	public boolean isMessage() {

		return false;
	}

	/**
	 * Save.
	 */
	public abstract void save();

	/**
	 * Sets the dirty.
	 *
	 * @param dirty the new dirty
	 */
	public void setDirty(final boolean dirty) {

		this.dirty = dirty;
		
		mainPanel.fireMainPanelListener();
		
		if (dirty&&isConsistent())
			mainPanel.setStatusInfo(new ImageIcon(FIXPusher.class.getResource("/net/sourceforge/fixpusher/view/images/16x16/documentinfo.png")),
					"Hit save or Ctrl-S to store changes.");
		
		else if (!dirty&&isConsistent())
			mainPanel.setStatusInfo(new ImageIcon(FIXPusher.class.getResource("/net/sourceforge/fixpusher/view/images/16x16/documentinfo.png")),
					"Hit the green button or Ctrl-F6 to start the session.");
		
		else
			mainPanel.setStatusInfo(null, null);

	}

	/**
	 * Sets the status.
	 *
	 * @param status the new status
	 */
	public abstract void setStatus(Status status);

	/**
	 * Sets the status info.
	 *
	 * @param imageIcon the image icon
	 * @param text the text
	 */
	public void setStatusInfo(final ImageIcon imageIcon, final String text) {

		mainPanel.setStatusInfo(imageIcon, text);
	}

}
