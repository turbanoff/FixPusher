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

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.sourceforge.fixpusher.view.FIXPusher;

/**
 * The Class OpenProjectDialog.
 */
public class OpenProjectDialog extends AbstractProjectDialog {

	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new open project dialog.
	 * 
	 * @param projects
	 *            the projects
	 */
	public OpenProjectDialog(final List<String> projects) {

		super(projects);

		setTitle("Open Project");
		projectNameLabel.setText("Open Project");

		okButton.setText("Open");
		okButton.setIcon(new ImageIcon(FIXPusher.class.getResource("/net/sourceforge/fixpusher/view/images/16x16/file-manager.png")));

		projectList.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(final ListSelectionEvent e) {

				projectNameTextField.setText(projectList.getSelectedValue().toString());

			}
		});

		projectList.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {

			}

			@Override
			public void mousePressed(MouseEvent e) {

			}

			@Override
			public void mouseExited(MouseEvent e) {

			}

			@Override
			public void mouseEntered(MouseEvent e) {

			}

			@Override
			public void mouseClicked(MouseEvent e) {

				if (e.getClickCount() == 2) {

					project = projectNameTextField.getText();
					OpenProjectDialog.this.setVisible(false);
				}

			}
		});

		templateCheckBox.setVisible(false);
		projectList.setSelectedIndex(0);

	}

}
