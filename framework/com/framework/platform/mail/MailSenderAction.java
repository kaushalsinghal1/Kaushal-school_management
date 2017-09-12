package com.framework.platform.mail;

import java.awt.Container;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import com.banti.framework.platform.Application;
import com.banti.framework.ui.DialogEsc;

public class MailSenderAction extends DialogEsc {
	public MailSenderAction() {
		super(Application.Frame, "Mail Sender", true);
		init();
		
	}

	private void init() {
		Container con = getContentPane();
		JPanel panel=new EmailPanel(this).createPanel();
		panel.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(1),
				"Send Mail from gmail account"));
		con.add(panel);
		setSize(460, 500);
		setLocationRelativeTo(Application.Frame);
		setVisible(true);
	}

	public static void main(String[] args) {
		new MailSenderAction();
	}
}
