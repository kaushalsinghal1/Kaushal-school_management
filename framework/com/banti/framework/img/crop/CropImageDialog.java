package com.banti.framework.img.crop;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
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
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class CropImageDialog extends JDialog implements PropertyChangeListener {
	int drag_status = 0, c1, c2, c3, c4;
	private BufferedImage bufferedImage;
	private JButton btnSave;
	private Rectangle clipRect;
	private ImagePanel im;
	private PropertyChangeListener propertyChangeListener;
	public static final String CROPED_IMAGE = "CROPED";

	public static void main(String args[]) {
		new CropImageDialog().start();
	}

	public CropImageDialog(Frame frame,Image source) {
		super(frame);
		this.bufferedImage = (BufferedImage) source;
		im = new ImagePanel(source);
		init();
		setSize(400, 400);
		setLocationRelativeTo(frame);
		// setVisible(true);
	}
	public CropImageDialog(Dialog frame,Image source) {
		super(frame);
		this.bufferedImage = (BufferedImage) source;
		im = new ImagePanel(source);
		init();
		setSize(400, 400);
		setLocationRelativeTo(frame);
		// setVisible(true);
	}
	
	public CropImageDialog(Image source) {
		this.bufferedImage = (BufferedImage) source;
		im = new ImagePanel(source);
		init();
		setSize(400, 400);
		// setVisible(true);
	}

	public CropImageDialog() {
	}

	public CropImageDialog(JDialog dialog, Image source) {
		super(dialog);
		this.bufferedImage = (BufferedImage) source;
		im = new ImagePanel(source);
		init();
		setSize(400, 400);
		setLocationRelativeTo(dialog);
	}

	public void start() {
		im = new ImagePanel("images/u1.jpg");
		bufferedImage = ImageCropper.readImage("images/u1.jpg");
		init();
		setSize(400, 400);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	private void init() {
		setLayout(new BorderLayout());
		
		add(getButtonPanel(), BorderLayout.NORTH);
		JScrollPane scrollPane = new JScrollPane(im);
		add(scrollPane, BorderLayout.CENTER);
		setSize(400, 400);
		setVisible(true);
		im.setListener(this);
		
		// im.addMouseListener(this);
		// im.addMouseMotionListener(this);
	}

	private JPanel getButtonPanel() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		btnSave = new JButton("Crop");
		JButton btnCancel = new JButton("Use Original");
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		btnSave.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					// saveDraggedScreen(clipRect);
					firePropertyChanged();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		btnSave.setEnabled(false);
		panel.add(new JLabel("Drag mouse in image to crop"));
		panel.add(btnSave);
		panel.add(btnCancel);
		return panel;
	}

	private void firePropertyChanged() {
		try {
			propertyChangeListener.propertyChange(new PropertyChangeEvent(this,
					CROPED_IMAGE, bufferedImage, getCroppedImage()));
		} catch (Exception e) {
			propertyChangeListener.propertyChange(new PropertyChangeEvent(this,
					CROPED_IMAGE, bufferedImage, null));
		}
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

	public BufferedImage getCroppedImage() throws Exception {

		int w = clipRect.x - clipRect.width;
		int h = clipRect.y - clipRect.height;
		w = w * -1;
		h = h * -1;
		BufferedImage img = ImageCropper.cropMyImage(bufferedImage, w, h,
				clipRect.x, clipRect.y);
		return img;
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

	public void setPropetyListener(PropertyChangeListener propertyChangeListener) {
		this.propertyChangeListener = propertyChangeListener;
	}

}
