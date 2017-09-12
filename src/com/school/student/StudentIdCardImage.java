package com.school.student;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import com.banti.framework.core.SchoolOpertaionAction;
import com.banti.framework.img.crop.ImageFilter;
import com.banti.framework.ui.DialogEsc;
import com.school.console.SchoolMain;
import com.school.constant.ApplicationConstant;
import com.school.hiebernate.dbo.StudentDetails;
import com.school.resource.ResourcesUtils;
import com.school.shared.SchoolInfo;
import com.school.utils.Logger;
import com.school.utils.MsgDialogUtils;

public class StudentIdCardImage extends DialogEsc implements ActionListener,
		Printable {
	int iWidth = 220;
	int iHeight = 320;
	private SchoolInfo schoolInfo = SchoolInfo.getInstance();
	private StudentDetails studentDetails;
	// private String studentName = "Kaushal Singhal";
	private final String TEMP_IMAGE_FILE = ApplicationConstant.TEMP_IMAGE_FILE;
	private final String SESSION_TITLE = "Session :   ";
	private final String CLASS_TITLE = "Class     :   ";
	private SchoolOpertaionAction saveAction;
	private SchoolOpertaionAction printAction;
	private final String SAVE_ID_COMMAND = "Save_ID_CARD";
	private final String PRINT_COMMAND = "PRINT";
	private BufferedImage bufferedImage;
	private JPopupMenu popupMenu;

	public StudentIdCardImage(StudentDetails studentDetails) {
		super(SchoolMain.Frame, "ID card");
		this.studentDetails = studentDetails;
		init();
		setSize(250, 400);
		setLocationRelativeTo(SchoolMain.Frame);
		setVisible(true);
	}

	private void init() {
		setLayout(new BorderLayout());
		add(getButtonPanel(), BorderLayout.NORTH);
		final JLabel lb = new JLabel(getIdImage());
		lb.setBorder(BorderFactory.createEtchedBorder());
		lb.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3) {
					popupMenu.show(lb, e.getX(), e.getY());
				}
			}
		});
		add(lb);
	}

	private JPanel getButtonPanel() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		saveAction = new SchoolOpertaionAction(
				ResourcesUtils.getString("SAVE_AS_IMAGE"), SAVE_ID_COMMAND);
		saveAction.registerActionListener(this);
		printAction = new SchoolOpertaionAction(
				ResourcesUtils.getString("PRINT"), PRINT_COMMAND);
		printAction.registerActionListener(this);
		JButton btnSave = new JButton(saveAction);
		JButton btnPrint = new JButton(printAction);
		panel.add(btnSave);
		panel.add(btnPrint);
		popupMenu = new JPopupMenu();
		popupMenu.add(saveAction);
		popupMenu.add(printAction);
		return panel;
	}

	private void createImage() {
		bufferedImage = new BufferedImage(iWidth, iHeight, Image.SCALE_DEFAULT);
		Graphics2D g = bufferedImage.createGraphics();
		g.setColor(new Color(239, 245, 247));
		g.fillRect(0, 0, iWidth, iHeight);
		g.setColor(Color.BLACK);
		g.drawRect(1, 1, iWidth - 2, iHeight - 2);
		draw(g, iWidth, iHeight);
	}

	private ImageIcon getIdImage() {
		createImage();
		return new ImageIcon(bufferedImage);
	}

	private String getStudentName() {
		StringBuffer sb = new StringBuffer();
		sb.append(studentDetails.getFirstName() != null ? studentDetails
				.getFirstName()+" " : "");
		sb.append(studentDetails.getLastName() != null ? studentDetails
				.getLastName() : "");
		return sb.toString();
	}

	private void draw(Graphics2D g, int w, int h) {
		final int TM = 15;
		final int LM = 5;
		int x = 0 + LM;
		int y = 0 + TM;
		FontMetrics fm = g.getFontMetrics();

		String s = schoolInfo.getIdCardTitle();
		int sW = fm.stringWidth(getWidthString(s, w - LM, fm));
		x = (w - LM - sW) / 2;
		g.drawString(s, x, y);
		y += fm.getHeight() + 3;
		x = 0 + LM;
		Image image = getImageFromStudent();
		if (image != null) {
			g.drawImage(image, (w - 140) / 2, y, 140, 130, null);
			y += 130 + 15;
		} else {
			MsgDialogUtils.showWarningDialog(null,
					"Student photo does not exist. Please upload phot first");
			return;
		}
		y += 10;
		x = 0 + LM;
		s = getStudentName();
		Font old = g.getFont();
		Font newF = old.deriveFont(Font.BOLD);
		g.setFont(newF);
		FontMetrics fm1 = g.getFontMetrics();
		sW = fm1.stringWidth(getWidthString(s, w - LM, fm1));
		x = (w - LM - sW) / 2;
		g.drawString(s, x, y);
		y += fm1.getHeight() + 10;
		g.setFont(old);

		s = SESSION_TITLE
				+ studentDetails.getSessionDetails().getDisplayString();
		sW = fm.stringWidth(getWidthString(s, w - LM, fm));
		x = LM;
		g.drawString(s, x, y);
		y += fm.getHeight() + 2;
		s = CLASS_TITLE + studentDetails.getClassDetails().getClassName();
		sW = fm.stringWidth(getWidthString(s, w - LM, fm));
		x = LM;
		g.drawString(s, x, y);
		y += fm.getHeight() + 8;
		s = schoolInfo.getName();
		String st = null;
		sW = fm.stringWidth(s);
		if (sW > w - LM) {
			int diff = sW - (w - LM);
			int dP = sW / diff;
			int lIndex = s.length() - (s.length() * dP / 100);
			if (schoolInfo.getSubtitle() == null
					|| schoolInfo.getSubtitle().isEmpty()) {

				st = s.substring(lIndex);
				s = s.substring(0, lIndex - 2) + "...";
			} else {
				s = s.substring(0, lIndex - 2) + "...";
			}
		}
		sW = fm.stringWidth(s);
		x = (w - LM - sW) / 2;
		g.drawString(s, x, y);
		y += fm.getHeight();
		// if (st != null) {
		// sW = fm.stringWidth(getWidthString(st, w - LM, fm));
		// x = (w - LM - sW) / 2;
		// g.drawString(st, x, y);
		// y += fm.getHeight() + 2;
		// } else {
		// if (schoolInfo.getSubtitle() != null) {
		// st = schoolInfo.getSubtitle();
		// sW = fm.stringWidth(getWidthString(st, w - LM, fm));
		// x = (w - LM - sW) / 2;
		// g.drawString(st, x, y);
		// y += fm.getHeight() + 2;
		// }
		// }

		/*
		 * Color color = g.getColor(); g.setColor(Color.LIGHT_GRAY); y += 5;
		 * g.drawLine(LM, y, w - 10, y); y += 5; g.setColor(color);
		 */
		Color color = g.getColor();
		g.setColor(Color.LIGHT_GRAY);
		g.drawLine(LM, y, w - LM, y);
		g.setColor(color);
		y += 15;
		s = schoolInfo.getAddress1();
		sW = fm.stringWidth(getWidthString(s, w - LM, fm));
		x = (w - LM - sW) / 2;
		g.drawString(s, x, y);
		y += fm.getHeight() + 2;
		s = schoolInfo.getAddress2();
		sW = fm.stringWidth(getWidthString(s, w - LM, fm));
		x = (w - LM - sW) / 2;
		if (x < LM) {
			x = LM;
		}
		g.drawString(s, x, y);
		y += fm.getHeight() + 2;

	}

	private Image getImageFromStudent() {
		Image image = null;
		if (studentDetails.getStudentImageDetails().getPhoto() != null) {
			byte[] bAvatar = studentDetails.getStudentImageDetails().getPhoto();
			try {
				File file = new File(TEMP_IMAGE_FILE);
				FileOutputStream fos = new FileOutputStream(file);
				fos.write(bAvatar);
				fos.close();
				image = ImageIO.read(file);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return image;
	}

	private String getWidthString(String s, int w, FontMetrics fm) {
		int sW = fm.stringWidth(s);
		if (sW > w) {
			int diff = sW - w;
			int dP = sW / diff;
			int lIndex = s.length() - (s.length() * dP / 100);
			s = s.substring(0, lIndex - 2) + "...";
		}
		return s;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (SAVE_ID_COMMAND.equals(e.getActionCommand())) {
			JFileChooser fc = new JFileChooser();
			fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
			fc.setFileFilter(new ImageFilter());
			int returnVal = fc.showDialog(this, "SAVE");

			// Process the results.
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				if (!file.getName().contains(".")) {
					file = new File(file.getAbsolutePath() + ".png");
				}
				try {
					ImageIO.write(bufferedImage, "png", file);
					MsgDialogUtils.showInformationDialog(this,
							"Identity Card image saved successfully ");
				} catch (IOException e1) {
					MsgDialogUtils.showWarningDialog(this,
							"Error occcured while saving Identity Card image ");
					Logger.EXCEPTION.log(Level.WARNING,
							"Error occcured while saving Identity Card image ",
							e);
				}
			}
		} else if ("PRINT".equals(e.getActionCommand())) {
			PrinterJob job = PrinterJob.getPrinterJob();
			job.setPrintable(this);
			boolean ok = job.printDialog();
			if (ok) {
				try {
					job.print();
				} catch (PrinterException ex) {
				}
			}
		}
	}

	@Override
	public int print(Graphics g, PageFormat pageFormat, int page)
			throws PrinterException {
		if (page > 0) { // We have only one page, and 'page' is zero-based
			return NO_SUCH_PAGE;
		}
		Graphics2D g2d = (Graphics2D) g;
		g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
		g2d.drawImage(bufferedImage, 10, 10, iWidth, iHeight, null);
		return PAGE_EXISTS;
	}
}
