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

import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.LinkedList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;

import net.sourceforge.fixpusher.control.ExcelExport;
import net.sourceforge.fixpusher.control.FIXConnectionListener;
import net.sourceforge.fixpusher.control.FIXConnector;
import net.sourceforge.fixpusher.model.FIXProperties;
import net.sourceforge.fixpusher.view.dialog.AboutDialog;
import net.sourceforge.fixpusher.view.dialog.NewProjectDialog;
import net.sourceforge.fixpusher.view.dialog.OpenProjectDialog;
import net.sourceforge.fixpusher.view.dialog.RemoveProjectDialog;
import net.sourceforge.fixpusher.view.message.FIXMainMessagePanel;
import quickfix.Message;

/**
 * The Class TopPanel.
 */
public class TopPanel extends JPanel implements FIXConnectionListener, MainPanelListener {

	private static final long serialVersionUID = 1L;

	private Desktop desktop = null;

	private JMenuItem editDataDictionaryMenuItem = null;

	private ToolbarButton exportToExcelButton = null;

	private JMenuItem exportToExcelMenuItem = null;

	private FIXConnector fixConnector = null;

	private FIXProperties fixProperties = null;

	private LinkedList<Message> history = new LinkedList<Message>();

	private MainPanel mainPanel = null;

	private Message message = null;

	private JMenuItem saveMenuItem = null;

	private JMenuItem stopSessionMenuItem = null;

	private ToolbarButton newProjectButton = null;

	private JMenuItem newProjectMenuItem = null;

	private Message nextMessage = null;

	private JButton nextMessageButton = null;

	private JMenuItem nextMessageMenuItem = null;

	private ToolbarButton openProjectButton = null;

	private JMenuItem openProjectMenuItem = null;

	private Message previousMessage = null;

	private JButton previousMessageButton = null;

	private JMenuItem previousMessageMenuItem = null;

	private JMenuItem removeProjectMenuItem = null;

	private ToolbarButton reportButton = null;

	private JButton saveButton = null;

	private JButton sendMessageButton = null;

	private JMenuItem sendMessageMenuItem = null;

	private ToolbarButton showDataDictionaryButton = null;

	private JButton startSessionButton = null;

	private JMenuItem startSessionMenuItem = null;

	private Status status = Status.DISCONNECTED;

	private StatusPanel statusPanel = null;

	private JButton stopSessionButton = null;

