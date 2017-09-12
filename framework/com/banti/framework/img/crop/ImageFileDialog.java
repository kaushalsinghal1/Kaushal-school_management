package com.banti.framework.img.crop;

import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;

public class ImageFileDialog extends JDialog implements ActionListener,
		PropertyChangeListener {
	ImageLabel imageLabel;
	JButton btnUpload;
	JButton btnSave;
	JFileChooser fc;
	CropImageDialog cropImageDialog;
	private final String UPLOAD = "Upload";
	private final String SAVE = "Save";
	private Image image;

	public ImageFileDialog() {
		super();
		setTitle("Upload Image");
		init();
		setSize(400, 400);

	}

	private void init() {
		setLayout(new FlowLayout());
		imageLabel = new ImageLabel();
		add(imageLabel);
		btnUpload = new JButton("Upload");
		btnUpload.setActionCommand(UPLOAD);
		btnUpload.addActionListener(this);
		btnSave = new JButton("Save");
		btnSave.setActionCommand(SAVE);
		btnSave.setEnabled(false);
		btnSave.addActionListener(this);
		add(btnUpload);
		add(btnSave);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (UPLOAD.equals(e.getActionCommand())) {
			if (fc == null) {
				fc = new JFileChooser();

				fc.addChoosableFileFilter(new ImageFilter());
				fc.setAcceptAllFileFilterUsed(false);
				fc.setAccessory(new ImagePreview(fc));
			}

			// Show it.
			int returnVal = fc.showDialog(this, "Attach");

			// Process the results.
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				try {
					// ImageIO.read(file);
					image = ImageIO.read(file);
					imageLabel.loadImage(image);
					cropImageDialog = new CropImageDialog(image);
					cropImageDialog.setPropetyListener(this);
					cropImageDialog.setVisible(true);
					btnSave.setEnabled(true);

					// readAndSave(file);
					// imgDisplay.setIcon(new ImageIcon(ImageIO.read(file)));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		} else if (SAVE.equals(e.getActionCommand())) {
			if (image != null) {
				JFileChooser jfs = new JFileChooser();
				jfs.addChoosableFileFilter(new ImageFilter());
				jfs.setAcceptAllFileFilterUsed(false);
				jfs.setDialogType(JFileChooser.SAVE_DIALOG);

				int i = jfs.showSaveDialog(this);
				if (i == JFileChooser.APPROVE_OPTION) {
					File f = jfs.getSelectedFile();
					if (!f.getAbsolutePath().contains(".")) {
						f=new File(f.getAbsolutePath()+".jpg");
					}
					try {
						ImageIO.write((BufferedImage) image, "jpg", f);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}

			}
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (CropImageDialog.CROPED_IMAGE.equals(evt.getPropertyName())) {
			if (evt.getNewValue() != null) {
				image = (Image) evt.getNewValue();
				imageLabel.loadImage(image);
			} else {
				// imageLabel.loadImage((Image)evt.getOldValue());
			}
			cropImageDialog.dispose();
		}
	}

	public static void main(String[] args) {
		new ImageFileDialog().setVisible(true);
	}
}
