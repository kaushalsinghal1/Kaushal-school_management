package com.banti.framework.img.crop;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFileChooser;

import com.banti.framework.theme.ToolKit;

public class ImageLabel extends JComponent implements PropertyChangeListener {
	ImageIcon thumbnail = null;
	File file = null;
	String DEFAULT_IMAGE = "/com/banti/framework/img/crop/noimage.jpeg";
	Dimension d;

	public ImageLabel(JFileChooser fc) {
		this(fc, new Dimension(100, 50));
	}

	public ImageLabel(JFileChooser fc, Dimension dimension) {
		this.d = dimension;
		setPreferredSize(dimension);
		fc.addPropertyChangeListener(this);
	}

	public ImageLabel() {
		this(new Dimension(180, 200));
	}

	public ImageLabel(Dimension dimension) {
		d = dimension;
		setPreferredSize(dimension);
		// fc.addPropertyChangeListener(this);
	}

	public void loadImage() {
		ImageIcon tmpIcon = null;
		if (file == null) {
			//file = new File(DEFAULT_IMAGE);
			tmpIcon=ToolKit.getInstance().createImageIcon(
					getClass().getResource(DEFAULT_IMAGE));
			//tmpIcon = new ImageIcon(image);
			// thumbnail = null;
			// return;
		} else {
			tmpIcon = new ImageIcon(file.getPath());
		}

		// Don't use createImageIcon (which is a wrapper for getResource)
		// because the image we're trying to load is probably not one
		// of this program's own resources.
		if (tmpIcon != null) {
			if (tmpIcon.getIconWidth() > d.width
					|| tmpIcon.getIconHeight() > d.height) {
				thumbnail = new ImageIcon(tmpIcon.getImage().getScaledInstance(
						180, -1, Image.SCALE_DEFAULT));
			} else { // no need to miniaturize
				thumbnail = tmpIcon;
			}
		}
	}

	public void loadImage(Image tmpIcon1) {
		if (file == null) {
			file = new File(DEFAULT_IMAGE);
		}

		// Don't use createImageIcon (which is a wrapper for getResource)
		// because the image we're trying to load is probably not one
		// of this program's own resources.
		ImageIcon tmpIcon = new ImageIcon(tmpIcon1);
		if (tmpIcon != null) {
			if (tmpIcon.getIconWidth() > d.width
					|| tmpIcon.getIconHeight() > d.height) {
				thumbnail = new ImageIcon(tmpIcon.getImage().getScaledInstance(
						d.width, -1, Image.SCALE_DEFAULT));
			} else { // no need to miniaturize
				thumbnail = tmpIcon;
			}
		}
		repaint();
	}

	public void propertyChange(PropertyChangeEvent e) {
		boolean update = false;
		String prop = e.getPropertyName();

		// If the directory changed, don't show an image.
		if (JFileChooser.DIRECTORY_CHANGED_PROPERTY.equals(prop)) {
			file = null;
			update = true;

			// If a file became selected, find out which one.
		} else if (JFileChooser.SELECTED_FILE_CHANGED_PROPERTY.equals(prop)) {
			file = (File) e.getNewValue();
			update = true;
		}

		// Update the preview accordingly.
		if (update) {
			thumbnail = null;
			if (isShowing()) {
				loadImage();
				repaint();
			}
		}
	}

	protected void paintComponent(Graphics g) {
		if (thumbnail == null) {
			loadImage();
		}
		if (thumbnail != null) {
			int x = getWidth() / 2 - thumbnail.getIconWidth() / 2;
			int y = getHeight() / 2 - thumbnail.getIconHeight() / 2;

			if (y < 0) {
				y = 0;
			}

			if (x < 5) {
				x = 5;
			}
			thumbnail.paintIcon(this, g, x, y);
		}
	}
}
