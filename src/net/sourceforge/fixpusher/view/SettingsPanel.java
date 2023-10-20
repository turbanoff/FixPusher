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
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;

import net.sourceforge.fixpusher.control.DictionaryParser;
import net.sourceforge.fixpusher.control.FIXConnectionListener.Status;
import net.sourceforge.fixpusher.model.FIXProperties;
import net.sourceforge.fixpusher.view.dialog.ExceptionDialog;
import net.sourceforge.fixpusher.view.document.IntegerDocument;

/**
 * The Class SettingsPanel.
 */
public class SettingsPanel extends AbstractMainPanelContent {

	private static final long serialVersionUID = 1L;

	private JRadioButton acceptorRadioButton = null;

	private JTextField beginStringField = null;

	private JLabel beginStringWarningLabel = null;

	private final ImageIcon bugIcon = new ImageIcon(FIXPusher.class.getResource("/net/sourceforge/fixpusher/view/images/16x16/stop.png"));

	private JButton changeSenderButton = null;

	private final LinkedBlockingQueue<String> checkAdressQueue = new LinkedBlockingQueue<String>();

	private JButton cleanMessageStoreButton = null;

	private boolean consistent = true;

	private JButton dataDictionaryButton = null;

	private JTextField dataDictionaryField = null;

	private JLabel dataDictionaryWarningLabel = null;

	private JButton fileLogPathButton = null;

	private JTextField fileLogPathField = null;

	private JLabel fileLogPathWarningLabel = null;

	private JButton fileStorePathButton = null;

	private JTextField fileStorePathField = null;

	private JLabel fileStorePathWarningLabel = null;

	private FIXProperties fixProperties = null;

	private JTextField heartbeatField = null;

	private JLabel heartbeatLabel = null;

	private JLabel heartbeatWarningLabel = null;

	private JRadioButton initiatorRadioButton = null;

	private MainPanel mainPanel = null;

	private JTextField nextSenderSeqNumberField = null;

	private JTextField nextTargetSeqNumberField = null;

	private JLabel projectNameLabel = null;

	private JTextField senderCompIDField = null;

	private JLabel senderCompIDWarningLabel = null;

	private JRadioButton seqResetNoRadioButton = null;

	private JRadioButton seqResetYesRadioButton = null;

	private JLabel sequenceResetLabel = null;

	private JTextField socketAdressField = null;

	private JLabel socketAdressWarningLabel = null;

	private JTextField socketPortField = null;

	private JLabel socketPortWarningLabel = null;

	private JTextField targetCompIDField = null;

	private JLabel targetCompIDWarningLabel = null;

	private JButton transportDataDictionaryButton = null;

	private JTextField transportDataDictionaryField = null;

	private JLabel transportDataDictionaryLabel = null;

	private JLabel transportDataDictionaryWarningLabel = null;

	private boolean visible = true;

	private final ImageIcon warningIcon = new ImageIcon(FIXPusher.class.getResource("/net/sourceforge/fixpusher/view/images/16x16/status_unknown.png"));

