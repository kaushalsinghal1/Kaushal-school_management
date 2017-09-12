package com.banti.framework.table;

import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;

public class CheckBoxList extends JList implements MouseListener {

	private int previous = -1;

	public CheckBoxList(JCheckBox[] boxes) {
		super(boxes);
		super.setCellRenderer(new ListRenderer());
		super.addMouseListener(this);
	}

	public CheckBoxList(ListModel model) {
		super(model);
		super.setCellRenderer(new ListRenderer());
		super.addMouseListener(this);
	}

	class ListRenderer implements ListCellRenderer {

		ListRenderer() {
		}

		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {
			JCheckBox chb = null;
			if (value instanceof JCheckBox) {
				chb = (JCheckBox) value;
			} else {
				chb = new JCheckBox(value.toString());
			}
			if (isSelected) {
				chb.setBackground(list.getSelectionBackground());
				chb.setForeground(Color.WHITE);
			} else {
				chb.setBackground(Color.white);
				chb.setForeground(list.getForeground());
			}
			return chb;
		}
	}

	public void mouseClicked(MouseEvent e) {
		if (!super.isEnabled()) {
			return;
		}
		Point p = e.getPoint();
		int index = this.locationToIndex(p);
		if (!(super.getModel().getElementAt(index) instanceof JCheckBox)) {
			return;
		}
		JCheckBox checkBox = (JCheckBox) super.getModel().getElementAt(index);
		if (p.getX() > 19 && previous != index) {
			previous = index;
			return;
		}
		if (checkBox.isSelected()) {
			checkBox.setSelected(false);
		} else {
			checkBox.setSelected(true);
		}
		previous = index;
		super.repaint();
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		DefaultListModel listModel = new DefaultListModel();
		listModel.addElement(new JCheckBox("A"));
		listModel.addElement(new JCheckBox("C"));
		listModel.addElement(new JCheckBox("D"));
		listModel.addElement(new JCheckBox("E"));
		listModel.addElement(new JCheckBox("F"));
		CheckBoxList cbl = new CheckBoxList(listModel);
		JScrollPane jsp = new JScrollPane(cbl);
		frame.getContentPane().add(jsp);
		frame.pack();
		frame.setVisible(true);
	}

}
