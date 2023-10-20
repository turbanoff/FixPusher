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
import java.awt.Frame;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Enumeration;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JSplitPane;
import javax.swing.JWindow;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;

import com.jgoodies.looks.plastic.Plastic3DLookAndFeel;

import net.sourceforge.fixpusher.control.FIXConnector;
import net.sourceforge.fixpusher.model.FIXProperties;
import net.sourceforge.fixpusher.model.log.LogTableModel;
import net.sourceforge.fixpusher.view.dialog.ExceptionDialog;
import net.sourceforge.fixpusher.view.log.LogPanel;

/**
 * The Class FIXPusher.
 */
public class FIXPusher extends JFrame {

	private static final long serialVersionUID = 1L;

	/** The count. */
	int count = 0;

	/** The version. */
	public static String version = "1.0.2";

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 */
	public static void main(final String[] args) {

		final JWindow splash = new SplashScreen();
		splash.setVisible(true);
		long wait = 5000 + System.currentTimeMillis();

		try {

			UIManager.setLookAndFeel(new Plastic3DLookAndFeel());
			
			UIManager.put("Panel.background", new Color(204, 216, 255));
			UIManager.put("OptionPane.background", new Color(204, 216, 255));			

			Locale.setDefault(Locale.ENGLISH);
			JComponent.setDefaultLocale(Locale.ENGLISH);

			final Enumeration<Object> keys = UIManager.getDefaults().keys();
			while (keys.hasMoreElements()) {

				final Object key = keys.nextElement();
				final Object value = UIManager.get(key);

				if (value instanceof javax.swing.plaf.FontUIResource)
					UIManager.put(key, new Font("Dialog", Font.PLAIN, 12));
			}

			final FIXPusher fixPusher = new FIXPusher();
			wait = wait - System.currentTimeMillis();

			if (wait > 0)
				try {

					Thread.sleep(wait);
				}
				catch (final InterruptedException e) {

					ExceptionDialog.showException(e);
				}

			splash.setVisible(false);

			fixPusher.setVisible(true);
			fixPusher.setExtendedState(fixPusher.getExtendedState() | Frame.MAXIMIZED_BOTH);

		}
		catch (final Exception e) {

			ExceptionDialog.showException(e);
		}

	}

	private FIXProperties fixProperties = null;

	private JSplitPane horizontalSplitPane = null;

	private JSplitPane verticalSplitPane = null;

	/**
	 * Instantiates a new fIX pusher.
	 */
	public FIXPusher() {

		super();

		setBounds(100, 100, 640, 480);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setIconImage(new ImageIcon(FIXPusher.class.getResource("/net/sourceforge/fixpusher/view/images/16x16/f-logo.png")).getImage());
		setTitle("FIX Pusher");

		fixProperties = new FIXProperties();

		verticalSplitPane = new JSplitPane();
		verticalSplitPane.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, new Color(229, 235, 255), new Color(179, 196, 255)));
		verticalSplitPane.setOpaque(true);
		verticalSplitPane.setDividerSize(2);
		verticalSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		verticalSplitPane.setResizeWeight(1.0);
		getContentPane().add(verticalSplitPane, BorderLayout.CENTER);

		horizontalSplitPane = new JSplitPane();
		horizontalSplitPane.setOpaque(true);
		horizontalSplitPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		horizontalSplitPane.setDividerSize(2);
		verticalSplitPane.setLeftComponent(horizontalSplitPane);

		final LogTableModel logTableModel = new LogTableModel(fixProperties);
		final FIXConnector fixConnector = new FIXConnector(fixProperties, logTableModel);
		
		final Dimension minimumSize = new Dimension(0, 0);
		final MainPanel mainPanel = new MainPanel(fixProperties, logTableModel);
		mainPanel.setMinimumSize(minimumSize);

		final TopPanel topPanel = new TopPanel(fixProperties, fixConnector, mainPanel);
		mainPanel.addMainPanelListener(topPanel);

		final LogPanel logPanel = new LogPanel(topPanel, logTableModel);

		final TreePanel treePanel = new TreePanel(topPanel);

		horizontalSplitPane.setLeftComponent(treePanel);

		horizontalSplitPane.setRightComponent(mainPanel);

		getContentPane().add(topPanel, BorderLayout.NORTH);

		getContentPane().add(topPanel.getStatusPanel(), BorderLayout.SOUTH);
		
		addWindowListener(new WindowListener() {
			
			@Override
			public void windowOpened(WindowEvent e) {
			
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowIconified(WindowEvent e) {
			
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeiconified(WindowEvent e) {
			
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeactivated(WindowEvent e) {
			
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowClosing(WindowEvent e) {
			
				topPanel.exit();
				
			}
			
			@Override
			public void windowClosed(WindowEvent e) {
			
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowActivated(WindowEvent e) {
			
				// TODO Auto-generated method stub
				
			}
		});

		addComponentListener(new ComponentListener() {

			@Override
			public void componentHidden(final ComponentEvent e) {

			}

			@Override
			public void componentMoved(final ComponentEvent e) {

			}

			@Override
			public void componentResized(final ComponentEvent e) {

			}

			@Override
			public void componentShown(final ComponentEvent e) {

				// http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=7105941

				verticalSplitPane.setRightComponent(logPanel);

				verticalSplitPane.setDividerLocation(fixProperties.getTableHeight());
				horizontalSplitPane.setDividerLocation(fixProperties.getTreeWidth());

				verticalSplitPane.resetToPreferredSizes();
				horizontalSplitPane.resetToPreferredSizes();

				verticalSplitPane.validate();
				validate();

			}
		});

		addWindowStateListener(new WindowStateListener() {

			private boolean notRegistered = true;

			@Override
			public void windowStateChanged(final WindowEvent e) {

				final int oldState = e.getOldState();
				final int newState = e.getNewState();

				if ((oldState & Frame.MAXIMIZED_BOTH) == 0 && (newState & Frame.MAXIMIZED_BOTH) != 0) {

					verticalSplitPane.validate();
					validate();

					verticalSplitPane.setDividerLocation(fixProperties.getTableHeight());
					horizontalSplitPane.setDividerLocation(fixProperties.getTreeWidth());
					verticalSplitPane.resetToPreferredSizes();
					horizontalSplitPane.resetToPreferredSizes();

					if (notRegistered) {
						
						notRegistered = false;
						
						horizontalSplitPane.addPropertyChangeListener(new PropertyChangeListener() {

							@Override
							public void propertyChange(final PropertyChangeEvent evt) {

								if (evt.getPropertyName().equals("dividerLocation")) {
									
									fixProperties.setTreeWidth(horizontalSplitPane.getDividerLocation());
									
									verticalSplitPane.validate();
									validate();
								}

							}
						});

						verticalSplitPane.addPropertyChangeListener(new PropertyChangeListener() {

							@Override
							public void propertyChange(final PropertyChangeEvent evt) {

								if (evt.getPropertyName().equals("dividerLocation") && verticalSplitPane.getLastDividerLocation() != -1) {
									
									fixProperties.setTableHeight(verticalSplitPane.getDividerLocation());
									
									verticalSplitPane.validate();
									validate();
								}

							}
						});

					}

				}

			}
		});

	}

}
