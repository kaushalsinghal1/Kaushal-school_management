package com.school.utils;

import java.awt.Component;

import javax.swing.JOptionPane;

public class MsgDialogUtils {
	public static void showWarningDialog(Component parent, String msg) {
		JOptionPane.showMessageDialog(parent, msg, "Warning",
				JOptionPane.WARNING_MESSAGE);
	}

	public static void showInformationDialog(Component parent, String msg) {
		JOptionPane.showMessageDialog(parent, msg, "information",
				JOptionPane.INFORMATION_MESSAGE);
	}

	public static void showInformationDialog(Component parent,String title, String msg) {
		JOptionPane.showMessageDialog(parent, msg,title,
				JOptionPane.INFORMATION_MESSAGE);
	}
}
