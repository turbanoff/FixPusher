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
package net.sourceforge.fixpusher.view.message;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.ProgressMonitor;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import net.sourceforge.fixpusher.model.message.AbstractFIXElement;
import net.sourceforge.fixpusher.model.message.AbstractFIXElement.FieldType;
import net.sourceforge.fixpusher.model.message.FIXComponent;
import net.sourceforge.fixpusher.model.message.FIXField;
import net.sourceforge.fixpusher.model.message.FIXGroup;
import net.sourceforge.fixpusher.model.message.FIXMessage;
import net.sourceforge.fixpusher.view.BackgroundViewPort;
import net.sourceforge.fixpusher.view.MainPanel;
import quickfix.Message;

/**
 * The Class FIXMessagePanel.
 */
public class FIXMessagePanel extends AbstractFIXPanel {

	private static final long serialVersionUID = 1L;
		
	private MainPanel mainPanel = null;
	
	private JPanel panel = null;
	
	private JPanel panel_4 = null;
	
	private JScrollPane scrollPane = null;

	/**
	 * Instantiates a new fIX message panel.
	 *
	 * @param abstractFIXElement the abstract fix element
	 * @param message the message
	 * @param mainPanel the main panel
	 * @param progressMonitor the progress monitor
	 */
	public FIXMessagePanel(final FIXMessage abstractFIXElement, final Message message, final MainPanel mainPanel, final ProgressMonitor progressMonitor) {

		super(null, abstractFIXElement, message, true, abstractFIXElement.getDepth() + 1, mainPanel.getFixProperties().getFixMessageFilter());
		this.mainPanel = mainPanel;
		setOpaque(false);
		
		warningIconLabel.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(final MouseEvent e) {

			}

			@Override
			public void mouseEntered(final MouseEvent e) {

			}

			@Override
			public void mouseExited(final MouseEvent e) {

			}

			@Override
			public void mousePressed(final MouseEvent e) {

				final AbstractFIXPanel abstractFIXPanel = browseFieldCheckResult(fieldCheckResult);
				scrollTo(abstractFIXPanel);
			}

			@Override
			public void mouseReleased(final MouseEvent e) {

			}

		});

		depth = abstractFIXElement.getDepth() + 1;
		enabled = true;

		numberLabel.setText(abstractFIXElement.getMessageType());
		requiredLabel.setText("");

		final JLabel lblNewLabel_5 = new JLabel("Message category: " + abstractFIXElement.getMessageCat());
		lblNewLabel_5.setFont(new Font("Dialog", Font.PLAIN, 12));
		final GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.anchor = GridBagConstraints.WEST;
		gbc_comboBox.insets = new Insets(0, 0, 0, 5);
		gbc_comboBox.gridx = 0;
		gbc_comboBox.gridy = 0;
		lastPanel.add(lblNewLabel_5, gbc_comboBox);

		final JViewport jViewport = new BackgroundViewPort();
		scrollPane = new JScrollPane();
		scrollPane.setBorder(new MatteBorder(0, 0, 0, 0, new Color(0, 0, 0)));
		scrollPane.setViewport(jViewport);
		scrollPane.getVerticalScrollBar().setUnitIncrement(16);
		childPanel.setLayout(new BorderLayout());
		childPanel.add(scrollPane, BorderLayout.CENTER);

		panel_4 = new JPanel();
		panel_4.setOpaque(false);
		scrollPane.setViewportView(panel_4);
		panel_4.setLayout(new BorderLayout(0, 0));

		panel = new JPanel();
		panel_4.add(panel, BorderLayout.NORTH);
		panel.setBackground(Color.GREEN);
		final GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWeights = new double[] { 1.0 };
		panel.setLayout(gbl_panel);

		int i = 0;

		final List<String> fieldCheckResult = abstractFIXElement.getFieldCheckResult(message);

		for (final String fieldTag : fieldCheckResult) {
			final String[] tag = fieldTag.split("=");
			final String fieldName = mainPanel.getFixProperties().getDictionaryParser().getUserDefinedFieldName(tag[0]);
			final FIXField fixField = new FIXField(fieldName, Integer.parseInt(tag[0]), FieldType.OPTIONAL, FIXField.Type.STRING);
			fixField.setFontProperties(abstractFIXElement.getFontProperties());
			final FIXUnknownFieldPanel panel_3 = new FIXUnknownFieldPanel(this, tag[1], fixField, message, true, abstractFIXElement.getDepth(),
					fixMessageFilter);
			final GridBagConstraints gbc_panel_3 = new GridBagConstraints();
			gbc_panel_3.anchor = GridBagConstraints.WEST;
			gbc_panel_3.fill = GridBagConstraints.HORIZONTAL;
			gbc_panel_3.gridx = 0;
			gbc_panel_3.gridy = i;
			children.add(panel_3);
			panel.add(panel_3, gbc_panel_3);
			i++;
		}

		if (!fixMessageFilter.isHideHeader()) {

			final FIXComponent fixComponent = abstractFIXElement.getHeader();
			final boolean atLeastOneSet = fixComponent.atLeastOneSet(message.getHeader());

			if (atLeastOneSet || !fixMessageFilter.isHideEmptyFields()) {

				final FIXComponentPanel panel_3 = new FIXComponentPanel(this, fixComponent, message.getHeader(), true, abstractFIXElement.getDepth(),
						fixMessageFilter);
				final GridBagConstraints gbc_panel_3 = new GridBagConstraints();
				gbc_panel_3.anchor = GridBagConstraints.WEST;
				gbc_panel_3.fill = GridBagConstraints.HORIZONTAL;
				gbc_panel_3.gridx = 0;
				gbc_panel_3.gridy = i;
				children.add(panel_3);
				panel.add(panel_3, gbc_panel_3);
				i++;

			}
		}

		int progress = 0;

		for (final AbstractFIXElement abstractFIXElement2 : abstractFIXElement.getAbstractFIXElements()) {

			final String progressMessage = String.format("Initialize %d%%.\n", progress * 100 / progressMonitor.getMaximum());
			progressMonitor.setNote(progressMessage);
			progressMonitor.setProgress(progress);
			progress++;

			if (!(abstractFIXElement2.getFieldType() == FieldType.OPTIONAL) || !fixMessageFilter.isHideOptionalFields()) {

				if (abstractFIXElement2 instanceof FIXField) {

					final FIXField fixField = (FIXField) abstractFIXElement2;

					if (message.isSetField(fixField.getNumber()) || !fixMessageFilter.isHideEmptyFields()) {

						final FIXFieldPanel panel_3 = new FIXFieldPanel(this, fixField, message, true, abstractFIXElement.getDepth(), fixMessageFilter);
						final GridBagConstraints gbc_panel_3 = new GridBagConstraints();
						gbc_panel_3.anchor = GridBagConstraints.WEST;
						gbc_panel_3.fill = GridBagConstraints.HORIZONTAL;
						gbc_panel_3.gridx = 0;
						gbc_panel_3.gridy = i;
						children.add(panel_3);
						panel.add(panel_3, gbc_panel_3);

						i++;
					}
				}

				if (abstractFIXElement2 instanceof FIXComponent) {
					final FIXComponent fixComponent = (FIXComponent) abstractFIXElement2;
					final boolean atLeastOneSet = fixComponent.atLeastOneSet(message);
					if (atLeastOneSet || !fixMessageFilter.isHideEmptyFields()) {

						final FIXComponentPanel panel_3 = new FIXComponentPanel(this, fixComponent, message, true, abstractFIXElement.getDepth(),
								fixMessageFilter);
						final GridBagConstraints gbc_panel_3 = new GridBagConstraints();
						gbc_panel_3.anchor = GridBagConstraints.WEST;
						gbc_panel_3.fill = GridBagConstraints.HORIZONTAL;
						gbc_panel_3.gridx = 0;
						gbc_panel_3.gridy = i;
						children.add(panel_3);
						panel.add(panel_3, gbc_panel_3);

						i++;
					}
				}

				if (abstractFIXElement2 instanceof FIXGroup) {

					final FIXGroup fixGroup = (FIXGroup) abstractFIXElement2;
					if (message.hasGroup(fixGroup.getNumber()) && message.getGroupCount(fixGroup.getNumber()) > 0 || !fixMessageFilter.isHideEmptyFields()) {

						final FIXGroupPanel panel_3 = new FIXGroupPanel(this, fixGroup, message, true, abstractFIXElement.getDepth(), fixMessageFilter);
						final GridBagConstraints gbc_panel_3 = new GridBagConstraints();
						gbc_panel_3.anchor = GridBagConstraints.WEST;
						gbc_panel_3.fill = GridBagConstraints.HORIZONTAL;
						gbc_panel_3.gridx = 0;
						gbc_panel_3.gridy = i;
						children.add(panel_3);
						panel.add(panel_3, gbc_panel_3);

						i++;
					}
				}
			}

		}
		
		progressMonitor.close();

		if (!fixMessageFilter.isHideHeader()) {

			final FIXComponent fixComponent = abstractFIXElement.getTrailer();
			final boolean atLeastOneSet = fixComponent.atLeastOneSet(message.getTrailer());

			if (atLeastOneSet || !fixMessageFilter.isHideEmptyFields()) {

				final FIXComponentPanel panel_3 = new FIXComponentPanel(this, fixComponent, message.getTrailer(), true, abstractFIXElement.getDepth(),
						fixMessageFilter);
				final GridBagConstraints gbc_panel_3 = new GridBagConstraints();
				gbc_panel_3.anchor = GridBagConstraints.WEST;
				gbc_panel_3.fill = GridBagConstraints.HORIZONTAL;
				gbc_panel_3.gridx = 0;
				gbc_panel_3.gridy = i;
				children.add(panel_3);
				panel.add(panel_3, gbc_panel_3);

				i++;
			}
		}

		if (i > 0) {

			topPanel.setBorder(new EmptyBorder(5, 25 * depth, 5, 10));
			add(leftPanel, BorderLayout.WEST);
			add(childPanel, BorderLayout.CENTER);
		}

		progressMonitor.close();
		fieldValueChanged();

	}

	/* (non-Javadoc)
	 * @see net.sourceforge.fixpusher.view.message.AbstractFIXPanel#fieldValueChanged()
	 */
	@Override
	public void fieldValueChanged() {

		FieldCheckResult i = new FieldCheckResult(0, "Message " + abstractFIXElement.getName() + " is ok.", this);

		for (final AbstractFIXElement abstractFIXElement2 : abstractFIXElement.getAbstractFIXElements()) {

			final FieldCheckResult j = checkFieldMap(abstractFIXElement2, fieldMap);

			if (j.getBugLevel() > i.getBugLevel())
				i = j;
		}

		fieldCheckResult = i;
		warningIconLabel.setIcon(i.getImageIcon());
		warningIconLabel.setToolTipText(i.getToolTipText());
		validate();
		if (i.getImageIcon() != null)
			mainPanel.setStatusInfo(i.getImageIcon(), i.getToolTipText());
		else
			mainPanel.setStatusInfo(null, null);

	}

	/* (non-Javadoc)
	 * @see net.sourceforge.fixpusher.view.message.AbstractFIXPanel#getPanelBackground()
	 */
	@Override
	protected Color getPanelBackground() {

		return new Color(204, 216, 255);
	}

	/**
	 * Scroll to.
	 *
	 * @param abstractFIXPanel the abstract fix panel
	 */
	public void scrollTo(final AbstractFIXPanel abstractFIXPanel) {

		if (abstractFIXPanel != null) {

			final Rectangle rectangle = abstractFIXPanel.getBounds();
			rectangle.height = scrollPane.getViewport().getHeight();
			panel_4.scrollRectToVisible(SwingUtilities.convertRectangle(abstractFIXPanel.getParent(), rectangle, panel_4));
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.Component#toString()
	 */
	@Override
	public String toString() {

		return "Message";
	}


}
