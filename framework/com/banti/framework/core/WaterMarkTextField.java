package com.banti.framework.core;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.Position;
import javax.swing.text.Segment;

public class WaterMarkTextField extends JTextField {
	private String waterMarkText = "";
	private String emptyText = "";

	public WaterMarkTextField(String text, int column) {
		super(column);
		init(text);
	}

	public WaterMarkTextField(String text) {
		init(text);
	}

	private void init(String text) {
		waterMarkText = text;
		emptyText = text;
		/*
		 * addFocusListener(new FocusAdapter() {
		 * 
		 * @Override public void focusGained(FocusEvent e) { emptyText = "";
		 * paintComponent(getGraphics()); super.focusGained(e); }
		 * 
		 * @Override public void focusLost(FocusEvent e) { if
		 * (getText().isEmpty()) { emptyText = waterMarkText;
		 * paintComponent(getGraphics()); } super.focusLost(e); } });
		 */
		getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent arg0) {
				if (!getText().equals("")) {
					emptyText = "";
				} else {
					emptyText = waterMarkText;
				}
				paintComponent(getGraphics());
			}

			@Override
			public void insertUpdate(DocumentEvent arg0) {
				if (!getText().equals("")) {
					emptyText = "";
				} else {
					emptyText = waterMarkText;
				}
				paintComponent(getGraphics());
			}

			@Override
			public void changedUpdate(DocumentEvent arg0) {
				if (!getText().equals("")) {
					emptyText = "";
				} else {
					emptyText = waterMarkText;
				}
				paintComponent(getGraphics());
			}
		});
	}

	@Override
	protected void paintComponent(Graphics g) {
		if (g != null) {
			super.paintComponent(g);
			g.setColor(Color.LIGHT_GRAY);
			g.drawString(emptyText, 5, 14);
		}
	}
}
