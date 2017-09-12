package com.banti.framework.ui;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;

public class DialogEsc extends JDialog {
	

	public DialogEsc() {
		super();
	}

	public DialogEsc(Dialog owner) {
		super(owner);
	}

	public DialogEsc(Dialog owner, boolean modal) {
		super(owner, modal);
	}

	public DialogEsc(Dialog owner, String title) {
		super(owner, title);
	}

	public DialogEsc(Dialog owner, String title, boolean modal) {
		super(owner, title, modal);
	}

	public DialogEsc(Dialog owner, String title, boolean modal, GraphicsConfiguration gc) {
		super(owner, title, modal, gc);
	}

	public DialogEsc(Frame owner) {
		super(owner);
	}

	public DialogEsc(Frame owner, boolean modal) {
		super(owner, modal);
	}

	public DialogEsc(Frame owner, String title) {
		super(owner, title);
	}

	public DialogEsc(Frame owner, String title, boolean modal) {
		super(owner, title, modal);
	}

	public DialogEsc(Frame owner, String title, boolean modal, GraphicsConfiguration gc) {
		super(owner, title, modal, gc);
	}

	protected JRootPane createRootPane() {
		ActionListener actionListener = new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				dispose();
			}
		};
		KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
		JRootPane rootPane = new JRootPane();
		rootPane.registerKeyboardAction(actionListener, stroke,
				JComponent.WHEN_IN_FOCUSED_WINDOW);
		return rootPane;
	}
}
