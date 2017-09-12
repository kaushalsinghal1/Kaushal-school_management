package com.school.fees.report;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;

public class RecieptPrintable implements Printable {
	private ReciptModel model;
	private RecieptTopPanel topPanel;
	private RecieptMainPanel mainPanel;

	public RecieptPrintable(ReciptModel reciptModel) {
		this.model = reciptModel;
		topPanel = new RecieptTopPanel(model);
		mainPanel = new RecieptMainPanel(model);
	}

	@Override
	public int print(Graphics g, PageFormat pageFormat, int page)
			throws PrinterException {
		if (page > 0) { // We have only one page, and 'page' is zero-based
			return NO_SUCH_PAGE;
		}
		Graphics2D g2d = (Graphics2D) g;
		g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
		int width = (int) pageFormat.getWidth();
		int height = (int) pageFormat.getHeight();
		// Graphics2D g1 = (Graphics2D) g2d.create(0, 0, wadmi idth, 100);
		// drawHeader(g1, width, 100);
		//
		// Graphics2D g2 = (Graphics2D) g2d.create(0, 100, width, height - 100);

		topPanel.draw(g2d, 10, 10, 450, 130);
		// Graphics2D gMid = (Graphics2D) g2d.create(0, 130, width, 320);
		mainPanel.draw(g2d, 10, 130, 450, 320);

		return PAGE_EXISTS;

		/*
		 * if (page > 0) { We have only one page, and 'page' is zero-based
		 * return NO_SUCH_PAGE; }
		 * 
		 * User (0,0) is typically outside the imageable area, so we must
		 * translate by the X and Y values in the PageFormat to avoid clipping
		 * 
		 * 
		 * Graphics2D g2d = (Graphics2D) g;
		 * g2d.translate(pageFormat.getImageableX(),
		 * pageFormat.getImageableY()); int width = 400; int height = 400;
		 * Graphics2D g1 = (Graphics2D) g2d.create(0, 0, width, 100);
		 * drawHeader(g1, width, 100);
		 * 
		 * Graphics2D g2 = (Graphics2D) g2d.create(0, 100, width, height - 100);
		 * // g.dr //draw(g2d, pf.getWidth(), pf.getHeight()); draw(g2, width,
		 * height - 100); Now we perform our rendering //
		 * g.drawString("Hello world!", 100, 100);
		 * 
		 * tell the caller that this page is part of the printed document return
		 * PAGE_EXISTS;
		 */

	}

	private void drawHeader(Graphics2D g, double w, double h) {
		int TM = 10;
		int width = (int) w;
		int height = (int) h;
		int x = 0;
		int y = 0;
		BufferedImage myPicture = null;
		/*
		 * try { URL u =
		 * this.getClass().getResource("/iss/ui/resources/ok1.JPG"); //File f =
		 * new File(this.getClass().getResource("/iss/ui/resources/ok1.JPG"));
		 * // myPicture = ImageIO.read(u); } catch (IOException e) { // 
		 * Auto-generated catch block e.printStackTrace(); }
		 */
		if (myPicture != null) {
			g.drawImage(myPicture, 0, 0, 50, 50, null);
			x += 50;
		}
		x += 20;
		y += TM;
		g.drawString("Title.........", x, y);

		y += 50;
		x -= 10;
		g.drawString("SN - 10", x, y);
		x += width - 170;
		g.drawString("Date - 2012-20-01", x, y);

	}

	private void draw(Graphics2D g, double w, double h) {
		int LM = 10;
		int TM = 10;
		int RM = 10;
		int M = 5;
		int x = 0 + LM;
		int y = 0 + TM;
		int width = (int) w;
		int height = (int) h;

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

		g.drawString("SNO", x, y);
		g.drawString("DES", x + PrintRendrer.getSNWidth(), y);
		g.drawString("AMT",
				x + PrintRendrer.getSNWidth() + PrintRendrer.getDESWidth(), y);

		y += PrintRendrer.getHederWidth();

		for (int i = 1; i <= 5; i++) {
			g.drawString(i + "", x, y);
			g.drawString("DES" + i, x + PrintRendrer.getSNWidth(), y);
			g.drawString("10" + i,
					x + PrintRendrer.getSNWidth() + PrintRendrer.getDESWidth(),
					y);
			y += PrintRendrer.getLineHeight();
		}
		y = height + tmpY - TM - PrintRendrer.getTOTHeightFromBT() + 10;

		g.drawString("GTOT", x + PrintRendrer.getSNWidth(), y);
		g.drawString("505",
				x + PrintRendrer.getSNWidth() + PrintRendrer.getDESWidth(), y);
	}

}