	/**
	 * Instantiates a new settings panel.
	 *
	 * @param mainPanel the main panel
	 */
	public SettingsPanel(final MainPanel mainPanel) {

		super(mainPanel);

		setLayout(new BorderLayout(0, 0));
		setBackground(new Color(204, 216, 255));

		this.fixProperties = mainPanel.getFixProperties();
		this.mainPanel = mainPanel;

		final JPanel headPanel = new GradientPanel();
		headPanel.setBorder(new MatteBorder(0, 0, 1, 0, new Color(0, 0, 0)));
		add(headPanel, BorderLayout.NORTH);
		final GridBagLayout gbl_headPanel = new GridBagLayout();
		gbl_headPanel.rowHeights = new int[] { 15 };
		gbl_headPanel.columnWeights = new double[] { 0.0, 0.0, 0.0, 1.0, 0.0 };
		gbl_headPanel.rowWeights = new double[] { 0.0 };
		headPanel.setLayout(gbl_headPanel);
		add(headPanel, BorderLayout.NORTH);

		projectNameLabel = new JLabel(fixProperties.getProjectName());
		projectNameLabel.setForeground(Color.WHITE);
		projectNameLabel.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 20));
		final GridBagConstraints gbc_projectNameLabel = new GridBagConstraints();
		gbc_projectNameLabel.anchor = GridBagConstraints.WEST;
		gbc_projectNameLabel.insets = new Insets(5, 25, 5, 5);
		gbc_projectNameLabel.gridx = 3;
		gbc_projectNameLabel.gridy = 0;
		headPanel.add(projectNameLabel, gbc_projectNameLabel);

		final JLabel settingsLabel = new JLabel();
		settingsLabel.setText("Settings");
		settingsLabel.setIcon(new ImageIcon(FIXPusher.class.getResource("/net/sourceforge/fixpusher/view/images/22x22/configure.png")));
		settingsLabel.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 20));
		settingsLabel.setForeground(Color.WHITE);
		final GridBagConstraints gbc_settingsLabel = new GridBagConstraints();
		gbc_settingsLabel.anchor = GridBagConstraints.EAST;
		gbc_settingsLabel.insets = new Insets(5, 0, 5, 25);
		gbc_settingsLabel.gridx = 4;
		gbc_settingsLabel.gridy = 0;
		headPanel.add(settingsLabel, gbc_settingsLabel);

		final JPanel contentPanel = new JPanel();
		contentPanel.setOpaque(false);
		contentPanel.setMinimumSize(new Dimension(600, 480));
		contentPanel.setPreferredSize(new Dimension(600, 480));
		final GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 };
		gbl_contentPanel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0 };
		contentPanel.setLayout(gbl_contentPanel);

		final JPanel leftFillPanel = new JPanel();
		leftFillPanel.setOpaque(false);
		final GridBagConstraints gbc_leftFillPanel = new GridBagConstraints();
		gbc_leftFillPanel.gridheight = 8;
		gbc_leftFillPanel.insets = new Insets(0, 0, 5, 5);
		gbc_leftFillPanel.fill = GridBagConstraints.BOTH;
		gbc_leftFillPanel.gridx = 0;
		gbc_leftFillPanel.gridy = 0;
		contentPanel.add(leftFillPanel, gbc_leftFillPanel);

		final JPanel rightFillPanel = new JPanel();
		rightFillPanel.setOpaque(false);
		final GridBagConstraints gbc_rightFillPanel = new GridBagConstraints();
		gbc_rightFillPanel.gridheight = 8;
		gbc_rightFillPanel.insets = new Insets(0, 0, 5, 0);
		gbc_rightFillPanel.fill = GridBagConstraints.BOTH;
		gbc_rightFillPanel.gridx = 10;
		gbc_rightFillPanel.gridy = 0;
		contentPanel.add(rightFillPanel, gbc_rightFillPanel);

		final JLabel fileStorePathLabel = new JLabel("File store path");
		fileStorePathLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
		final GridBagConstraints gbc_fileStorePathLabel = new GridBagConstraints();
		gbc_fileStorePathLabel.anchor = GridBagConstraints.WEST;
		gbc_fileStorePathLabel.insets = new Insets(50, 5, 5, 5);
		gbc_fileStorePathLabel.gridx = 1;
		gbc_fileStorePathLabel.gridy = 0;
		contentPanel.add(fileStorePathLabel, gbc_fileStorePathLabel);

		fileStorePathField = new JTextField();
		fileStorePathField.setFont(new Font("Dialog", Font.PLAIN, 12));
		fileStorePathField.setPreferredSize(new Dimension(4, 25));
		fileStorePathField.setColumns(20);
		fileStorePathField.setText(fixProperties.getFileStorePath());
		final GridBagConstraints gbc_fileStorePathField = new GridBagConstraints();
		gbc_fileStorePathField.fill = GridBagConstraints.HORIZONTAL;
		gbc_fileStorePathField.gridwidth = 6;
		gbc_fileStorePathField.insets = new Insets(50, 0, 5, 5);
		gbc_fileStorePathField.gridx = 3;
		gbc_fileStorePathField.gridy = 0;
		contentPanel.add(fileStorePathField, gbc_fileStorePathField);

		fileStorePathWarningLabel = new JLabel("");
		fileStorePathWarningLabel.setPreferredSize(new Dimension(21, 25));
		final GridBagConstraints gbc_fileStorePathWarningLabel = new GridBagConstraints();
		gbc_fileStorePathWarningLabel.insets = new Insets(50, 0, 5, 5);
		gbc_fileStorePathWarningLabel.anchor = GridBagConstraints.EAST;
		gbc_fileStorePathWarningLabel.gridx = 2;
		gbc_fileStorePathWarningLabel.gridy = 0;
		contentPanel.add(fileStorePathWarningLabel, gbc_fileStorePathWarningLabel);

		fileStorePathField.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void changedUpdate(final DocumentEvent e) {

				checkConsistency(true);

			}

			@Override
			public void insertUpdate(final DocumentEvent e) {

				checkConsistency(true);

			}

			@Override
			public void removeUpdate(final DocumentEvent e) {

				checkConsistency(true);

			}
		});

		fileStorePathButton = new JButton("Browse ...");
		fileStorePathButton.setIcon(new ImageIcon(FIXPusher.class.getResource("/net/sourceforge/fixpusher/view/images/16x16/folder.png")));
		fileStorePathButton.setFont(new Font("Dialog", Font.PLAIN, 12));
		final GridBagConstraints gbc_fileStorePathButton = new GridBagConstraints();
		gbc_fileStorePathButton.fill = GridBagConstraints.VERTICAL;
		gbc_fileStorePathButton.insets = new Insets(50, 0, 5, 5);
		gbc_fileStorePathButton.gridx = 9;
		gbc_fileStorePathButton.gridy = 0;
		contentPanel.add(fileStorePathButton, gbc_fileStorePathButton);

		fileStorePathButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {

				final JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new java.io.File("."));
				chooser.setDialogTitle("File store path");
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				chooser.setAcceptAllFileFilterUsed(false);

				if (chooser.showOpenDialog(SettingsPanel.this.mainPanel) == JFileChooser.APPROVE_OPTION)
					fileStorePathField.setText(chooser.getSelectedFile().toString());
			}
		});

		final JLabel fileLogPathLabel = new JLabel("File log path");
		fileLogPathLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
		final GridBagConstraints gbc_fileLogPathLabel = new GridBagConstraints();
		gbc_fileLogPathLabel.anchor = GridBagConstraints.WEST;
		gbc_fileLogPathLabel.insets = new Insets(0, 5, 5, 5);
		gbc_fileLogPathLabel.gridx = 1;
		gbc_fileLogPathLabel.gridy = 1;
		contentPanel.add(fileLogPathLabel, gbc_fileLogPathLabel);

		fileLogPathField = new JTextField();
		fileLogPathField.setPreferredSize(new Dimension(4, 25));
		fileLogPathField.setFont(new Font("Dialog", Font.PLAIN, 12));
		fileLogPathField.setColumns(20);
		fileLogPathField.setText(fixProperties.getFileLogPath());
		final GridBagConstraints gbc_fileLogPathField = new GridBagConstraints();
		gbc_fileLogPathField.fill = GridBagConstraints.HORIZONTAL;
		gbc_fileLogPathField.gridwidth = 6;
		gbc_fileLogPathField.insets = new Insets(0, 0, 5, 5);
		gbc_fileLogPathField.gridx = 3;
		gbc_fileLogPathField.gridy = 1;
		contentPanel.add(fileLogPathField, gbc_fileLogPathField);

		fileLogPathWarningLabel = new JLabel("");
		fileLogPathWarningLabel.setPreferredSize(new Dimension(21, 16));
		final GridBagConstraints gbc_fileLogPathWarningLabel = new GridBagConstraints();
		gbc_fileLogPathWarningLabel.insets = new Insets(0, 0, 5, 5);
		gbc_fileLogPathWarningLabel.anchor = GridBagConstraints.EAST;
		gbc_fileLogPathWarningLabel.gridx = 2;
		gbc_fileLogPathWarningLabel.gridy = 1;
		contentPanel.add(fileLogPathWarningLabel, gbc_fileLogPathWarningLabel);

		fileLogPathField.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void changedUpdate(final DocumentEvent e) {

				checkConsistency(true);

			}

			@Override
			public void insertUpdate(final DocumentEvent e) {

				checkConsistency(true);

			}

			@Override
			public void removeUpdate(final DocumentEvent e) {

				checkConsistency(true);
			}
		});

		fileLogPathButton = new JButton("Browse ...");
		fileLogPathButton.setIcon(new ImageIcon(FIXPusher.class.getResource("/net/sourceforge/fixpusher/view/images/16x16/folder.png")));
		fileLogPathButton.setFont(new Font("Dialog", Font.PLAIN, 12));
		final GridBagConstraints gbc_fileLogPathButton = new GridBagConstraints();
		gbc_fileLogPathButton.fill = GridBagConstraints.VERTICAL;
		gbc_fileLogPathButton.insets = new Insets(0, 0, 5, 5);
		gbc_fileLogPathButton.gridx = 9;
		gbc_fileLogPathButton.gridy = 1;
		contentPanel.add(fileLogPathButton, gbc_fileLogPathButton);

		fileLogPathButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {

				final JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new java.io.File("."));
				chooser.setDialogTitle("File Log Path");
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				chooser.setAcceptAllFileFilterUsed(false);

				if (chooser.showOpenDialog(SettingsPanel.this.mainPanel) == JFileChooser.APPROVE_OPTION)
					fileLogPathField.setText(chooser.getSelectedFile().toString());

			}
		});

		final JLabel dataDictionaryLabel = new JLabel("Data dictionary");
		dataDictionaryLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
		final GridBagConstraints gbc_dataDictionaryLabel = new GridBagConstraints();
		gbc_dataDictionaryLabel.anchor = GridBagConstraints.WEST;
		gbc_dataDictionaryLabel.insets = new Insets(0, 5, 5, 5);
		gbc_dataDictionaryLabel.gridx = 1;
		gbc_dataDictionaryLabel.gridy = 2;
		contentPanel.add(dataDictionaryLabel, gbc_dataDictionaryLabel);

		dataDictionaryField = new JTextField();
		dataDictionaryField.setPreferredSize(new Dimension(4, 25));
		dataDictionaryField.setFont(new Font("Dialog", Font.PLAIN, 12));
		dataDictionaryField.setColumns(20);
		dataDictionaryField.setText(fixProperties.getDataDictionary());
		final GridBagConstraints gbc_dataDictionaryField = new GridBagConstraints();
		gbc_dataDictionaryField.fill = GridBagConstraints.HORIZONTAL;
		gbc_dataDictionaryField.gridwidth = 6;
		gbc_dataDictionaryField.insets = new Insets(0, 0, 5, 5);
		gbc_dataDictionaryField.gridx = 3;
		gbc_dataDictionaryField.gridy = 2;
		contentPanel.add(dataDictionaryField, gbc_dataDictionaryField);

		dataDictionaryWarningLabel = new JLabel("");
		dataDictionaryWarningLabel.setPreferredSize(new Dimension(21, 16));
		final GridBagConstraints gbc_dataDictionaryWarningLabel = new GridBagConstraints();
		gbc_dataDictionaryWarningLabel.insets = new Insets(0, 0, 5, 5);
		gbc_dataDictionaryWarningLabel.anchor = GridBagConstraints.EAST;
		gbc_dataDictionaryWarningLabel.gridx = 2;
		gbc_dataDictionaryWarningLabel.gridy = 2;
		contentPanel.add(dataDictionaryWarningLabel, gbc_dataDictionaryWarningLabel);

		dataDictionaryField.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void changedUpdate(final DocumentEvent e) {

				checkConsistency(true);
			}

			@Override
			public void insertUpdate(final DocumentEvent e) {

				checkConsistency(true);

			}

			@Override
			public void removeUpdate(final DocumentEvent e) {

				checkConsistency(true);
			}
		});

		dataDictionaryButton = new JButton("Browse ...");
		dataDictionaryButton.setIcon(new ImageIcon(FIXPusher.class.getResource("/net/sourceforge/fixpusher/view/images/16x16/folder.png")));
		dataDictionaryButton.setFont(new Font("Dialog", Font.PLAIN, 12));
		final GridBagConstraints gbc_dataDictionaryButton = new GridBagConstraints();
		gbc_dataDictionaryButton.fill = GridBagConstraints.VERTICAL;
		gbc_dataDictionaryButton.insets = new Insets(0, 0, 5, 5);
		gbc_dataDictionaryButton.gridx = 9;
		gbc_dataDictionaryButton.gridy = 2;
		contentPanel.add(dataDictionaryButton, gbc_dataDictionaryButton);

		dataDictionaryButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {

				final JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new java.io.File("."));
				chooser.setDialogTitle("Data dictionary");
				chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

				final FileFilter fileFilter = new FileFilter() {

					@Override
					public boolean accept(final File file) {

						if (file.isDirectory())
							return true;
						else {

							final String filename = file.getName();
							return filename.toLowerCase().endsWith(".xml");

						}
					}

					@Override
					public String getDescription() {

						return "*.xml";
					}
				};

				chooser.setFileFilter(fileFilter);
				chooser.setAcceptAllFileFilterUsed(false);

				if (chooser.showOpenDialog(SettingsPanel.this.mainPanel) == JFileChooser.APPROVE_OPTION)
					dataDictionaryField.setText(chooser.getSelectedFile().toString());
			}
		});

		final JLabel beginStringLabel = new JLabel("Begin string");
		beginStringLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
		final GridBagConstraints gbc_beginStringLabel = new GridBagConstraints();
		gbc_beginStringLabel.anchor = GridBagConstraints.WEST;
		gbc_beginStringLabel.insets = new Insets(0, 5, 5, 5);
		gbc_beginStringLabel.gridx = 1;
		gbc_beginStringLabel.gridy = 3;
		contentPanel.add(beginStringLabel, gbc_beginStringLabel);

		beginStringField = new JTextField();
		beginStringField.setPreferredSize(new Dimension(250, 25));
		beginStringField.setFont(new Font("Dialog", Font.PLAIN, 12));
		beginStringField.setColumns(10);
		beginStringField.setText(fixProperties.getBeginString());
		final GridBagConstraints gbc_beginStringField = new GridBagConstraints();
		gbc_beginStringField.fill = GridBagConstraints.HORIZONTAL;
		gbc_beginStringField.gridwidth = 2;
		gbc_beginStringField.insets = new Insets(0, 0, 5, 5);
		gbc_beginStringField.gridx = 3;
		gbc_beginStringField.gridy = 3;
		contentPanel.add(beginStringField, gbc_beginStringField);

		beginStringWarningLabel = new JLabel("");
		beginStringWarningLabel.setPreferredSize(new Dimension(21, 16));
		final GridBagConstraints gbc_beginStringWarningLabel = new GridBagConstraints();
		gbc_beginStringWarningLabel.insets = new Insets(0, 0, 5, 5);
		gbc_beginStringWarningLabel.anchor = GridBagConstraints.EAST;
		gbc_beginStringWarningLabel.gridx = 2;
		gbc_beginStringWarningLabel.gridy = 3;
		contentPanel.add(beginStringWarningLabel, gbc_beginStringWarningLabel);

		beginStringField.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void changedUpdate(final DocumentEvent e) {

				checkConsistency(true);

			}

			@Override
			public void insertUpdate(final DocumentEvent e) {

				checkConsistency(true);

			}

			@Override
			public void removeUpdate(final DocumentEvent e) {

				checkConsistency(true);
			}
		});

		heartbeatLabel = new JLabel("Heartbeat");
		heartbeatLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
		final GridBagConstraints gbc_heartbeatLabel = new GridBagConstraints();
		gbc_heartbeatLabel.fill = GridBagConstraints.HORIZONTAL;
		gbc_heartbeatLabel.insets = new Insets(0, 50, 5, 5);
		gbc_heartbeatLabel.gridx = 5;
		gbc_heartbeatLabel.gridy = 3;
		contentPanel.add(heartbeatLabel, gbc_heartbeatLabel);

		heartbeatWarningLabel = new JLabel("");
		heartbeatWarningLabel.setPreferredSize(new Dimension(21, 16));
		final GridBagConstraints gbc_heartbeatWarningLabel = new GridBagConstraints();
		gbc_heartbeatWarningLabel.insets = new Insets(0, 0, 5, 5);
		gbc_heartbeatWarningLabel.anchor = GridBagConstraints.EAST;
		gbc_heartbeatWarningLabel.gridx = 6;
		gbc_heartbeatWarningLabel.gridy = 3;
		contentPanel.add(heartbeatWarningLabel, gbc_heartbeatWarningLabel);

		heartbeatField = new JTextField();
		heartbeatField.setDocument(new IntegerDocument());
		heartbeatField.setFont(new Font("Dialog", Font.PLAIN, 12));
		heartbeatField.setPreferredSize(new Dimension(250, 25));
		heartbeatField.setColumns(10);
		heartbeatField.setText(fixProperties.getHeartbeat());
		final GridBagConstraints gbc_heartbeatField = new GridBagConstraints();
		gbc_heartbeatField.fill = GridBagConstraints.HORIZONTAL;
		gbc_heartbeatField.gridwidth = 2;
		gbc_heartbeatField.insets = new Insets(0, 0, 5, 5);
		gbc_heartbeatField.gridx = 7;
		gbc_heartbeatField.gridy = 3;
		contentPanel.add(heartbeatField, gbc_heartbeatField);

		heartbeatField.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void changedUpdate(final DocumentEvent e) {

				checkConsistency(true);

			}

			@Override
			public void insertUpdate(final DocumentEvent e) {

				checkConsistency(true);

			}

			@Override
			public void removeUpdate(final DocumentEvent e) {

				checkConsistency(true);
			}
		});

		transportDataDictionaryLabel = new JLabel("Transport data dictionary");
		transportDataDictionaryLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
		final GridBagConstraints gbc_transportdataDicitionaryLabel = new GridBagConstraints();
		gbc_transportdataDicitionaryLabel.anchor = GridBagConstraints.WEST;
		gbc_transportdataDicitionaryLabel.insets = new Insets(0, 5, 5, 5);
		gbc_transportdataDicitionaryLabel.gridx = 1;
		gbc_transportdataDicitionaryLabel.gridy = 4;
		contentPanel.add(transportDataDictionaryLabel, gbc_transportdataDicitionaryLabel);

		transportDataDictionaryWarningLabel = new JLabel("");
		transportDataDictionaryWarningLabel.setPreferredSize(new Dimension(21, 16));
		final GridBagConstraints gbc_transportDataDictionaryWarningLabel = new GridBagConstraints();
		gbc_transportDataDictionaryWarningLabel.insets = new Insets(0, 0, 5, 5);
		gbc_transportDataDictionaryWarningLabel.anchor = GridBagConstraints.EAST;
		gbc_transportDataDictionaryWarningLabel.gridx = 2;
		gbc_transportDataDictionaryWarningLabel.gridy = 4;
		contentPanel.add(transportDataDictionaryWarningLabel, gbc_transportDataDictionaryWarningLabel);

		transportDataDictionaryField = new JTextField(fixProperties.getTransportDataDictionary());
		transportDataDictionaryField.setPreferredSize(new Dimension(4, 25));
		transportDataDictionaryField.setFont(new Font("Dialog", Font.PLAIN, 12));
		transportDataDictionaryField.setColumns(20);
		final GridBagConstraints gbc_transportDataDicitionaryField = new GridBagConstraints();
		gbc_transportDataDicitionaryField.gridwidth = 6;
		gbc_transportDataDicitionaryField.insets = new Insets(0, 0, 5, 5);
		gbc_transportDataDicitionaryField.fill = GridBagConstraints.HORIZONTAL;
		gbc_transportDataDicitionaryField.gridx = 3;
		gbc_transportDataDicitionaryField.gridy = 4;
		contentPanel.add(transportDataDictionaryField, gbc_transportDataDicitionaryField);

		transportDataDictionaryField.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void changedUpdate(final DocumentEvent e) {

				checkConsistency(true);

			}

			@Override
			public void insertUpdate(final DocumentEvent e) {

				checkConsistency(true);

			}

			@Override
			public void removeUpdate(final DocumentEvent e) {

				checkConsistency(true);

			}
		});

		transportDataDictionaryButton = new JButton("Browse ...");
		transportDataDictionaryButton.setIcon(new ImageIcon(FIXPusher.class.getResource("/net/sourceforge/fixpusher/view/images/16x16/folder.png")));
		transportDataDictionaryButton.setFont(new Font("Dialog", Font.PLAIN, 12));
		final GridBagConstraints gbc_transportDataDictionaryButton = new GridBagConstraints();
		gbc_transportDataDictionaryButton.fill = GridBagConstraints.VERTICAL;
		gbc_transportDataDictionaryButton.insets = new Insets(0, 0, 5, 5);
		gbc_transportDataDictionaryButton.gridx = 9;
		gbc_transportDataDictionaryButton.gridy = 4;
		contentPanel.add(transportDataDictionaryButton, gbc_transportDataDictionaryButton);

		transportDataDictionaryButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {

				final JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new java.io.File("."));
				chooser.setDialogTitle("Transport data dictionary");
				chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

				final FileFilter fileFilter = new FileFilter() {

					@Override
					public boolean accept(final File file) {

						if (file.isDirectory())
							return true;
						else {

							final String filename = file.getName();
							return filename.toLowerCase().endsWith(".xml");
						}
					}

					@Override
					public String getDescription() {

						return "*.xml";
					}
				};

				chooser.setFileFilter(fileFilter);
				chooser.setAcceptAllFileFilterUsed(false);

				if (chooser.showOpenDialog(SettingsPanel.this.mainPanel) == JFileChooser.APPROVE_OPTION)
					transportDataDictionaryField.setText(chooser.getSelectedFile().toString());
			}
		});

		final JLabel connectionTypeLabel = new JLabel("Connection type");
		connectionTypeLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
		final GridBagConstraints gbc_connectionTypeLabel = new GridBagConstraints();
		gbc_connectionTypeLabel.anchor = GridBagConstraints.WEST;
		gbc_connectionTypeLabel.insets = new Insets(0, 5, 5, 5);
		gbc_connectionTypeLabel.gridx = 1;
		gbc_connectionTypeLabel.gridy = 5;
		contentPanel.add(connectionTypeLabel, gbc_connectionTypeLabel);

		final ButtonGroup connectionTypeButtonGroup = new ButtonGroup();

		acceptorRadioButton = new JRadioButton("Acceptor");
		acceptorRadioButton.setPreferredSize(new Dimension(100, 25));
		acceptorRadioButton.setOpaque(false);
		acceptorRadioButton.setFont(new Font("Dialog", Font.PLAIN, 12));
		final GridBagConstraints gbc_acceptorRadioButton = new GridBagConstraints();
		gbc_acceptorRadioButton.anchor = GridBagConstraints.WEST;
		gbc_acceptorRadioButton.insets = new Insets(0, 0, 5, 5);
		gbc_acceptorRadioButton.gridx = 3;
		gbc_acceptorRadioButton.gridy = 5;
		contentPanel.add(acceptorRadioButton, gbc_acceptorRadioButton);

		initiatorRadioButton = new JRadioButton("Initiator");
		initiatorRadioButton.setPreferredSize(new Dimension(100, 25));
		initiatorRadioButton.setOpaque(false);
		initiatorRadioButton.setFont(new Font("Dialog", Font.PLAIN, 12));
		final GridBagConstraints gbc_initiatorRadioButton = new GridBagConstraints();
		gbc_initiatorRadioButton.anchor = GridBagConstraints.WEST;
		gbc_initiatorRadioButton.insets = new Insets(0, 0, 5, 5);
		gbc_initiatorRadioButton.gridx = 4;
		gbc_initiatorRadioButton.gridy = 5;
		contentPanel.add(initiatorRadioButton, gbc_initiatorRadioButton);

		connectionTypeButtonGroup.add(acceptorRadioButton);
		connectionTypeButtonGroup.add(initiatorRadioButton);

		if (fixProperties.getConnectionType().equals("initiator"))
			initiatorRadioButton.setSelected(true);
		else
			acceptorRadioButton.setSelected(true);

		acceptorRadioButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {

				checkConsistency(true);

			}
		});

		initiatorRadioButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {

				checkConsistency(true);

			}
		});

		sequenceResetLabel = new JLabel("Sequence reset at logon");
		sequenceResetLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
		final GridBagConstraints gbc_sequenceResetLabel = new GridBagConstraints();
		gbc_sequenceResetLabel.fill = GridBagConstraints.HORIZONTAL;
		gbc_sequenceResetLabel.insets = new Insets(0, 50, 5, 5);
		gbc_sequenceResetLabel.gridx = 5;
		gbc_sequenceResetLabel.gridy = 5;
		contentPanel.add(sequenceResetLabel, gbc_sequenceResetLabel);

		final ButtonGroup sequenceResetButtonGroup = new ButtonGroup();

		seqResetYesRadioButton = new JRadioButton("Yes");
		seqResetYesRadioButton.setPreferredSize(new Dimension(100, 25));
		seqResetYesRadioButton.setOpaque(false);
		seqResetYesRadioButton.setFont(new Font("Dialog", Font.PLAIN, 12));
		final GridBagConstraints gbc_seqResetYesRadioButton = new GridBagConstraints();
		gbc_seqResetYesRadioButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_seqResetYesRadioButton.insets = new Insets(0, 0, 5, 5);
		gbc_seqResetYesRadioButton.gridx = 7;
		gbc_seqResetYesRadioButton.gridy = 5;
		contentPanel.add(seqResetYesRadioButton, gbc_seqResetYesRadioButton);
		sequenceResetButtonGroup.add(seqResetYesRadioButton);

		seqResetYesRadioButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {

				checkConsistency(true);

			}
		});

		seqResetNoRadioButton = new JRadioButton("No");
		seqResetNoRadioButton.setPreferredSize(new Dimension(100, 25));
		seqResetNoRadioButton.setOpaque(false);
		seqResetNoRadioButton.setFont(new Font("Dialog", Font.PLAIN, 12));
		final GridBagConstraints gbc_seqResetNoRadioButton = new GridBagConstraints();
		gbc_seqResetNoRadioButton.anchor = GridBagConstraints.WEST;
		gbc_seqResetNoRadioButton.insets = new Insets(0, 0, 5, 5);
		gbc_seqResetNoRadioButton.gridx = 8;
		gbc_seqResetNoRadioButton.gridy = 5;
		contentPanel.add(seqResetNoRadioButton, gbc_seqResetNoRadioButton);

		sequenceResetButtonGroup.add(seqResetNoRadioButton);

		seqResetNoRadioButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {

				checkConsistency(true);

			}
		});

		final JLabel socketAdressLabel = new JLabel("Socket adress");
		socketAdressLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
		final GridBagConstraints gbc_socketAdressLabel = new GridBagConstraints();
		gbc_socketAdressLabel.anchor = GridBagConstraints.WEST;
		gbc_socketAdressLabel.insets = new Insets(0, 5, 5, 5);
		gbc_socketAdressLabel.gridx = 1;
		gbc_socketAdressLabel.gridy = 6;
		contentPanel.add(socketAdressLabel, gbc_socketAdressLabel);

		socketAdressField = new JTextField();
		socketAdressField.setPreferredSize(new Dimension(4, 25));
		socketAdressField.setFont(new Font("Dialog", Font.PLAIN, 12));
		socketAdressField.setColumns(10);
		socketAdressField.setText(fixProperties.getSocketAdress());
		final GridBagConstraints gbc_socketAdressField = new GridBagConstraints();
		gbc_socketAdressField.fill = GridBagConstraints.HORIZONTAL;
		gbc_socketAdressField.gridwidth = 2;
		gbc_socketAdressField.insets = new Insets(0, 0, 5, 5);
		gbc_socketAdressField.gridx = 3;
		gbc_socketAdressField.gridy = 6;
		contentPanel.add(socketAdressField, gbc_socketAdressField);

		socketAdressWarningLabel = new JLabel("");
		socketAdressWarningLabel.setPreferredSize(new Dimension(21, 16));
		final GridBagConstraints gbc_socketAdressWarningLabel = new GridBagConstraints();
		gbc_socketAdressWarningLabel.insets = new Insets(0, 0, 5, 5);
		gbc_socketAdressWarningLabel.anchor = GridBagConstraints.EAST;
		gbc_socketAdressWarningLabel.gridx = 2;
		gbc_socketAdressWarningLabel.gridy = 6;
		contentPanel.add(socketAdressWarningLabel, gbc_socketAdressWarningLabel);

		socketAdressField.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void changedUpdate(final DocumentEvent e) {

				updateSocketAdress();

			}

			@Override
			public void insertUpdate(final DocumentEvent e) {

				updateSocketAdress();

			}

			@Override
			public void removeUpdate(final DocumentEvent e) {

				updateSocketAdress();

			}
		});

		final JLabel socketPortLabel = new JLabel("Socket port");
		socketPortLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
		final GridBagConstraints gbc_socketPortLabel = new GridBagConstraints();
		gbc_socketPortLabel.anchor = GridBagConstraints.WEST;
		gbc_socketPortLabel.insets = new Insets(0, 50, 5, 5);
		gbc_socketPortLabel.gridx = 5;
		gbc_socketPortLabel.gridy = 6;
		contentPanel.add(socketPortLabel, gbc_socketPortLabel);

		socketPortField = new JTextField();
		socketPortField.setDocument(new IntegerDocument());
		socketPortField.setPreferredSize(new Dimension(4, 25));
		socketPortField.setFont(new Font("Dialog", Font.PLAIN, 12));
		socketPortField.setColumns(10);
		socketPortField.setText(fixProperties.getSocketPort());
		final GridBagConstraints gbc_socketPortField = new GridBagConstraints();
		gbc_socketPortField.fill = GridBagConstraints.HORIZONTAL;
		gbc_socketPortField.gridwidth = 2;
		gbc_socketPortField.insets = new Insets(0, 0, 5, 5);
		gbc_socketPortField.gridx = 7;
		gbc_socketPortField.gridy = 6;
		contentPanel.add(socketPortField, gbc_socketPortField);

		socketPortField.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void changedUpdate(final DocumentEvent e) {

				checkConsistency(true);

			}

			@Override
			public void insertUpdate(final DocumentEvent e) {

				checkConsistency(true);

			}

			@Override
			public void removeUpdate(final DocumentEvent e) {

				checkConsistency(true);

			}
		});

		socketPortWarningLabel = new JLabel();
		socketPortWarningLabel.setPreferredSize(new Dimension(21, 16));
		final GridBagConstraints gbc_socketPortWarningLabel = new GridBagConstraints();
		gbc_socketPortWarningLabel.insets = new Insets(0, 0, 5, 5);
		gbc_socketPortWarningLabel.anchor = GridBagConstraints.EAST;
		gbc_socketPortWarningLabel.gridx = 6;
		gbc_socketPortWarningLabel.gridy = 6;
		contentPanel.add(socketPortWarningLabel, gbc_socketPortWarningLabel);

		if (fixProperties.getSendResetSeqNumFlag().equals("Y"))
			seqResetYesRadioButton.setSelected(true);
		else
			seqResetNoRadioButton.setSelected(true);

		final JLabel senderCompIDLabel = new JLabel("Sender comp ID");
		senderCompIDLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
		final GridBagConstraints gbc_senderCompIDLabel = new GridBagConstraints();
		gbc_senderCompIDLabel.anchor = GridBagConstraints.WEST;
		gbc_senderCompIDLabel.insets = new Insets(0, 5, 5, 5);
		gbc_senderCompIDLabel.gridx = 1;
		gbc_senderCompIDLabel.gridy = 7;
		contentPanel.add(senderCompIDLabel, gbc_senderCompIDLabel);

		senderCompIDWarningLabel = new JLabel();
		senderCompIDWarningLabel.setPreferredSize(new Dimension(21, 16));
		final GridBagConstraints gbc_senderCompIDWarningLabel = new GridBagConstraints();
		gbc_senderCompIDWarningLabel.insets = new Insets(0, 0, 5, 5);
		gbc_senderCompIDWarningLabel.anchor = GridBagConstraints.EAST;
		gbc_senderCompIDWarningLabel.gridx = 2;
		gbc_senderCompIDWarningLabel.gridy = 7;
		contentPanel.add(senderCompIDWarningLabel, gbc_senderCompIDWarningLabel);

		senderCompIDField = new JTextField();
		senderCompIDField.setPreferredSize(new Dimension(4, 25));
		senderCompIDField.setFont(new Font("Dialog", Font.PLAIN, 12));
		senderCompIDField.setColumns(10);
		senderCompIDField.setText(fixProperties.getSenderCompID());
		final GridBagConstraints gbc_senderCompIDField = new GridBagConstraints();
		gbc_senderCompIDField.fill = GridBagConstraints.HORIZONTAL;
		gbc_senderCompIDField.gridwidth = 2;
		gbc_senderCompIDField.insets = new Insets(0, 0, 5, 5);
		gbc_senderCompIDField.gridx = 3;
		gbc_senderCompIDField.gridy = 7;
		contentPanel.add(senderCompIDField, gbc_senderCompIDField);

		senderCompIDField.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void changedUpdate(final DocumentEvent e) {

				checkConsistency(true);

			}

			@Override
			public void insertUpdate(final DocumentEvent e) {

				checkConsistency(true);

			}

			@Override
			public void removeUpdate(final DocumentEvent e) {

				checkConsistency(true);
			}
		});

		final JLabel targetCompIDLabel = new JLabel("Target comp ID");
		targetCompIDLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
		final GridBagConstraints gbc_targetCompIDLabel = new GridBagConstraints();
		gbc_targetCompIDLabel.anchor = GridBagConstraints.WEST;
		gbc_targetCompIDLabel.insets = new Insets(0, 50, 5, 5);
		gbc_targetCompIDLabel.gridx = 5;
		gbc_targetCompIDLabel.gridy = 7;
		contentPanel.add(targetCompIDLabel, gbc_targetCompIDLabel);

		targetCompIDWarningLabel = new JLabel();
		targetCompIDWarningLabel.setPreferredSize(new Dimension(21, 16));
		final GridBagConstraints gbc_targetCompIDWarningLabel = new GridBagConstraints();
		gbc_targetCompIDWarningLabel.insets = new Insets(0, 0, 5, 5);
		gbc_targetCompIDWarningLabel.anchor = GridBagConstraints.EAST;
		gbc_targetCompIDWarningLabel.gridx = 6;
		gbc_targetCompIDWarningLabel.gridy = 7;
		contentPanel.add(targetCompIDWarningLabel, gbc_targetCompIDWarningLabel);

		targetCompIDField = new JTextField();
		targetCompIDField.setPreferredSize(new Dimension(4, 25));
		targetCompIDField.setFont(new Font("Dialog", Font.PLAIN, 12));
		targetCompIDField.setColumns(10);
		targetCompIDField.setText(fixProperties.getTargetCompID());
		final GridBagConstraints gbc_targetCompIDField = new GridBagConstraints();
		gbc_targetCompIDField.fill = GridBagConstraints.HORIZONTAL;
		gbc_targetCompIDField.gridwidth = 2;
		gbc_targetCompIDField.insets = new Insets(0, 0, 5, 5);
		gbc_targetCompIDField.gridx = 7;
		gbc_targetCompIDField.gridy = 7;
		contentPanel.add(targetCompIDField, gbc_targetCompIDField);

		targetCompIDField.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void changedUpdate(final DocumentEvent e) {

				checkConsistency(true);

			}

			@Override
			public void insertUpdate(final DocumentEvent e) {

				checkConsistency(true);

			}

			@Override
			public void removeUpdate(final DocumentEvent e) {

				checkConsistency(true);
			}
		});

		final JViewport jViewport = new BackgroundViewPort();
		final JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewport(jViewport);
		scrollPane.setViewportView(contentPanel);

		final JPanel actionPanel = new JPanel() {

			private static final long serialVersionUID = 1L;

			@Override
			protected void paintComponent(final Graphics gra) {

				final Graphics2D g = (Graphics2D) gra;
				super.paintComponent(g);
				final int w = this.getWidth();
				final int h = this.getHeight();

				final GradientPaint gradientPaint = new GradientPaint(w / 2.F, 0, Color.GRAY, w / 2.F, h, Color.BLACK);
				g.setPaint(gradientPaint);
				g.fillRect(0, 0, w, h);

				getUI().paint(g, this);
			}
		};
		actionPanel.setPreferredSize(new Dimension(10, 25));
		actionPanel.setBorder(new MatteBorder(0, 0, 1, 0, Color.GRAY));

		final GridBagConstraints gbc_actionPanel = new GridBagConstraints();
		gbc_actionPanel.gridwidth = 9;
		gbc_actionPanel.insets = new Insets(50, 0, 25, 5);
		gbc_actionPanel.fill = GridBagConstraints.BOTH;
		gbc_actionPanel.gridx = 1;
		gbc_actionPanel.gridy = 8;
		contentPanel.add(actionPanel, gbc_actionPanel);
		actionPanel.setLayout(new BorderLayout(0, 0));

		final JLabel actionLabel = new JLabel("Actions");
		actionLabel.setBorder(new EmptyBorder(0, 5, 0, 0));
		actionLabel.setForeground(Color.WHITE);
		actionLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
		actionPanel.add(actionLabel, BorderLayout.WEST);

		final JLabel lblNextSequenceNumber = new JLabel("Next sender sequence");
		lblNextSequenceNumber.setFont(new Font("Dialog", Font.PLAIN, 12));
		final GridBagConstraints gbc_lblNextSequenceNumber = new GridBagConstraints();
		gbc_lblNextSequenceNumber.anchor = GridBagConstraints.WEST;
		gbc_lblNextSequenceNumber.insets = new Insets(0, 5, 5, 5);
		gbc_lblNextSequenceNumber.gridx = 1;
		gbc_lblNextSequenceNumber.gridy = 9;
		contentPanel.add(lblNextSequenceNumber, gbc_lblNextSequenceNumber);

		nextSenderSeqNumberField = new JTextField();
		nextSenderSeqNumberField.setDocument(new IntegerDocument());
		nextSenderSeqNumberField.setFont(new Font("Dialog", Font.PLAIN, 12));
		nextSenderSeqNumberField.setPreferredSize(new Dimension(4, 25));
		final GridBagConstraints gbc_nextSenderSeqNumberField = new GridBagConstraints();
		gbc_nextSenderSeqNumberField.gridwidth = 2;
		gbc_nextSenderSeqNumberField.insets = new Insets(0, 0, 5, 5);
		gbc_nextSenderSeqNumberField.fill = GridBagConstraints.HORIZONTAL;
		gbc_nextSenderSeqNumberField.gridx = 3;
		gbc_nextSenderSeqNumberField.gridy = 9;
		contentPanel.add(nextSenderSeqNumberField, gbc_nextSenderSeqNumberField);
		nextSenderSeqNumberField.setColumns(10);
		nextSenderSeqNumberField.setText(fixProperties.getNextSenderSequenceNumber());

		changeSenderButton = new JButton("Change");
		changeSenderButton.setEnabled(false);
		changeSenderButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {

				if (nextSenderSeqNumberField.getText().trim().length() == 0)
					nextSenderSeqNumberField.setText("0");
				SettingsPanel.this.fixProperties.setNextSenderSequenceNumber(nextSenderSeqNumberField.getText());

				if (nextTargetSeqNumberField.getText().trim().length() == 0)
					nextTargetSeqNumberField.setText("0");

				SettingsPanel.this.fixProperties.setNextTargetSequenceNumber(nextTargetSeqNumberField.getText());

				changeSenderButton.setEnabled(false);

			}

		});

		nextSenderSeqNumberField.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void changedUpdate(final DocumentEvent e) {

				changeSenderButton.setEnabled(true);
			}

			@Override
			public void insertUpdate(final DocumentEvent e) {

				changeSenderButton.setEnabled(true);

			}

			@Override
			public void removeUpdate(final DocumentEvent e) {

				changeSenderButton.setEnabled(true);
			}
		});

		final JLabel nextTargetSeqNumberLabel = new JLabel("Next target sequence");
		nextTargetSeqNumberLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
		final GridBagConstraints gbc_nextTargetSeqNumberLabel = new GridBagConstraints();
		gbc_nextTargetSeqNumberLabel.anchor = GridBagConstraints.WEST;
		gbc_nextTargetSeqNumberLabel.insets = new Insets(0, 50, 5, 5);
		gbc_nextTargetSeqNumberLabel.gridx = 5;
		gbc_nextTargetSeqNumberLabel.gridy = 9;
		contentPanel.add(nextTargetSeqNumberLabel, gbc_nextTargetSeqNumberLabel);

		nextTargetSeqNumberField = new JTextField();
		nextTargetSeqNumberField.setDocument(new IntegerDocument());
		nextTargetSeqNumberField.setFont(new Font("Dialog", Font.PLAIN, 12));
		nextTargetSeqNumberField.setPreferredSize(new Dimension(4, 25));
		final GridBagConstraints gbc_nextTargetSeqNumberField = new GridBagConstraints();
		gbc_nextTargetSeqNumberField.gridwidth = 2;
		gbc_nextTargetSeqNumberField.insets = new Insets(0, 0, 5, 5);
		gbc_nextTargetSeqNumberField.fill = GridBagConstraints.HORIZONTAL;
		gbc_nextTargetSeqNumberField.gridx = 7;
		gbc_nextTargetSeqNumberField.gridy = 9;
		contentPanel.add(nextTargetSeqNumberField, gbc_nextTargetSeqNumberField);
		nextTargetSeqNumberField.setColumns(10);

		nextTargetSeqNumberField.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void changedUpdate(final DocumentEvent e) {

				changeSenderButton.setEnabled(true);
			}

			@Override
			public void insertUpdate(final DocumentEvent e) {

				changeSenderButton.setEnabled(true);

			}

			@Override
			public void removeUpdate(final DocumentEvent e) {

				changeSenderButton.setEnabled(true);
			}
		});
		nextTargetSeqNumberField.setText(fixProperties.getNextTargetSequenceNumber());

		changeSenderButton.setIcon(new ImageIcon(FIXPusher.class.getResource("/net/sourceforge/fixpusher/view/images/16x16/configure.png")));
		changeSenderButton.setPreferredSize(new Dimension(93, 25));
		changeSenderButton.setFont(new Font("Dialog", Font.PLAIN, 12));
		final GridBagConstraints gbc_changeSenderButton = new GridBagConstraints();
		gbc_changeSenderButton.fill = GridBagConstraints.BOTH;
		gbc_changeSenderButton.insets = new Insets(0, 0, 5, 5);
		gbc_changeSenderButton.gridx = 9;
		gbc_changeSenderButton.gridy = 9;
		contentPanel.add(changeSenderButton, gbc_changeSenderButton);

		cleanMessageStoreButton = new JButton("Clean");
		cleanMessageStoreButton.setIcon(new ImageIcon(FIXPusher.class.getResource("/net/sourceforge/fixpusher/view/images/16x16/edit-delete.png")));
		cleanMessageStoreButton.setFont(new Font("Dialog", Font.PLAIN, 12));

		cleanMessageStoreButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {

				final Object[] options = { "Yes", "No" };
				final int n = JOptionPane.showOptionDialog(SettingsPanel.this, "Are you sure you want to remove the files in the message store from disk?",
						"Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
						new ImageIcon(FIXPusher.class.getResource("/net/sourceforge/fixpusher/view/images/22x22/messagebox_warning.png")), options, options[1]);

				if (n == 0) {

					SettingsPanel.this.fixProperties.cleanMessageStore();
					setDirty(false);
				}
			}
		});

		final JLabel messageStoreLabel = new JLabel("Message store");
		messageStoreLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
		final GridBagConstraints gbc_messageStoreLabel = new GridBagConstraints();
		gbc_messageStoreLabel.anchor = GridBagConstraints.WEST;
		gbc_messageStoreLabel.insets = new Insets(0, 50, 5, 5);
		gbc_messageStoreLabel.gridx = 5;
		gbc_messageStoreLabel.gridy = 10;
		contentPanel.add(messageStoreLabel, gbc_messageStoreLabel);
		final GridBagConstraints gbc_cleanMessageStoreButton = new GridBagConstraints();
		gbc_cleanMessageStoreButton.fill = GridBagConstraints.BOTH;
		gbc_cleanMessageStoreButton.insets = new Insets(0, 0, 5, 5);
		gbc_cleanMessageStoreButton.gridx = 9;
		gbc_cleanMessageStoreButton.gridy = 10;
		contentPanel.add(cleanMessageStoreButton, gbc_cleanMessageStoreButton);

		final JPanel bottomFillPanel = new JPanel();
		bottomFillPanel.setOpaque(false);
		final GridBagConstraints gbc_bottomFillPanel = new GridBagConstraints();
		gbc_bottomFillPanel.insets = new Insets(0, 0, 0, 5);
		gbc_bottomFillPanel.gridwidth = 9;
		gbc_bottomFillPanel.fill = GridBagConstraints.BOTH;
		gbc_bottomFillPanel.gridx = 0;
		gbc_bottomFillPanel.gridy = 12;
		contentPanel.add(bottomFillPanel, gbc_bottomFillPanel);
		add(scrollPane, BorderLayout.CENTER);

		final Thread thread = new Thread() {

			@Override
			public void run() {

				inetAdressCheck();
			}
		};
		thread.start();

		checkConsistency(true);

	}

	/**
	 * Check consistency.
	 *
	 * @param valid the valid
	 */
	public void checkConsistency(boolean valid) {

		ImageIcon statusIcon = null;

		String statusText = null;

		consistent = true;
		boolean dirty = false;

		// check acceptor

		if (acceptorRadioButton.isSelected()) {

			sequenceResetLabel.setEnabled(true);
			seqResetNoRadioButton.setEnabled(true);
			seqResetYesRadioButton.setEnabled(true);

			heartbeatLabel.setEnabled(false);
			heartbeatField.setEnabled(false);
			heartbeatWarningLabel.setIcon(null);

			if (!fixProperties.getConnectionType().equals("acceptor"))
				dirty = true;

		}
		else {

			sequenceResetLabel.setEnabled(false);
			seqResetNoRadioButton.setEnabled(false);
			seqResetYesRadioButton.setEnabled(false);

			heartbeatLabel.setEnabled(true);
			heartbeatField.setEnabled(true);

			if (!fixProperties.getConnectionType().equals("initiator"))
				dirty = true;
		}

		if (seqResetYesRadioButton.isSelected() && !fixProperties.getSendResetSeqNumFlag().equals("Y"))
			dirty = true;

		if (seqResetNoRadioButton.isSelected() && !fixProperties.getSendResetSeqNumFlag().equals("N"))
			dirty = true;

		// check target comp id

		valid = true;

		if (targetCompIDField.getText().trim().length() == 0) {

			statusIcon = bugIcon;
			statusText = "Target comp ID is empty.";

			targetCompIDWarningLabel.setToolTipText(statusText);
			targetCompIDWarningLabel.setIcon(bugIcon);

			valid = false;

		}
		else {

			targetCompIDWarningLabel.setToolTipText(null);
			targetCompIDWarningLabel.setIcon(null);

			if (!fixProperties.getTargetCompID().equals(targetCompIDField.getText()))
				dirty = true;
		}

		consistent = consistent && valid;

		// check sender comp id

		valid = true;

		if (senderCompIDField.getText().trim().length() == 0) {

			statusIcon = bugIcon;
			statusText = "Sender comp ID is empty.";

			senderCompIDWarningLabel.setToolTipText(statusText);
			senderCompIDWarningLabel.setIcon(bugIcon);

			valid = false;

		}
		else {

			senderCompIDWarningLabel.setToolTipText(null);
			senderCompIDWarningLabel.setIcon(null);

			if (!fixProperties.getSenderCompID().equals(senderCompIDField.getText()))
				dirty = true;
		}

		consistent = consistent && valid;

		// check socket port

		valid = true;

		try {

			final int value = Integer.parseInt(socketPortField.getText());

			if (value == 0)
				valid = false;

			if (!fixProperties.getSocketPort().equals(socketPortField.getText()))
				dirty = true;

		}
		catch (final NumberFormatException e) {

			valid = false;

		}

		if (!valid) {

			statusIcon = bugIcon;
			statusText = "Socket port is not a number > 0.";

			socketPortWarningLabel.setToolTipText(statusText);
			socketPortWarningLabel.setIcon(bugIcon);
		}
		else {

			socketPortWarningLabel.setToolTipText(null);
			socketPortWarningLabel.setIcon(null);
		}

		consistent = consistent && valid;

		// check heartbeat

		valid = true;

		try {

			final int value = Integer.parseInt(heartbeatField.getText());

			if (value == 0)
				valid = false;
		}
		catch (final NumberFormatException e) {

			valid = false;
		}

		if (!valid) {

			statusIcon = bugIcon;
			statusText = "Heartbeat is not a number > 0.";

			heartbeatWarningLabel.setToolTipText(statusText);
			heartbeatWarningLabel.setIcon(bugIcon);
		}
		else {

			heartbeatWarningLabel.setToolTipText(null);
			heartbeatWarningLabel.setIcon(null);
			if (!fixProperties.getHeartbeat().equals(heartbeatField.getText()))
				dirty = true;
		}

		consistent = consistent && valid;

		// check begin string

		transportDataDictionaryField.setEnabled(false);
		transportDataDictionaryButton.setEnabled(false);
		transportDataDictionaryLabel.setEnabled(false);

		if (beginStringField.getText().trim().length() == 0) {

			statusIcon = bugIcon;
			statusText = "Begin string is empty.";

			beginStringWarningLabel.setToolTipText(statusText);
			beginStringWarningLabel.setIcon(bugIcon);

			consistent = consistent && false;

		}
		else {

			final String[] beginStringSplit = beginStringField.getText().trim().split("\\.");

			valid = true;

			if (beginStringSplit.length != 3)
				valid = false;

			else if (!(beginStringSplit[0].equals("FIX") || beginStringSplit[0].equals("FIXT")))
				valid = false;

			else
				try {

					Integer.parseInt(beginStringSplit[1]);
					Integer.parseInt(beginStringSplit[2]);
					if (beginStringSplit[0].equals("FIXT")) {
						transportDataDictionaryField.setEnabled(true);
						transportDataDictionaryButton.setEnabled(true);
						transportDataDictionaryLabel.setEnabled(true);
					}
				}
				catch (final NumberFormatException e) {

					valid = false;
				}
			if (!valid) {

				if (statusIcon != bugIcon) {

					statusIcon = warningIcon;
					statusText = "Begin string is not in standard format.";
				}

				beginStringWarningLabel.setToolTipText("Begin string is not in standard format.");
				beginStringWarningLabel.setIcon(warningIcon);

			}
			else {

				beginStringWarningLabel.setToolTipText(null);
				beginStringWarningLabel.setIcon(null);
			}

			if (!fixProperties.getBeginString().equals(beginStringField.getText().trim()))
				dirty = true;

		}

		// check transport dictionary

		if (transportDataDictionaryField.isEnabled()) {

			valid = true;

			final File file = new File(transportDataDictionaryField.getText());
			String checkResult = null;

			if (!file.isFile())
				checkResult = "The path does not point to a file.";

			else if (!file.canRead())
				checkResult = "The file is not readable.";

			else if (!DictionaryParser.check(file))
				checkResult = "The file does not contain valid data.";

			if (checkResult != null) {

				statusIcon = bugIcon;
				statusText = checkResult;

				transportDataDictionaryWarningLabel.setToolTipText(checkResult);
				transportDataDictionaryWarningLabel.setIcon(bugIcon);

				valid = false;
			}
			else {

				transportDataDictionaryWarningLabel.setToolTipText(null);
				transportDataDictionaryWarningLabel.setIcon(null);
			}

			if (!fixProperties.getTransportDataDictionary().equals(transportDataDictionaryField.getText()))
				dirty = true;

			consistent = consistent && valid;

		}
		else {

			transportDataDictionaryWarningLabel.setToolTipText(null);
			transportDataDictionaryWarningLabel.setIcon(null);
		}

		// check dictionary

		valid = true;

		final File file = new File(dataDictionaryField.getText());
		String checkResult = null;

		if (!file.isFile())
			checkResult = "The path does not point to a file.";

		else if (!file.canRead())
			checkResult = "The file is not readable.";

		else if (!DictionaryParser.check(file))
			checkResult = "The file does not contain valid data.";

		if (checkResult != null) {

			statusIcon = bugIcon;
			statusText = checkResult;

			dataDictionaryWarningLabel.setToolTipText(checkResult);
			dataDictionaryWarningLabel.setIcon(bugIcon);

			valid = false;

		}
		else {

			dataDictionaryWarningLabel.setToolTipText(null);
			dataDictionaryWarningLabel.setIcon(null);

		}

		if (!fixProperties.getDataDictionary().equals(dataDictionaryField.getText()))
			dirty = true;

		consistent = consistent && valid;

		// check log path

		valid = true;

		checkResult = checkPath(fileLogPathField, fileLogPathWarningLabel);

		if (checkResult != null) {

			statusText = checkResult;
			statusIcon = bugIcon;

			valid = false;
		}

		if (!fixProperties.getFileLogPath().equals(fileLogPathField.getText()))
			dirty = true;

		consistent = consistent && valid;

		// check store path

		valid = true;

		checkResult = checkPath(fileStorePathField, fileStorePathWarningLabel);

		if (checkResult != null) {

			statusText = checkResult;
			statusIcon = bugIcon;
			valid = false;
		}

		if (!fixProperties.getFileStorePath().equals(fileStorePathField.getText()))
			dirty = true;

		consistent = consistent && valid;

		if (!fixProperties.getSocketAdress().equals(socketAdressField.getText()))
			dirty = true;

		setDirty(dirty);

		if (statusText != null)
			mainPanel.setStatusInfo(statusIcon, statusText);

	}

	private String checkPath(final JTextField jTextField, final JLabel jLabel) {

		final File file = new File(jTextField.getText());
		String checkResult = null;

		if (!file.isDirectory())
			checkResult = "The path is not a directory.";

		else if (!file.canRead())
			checkResult = "The path is not readable.";

		else if (!file.canWrite())
			checkResult = "The path is not writable.";

		if (checkResult != null) {

			jLabel.setToolTipText(checkResult);
			jLabel.setIcon(bugIcon);
		}
		else {

			jLabel.setToolTipText(null);
			jLabel.setIcon(null);
		}

		return checkResult;
	}


	private void inetAdressCheck() {

		while (visible)
			try {

				final String adress = checkAdressQueue.take();

				if (visible)
					try {

						InetAddress.getByName(adress);
						socketAdressWarningLabel.setToolTipText(null);
						socketAdressWarningLabel.setIcon(null);
						checkConsistency(true);
					}
					catch (final UnknownHostException e) {

						checkConsistency(false);
						socketAdressWarningLabel.setToolTipText("Unknown host");
						socketAdressWarningLabel.setIcon(warningIcon);
						mainPanel.setStatusInfo(warningIcon, "Unknown host");
					}
			}
			catch (final InterruptedException e1) {
			}
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.fixpusher.view.AbstractMainPanelContent#isConsistent()
	 */
	@Override
	public boolean isConsistent() {

		return consistent;
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.fixpusher.view.AbstractMainPanelContent#save()
	 */
	@Override
	public void save() {

		if (acceptorRadioButton.isSelected())
			fixProperties.setConnectionType("acceptor");
		
		else
			fixProperties.setConnectionType("initiator");

		if (seqResetYesRadioButton.isSelected())
			fixProperties.setSendResetSeqNumFlag("Y");
		
		else
			fixProperties.setSendResetSeqNumFlag("N");

		fixProperties.setSocketPort(socketPortField.getText());

		fixProperties.setHeartbeat(heartbeatField.getText());

		fixProperties.setSenderCompID(senderCompIDField.getText());

		fixProperties.setTargetCompID(targetCompIDField.getText());

		fixProperties.setBeginString(beginStringField.getText().trim());

		fixProperties.setDataDictionary(dataDictionaryField.getText());

		fixProperties.setTransportDataDictionary(transportDataDictionaryField.getText());

		fixProperties.setFileStorePath(fileStorePathField.getText());

		fixProperties.setFileLogPath(fileLogPathField.getText());

		fixProperties.setSocketAdress(socketAdressField.getText());

		fixProperties.store();

		checkConsistency(true);

	}

	/* (non-Javadoc)
	 * @see net.sourceforge.fixpusher.view.AbstractMainPanelContent#setDirty(boolean)
	 */
	@Override
	public void setDirty(final boolean dirty) {

		super.setDirty(dirty);

		if (dirty) {

			nextTargetSeqNumberField.setEnabled(false);
			nextSenderSeqNumberField.setEnabled(false);
			cleanMessageStoreButton.setEnabled(false);
		}
		else {

			nextSenderSeqNumberField.setText(fixProperties.getNextSenderSequenceNumber());
			nextTargetSeqNumberField.setText(fixProperties.getNextTargetSequenceNumber());

			nextTargetSeqNumberField.setEnabled(true);
			nextSenderSeqNumberField.setEnabled(true);
			cleanMessageStoreButton.setEnabled(true);
		}

		changeSenderButton.setEnabled(false);
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.fixpusher.view.AbstractMainPanelContent#setStatus(net.sourceforge.fixpusher.control.FIXConnectionListener.Status)
	 */
	@Override
	public void setStatus(final Status status) {

		if (status == Status.DISCONNECTED) {
			
			nextTargetSeqNumberField.setText(fixProperties.getNextTargetSequenceNumber());
			nextSenderSeqNumberField.setText(fixProperties.getNextSenderSequenceNumber());
			changeSenderButton.setEnabled(false);
			cleanMessageStoreButton.setEnabled(true);
			acceptorRadioButton.setEnabled(true);
			initiatorRadioButton.setEnabled(true);
			
			if (acceptorRadioButton.isSelected()) {
				
				seqResetYesRadioButton.setEnabled(true);
				seqResetNoRadioButton.setEnabled(true);
			}
			
			fileStorePathButton.setEnabled(true);
			fileLogPathButton.setEnabled(true);
			dataDictionaryButton.setEnabled(true);
			dataDictionaryField.setEditable(true);
			
			if (beginStringField.getText().startsWith("FIXT.")) {
				
				transportDataDictionaryButton.setEnabled(true);
				transportDataDictionaryField.setEditable(true);
			}
			
			fileStorePathField.setEditable(true);
			fileLogPathField.setEditable(true);
			beginStringField.setEditable(true);
			heartbeatField.setEditable(true);
			socketAdressField.setEditable(true);
			socketPortField.setEditable(true);
			senderCompIDField.setEditable(true);
			targetCompIDField.setEditable(true);
			nextSenderSeqNumberField.setEditable(true);
			nextTargetSeqNumberField.setEditable(true);
		}
		else {
			
			changeSenderButton.setEnabled(false);
			cleanMessageStoreButton.setEnabled(false);
			acceptorRadioButton.setEnabled(false);
			initiatorRadioButton.setEnabled(false);
			seqResetYesRadioButton.setEnabled(false);
			seqResetNoRadioButton.setEnabled(false);
			fileStorePathButton.setEnabled(false);
			fileLogPathButton.setEnabled(false);
			dataDictionaryButton.setEnabled(false);
			transportDataDictionaryButton.setEnabled(false);
			transportDataDictionaryField.setEditable(false);
			fileStorePathField.setEditable(false);
			fileLogPathField.setEditable(false);
			dataDictionaryField.setEditable(false);
			beginStringField.setEditable(false);
			heartbeatField.setEditable(false);
			socketAdressField.setEditable(false);
			socketPortField.setEditable(false);
			senderCompIDField.setEditable(false);
			targetCompIDField.setEditable(false);
			nextSenderSeqNumberField.setEditable(false);
			nextTargetSeqNumberField.setEditable(false);
		}
	}

	private void updateSocketAdress() {

		try {

			checkAdressQueue.put(socketAdressField.getText());
		}
		catch (final InterruptedException e) {

			ExceptionDialog.showException(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#removeNotify()
	 */
	@Override
	public void removeNotify() {
		
		visible = false;
		checkAdressQueue.add("127.0.0.1");

		super.removeNotify();
	}


}
