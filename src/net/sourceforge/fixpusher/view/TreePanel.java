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

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.plaf.basic.BasicTreeUI;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import net.sourceforge.fixpusher.model.FIXPropertyListener;
import net.sourceforge.fixpusher.model.message.FIXMessage;
import quickfix.Message;
import quickfix.field.MsgType;

/**
 * The Class TreePanel.
 */
public class TreePanel extends JScrollPane implements FIXPropertyListener {

	private static final long serialVersionUID = 1L;
	
	private Object mouseOver = null;
	
	private DefaultMutableTreeNode topNode = null;
	
	private TopPanel topPanel;
	
	private JTree tree = null;
	
	private int width = 0;

	/**
	 * Instantiates a new tree panel.
	 *
	 * @param topPanel the top panel
	 */
	public TreePanel(final TopPanel topPanel) {

		super();
		
		this.topPanel = topPanel;
		topPanel.getFixProperties().addFIXPropertyListener(this);
		topNode = new DefaultMutableTreeNode("FIX Messages");
		
		tree = new JTree(topNode) {

			private static final long serialVersionUID = 1L;

			@Override
			public void paintComponent(final Graphics g) {

				width = this.getWidth();
				super.paintComponent(g);
			}
		};
		
		final BasicTreeUI ui = new BasicTreeUI() {

			@Override
			protected void paintRow(final Graphics g, final Rectangle clipBounds, final Insets insets, final Rectangle bounds, final TreePath path,
					final int row, final boolean isExpanded, final boolean hasBeenExpanded, final boolean isLeaf) {

				bounds.width = width - bounds.x;
				super.paintRow(g, clipBounds, insets, bounds, path, row, isExpanded, hasBeenExpanded, isLeaf);
			}
		};
		
		tree.setUI(ui);
		tree.setBackground(new Color(255, 243, 204));
		tree.setFont(new Font("Dialog", Font.PLAIN, 12));
		tree.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		int row = 0;
		
		while (row < tree.getRowCount()) {
		
			tree.expandRow(row);
			row++;
		}

		setViewportView(tree);

		final ImageIcon leafIcon = new ImageIcon(FIXPusher.class.getResource("/net/sourceforge/fixpusher/view/images/16x16/mail_generic.png"));
		
		if (leafIcon != null) {
			
			final DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer() {

				private static final long serialVersionUID = 1L;

				@Override
				public Component getTreeCellRendererComponent(final JTree tree, final Object value, final boolean sel, final boolean expanded,
						final boolean leaf, final int row, final boolean hasFocus) {

					super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
					
					if (value == mouseOver && leaf)
						this.setBackground(new Color(179, 198, 255));
					
					else
						this.setBackground(new Color(255, 243, 204));
					
					return this;
				}

			};
			
			renderer.setOpaque(true);
			renderer.setBackgroundNonSelectionColor(null);
			renderer.setLeafIcon(leafIcon);
			renderer.setBorder(new EmptyBorder(2, 2, 2, 0));
			renderer.setFont(new Font("Dialog", Font.PLAIN, 12));
			renderer.setClosedIcon(new ImageIcon(FIXPusher.class.getResource("/net/sourceforge/fixpusher/view/images/16x16/folder.png")));
			renderer.setOpenIcon(new ImageIcon(FIXPusher.class.getResource("/net/sourceforge/fixpusher/view/images/16x16/folder.png")));
			
			tree.setCellRenderer(renderer);
		}

		tree.addMouseMotionListener(new MouseMotionAdapter() {

			@Override
			public void mouseMoved(final MouseEvent e) {

				final int x = (int) e.getPoint().getX();
				final int y = (int) e.getPoint().getY();
				final TreePath path = tree.getPathForLocation(x, y);
				
				if (path == null)
					mouseOver = null;
				
				else
					mouseOver = path.getLastPathComponent();
				
				tree.repaint();
			}
		});

		tree.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(final MouseEvent e) {

			}

			@Override
			public void mouseEntered(final MouseEvent e) {

			}

			@Override
			public void mouseExited(final MouseEvent e) {

				mouseOver = null;
				tree.repaint();
			}

			@Override
			public void mousePressed(final MouseEvent e) {

			}

			@Override
			public void mouseReleased(final MouseEvent e) {

			}
		});

		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		
		tree.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {

			@Override
			public void valueChanged(final TreeSelectionEvent e) {

				final DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
				
				if (node != null && node.getUserObject() instanceof FIXMessage) {
					
					tree.setSelectionPath(null);
					
					final FIXMessage fixMessage = (FIXMessage) node.getUserObject();
					Message message = new Message();
			        message.getHeader().setString(MsgType.FIELD, fixMessage.getMessageType());
					
					TreePanel.this.topPanel.openMessage(message);
				}
			}
		});
		
		fixPropertyChanged(new ArrayList<Message>());
	}

	/**
	 * Adds the messages.
	 *
	 * @param messageList the message list
	 */
	public void addMessages(final List<FIXMessage> messageList) {

		topNode.removeAllChildren();
		
		for (final FIXMessage fixMessage : messageList) {
			
			final DefaultMutableTreeNode messageNode = new DefaultMutableTreeNode();
			messageNode.setUserObject(fixMessage);
			topNode.add(messageNode);
		}
		
		((DefaultTreeModel) tree.getModel()).reload();
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.fixpusher.model.FIXPropertyListener#fixPropertyChanged(java.util.List)
	 */
	@Override
	public void fixPropertyChanged(final List<Message> messages) {

		addMessages(topPanel.getFixProperties().getDictionaryParser().getMessageList());
	}

}