	/**
	 * Instantiates a new top panel.
	 * 
	 * @param fixProperties
	 *            the fix properties
	 * @param fixConnector
	 *            the fix connector
	 * @param mainPanel
	 *            the main panel
	 */
	public TopPanel(final FIXProperties fixProperties, final FIXConnector fixConnector, final MainPanel mainPanel) {

		super();

		this.fixProperties = fixProperties;
		this.mainPanel = mainPanel;
		this.fixConnector = fixConnector;
		this.statusPanel = new StatusPanel();

		fixConnector.addFIXConnectionListener(this);

		initDesktop();

		final GridBagLayout gbl_topPanel = new GridBagLayout();
		gbl_topPanel.columnWidths = new int[] { 0, 0 };
		gbl_topPanel.rowHeights = new int[] { 0, 0 };
		gbl_topPanel.columnWeights = new double[] { 1.0, 0.0 };
		gbl_topPanel.rowWeights = new double[] { 0.0, 0.0 };
		setLayout(gbl_topPanel);

		final JMenuBar menuBar = new JMenuBar();
		final GridBagConstraints gbc_menuBar = new GridBagConstraints();
		gbc_menuBar.gridwidth = 2;
		gbc_menuBar.fill = GridBagConstraints.HORIZONTAL;
		gbc_menuBar.gridx = 0;
		gbc_menuBar.gridy = 0;
		add(menuBar, gbc_menuBar);

		final JMenu fileMenu = new JMenu("File");
		fileMenu.setFont(new Font("Dialog", Font.PLAIN, 12));
		menuBar.add(fileMenu);

		final JMenuItem mntmNewMenuItem = new JMenuItem("Exit");
		mntmNewMenuItem.setIcon(new ImageIcon(FIXPusher.class.getResource("/net/sourceforge/fixpusher/view/images/16x16/cancel.png")));
		mntmNewMenuItem.setFont(new Font("Dialog", Font.PLAIN, 12));
		mntmNewMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {

				exit();
			}
		});

		newProjectMenuItem = new JMenuItem("New Project ...", 'N');
		newProjectMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		newProjectMenuItem.setEnabled(true);
		newProjectMenuItem.setIcon(new ImageIcon(FIXPusher.class.getResource("/net/sourceforge/fixpusher/view/images/16x16/window_new.png")));
		newProjectMenuItem.setFont(new Font("Dialog", Font.PLAIN, 12));
		fileMenu.add(newProjectMenuItem);

		newProjectMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {

				createProject();
			}
		});

		openProjectMenuItem = new JMenuItem("Open Project ...", 'O');
		openProjectMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		openProjectMenuItem.setEnabled(true);
		openProjectMenuItem.setIcon(new ImageIcon(FIXPusher.class.getResource("/net/sourceforge/fixpusher/view/images/16x16/file-manager.png")));
		openProjectMenuItem.setFont(new Font("Dialog", Font.PLAIN, 12));
		fileMenu.add(openProjectMenuItem);

		openProjectMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {

				openProject();
			}
		});

		removeProjectMenuItem = new JMenuItem("Remove Project ...");
		removeProjectMenuItem.setEnabled(true);
		removeProjectMenuItem.setIcon(new ImageIcon(FIXPusher.class.getResource("/net/sourceforge/fixpusher/view/images/16x16/edit-delete.png")));
		removeProjectMenuItem.setFont(new Font("Dialog", Font.PLAIN, 12));
		fileMenu.add(removeProjectMenuItem);

		removeProjectMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {

				removeProject();
			}
		});

		saveMenuItem = new JMenuItem("Save", 'S');
		saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		saveMenuItem.setEnabled(false);
		saveMenuItem.setIcon(new ImageIcon(FIXPusher.class.getResource("/net/sourceforge/fixpusher/view/images/16x16/3floppy_unmount.png")));
		saveMenuItem.setFont(new Font("Dialog", Font.PLAIN, 12));
		fileMenu.add(saveMenuItem);

		saveMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {

				TopPanel.this.mainPanel.save();
			}
		});

		final JSeparator separator = new JSeparator();
		fileMenu.add(separator);
		fileMenu.add(mntmNewMenuItem);

		final JMenu editMenu = new JMenu("Edit");
		editMenu.setFont(new Font("Dialog", Font.PLAIN, 12));
		menuBar.add(editMenu);

		final JMenuItem settingsMenuItem = new JMenuItem("Settings");
		settingsMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, ActionEvent.CTRL_MASK));
		settingsMenuItem.setIcon(new ImageIcon(FIXPusher.class.getResource("/net/sourceforge/fixpusher/view/images/16x16/utilities.png")));
		settingsMenuItem.setFont(new Font("Dialog", Font.PLAIN, 12));
		editMenu.add(settingsMenuItem);

		settingsMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {

				TopPanel.this.mainPanel.showSettingsPanel(status);
			}
		});

		startSessionMenuItem = new JMenuItem("Start Session");
		startSessionMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F6, ActionEvent.CTRL_MASK));
		startSessionMenuItem.setIcon(new ImageIcon(FIXPusher.class.getResource("/net/sourceforge/fixpusher/view/images/16x16/agt_login.png")));
		startSessionMenuItem.setFont(new Font("Dialog", Font.PLAIN, 12));
		startSessionMenuItem.setEnabled(true);
		editMenu.add(startSessionMenuItem);

		startSessionMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {

				connect();
			}
		});

		stopSessionMenuItem = new JMenuItem("Stop Session");
		stopSessionMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F7, ActionEvent.CTRL_MASK));
		stopSessionMenuItem.setIcon(new ImageIcon(FIXPusher.class.getResource("/net/sourceforge/fixpusher/view/images/16x16/shutdown.png")));
		stopSessionMenuItem.setFont(new Font("Dialog", Font.PLAIN, 12));
		stopSessionMenuItem.setEnabled(false);
		editMenu.add(stopSessionMenuItem);

		stopSessionMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {

				disconnect();
			}
		});

		editMenu.add(new JSeparator(SwingConstants.HORIZONTAL));

		previousMessageMenuItem = new JMenuItem("Previous Message");
		previousMessageMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F9, ActionEvent.CTRL_MASK));
		previousMessageMenuItem.setIcon(new ImageIcon(FIXPusher.class.getResource("/net/sourceforge/fixpusher/view/images/16x16/player_rew.png")));
		previousMessageMenuItem.setFont(new Font("Dialog", Font.PLAIN, 12));
		previousMessageMenuItem.setEnabled(false);
		editMenu.add(previousMessageMenuItem);

		previousMessageMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {

				openMessage(previousMessage);
			}
		});

		nextMessageMenuItem = new JMenuItem("Next Message");
		nextMessageMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F10, ActionEvent.CTRL_MASK));
		nextMessageMenuItem.setIcon(new ImageIcon(FIXPusher.class.getResource("/net/sourceforge/fixpusher/view/images/16x16/player_fwd.png")));
		nextMessageMenuItem.setFont(new Font("Dialog", Font.PLAIN, 12));
		nextMessageMenuItem.setEnabled(false);
		editMenu.add(nextMessageMenuItem);

		nextMessageMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {

				openMessage(previousMessage);
			}
		});

		sendMessageMenuItem = new JMenuItem("Send Message");
		sendMessageMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F12, ActionEvent.CTRL_MASK));
		sendMessageMenuItem.setIcon(new ImageIcon(FIXPusher.class.getResource("/net/sourceforge/fixpusher/view/images/16x16/player_play.png")));
		sendMessageMenuItem.setFont(new Font("Dialog", Font.PLAIN, 12));
		sendMessageMenuItem.setEnabled(false);
		editMenu.add(sendMessageMenuItem);

		sendMessageMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {

				send();
			}
		});

		final JMenu extrasMenu = new JMenu("Extras");
		extrasMenu.setFont(new Font("Dialog", Font.PLAIN, 12));
		menuBar.add(extrasMenu);

		editDataDictionaryMenuItem = new JMenuItem("Edit Data Dictionary", 'D');
		editDataDictionaryMenuItem.setIcon(new ImageIcon(FIXPusher.class.getResource("/net/sourceforge/fixpusher/view/images/16x16/txt.png")));
		editDataDictionaryMenuItem.setFont(new Font("Dialog", Font.PLAIN, 12));
		editDataDictionaryMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, ActionEvent.CTRL_MASK));
		extrasMenu.add(editDataDictionaryMenuItem);

		editDataDictionaryMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {

				TopPanel.this.mainPanel.showDataDictionaryPanel(status);
			}
		});

		final JMenuItem logReportMenuItem = new JMenuItem("Open Log Report", 'R');
		logReportMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.CTRL_MASK));
		logReportMenuItem.setIcon(new ImageIcon(FIXPusher.class.getResource("/net/sourceforge/fixpusher/view/images/16x16/kchart_chrt.png")));
		logReportMenuItem.setFont(new Font("Dialog", Font.PLAIN, 12));
		extrasMenu.add(logReportMenuItem);

		logReportMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {

				TopPanel.this.mainPanel.showFIXChartPanel();
			}
		});

		exportToExcelMenuItem = new JMenuItem("Export to Excel", 'E');
		exportToExcelMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK));
		exportToExcelMenuItem.setIcon(new ImageIcon(FIXPusher.class.getResource("/net/sourceforge/fixpusher/view/images/16x16/application-vnd.ms-excel.png")));
		exportToExcelMenuItem.setFont(new Font("Dialog", Font.PLAIN, 12));
		extrasMenu.add(exportToExcelMenuItem);

		exportToExcelMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {

				exportToExcel();

			}
		});

		final JMenu helpMenu = new JMenu("Help");
		helpMenu.setFont(new Font("Dialog", Font.PLAIN, 12));
		menuBar.add(helpMenu);

		if (desktop != null) {

			final JMenuItem helpMenuItem = new JMenuItem("FIX Pusher Help", 'F');
			helpMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
			helpMenuItem.setIcon(new ImageIcon(FIXPusher.class.getResource("/net/sourceforge/fixpusher/view/images/16x16/help.png")));
			helpMenuItem.setFont(new Font("Dialog", Font.PLAIN, 12));
			helpMenu.add(helpMenuItem);

			helpMenuItem.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(final ActionEvent e) {

					try {

						final File file = new File("doc/html/help.html");
						desktop.open(file);
					}
					catch (final Exception e1) {

						JOptionPane.showMessageDialog(TopPanel.this.mainPanel, e1.getMessage(), "Connection Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			});
		}

		final JMenuItem mntmNewMenuItem_8 = new JMenuItem("About FIX Pusher");
		mntmNewMenuItem_8.setIcon(new ImageIcon(FIXPusher.class.getResource("/net/sourceforge/fixpusher/view/images/16x16/documentinfo.png")));
		mntmNewMenuItem_8.setFont(new Font("Dialog", Font.PLAIN, 12));
		helpMenu.add(mntmNewMenuItem_8);
		mntmNewMenuItem_8.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {

				final AboutDialog aboutDialog = new AboutDialog();
				aboutDialog.setLocationRelativeTo(TopPanel.this.mainPanel);
				aboutDialog.setVisible(true);
			}
		});

		final JToolBar toolBar = new JToolBar() {

			private static final long serialVersionUID = 1L;
			ImageIcon backImage = new ImageIcon(FIXPusher.class.getResource("/net/sourceforge/fixpusher/view/images/background.png"));
			Image image = backImage.getImage();

			@Override
			public void paintComponent(final Graphics g) {

				super.paintComponent(g);
				for (int i = 0; i < getWidth(); i = i + backImage.getIconWidth())
					g.drawImage(image, i, 0, null);
			}
		};

		toolBar.setFloatable(false);
		toolBar.setOpaque(false);
		toolBar.setBorder(new EmptyBorder(0, 0, 0, 0));
		toolBar.setBorderPainted(false);
		toolBar.setPreferredSize(new Dimension(18, 29));
		final GridBagConstraints gbc_toolBar = new GridBagConstraints();
		gbc_toolBar.fill = GridBagConstraints.HORIZONTAL;
		gbc_toolBar.gridx = 0;
		gbc_toolBar.gridy = 1;
		add(toolBar, gbc_toolBar);

		newProjectButton = new ToolbarButton(new ImageIcon(FIXPusher.class.getResource("/net/sourceforge/fixpusher/view/images/22x22/window_new.png")),
				"Create Project");
		newProjectButton.setEnabled(true);
		toolBar.add(newProjectButton);

		newProjectButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {

				createProject();
			}
		});

		openProjectButton = new ToolbarButton(new ImageIcon(FIXPusher.class.getResource("/net/sourceforge/fixpusher/view/images/22x22/file-manager.png")),
				"Open Project");
		openProjectButton.setEnabled(true);
		toolBar.add(openProjectButton);

		openProjectButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {

				openProject();

			}
		});

		checkProjectCount();

		saveButton = new ToolbarButton(new ImageIcon(FIXPusher.class.getResource("/net/sourceforge/fixpusher/view/images/22x22/filesave.png")), "Save Settings");
		toolBar.add(saveButton);

		saveButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {

				TopPanel.this.mainPanel.save();
			}
		});

		final JLabel separatorLabel = new JLabel();
		separatorLabel.setIcon(new ImageIcon(FIXPusher.class.getResource("/net/sourceforge/fixpusher/view/images/separator.png")));
		separatorLabel.setVerticalAlignment(SwingConstants.TOP);
		toolBar.add(separatorLabel);

		final JButton openSettingsButton = new ToolbarButton(new ImageIcon(
				FIXPusher.class.getResource("/net/sourceforge/fixpusher/view/images/22x22/utilities.png")), "Open Settings");
		openSettingsButton.setEnabled(true);
		toolBar.add(openSettingsButton);

		openSettingsButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {

				TopPanel.this.mainPanel.showSettingsPanel(status);
			}
		});

		startSessionButton = new ToolbarButton(new ImageIcon(FIXPusher.class.getResource("/net/sourceforge/fixpusher/view/images/22x22/agt_login.png")),
				"Start Session");
		startSessionButton.setEnabled(false);
		toolBar.add(startSessionButton);

		startSessionButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {

				connect();
			}
		});

		stopSessionButton = new ToolbarButton(new ImageIcon(FIXPusher.class.getResource("/net/sourceforge/fixpusher/view/images/22x22/shutdown.png")),
				"Stop Session");
		toolBar.add(stopSessionButton);

		stopSessionButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {

				TopPanel.this.fixConnector.disconnect();
			}
		});

		final JLabel separatorLabel2 = new JLabel();
		separatorLabel2.setIcon(new ImageIcon(FIXPusher.class.getResource("/net/sourceforge/fixpusher/view/images/separator.png")));
		toolBar.add(separatorLabel2);

		previousMessageButton = new ToolbarButton(new ImageIcon(FIXPusher.class.getResource("/net/sourceforge/fixpusher/view/images/22x22/player_rew.png")),
				"Previous Message");
		toolBar.add(previousMessageButton);

		previousMessageButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {

				openMessage(previousMessage);
			}
		});

		nextMessageButton = new ToolbarButton(new ImageIcon(FIXPusher.class.getResource("/net/sourceforge/fixpusher/view/images/22x22/player_fwd.png")),
				"Next Message");
		toolBar.add(nextMessageButton);

		nextMessageButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {

				openMessage(nextMessage);
			}
		});

		sendMessageButton = new ToolbarButton(new ImageIcon(FIXPusher.class.getResource("/net/sourceforge/fixpusher/view/images/22x22/player_play.png")),
				"Send Message");
		toolBar.add(sendMessageButton);

		sendMessageButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {

				send();
			}
		});

		final JLabel separatorLabel3 = new JLabel();
		separatorLabel3.setIcon(new ImageIcon(FIXPusher.class.getResource("/net/sourceforge/fixpusher/view/images/separator.png")));
		toolBar.add(separatorLabel3);

		showDataDictionaryButton = new ToolbarButton(new ImageIcon(FIXPusher.class.getResource("/net/sourceforge/fixpusher/view/images/22x22/txt.png")),
				"Edit Data Dictionary");
		showDataDictionaryButton.setEnabled(true);
		toolBar.add(showDataDictionaryButton);

		showDataDictionaryButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {

				TopPanel.this.mainPanel.showDataDictionaryPanel(status);
			}
		});

		reportButton = new ToolbarButton(new ImageIcon(FIXPusher.class.getResource("/net/sourceforge/fixpusher/view/images/22x22/kchart_chrt.png")),
				"Open Log Report");
		reportButton.setEnabled(true);
		toolBar.add(reportButton);

		reportButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {

				TopPanel.this.mainPanel.showFIXChartPanel();

			}
		});

		exportToExcelButton = new ToolbarButton(new ImageIcon(FIXPusher.class.getResource("/net/sourceforge/fixpusher/view/images/22x22/excel.png")),
				"Export to Excel");
		exportToExcelButton.setEnabled(true);
		toolBar.add(exportToExcelButton);

		exportToExcelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {

				exportToExcel();

			}

		});

		if (desktop != null) {

			final JLabel separatorLabel4 = new JLabel();
			separatorLabel4.setIcon(new ImageIcon(FIXPusher.class.getResource("/net/sourceforge/fixpusher/view/images/separator.png")));
			toolBar.add(separatorLabel4);

			final ToolbarButton helpButton = new ToolbarButton(new ImageIcon(
					FIXPusher.class.getResource("/net/sourceforge/fixpusher/view/images/22x22/help.png")), "FIX Pusher Help");
			helpButton.setEnabled(true);
			toolBar.add(helpButton);

			helpButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(final ActionEvent e) {

					try {
						final File file = new File("doc/html/help.html");
						desktop.open(file);
					}
					catch (final Exception e1) {

					}
				}
			});

		}

		final JLabel logoLabel = new JLabel();
		logoLabel.setIcon(new ImageIcon(FIXPusher.class.getResource("/net/sourceforge/fixpusher/view/images/toolbar-logo.png")));
		final GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.anchor = GridBagConstraints.NORTH;
		gbc_lblNewLabel.gridx = 1;
		gbc_lblNewLabel.gridy = 1;
		add(logoLabel, gbc_lblNewLabel);

		final JLabel backgroundLabel = new JLabel();
		backgroundLabel.setIcon(new ImageIcon(FIXPusher.class.getResource("/net/sourceforge/fixpusher/view/images/background.png")));
		toolBar.add(backgroundLabel);

		connectionStatusChanged(Status.DISCONNECTED, "Disconnected");
		checkProjectCount();
		onMainPanelChanged();

	}

	private void checkProjectCount() {

		if (fixProperties.getProjects().size() <1) {

			openProjectButton.setEnabled(false);
			openProjectMenuItem.setEnabled(false);
			removeProjectMenuItem.setEnabled(false);

		}
		else {

			openProjectButton.setEnabled(true);
			openProjectMenuItem.setEnabled(true);
			removeProjectMenuItem.setEnabled(true);
		}
	}

	/**
	 * Connect.
	 */
	protected void connect() {

		startSessionButton.setEnabled(false);
		startSessionMenuItem.setEnabled(false);

		final Thread thread = new Thread() {

			@Override
			public void run() {

				try {

					fixConnector.connect();
				}
				catch (final Exception e1) {

					JOptionPane.showMessageDialog(mainPanel, e1.getMessage(), "Connection Error", JOptionPane.ERROR_MESSAGE);
					startSessionButton.setEnabled(true);
					startSessionMenuItem.setEnabled(true);
				}
			}
		};

		thread.start();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sourceforge.fixpusher.control.FIXConnectionListener#
	 * connectionStatusChanged
	 * (net.sourceforge.fixpusher.control.FIXConnectionListener.Status,
	 * java.lang.String)
	 */
	@Override
	public void connectionStatusChanged(final Status status, final String text) {

		this.status = status;

		statusPanel.setStatus(status, text);
		mainPanel.setStatus(status);

		if (status == Status.CONNECTED && mainPanel.isMessage()) {

			sendMessageButton.setEnabled(true);
			sendMessageMenuItem.setEnabled(true);
		}
		else {

			mainPanel.setStatusInfo(new ImageIcon(FIXPusher.class.getResource("/net/sourceforge/fixpusher/view/images/16x16/documentinfo.png")),
					"Hit Ctrl F7 to stop the session.");
			sendMessageButton.setEnabled(false);
			sendMessageMenuItem.setEnabled(false);
		}
		if (status == Status.DISCONNECTED) {

			stopSessionButton.setEnabled(false);
			stopSessionMenuItem.setEnabled(false);

			startSessionButton.setEnabled(true);
			startSessionMenuItem.setEnabled(true);
			newProjectMenuItem.setEnabled(true);
			openProjectMenuItem.setEnabled(true);
			if (fixProperties.getProjects().size() > 0)
				removeProjectMenuItem.setEnabled(true);
			else
				removeProjectMenuItem.setEnabled(false);
			newProjectButton.setEnabled(true);
			openProjectButton.setEnabled(true);
		}
		else {

			stopSessionButton.setEnabled(true);
			stopSessionMenuItem.setEnabled(true);
			startSessionButton.setEnabled(false);
			startSessionMenuItem.setEnabled(false);
			newProjectMenuItem.setEnabled(false);
			openProjectMenuItem.setEnabled(false);
			newProjectButton.setEnabled(false);
			openProjectButton.setEnabled(false);
			removeProjectMenuItem.setEnabled(false);
		}

	}

	private void createProject() {

		final NewProjectDialog openProjectDialog = new NewProjectDialog(fixProperties.getAllProjects());
		openProjectDialog.setLocationRelativeTo(mainPanel);
		openProjectDialog.setVisible(true);

		final String project = openProjectDialog.getProject();

		if (project != null) {
			history.clear();
			fixProperties.createProject(project, openProjectDialog.getTemplate());
			mainPanel.showSettingsPanel(status);
		}

		checkProjectCount();
	}

	/**
	 * Disconnect.
	 */
	protected void disconnect() {

		final Thread thread = new Thread() {

			@Override
			public void run() {

				stopSessionButton.setEnabled(false);
				stopSessionMenuItem.setEnabled(false);

				fixConnector.disconnect();
			}
		};

		thread.start();
	}

	private void exportToExcel() {

		final Thread thread = new Thread() {

			@Override
			public void run() {

				final JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new java.io.File("."));
				chooser.setDialogTitle("Excel Export");
				chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

				final FileFilter fileFilter = new FileFilter() {

					@Override
					public boolean accept(final File file) {

						if (file.isDirectory())
							return true;

						else {

							final String filename = file.getName();
							return filename.toLowerCase().endsWith(".xls");
						}
					}

					@Override
					public String getDescription() {

						return "*.xls";
					}
				};

				chooser.setFileFilter(fileFilter);
				chooser.setAcceptAllFileFilterUsed(false);
				chooser.setApproveButtonText("Export");

				if (chooser.showOpenDialog(mainPanel) == JFileChooser.APPROVE_OPTION) {

					final Component component = TopPanel.this.mainPanel.getContent();

					if (component instanceof FIXMainMessagePanel) {

						final FIXMainMessagePanel fixMainMessagePanel = (FIXMainMessagePanel) component;
						final ExcelExport excelExport = new ExcelExport();

						if (excelExport.exportToExcel(chooser.getSelectedFile().toString(), fixMainMessagePanel.getMessage(), TopPanel.this.fixProperties))
							JOptionPane.showMessageDialog(TopPanel.this.mainPanel, "Message succesfully exported.", "Excel Export",
									JOptionPane.INFORMATION_MESSAGE);

						else
							JOptionPane.showMessageDialog(TopPanel.this.mainPanel, "Message export failed.", "Excel Export", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		};

		thread.start();
	}

	/**
	 * Gets the fix properties.
	 * 
	 * @return the fix properties
	 */
	public FIXProperties getFixProperties() {

		return fixProperties;
	}

	/**
	 * Gets the status panel.
	 * 
	 * @return the status panel
	 */
	public StatusPanel getStatusPanel() {

		return statusPanel;
	}

	private void initDesktop() {
		
		if (java.awt.Desktop.isDesktopSupported()) {
			desktop = java.awt.Desktop.getDesktop();

			if (!desktop.isSupported(java.awt.Desktop.Action.OPEN))
				desktop = null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.sourceforge.fixpusher.view.MainPanelListener#onMainPanelChanged()
	 */
	@Override
	public void onMainPanelChanged() {

		saveButton.setEnabled(mainPanel.isDirty() && mainPanel.isConsistent());
		saveMenuItem.setEnabled(mainPanel.isDirty() && mainPanel.isConsistent());
		startSessionButton.setEnabled(mainPanel.isConsistent() && !mainPanel.isDirty() && status == Status.DISCONNECTED
				&& fixProperties.getDictionaryParser().getMessageList().size() > 0);
		startSessionMenuItem.setEnabled(mainPanel.isConsistent() && !mainPanel.isDirty() && status == Status.DISCONNECTED
				&& fixProperties.getDictionaryParser().getMessageList().size() > 0);
		exportToExcelButton.setEnabled(mainPanel.isMessage());
		exportToExcelMenuItem.setEnabled(mainPanel.isMessage());

		if (mainPanel.isMessage()) {

			if (status == Status.CONNECTED) {

				sendMessageButton.setEnabled(true);
				sendMessageMenuItem.setEnabled(true);
			}
			else {

				sendMessageButton.setEnabled(false);
				sendMessageMenuItem.setEnabled(false);
			}
			if (previousMessage == null) {

				previousMessageButton.setEnabled(false);
				previousMessageMenuItem.setEnabled(false);
			}
			else {

				previousMessageButton.setEnabled(true);
				previousMessageMenuItem.setEnabled(true);
			}
			if (nextMessage == null) {

				nextMessageButton.setEnabled(false);
				nextMessageMenuItem.setEnabled(false);
			}
			else {

				nextMessageButton.setEnabled(true);
				nextMessageMenuItem.setEnabled(true);
			}
		}
		else {

			sendMessageButton.setEnabled(false);
			sendMessageMenuItem.setEnabled(false);
			previousMessageButton.setEnabled(false);
			previousMessageMenuItem.setEnabled(false);
			nextMessageButton.setEnabled(false);
			nextMessageMenuItem.setEnabled(false);
		}

	}

	/**
	 * Open message.
	 * 
	 * @param message
	 *            the message
	 */
	public void openMessage(final Message message) {

		if (mainPanel.showFIXMessagePanel(message)) {
			this.message = message;

			if (!history.contains(message)) {

				history.add(message);

				if (history.size() > 100)
					history.removeFirst();
			}

			final int index = history.indexOf(message);

			if (index == 0)
				setPreviousMessage(null);

			else
				setPreviousMessage(history.get(index - 1));

			if (history.size() - 1 == index)
				setNextMessage(null);

			else
				setNextMessage(history.get(index + 1));
		}
	}

	private void openProject() {

		final OpenProjectDialog openProjectDialog = new OpenProjectDialog(fixProperties.getProjects());
		openProjectDialog.setLocationRelativeTo(mainPanel);
		openProjectDialog.setVisible(true);

		final String project = openProjectDialog.getProject();

		if (project != null) {
			history.clear();
			fixProperties.setProject(project);
			mainPanel.showSettingsPanel(status);
		}
	}

	private void removeProject() {

		final RemoveProjectDialog removeProjectDialog = new RemoveProjectDialog(fixProperties.getProjects());
		removeProjectDialog.setLocationRelativeTo(mainPanel);
		removeProjectDialog.setVisible(true);

		final String project = removeProjectDialog.getProject();

		if (project != null)
			fixProperties.removeProject(project);

		checkProjectCount();
	}

	/**
	 * Send.
	 */
	public void send() {

		if (message != null)
			fixConnector.send(message);
	}
	
	public void exit() {

		fixConnector.disconnect();
		fixProperties.store();
		System.exit(0);
	}

	/**
	 * Sets the next message.
	 * 
	 * @param message
	 *            the new next message
	 */
	public void setNextMessage(final Message message) {

		nextMessage = message;

		if (nextMessage == null) {

			nextMessageButton.setEnabled(false);
			nextMessageMenuItem.setEnabled(false);
		}
		else {

			nextMessageButton.setEnabled(true);
			nextMessageMenuItem.setEnabled(true);
		}

		repaint();
	}

	/**
	 * Sets the previous message.
	 * 
	 * @param message
	 *            the new previous message
	 */
	public void setPreviousMessage(final Message message) {

		previousMessage = message;

		if (previousMessage == null) {

			previousMessageButton.setEnabled(false);
			previousMessageMenuItem.setEnabled(false);
		}
		else {

			previousMessageButton.setEnabled(true);
			previousMessageMenuItem.setEnabled(true);
		}

		repaint();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.sourceforge.fixpusher.view.MainPanelListener#setStatusInfo(javax.
	 * swing.ImageIcon, java.lang.String)
	 */
	@Override
	public void setStatusInfo(final ImageIcon imageIcon, final String text) {

		statusPanel.setStatusInfo(imageIcon, text);
	}

}
