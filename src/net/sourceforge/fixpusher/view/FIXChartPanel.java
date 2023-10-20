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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.border.MatteBorder;

import net.sourceforge.fixpusher.control.FIXConnectionListener.Status;
import net.sourceforge.fixpusher.model.log.LogTableModel;
import net.sourceforge.fixpusher.view.dialog.ExceptionDialog;

import org.apache.poi.hssf.record.formula.functions.T;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PieLabelLinkStyle;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;

import quickfix.FieldNotFound;
import quickfix.Message;

/**
 * The Class FIXChartPanel.
 */
public class FIXChartPanel extends AbstractMainPanelContent {

	private static final long serialVersionUID = 1L;

	private final Map<String, Integer> messageCountMap = new HashMap<String, Integer>();

	private JLabel projectNameLabel = null;

	/**
	 * Instantiates a new fIX chart panel.
	 *
	 * @param mainPanel the main panel
	 * @param logTableModel the log table model
	 */
	public FIXChartPanel(final MainPanel mainPanel, final LogTableModel logTableModel) {

		super(mainPanel);
		setLayout(new BorderLayout(0, 0));
		setBackground(new Color(204, 216, 255));

		final JPanel topPanel = new GradientPanel();
		topPanel.setBorder(new MatteBorder(0, 0, 1, 0, Color.BLACK));
		add(topPanel, BorderLayout.NORTH);
		final GridBagLayout gbl_topPanel = new GridBagLayout();
		gbl_topPanel.rowHeights = new int[] { 15 };
		gbl_topPanel.columnWeights = new double[] { 0.0, 0.0, 0.0, 1.0, 0.0 };
		gbl_topPanel.rowWeights = new double[] { 0.0 };
		topPanel.setLayout(gbl_topPanel);

		projectNameLabel = new JLabel(mainPanel.getFixProperties().getProjectName());
		projectNameLabel.setForeground(Color.WHITE);
		projectNameLabel.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 20));
		final GridBagConstraints gbc_projectNameLabel = new GridBagConstraints();
		gbc_projectNameLabel.anchor = GridBagConstraints.WEST;
		gbc_projectNameLabel.insets = new Insets(5, 25, 5, 5);
		gbc_projectNameLabel.gridx = 3;
		gbc_projectNameLabel.gridy = 0;
		topPanel.add(projectNameLabel, gbc_projectNameLabel);

		final JLabel settingsLabel = new JLabel();
		settingsLabel.setText("Log Reporting");
		settingsLabel.setIcon(new ImageIcon(FIXPusher.class.getResource("/net/sourceforge/fixpusher/view/images/24x24/kchart_chrt.png")));
		settingsLabel.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 20));
		settingsLabel.setForeground(Color.WHITE);
		final GridBagConstraints gbc_settingsLabel = new GridBagConstraints();
		gbc_settingsLabel.anchor = GridBagConstraints.EAST;
		gbc_settingsLabel.insets = new Insets(5, 0, 5, 25);
		gbc_settingsLabel.gridx = 4;
		gbc_settingsLabel.gridy = 0;
		topPanel.add(settingsLabel, gbc_settingsLabel);

		final JPanel chartMainPanel = new JPanel();
		chartMainPanel.setOpaque(false);
		final GridBagLayout gbl_mainPanel = new GridBagLayout();
		chartMainPanel.setLayout(gbl_mainPanel);

		try {
			for (int i = 0; i < logTableModel.getRowCount(); i++) {
				final Message message = logTableModel.getMessage(i);
				final String messageName = mainPanel.getFixProperties().getDictionaryParser().getMessageName(message.getHeader().getString(35));
				Integer integer = messageCountMap.get(messageName);
				if (integer == null)
					integer = new Integer(0);
				messageCountMap.put(messageName, integer + 1);
			}
		}
		catch (final FieldNotFound e) {
			
			ExceptionDialog.showException(e);
		}

		final List<Entry<String, Integer>> entries = new ArrayList<Map.Entry<String, Integer>>();

		int j = 0;

		for (final Entry<String, Integer> entry : messageCountMap.entrySet()) {

			for (int i = 0; i <= j; i++)
				if (i == j)
					entries.add(i, entry);
				else if (entries.get(i).getValue() < entry.getValue()) {
					entries.add(i, entry);
					i = j + 1;
				}

			j++;

		}

		int sum = 0;

		final DefaultPieDataset defaultPieDataset = new DefaultPieDataset();

		int size = entries.size();

		if (size > 5)
			size = 5;

		for (int i = 0; i < size; i++) {

			final int count = entries.get(i).getValue();
			sum = sum + count;
			final double percentage = (double) count / (double) logTableModel.getRowCount() * 100d;
			final String labelText = String.format(" %d ", count) + entries.get(i).getKey() + String.format(" %.2f", percentage) + " %";
			
			defaultPieDataset.setValue(labelText, percentage);

		}

		if (logTableModel.getRowCount() - sum > 0) {

			final int count = logTableModel.getRowCount() - sum;
			final double percentage = (double) count / (double) logTableModel.getRowCount() * 100d;
			final String labelText = String.format(" %d ", count) + "Other Messages" + String.format(" %.2f", percentage) + " %";
			
			defaultPieDataset.setValue(labelText, percentage);

		}

		final JFreeChart jFreeChart = ChartFactory.createPieChart3D("FIX Messages", defaultPieDataset, false, false, false);

		final PiePlot piePlot = (PiePlot) jFreeChart.getPlot();

		final Color[] colors = { new Color(255, 215, 82), new Color(218, 204, 255), new Color(143, 169, 255), new Color(204, 255, 243),
				new Color(255, 204, 216), new Color(255, 229, 243) };

		@SuppressWarnings("unchecked")
		final List<Comparable<T>> keys = defaultPieDataset.getKeys();
		int aInt;

		for (int i = 0; i < keys.size(); i++) {
			aInt = i % colors.length;
			piePlot.setSectionPaint(keys.get(i), colors[aInt]);
		}

		jFreeChart.setBackgroundPaint(null);
		jFreeChart.setTextAntiAlias(true);

		final PiePlot3D piePlot3D = (PiePlot3D) jFreeChart.getPlot();
		piePlot3D.setCircular(false);
		piePlot3D.setLabelFont(new Font("Dialog", Font.PLAIN, 12));
		piePlot3D.setLabelLinkStyle(PieLabelLinkStyle.STANDARD);
		piePlot3D.setLabelBackgroundPaint(new Color(255, 243, 204, 255));
		piePlot3D.setBackgroundPaint(null);
		piePlot3D.setOutlineVisible(false);

		final ChartPanel chartPanel = new ChartPanel(jFreeChart, false);
		chartPanel.setOpaque(false);
		chartPanel.setMinimumSize(new Dimension(1200, 400));
		chartPanel.setMaximumSize(new Dimension(1200, 400));
		chartPanel.setPreferredSize(new Dimension(1200, 400));
		final GridBagConstraints gbc_chartPanel = new GridBagConstraints();
		gbc_chartPanel.insets = new Insets(50, 0, 0, 0);
		gbc_chartPanel.gridx = 0;
		gbc_chartPanel.gridy = 0;
		chartMainPanel.add(chartPanel, gbc_chartPanel);
		
		final JPanel fillPanel = new JPanel();
		final GridBagConstraints gbc_fillPanel = new GridBagConstraints();
		fillPanel.setOpaque(false);
		gbc_fillPanel.insets = new Insets(0, 0, 0, 0);
		gbc_fillPanel.weighty = 1.0;
		gbc_fillPanel.gridx = 0;
		gbc_fillPanel.gridy = 1;
		chartMainPanel.add(fillPanel, gbc_fillPanel);

		final JScrollPane scrollPane = new JScrollPane(chartMainPanel);
		final JViewport jViewport = new BackgroundViewPort();
		
		scrollPane.setViewport(jViewport);
		scrollPane.getViewport().add(chartMainPanel);
		add(scrollPane, BorderLayout.CENTER);
		
		mainPanel.setStatusInfo(new ImageIcon(FIXPusher.class.getResource("/net/sourceforge/fixpusher/view/images/16x16/documentinfo.png")),
				"Set the filter to hide heartbeat, sent or received messages.");

	}

	/* (non-Javadoc)
	 * @see net.sourceforge.fixpusher.view.AbstractMainPanelContent#save()
	 */
	@Override
	public void save() {

	}

	/* (non-Javadoc)
	 * @see net.sourceforge.fixpusher.view.AbstractMainPanelContent#setStatus(net.sourceforge.fixpusher.control.FIXConnectionListener.Status)
	 */
	@Override
	public void setStatus(final Status status) {

	}


}
