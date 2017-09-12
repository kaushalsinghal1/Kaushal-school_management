package com.banti.framework.img.crop;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;

public class CropImage extends JFrame implements MouseListener,
		MouseMotionListener, PropertyChangeListener {
	int drag_status = 0, c1, c2, c3, c4;
	private BufferedImage bufferedImage;
	private JButton btnSave;
	private Rectangle clipRect;
	private ImagePanel im;

	public static void main(String args[]) {
		new CropImage().start();
	}

	public CropImage(Image source) {
		this.bufferedImage = (BufferedImage) source;
		ImagePanel im = new ImagePanel(source);
		init();
	}

	public CropImage() {
	}

	public void start() {
		ImagePanel im = new ImagePanel("images/u1.jpg");
		bufferedImage = ImageCropper.readImage("images/u1.jpg");
		init();
	}

	private void init() {
		setLayout(new BorderLayout());
		btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					saveDraggedScreen(clipRect);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		btnSave.setEnabled(false);
		add(btnSave, BorderLayout.NORTH);
		add(im, BorderLayout.CENTER);
		setSize(400, 400);
		setVisible(true);
		im.setListener(this);
		// im.addMouseListener(this);
		// im.addMouseMotionListener(this);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	public void draggedScreen1() throws Exception {
		int w = c1 - c3;
		int h = c2 - c4;
		w = w * -1;
		h = h * -1;
		Robot robot = new Robot();
		BufferedImage img = robot.createScreenCapture(new Rectangle(c1, c2, w,
				h));
		File save_path = new File("images/screen1.jpg");
		ImageIO.write(img, "JPG", save_path);
		System.out.println("Cropped image saved successfully.");
	}

	public void saveDraggedScreen() throws Exception {
		int w = c1 - c3;
		int h = c2 - c4;
		w = w * -1;
		h = h * -1;
		BufferedImage img = ImageCropper.cropMyImage(bufferedImage, w, h, c1,
				c2);
		File save_path = new File("images/screen1.jpg");
		ImageIO.write(img, "JPG", save_path);
		System.out.println("Cropped image saved successfully.");
	}

	public void saveDraggedScreen(Rectangle rect) throws Exception {
		int w = rect.x - rect.width;
		int h = rect.y - rect.height;
		w = w * -1;
		h = h * -1;
		BufferedImage img = ImageCropper.cropMyImage(bufferedImage, w, h,
				rect.x, rect.y);
		File save_path = new File("images/screen1.jpg");
		ImageIO.write(img, "JPG", save_path);
		System.out.println("Cropped image saved successfully.");
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		repaint();
		c1 = arg0.getX();
		c2 = arg0.getY();
		btnSave.setEnabled(false);
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		repaint();
		if (drag_status == 1) {
			c3 = arg0.getX();
			c4 = arg0.getY();
			try {
				btnSave.setEnabled(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		repaint();
		drag_status = 1;
		c3 = arg0.getX();
		c4 = arg0.getY();
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {

	}

	public void paint(Graphics g) {
		super.paint(g);
		int w = c1 - c3;
		int h = c2 - c4;
		w = w * -1;
		h = h * -1;
		if (w < 0)
			w = w * -1;
		g.drawRect(c1, c2, w, h);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (ImagePanel.PRESSED.equals(evt.getPropertyName())) {
			btnSave.setEnabled(false);
		} else if (ImagePanel.RELEASED.equals(evt.getPropertyName())) {
			btnSave.setEnabled(true);
			this.clipRect = (Rectangle) evt.getNewValue();
			/*
			 * try { saveDraggedScreen((Rectangle) evt.getNewValue()); } catch
			 * (Exception e) { e.printStackTrace(); }
			 */
		}
	}

}
