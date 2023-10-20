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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.MatteBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import jsyntaxpane.syntaxkits.XmlSyntaxKit;
import net.sourceforge.fixpusher.control.FIXConnectionListener.Status;
import net.sourceforge.fixpusher.model.FIXProperties;
import net.sourceforge.fixpusher.view.dialog.ExceptionDialog;

/**
 * The Class DataDictionaryPanel.
 */
public class DataDictionaryPanel extends AbstractMainPanelContent {

	private static final long serialVersionUID = 1L;
	
	private JEditorPane editorPane = null;
	
	private FIXProperties fixProperties = null;
	
	private MainPanel mainPanel = null;
	
	private JLabel projectNameLabel = null;

	/**
	 * Instantiates a new data dictionary panel.
	 *
	 * @param mainPanel the main panel
	 * @param enabled the enabled
	 */
	public DataDictionaryPanel(final MainPanel mainPanel, final boolean enabled) {

		super(mainPanel);
		setLayout(new BorderLayout(0, 0));
		setBackground(new Color(204, 216, 255));

		this.mainPanel = mainPanel;
		fixProperties = mainPanel.getFixProperties();

		final JPanel topPanel = new GradientPanel();
		topPanel.setBorder(new MatteBorder(0, 0, 1, 0, new Color(0, 0, 0)));
		final GridBagLayout gbl_topPanel = new GridBagLayout();
		gbl_topPanel.rowHeights = new int[] { 15, 0, 0 };
		gbl_topPanel.columnWeights = new double[] { 0.0, 0.0, 0.0, 1.0, 0.0 };
		gbl_topPanel.rowWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
		topPanel.setLayout(gbl_topPanel);
		add(topPanel, BorderLayout.NORTH);

		projectNameLabel = new JLabel(fixProperties.getDataDictionary());
		projectNameLabel.setForeground(Color.WHITE);
		projectNameLabel.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 20));
		final GridBagConstraints gbc_projectNameLabel = new GridBagConstraints();
		gbc_projectNameLabel.anchor = GridBagConstraints.WEST;
		gbc_projectNameLabel.insets = new Insets(5, 25, 5, 5);
		gbc_projectNameLabel.gridx = 3;
		gbc_projectNameLabel.gridy = 0;
		topPanel.add(projectNameLabel, gbc_projectNameLabel);

		final JLabel settingsLabel = new JLabel();
		settingsLabel.setText("Data Dictionary");
		settingsLabel.setIcon(new ImageIcon(FIXPusher.class.getResource("/net/sourceforge/fixpusher/view/images/24x24/txt.png")));
		settingsLabel.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 20));
		settingsLabel.setForeground(Color.WHITE);
		final GridBagConstraints gbc_settingsLabel = new GridBagConstraints();
		gbc_settingsLabel.anchor = GridBagConstraints.EAST;
		gbc_settingsLabel.insets = new Insets(5, 0, 5, 25);
		gbc_settingsLabel.gridx = 4;
		gbc_settingsLabel.gridy = 0;
		topPanel.add(settingsLabel, gbc_settingsLabel);

		final JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, BorderLayout.CENTER);

		editorPane = new JEditorPane();
		editorPane.setBackground(new Color(255, 243, 204));
		editorPane.setFont(new Font("Dialog", Font.PLAIN, 12));
		final XmlSyntaxKit xmlSyntaxKit = new XmlSyntaxKit();

		xmlSyntaxKit.setProperty("DefaultFont", "Dialog-12");
		xmlSyntaxKit.setProperty("LineNumbers.Background", "0xCCD8FF");
		xmlSyntaxKit.setProperty("LineNumbers.Foreground", "0x000000");
		xmlSyntaxKit.setProperty("Style.STRING", "0x527AFF, 0");
		xmlSyntaxKit.setProperty("Style.STRING2", "0x527AFF, 0");
		xmlSyntaxKit.setProperty("Style.TYPE", "0x0032D6, 2");
		scrollPane.setViewportView(editorPane);
		editorPane.setEditorKit(xmlSyntaxKit);

		try {
			
			final FileInputStream fstream = new FileInputStream(fixProperties.getDataDictionary());
			final DataInputStream in = new DataInputStream(fstream);
			final BufferedReader br = new BufferedReader(new InputStreamReader(in));
			final StringBuffer stringBuffer = new StringBuffer();
			
			String strLine = null;
			
			while ((strLine = br.readLine()) != null)
				stringBuffer.append(strLine + "\n");

			editorPane.setText(stringBuffer.toString());
		}
		catch (final Exception e) {
			
			ExceptionDialog.showException(e);
		}

		editorPane.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void changedUpdate(final DocumentEvent e) {

				setDirty(true);

			}

			@Override
			public void insertUpdate(final DocumentEvent e) {

				setDirty(true);

			}

			@Override
			public void removeUpdate(final DocumentEvent e) {

				setDirty(true);

			}
		});

		if (enabled)
			mainPanel.setStatusInfo(new ImageIcon(FIXPusher.class.getResource("/net/sourceforge/fixpusher/view/images/16x16/status_unknown.png")),
					"Be careful changing the dictionary.");
		else
			mainPanel.setStatusInfo(new ImageIcon(FIXPusher.class.getResource("/net/sourceforge/fixpusher/view/images/16x16/documentinfo.png")),
					"The dictionary is locked because you are not disconnected.");

		editorPane.setEditable(enabled);

	}

	/* (non-Javadoc)
	 * @see net.sourceforge.fixpusher.view.AbstractMainPanelContent#save()
	 */
	@Override
	public void save() {

		try {
			
			final FileOutputStream fstream = new FileOutputStream(fixProperties.getDataDictionary());
			fstream.write(editorPane.getText().getBytes());
			fstream.flush();
			fstream.close();
			setDirty(false);
			fixProperties.init();
			mainPanel.fireMainPanelListener();
			
		}
		catch (final Exception e) {
			
			ExceptionDialog.showException(e);
			mainPanel.fireMainPanelListener();
		}
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.fixpusher.view.AbstractMainPanelContent#setStatus(net.sourceforge.fixpusher.control.FIXConnectionListener.Status)
	 */
	@Override
	public void setStatus(final Status status) {

		if (status == Status.DISCONNECTED) {
			
			mainPanel.setStatusInfo(new ImageIcon(FIXPusher.class.getResource("/net/sourceforge/fixpusher/view/images/16x16/status_unknown.png")),
					"Be careful changing the dictionary.");
			
			editorPane.setEditable(true);
		}
		else {
			
			mainPanel.setStatusInfo(new ImageIcon(FIXPusher.class.getResource("/net/sourceforge/fixpusher/view/images/16x16/documentinfo.png")),
					"The dictionary is locked because you are not disconnected.");
			
			editorPane.setEditable(false);
		}

	}
	
	
}
