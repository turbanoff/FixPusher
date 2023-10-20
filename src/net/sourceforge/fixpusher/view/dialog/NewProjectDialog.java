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

import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import net.sourceforge.fixpusher.view.FIXPusher;

/**
 * The Class NewProjectDialog.
 */
public class NewProjectDialog extends AbstractProjectDialog {

	private static final long serialVersionUID = 1L;

	private List<String> projects = null;

	/**
	 * Instantiates a new new project dialog.
	 *
	 * @param projects the projects
	 */
	public NewProjectDialog(final List<String> projects) {

		super(projects);
		
		setTitle("Create Project");

		this.projects = projects;
		
		projectList.setSelectedIndex(0);

		projectNameTextField.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void changedUpdate(final DocumentEvent e) {

				check();

			}

			@Override
			public void insertUpdate(final DocumentEvent e) {

				check();

			}

			@Override
			public void removeUpdate(final DocumentEvent e) {

				check();

			}

		});

		projectNameTextField.setEditable(true);
		projectNameTextField.setText("New Project");
		
		okButton.setText("Create");
		okButton.setIcon(new ImageIcon(FIXPusher.class.getResource("/net/sourceforge/fixpusher/view/images/16x16/window_new.png")));

	}

	private void check() {

		okButton.setEnabled(true);

		if (projectNameTextField.getText().trim().length() == 0)
			okButton.setEnabled(false);

		if (projects.contains(projectNameTextField.getText().trim()))
			okButton.setEnabled(false);

	}

	/**
	 * Gets the template.
	 *
	 * @return the template
	 */
	public String getTemplate() {

		if (templateCheckBox.isSelected())
			return projectList.getSelectedValue().toString();
		
		return null;
	}

}
