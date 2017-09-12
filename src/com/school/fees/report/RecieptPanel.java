package com.school.fees.report;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class RecieptPanel extends JPanel {
	private ReciptModel model;
	private RecieptTopPanel topPanel;
	private RecieptMainPanel mainPanel;
	private BottomPanel bottomPanel;

	public RecieptPanel(ReciptModel model) {
		this.model = model;
		topPanel = new RecieptTopPanel(model);
		mainPanel = new RecieptMainPanel(model);
		bottomPanel = new BottomPanel();
	//	setPreferredSize(new Dimension(500, 610));
		setLayout(new BorderLayout());
		
		add(topPanel,BorderLayout.NORTH);
		add(mainPanel,BorderLayout.CENTER);
	//	add(bottomPanel,c);
		setBackground(Color.WHITE);
	}

	@Override
	public void setPreferredSize(Dimension d) {
	//	super.setPreferredSize(d);
		topPanel.setPreferredSize(new Dimension(d.width, 120));
		mainPanel.setPreferredSize(new Dimension(d.width-10, 320));
	//	bottomPanel.setPreferredSize(new Dimension(d.width-10, 100));
	}

	class BottomPanel extends JPanel {
		private int TM = 30;
		private int M = 30;
		private int gWidth = 400;
		private int gHeight = 100;

		private void draw(Graphics2D g) {
			String footer = model.getFooterString();
			FontMetrics fm = g.getFontMetrics();
			int w = fm.stringWidth(footer);
			g.drawString(footer,gWidth-w-M, TM);
			setBackground(Color.WHITE);
			//g.drawString(footer, gWidth - w - M, TM);

		}

		@Override
		public void setPreferredSize(Dimension preferredSize) {
			gWidth = preferredSize.width;
			gHeight = preferredSize.height;
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			draw((Graphics2D) g);
		}
	}
}
