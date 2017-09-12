package com.school.fees.report;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.List;

import javax.swing.JPanel;

public class RecieptMainPanel extends JPanel {
	private int gWidth = 400;
	private int gHeight = 250;
	private ReciptModel model;

	public RecieptMainPanel(ReciptModel model) {
		setBackground(Color.WHITE);
		this.model = model;
	}



	public void draw(Graphics2D g, int w, int h) {
		draw(g, 0, 0, w, h);
	}

	public void draw(Graphics2D g, int x, int y, int w, int h) {
		int LM = 10;
		int TM = 10;
		int RM = 10;
		int M = 5;
		x += LM;
		y += TM;
		int width = (int) w - RM;
		int height = (int) h - 90;

		FontMetrics fm = g.getFontMetrics();

		g.drawRect(x, y, width - LM - M, height - TM - M);// outer
		g.drawLine(x + PrintRendrer.getSNWidth(), y,
				x + PrintRendrer.getSNWidth(), y + height - TM - M);// SN
		g.drawLine(x + PrintRendrer.getSNWidth() + PrintRendrer.getDESWidth(),
				y, x + PrintRendrer.getSNWidth() + PrintRendrer.getDESWidth(),
				y + height - TM - M);// des

		g.drawLine(x, y + PrintRendrer.getHederWidth(), x + width - LM - M, y
				+ PrintRendrer.getHederWidth());// Header

		g.drawLine(x, height + y - TM - M - PrintRendrer.getTOTHeightFromBT(),
				x + width - LM - M,
				height + y - TM - M - PrintRendrer.getTOTHeightFromBT());// tn

		int tmpY = y;
		x = x + M;
		y = y + M + 10;
		String[] s = model.getTableHeaderDetails();
		g.drawString(s[0], x, y);
		g.drawString(s[1], x + PrintRendrer.getSNWidth(), y);
		g.drawString(s[2],
				x + PrintRendrer.getSNWidth() + PrintRendrer.getDESWidth(), y);

		y += PrintRendrer.getHederWidth();

		List<String[]> sList = model.getDetailsAndAmout();
		for (int i = 0; i < sList.size(); i++) {
			String[] s1 = sList.get(i);
			g.drawString((i + 1) + "", x, y);
			g.drawString(s1[0], x + PrintRendrer.getSNWidth(), y);
			g.drawString(s1[1],
					x + PrintRendrer.getSNWidth() + PrintRendrer.getDESWidth(),
					y);
			y += fm.getHeight() + 5;
		}
		y = height + tmpY - TM - PrintRendrer.getTOTHeightFromBT() + 10;
		s = model.getTotAmountWithDes();
		g.drawString(s[0], x + PrintRendrer.getSNWidth(), y);
		g.drawString(s[1],
				x + PrintRendrer.getSNWidth() + PrintRendrer.getDESWidth(), y);
		y = height + tmpY - TM - PrintRendrer.getTOTHeightFromBT() + 10;

		// Graphics g1 = g.create(0, y, w, 80);
		drawFooter(g, 0, y, w, 80);
	}

	private void drawFooter(Graphics2D g, int x, int y, int gWidth, int gHeight) {
		int TM = 40;
		int M = 30;
		String footer = model.getFooterString();
		FontMetrics fm = g.getFontMetrics();
		int w = fm.stringWidth(footer);
		g.drawString(footer, x + gWidth - w - M, y + gHeight - fm.getHeight()
				- 10);

	}

	@Override
	public void setPreferredSize(Dimension preferredSize) {
		super.setPreferredSize(preferredSize);
		gWidth = preferredSize.width;
		gHeight = preferredSize.height;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw((Graphics2D) g);
	}

	private void draw(Graphics2D g) {
		draw(g, gWidth, gHeight);

	}
	
//	private void draw1(Graphics2D g, int w, int h) {
//	int LM = 10;
//	int TM = 10;
//	int RM = 10;
//	int M = 5;
//	int x = 0 + LM;
//	int y = 0 + TM;
//	int width = (int) w - RM;
//	int height = (int) h - 90;
//	FontMetrics fm = g.getFontMetrics();
//
//	g.drawRect(x, y, width - LM - M, height - TM - M);// outer
//	g.drawLine(x + PrintRendrer.getSNWidth(), y,
//			x + PrintRendrer.getSNWidth(), y + height - TM - M);// SN
//	g.drawLine(x + PrintRendrer.getSNWidth() + PrintRendrer.getDESWidth(),
//			y, x + PrintRendrer.getSNWidth() + PrintRendrer.getDESWidth(),
//			y + height - TM - M);// des
//
//	g.drawLine(x, y + PrintRendrer.getHederWidth(), x + width - LM - M, y
//			+ PrintRendrer.getHederWidth());// Header
//
//	g.drawLine(x, height + y - TM - M - PrintRendrer.getTOTHeightFromBT(),
//			x + width - LM - M,
//			height + y - TM - M - PrintRendrer.getTOTHeightFromBT());// tn
//
//	int tmpY = y;
//	x = x + M;
//	y = y + M + 10;
//
//	g.drawString("SNO", x, y);
//	g.drawString("DES", x + PrintRendrer.getSNWidth(), y);
//	g.drawString("AMT",
//			x + PrintRendrer.getSNWidth() + PrintRendrer.getDESWidth(), y);
//
//	y += PrintRendrer.getHederWidth();
//
//	for (int i = 1; i <= 5; i++) {
//		g.drawString(i + "", x, y);
//		g.drawString("DES" + i, x + PrintRendrer.getSNWidth(), y);
//		g.drawString("10" + i,
//				x + PrintRendrer.getSNWidth() + PrintRendrer.getDESWidth(),
//				y);
//		y += fm.getHeight() + 5;
//	}
//	y = height + tmpY - TM - PrintRendrer.getTOTHeightFromBT() + 10;
//
//	g.drawString("GTOT", x + PrintRendrer.getSNWidth(), y);
//	g.drawString("505",
//			x + PrintRendrer.getSNWidth() + PrintRendrer.getDESWidth(), y);
//	y = height + tmpY - TM - PrintRendrer.getTOTHeightFromBT() + 10;
//
//	// Graphics g1 = g.create(0, y, w, 80);
//	// drawFooter((Graphics2D) g1, w, 80);
//}
}
