package com.school.fees.report;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.JPanel;

public class RecieptTopPanel extends JPanel {
	private int gWidth = 500;
	private int gHeight = 200;
	private ReciptModel model;

	public RecieptTopPanel(ReciptModel model) {
		this.model = model;
		setBackground(Color.WHITE);
	}

	/*
	 * private void draw1(Graphics2D g) { drawHeader(g, gWidth, gHeight); }
	 */
	public void draw(Graphics2D g, int width, int height) {
		draw(g, 0, 0, width, height);
	}

	public void draw(Graphics2D g, int x, int y, int width, int height) {
		int TM = 25;
		int LS = 5; // line space

		Image myPicture = model.getLogo();
		if (myPicture != null) {
			g.drawImage(myPicture, x, y, 50, 50, null);
			x += 50;
		}
		x += 5;
		y += TM;
		FontMetrics fm = g.getFontMetrics();
		String[] titles = getTitles(fm, width - 60);
		for (int i = 0; i < titles.length; i++) {
			int wx = (width - 50 - fm.stringWidth(titles[i])) / 2;
			g.drawString(titles[i], x + wx, y);
			y += fm.getHeight() + LS;
		}
		y += 10;
		String sName = getString(model.getStudentNamewithLabel(), fm,
				width - 60);
		g.drawString(sName, x, y);
		y += fm.getHeight() + LS;

		int tempWidth = (width - 60) / 2;
		String cName = getString(model.getClassNameWithLabel(), fm, tempWidth);
		g.drawString(cName, x, y);

		String session = getString(model.getSessionNameWithLabel(), fm,
				tempWidth);
		g.drawString(session, x + tempWidth, y);

		y += fm.getHeight() + LS;

		String recieptNo = model.getRecieptNoWithLabel();
		g.drawString(recieptNo, x, y);

		String date = model.getDateWithLabel();
		g.drawString(date, x + tempWidth, y);
		y += fm.getHeight() + LS;
		// g.drawString("Title.........", x, y);

	}

	private String getString(String title, FontMetrics fm, int width) {
		int sw = fm.stringWidth(title);
		if (sw > width) {
			int wp = (sw - width * 100) / 100;
			int l = title.length();
			int sp = l * (l - wp) / 100;
			title = title.substring(0, sp - 3);
			title += "...";
		}
		return title;
	}

	private String[] getTitles(FontMetrics fm, int width) {
		String title = model.getSchoolTitle();
		String subTitle = model.getSchoolSubTitle();
		String[] s = new String[2];
		int sw = fm.stringWidth(title);
		if (sw > width) {
			int wp = (sw - width * 100) / 100;
			int l = title.length();
			int sp = l * (l - wp) / 100;
			title = title.substring(0, sp - 3);
			title += "...";
		}
		s[0] = title;
		sw = fm.stringWidth(subTitle);
		if (sw > width) {
			int wp = (sw - width * 100) / 100;
			int l = subTitle.length();
			int sp = l * (l - wp) / 100;
			subTitle = subTitle.substring(0, sp - 3);
			subTitle += "...";
		}
		s[1] = subTitle;
		return s;
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
		draw((Graphics2D) g, gWidth, gHeight);
	}
}
