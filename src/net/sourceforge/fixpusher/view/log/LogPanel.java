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
package net.sourceforge.fixpusher.view.log;

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
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.MatteBorder;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import net.sourceforge.fixpusher.model.FIXMessageFilterListener;
import net.sourceforge.fixpusher.model.log.LogTableModel;
import net.sourceforge.fixpusher.view.ColoredCheckBoxIcon;
import net.sourceforge.fixpusher.view.FIXPusher;
import net.sourceforge.fixpusher.view.GradientPanel;
import net.sourceforge.fixpusher.view.TopPanel;
import net.sourceforge.fixpusher.view.dialog.ExceptionDialog;
import quickfix.DefaultMessageFactory;
import quickfix.InvalidMessage;
import quickfix.Message;
import quickfix.MessageUtils;

/**
 * The Class LogPanel.
 */
public class LogPanel extends JPanel implements FIXMessageFilterListener {

	private static final long serialVersionUID = 1L;

	private JCheckBox hideHeartbeatsCheckBox = null;

	private JCheckBox hideReceivedCheckBox = null;

	private JCheckBox hideSentCheckBox = null;

	private int row = -1;

	private JTable table = null;

	private TopPanel topPanel = null;

	/**
	 * Instantiates a new log panel.
	 * 
	 * @param topPanel
	 *            the top panel
	 * @param logTableModel
	 *            the log table model
	 */
	public LogPanel(final TopPanel topPanel, final LogTableModel logTableModel) {

		this.topPanel = topPanel;
		setLayout(new BorderLayout(0, 0));

		final JPanel filterPanel = new GradientPanel();
		filterPanel.setBorder(new MatteBorder(0, 0, 1, 0, new Color(0, 0, 0)));
		final GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.rowHeights = new int[] { 15, 0, 0 };
		gbl_panel.columnWeights = new double[] { 0.0, 0.0, 0.0, 1.0, 0.0 };
		gbl_panel.rowWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
		filterPanel.setLayout(gbl_panel);
		add(filterPanel, BorderLayout.NORTH);

		final JLabel filterLabel = new JLabel("Filter");
		filterLabel.setForeground(Color.WHITE);
		filterLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
		filterLabel.setIcon(new ImageIcon(FIXPusher.class.getResource("/net/sourceforge/fixpusher/view/images/16x16/filter.png")));
		final GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel.insets = new Insets(5, 25, 5, 5);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		filterPanel.add(filterLabel, gbc_lblNewLabel);

		hideHeartbeatsCheckBox = new JCheckBox("Hide heartbeats");
		hideHeartbeatsCheckBox.setForeground(Color.WHITE);
		hideHeartbeatsCheckBox.setOpaque(false);
		hideHeartbeatsCheckBox.setSelected(topPanel.getFixProperties().getFixMessageFilter().isHideHeartbeats());
		hideHeartbeatsCheckBox.setFont(new Font("Dialog", Font.PLAIN, 12));
		hideHeartbeatsCheckBox.setIcon(new ColoredCheckBoxIcon(Color.WHITE));
		hideHeartbeatsCheckBox.setFocusPainted(false);
		final GridBagConstraints gbc_hideHeartbeatsCheckBox = new GridBagConstraints();
		gbc_hideHeartbeatsCheckBox.insets = new Insets(5, 20, 5, 5);
		gbc_hideHeartbeatsCheckBox.gridx = 1;
		gbc_hideHeartbeatsCheckBox.gridy = 0;
		filterPanel.add(hideHeartbeatsCheckBox, gbc_hideHeartbeatsCheckBox);

		hideHeartbeatsCheckBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {

				LogPanel.this.topPanel.getFixProperties().getFixMessageFilter().setHideHeartbeats(hideHeartbeatsCheckBox.isSelected());
			}
		});

		hideReceivedCheckBox = new JCheckBox("Hide received");
		hideReceivedCheckBox.setOpaque(false);
		hideReceivedCheckBox.setFocusPainted(false);
		hideReceivedCheckBox.setSelected(topPanel.getFixProperties().getFixMessageFilter().isHideReceived());
		hideReceivedCheckBox.setForeground(Color.WHITE);
		hideReceivedCheckBox.setIcon(new ColoredCheckBoxIcon(Color.WHITE));
		hideReceivedCheckBox.setFont(new Font("Dialog", Font.PLAIN, 12));
		final GridBagConstraints gbc_hideReceivedCheckBox = new GridBagConstraints();
		gbc_hideReceivedCheckBox.insets = new Insets(5, 20, 5, 5);
		gbc_hideReceivedCheckBox.gridx = 2;
		gbc_hideReceivedCheckBox.gridy = 0;
		filterPanel.add(hideReceivedCheckBox, gbc_hideReceivedCheckBox);

		hideReceivedCheckBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {

				LogPanel.this.topPanel.getFixProperties().getFixMessageFilter().setHideReceived(hideReceivedCheckBox.isSelected());
			}
		});

		hideSentCheckBox = new JCheckBox("Hide sent");
		hideSentCheckBox.setFocusPainted(false);
		hideSentCheckBox.setSelected(topPanel.getFixProperties().getFixMessageFilter().isHideSent());
		hideSentCheckBox.setForeground(Color.WHITE);
		hideSentCheckBox.setIcon(new ColoredCheckBoxIcon(Color.WHITE));
		hideSentCheckBox.setOpaque(false);
		hideSentCheckBox.setFont(new Font("Dialog", Font.PLAIN, 12));
		final GridBagConstraints gbc_hideSentCheckBox = new GridBagConstraints();
		gbc_hideSentCheckBox.anchor = GridBagConstraints.WEST;
		gbc_hideSentCheckBox.insets = new Insets(5, 20, 5, 0);
		gbc_hideSentCheckBox.gridx = 3;
		gbc_hideSentCheckBox.gridy = 0;
		filterPanel.add(hideSentCheckBox, gbc_hideSentCheckBox);

		hideSentCheckBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {

				LogPanel.this.topPanel.getFixProperties().getFixMessageFilter().setHideSent(hideSentCheckBox.isSelected());
			}
		});

		final JScrollPane scrollPane = new JScrollPane(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void paintComponent(final Graphics graphics) {

				final Graphics2D graphics2D = (Graphics2D) graphics;

				super.paintComponent(graphics2D);

				final int width = this.getWidth();
				final GradientPaint gradientPaint = new GradientPaint(width / 2.F, 0, Color.GRAY, width / 2.F, 21, Color.BLACK);

				graphics2D.setPaint(gradientPaint);
				graphics2D.fillRect(0, 0, width, 26);

				getUI().paint(graphics2D, this);
			}

		};
		scrollPane.setOpaque(true);
		scrollPane.getViewport().setBackground(new Color(255, 243, 204));
		add(scrollPane, BorderLayout.CENTER);

		table = new JTable();
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setModel(logTableModel);
		table.getColumnModel().getColumn(0).setPreferredWidth(150);
		table.getColumnModel().getColumn(1).setPreferredWidth(100);
		table.getColumnModel().getColumn(2).setPreferredWidth(250);
		table.setDefaultRenderer(Object.class, new LogTableRenderer(logTableModel));
		table.setShowHorizontalLines(false);
		table.setShowVerticalLines(false);
		table.setIntercellSpacing(new Dimension(0, 0));
		table.setAutoscrolls(false);
		table.setRowHeight(20);

		logTableModel.setTable(table);
		for (int i = 0; i < table.getColumnCount(); i++)
			table.getColumnModel().getColumn(i).setHeaderRenderer(new LogTableHeaderRenderer());

		final JPopupMenu jPopupMenu = new JPopupMenu();

		final JMenuItem openFIXMessageMenuItem = new JMenuItem("Open FIX Message");
		openFIXMessageMenuItem.setIcon(new ImageIcon(FIXPusher.class.getResource("/net/sourceforge/fixpusher/view/images/16x16/mail_generic.png")));
		openFIXMessageMenuItem.setFont(new Font("Dialog", Font.PLAIN, 12));
		openFIXMessageMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent arg0) {

				try {
					final Message message = logTableModel.getMessage(row);
					final Message message2 = MessageUtils.parse(new DefaultMessageFactory(), LogPanel.this.topPanel.getFixProperties()
							.getQuickFixDataDictionary(), message.toString());
					LogPanel.this.topPanel.openMessage(message2);
				}
				catch (final InvalidMessage e) {

					ExceptionDialog.showException(e);
				}

			}
		});

		jPopupMenu.add(openFIXMessageMenuItem);

		final JMenuItem adoptValuesMenuItem = new JMenuItem("Adopt Values");
		adoptValuesMenuItem.setIcon(new ImageIcon(FIXPusher.class.getResource("/net/sourceforge/fixpusher/view/images/16x16/insertcellcopy.png")));
		adoptValuesMenuItem.setFont(new Font("Dialog", Font.PLAIN, 12));
		adoptValuesMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent arg0) {

				logTableModel.adoptValues();
			}
		});

		jPopupMenu.add(adoptValuesMenuItem);

		jPopupMenu.addPopupMenuListener(new PopupMenuListener() {

			@Override
			public void popupMenuCanceled(final PopupMenuEvent e) {

			}

			@Override
			public void popupMenuWillBecomeInvisible(final PopupMenuEvent e) {

			}

			@Override
			public void popupMenuWillBecomeVisible(final PopupMenuEvent e) {

				if (logTableModel.getFIXMainMessagePanel() == null)
					adoptValuesMenuItem.setEnabled(false);

				else
					adoptValuesMenuItem.setEnabled(true);

			}
		});

		table.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(final MouseEvent e) {

				if (e.getClickCount() == 2)

					try {

						final Message message = logTableModel.getMessage(row);
						final Message message2 = MessageUtils.parse(new DefaultMessageFactory(), LogPanel.this.topPanel.getFixProperties()
								.getQuickFixDataDictionary(), message.toString());

						LogPanel.this.topPanel.openMessage(message2);

					}
					catch (final Exception e1) {

						ExceptionDialog.showException(e1);
					}

			}

			@Override
			public void mouseEntered(final MouseEvent e) {

			}

			@Override
			public void mouseExited(final MouseEvent e) {

				if (jPopupMenu.getMousePosition() == null) {

					jPopupMenu.setVisible(false);
					logTableModel.setMouseOverRow(-1);

					table.repaint();
				}
			}

			@Override
			public void mousePressed(final MouseEvent e) {

				row = table.rowAtPoint(e.getPoint());
				logTableModel.setMouseOverRow(row);

				table.repaint();

				if (e.getButton() != MouseEvent.BUTTON1)
					jPopupMenu.show(e.getComponent(), e.getX(), e.getY());
			}

			@Override
			public void mouseReleased(final MouseEvent e) {

			}
		});

		table.addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseDragged(final MouseEvent e) {

			}

			@Override
			public void mouseMoved(final MouseEvent e) {

				if (!jPopupMenu.isVisible()) {

					row = table.rowAtPoint(e.getPoint());
					logTableModel.setMouseOverRow(row);

					table.repaint();
				}
			}
		});

		scrollPane.setViewportView(table);
		scrollPane.addComponentListener(new ComponentListener() {

			@Override
			public void componentHidden(final ComponentEvent e) {

			}

			@Override
			public void componentMoved(final ComponentEvent e) {

			}

			@Override
			public void componentResized(final ComponentEvent e) {

				logTableModel.setMinWidth((int) scrollPane.getSize().getWidth() - 500);

			}

			@Override
			public void componentShown(final ComponentEvent e) {

			}
		});

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sourceforge.fixpusher.model.FIXMessageFilterListener#
	 * fixMessageFilterChanged()
	 */
	@Override
	public void fixMessageFilterChanged() {

		hideHeartbeatsCheckBox.setSelected(topPanel.getFixProperties().getFixMessageFilter().isHideHeartbeats());
		hideReceivedCheckBox.setSelected(topPanel.getFixProperties().getFixMessageFilter().isHideReceived());
		hideSentCheckBox.setSelected(topPanel.getFixProperties().getFixMessageFilter().isHideSent());

	}

}
