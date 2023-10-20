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
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;

import net.sourceforge.fixpusher.view.FIXPusher;

/**
 * The Class AbstractProjectDialog.
 */
public abstract class AbstractProjectDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	private JPanel contentPanel = null;

	/** The ok button. */
	protected JButton okButton = null;

	/** The project. */
	protected String project = null;

	/** The project list. */
	protected JList projectList = null;

	/** The project name label. */
	protected JLabel projectNameLabel = null;

	/** The project name text field. */
	protected JTextField projectNameTextField = null;

	/** The template check box. */
	protected JCheckBox templateCheckBox = null;

	/**
	 * Instantiates a new abstract project dialog.
	 *
	 * @param projects the projects
	 */
	public AbstractProjectDialog(final List<String> projects) {

		setBounds(100, 100, 450, 300);
		setResizable(false);
		setModal(true);
		setIconImage(new ImageIcon(FIXPusher.class.getResource("/net/sourceforge/fixpusher/view/images/16x16/f-logo.png")).getImage());
		getContentPane().setLayout(new BorderLayout());
		
		contentPanel = new JPanel();
		contentPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
		contentPanel.setBackground(new Color(204, 216, 255));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		final GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[] { 0, 0, 0 };
		gbl_contentPanel.rowHeights = new int[] { 0, 0, 0, 0 };
		gbl_contentPanel.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		gbl_contentPanel.rowWeights = new double[] { 0.0, 0.0, 1.0, Double.MIN_VALUE };
		contentPanel.setLayout(gbl_contentPanel);

		projectNameLabel = new JLabel("Project Name");
		projectNameLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
		final GridBagConstraints gbc_projectNameLabel = new GridBagConstraints();
		gbc_projectNameLabel.insets = new Insets(0, 0, 5, 5);
		gbc_projectNameLabel.anchor = GridBagConstraints.WEST;
		gbc_projectNameLabel.gridx = 0;
		gbc_projectNameLabel.gridy = 0;
		contentPanel.add(projectNameLabel, gbc_projectNameLabel);

		projectNameTextField = new JTextField();
		projectNameTextField.setColumns(10);
		projectNameTextField.setMinimumSize(new Dimension(4, 25));
		projectNameTextField.setPreferredSize(new Dimension(4, 25));
		projectNameTextField.setEditable(false);
		projectNameTextField.setBackground(Color.WHITE);
		final GridBagConstraints gbc_projectNameTextField = new GridBagConstraints();
		gbc_projectNameTextField.insets = new Insets(0, 0, 5, 0);
		gbc_projectNameTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_projectNameTextField.gridx = 1;
		gbc_projectNameTextField.gridy = 0;
		contentPanel.add(projectNameTextField, gbc_projectNameTextField);

		final JLabel projectListLabel = new JLabel("Project List");
		projectListLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
		final GridBagConstraints gbc_projectListLabel = new GridBagConstraints();
		gbc_projectListLabel.anchor = GridBagConstraints.WEST;
		gbc_projectListLabel.insets = new Insets(10, 0, 5, 5);
		gbc_projectListLabel.gridx = 0;
		gbc_projectListLabel.gridy = 1;
		contentPanel.add(projectListLabel, gbc_projectListLabel);

		templateCheckBox = new JCheckBox("Use as Template");
		templateCheckBox.setOpaque(false);
		templateCheckBox.setFont(new Font("Dialog", Font.PLAIN, 12));
		final GridBagConstraints gbc_templateCheckBox = new GridBagConstraints();
		gbc_templateCheckBox.insets = new Insets(10, 0, 5, 0);
		gbc_templateCheckBox.anchor = GridBagConstraints.WEST;
		gbc_templateCheckBox.gridx = 1;
		gbc_templateCheckBox.gridy = 1;
		contentPanel.add(templateCheckBox, gbc_templateCheckBox);

		final JScrollPane scrollPane = new JScrollPane();
		final GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridwidth = 2;
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 2;
		contentPanel.add(scrollPane, gbc_scrollPane);

		projectList = new JList(projects.toArray());
		projectList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		scrollPane.setViewportView(projectList);

		final JPanel buttonPane = new JPanel();
		buttonPane.setBorder(new EmptyBorder(0, 0, 15, 5));
		buttonPane.setBackground(new Color(204, 216, 255));
		final FlowLayout fl_buttonPane = new FlowLayout(FlowLayout.RIGHT);
		fl_buttonPane.setHgap(10);
		fl_buttonPane.setVgap(0);
		buttonPane.setLayout(fl_buttonPane);
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		okButton = new JButton("Create");
		okButton.setFont(new Font("Dialog", Font.PLAIN, 12));
		okButton.setPreferredSize(new Dimension(100, 25));
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);

		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {

				project = projectNameTextField.getText();
				AbstractProjectDialog.this.setVisible(false);

			}
		});

		final JButton cancelButton = new JButton("Cancel");
		cancelButton.setFont(new Font("Dialog", Font.PLAIN, 12));
		cancelButton.setPreferredSize(new Dimension(100, 25));
		cancelButton.setActionCommand("Cancel");
		cancelButton.setIcon(new ImageIcon(FIXPusher.class.getResource("/net/sourceforge/fixpusher/view/images/16x16/fileclose.png")));
		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {

				project = null;
				AbstractProjectDialog.this.setVisible(false);

			}
		});
		buttonPane.add(cancelButton);

	}

	/**
	 * Gets the project.
	 *
	 * @return the project
	 */
	public String getProject() {

		return project;
	}

}
