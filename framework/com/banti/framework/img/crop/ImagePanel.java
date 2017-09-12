package com.banti.framework.img.crop;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ImagePanel extends JLabel implements MouseListener,
		MouseMotionListener {
	private Image img;
	int drag_status = 0, c1, c2, c3, c4;
	private PropertyChangeListener listener;
	public static final String RELEASED = "RELEASED";
	public static final String PRESSED = "PRESSED";

	public ImagePanel(String img) {
		this(new ImageIcon(img).getImage());
	}

	public ImagePanel(Image img) {
		this.img = img;
		Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));
		// Dimension size = new Dimension(10,10);
		setPreferredSize(size);
		setMinimumSize(size);
		setMaximumSize(size);
		setSize(size);
		addMouseListener(this);
		addMouseMotionListener(this);
		setLayout(null);
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
		listener.propertyChange(new PropertyChangeEvent(this,
				PRESSED, null, null));
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		repaint();
		if (drag_status == 1) {
			c3 = arg0.getX();
			c4 = arg0.getY();
			if (c3 <= c1 || c4 <= c2) {
				return;
			}
			Rectangle rectangle = new Rectangle(c1, c2, c3, c4);
			try {
				listener.propertyChange(new PropertyChangeEvent(this,
						RELEASED, null, rectangle));
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

	public void paintComponent(Graphics g) {
		g.drawImage(img, 0, 0, null);
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

	public void setListener(PropertyChangeListener listener) {
		this.listener = listener;
	}
}
