package com.school.console.admin;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.hibernate.HibernateException;

import com.banti.framework.core.ActionDispatcher;
import com.banti.framework.ui.DialogEsc;
import com.school.console.SchoolMain;
import com.school.hiebernate.HiebernateDboUtil;
import com.school.hiebernate.dbo.SessionDetails;
import com.school.resource.CommandConstant;
import com.school.resource.ResourceConstant;
import com.school.shared.CommandCodeConstant;

public class SessionRegistrationAction extends DialogEsc implements
		ActionListener {

	private JTextField txtStartDate;
	private JTextField txtEndDate;
	private JTextField txtDisplayString;
	private JButton btnRegister;
	private JButton btnUpdate;
	private JButton btnCancel;
	private boolean isUpdate = false;
	private SessionDetails sessionDetails;
	private SimpleDateFormat sdf = new SimpleDateFormat("MM-yyyy");
	private SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");

	public SessionRegistrationAction() {
		super(SchoolMain.Frame, "Session Registration", false);
		init();
		setSize(300, 250);
		setLocationRelativeTo(SchoolMain.Frame);
		setResizable(false);
		setVisible(true);

	}

	public SessionRegistrationAction(JDialog parent) {
		super(parent, "Session Registration", true);
		init();
		setLocationRelativeTo(parent);
		setSize(300, 250);
		setResizable(false);
		setButtonState(isUpdate);
		// setVisible(true);

	}

	public SessionRegistrationAction(JDialog parent, SessionDetails s) {
		super(parent, "Session Updation", true);
		this.sessionDetails = s;
		init();
		if (s != null) {
			this.sessionDetails = s;
			isUpdate = true;
			setData();
		}
		setButtonState(isUpdate);
		setSize(300, 250);
		setResizable(false);
	}

	private void setData() {
		txtDisplayString.setText(sessionDetails.getDisplayString());
		txtStartDate.setText(sdf.format(sessionDetails.getStartDate()));
		txtEndDate.setText(sdf.format(sessionDetails.getEndDate()));
	}

	private void setButtonState(boolean isUpdate) {
		if (isUpdate) {
			btnUpdate.setFocusable(true);
		} else {
			btnRegister.setFocusable(true);
		}
		btnRegister.setEnabled(!isUpdate);
		btnUpdate.setEnabled(isUpdate);
	}

	private void init() {
		Container con = getContentPane();
		con.setLayout(new BorderLayout());
		con.add(getPanel(), BorderLayout.NORTH);
	}

	private JPanel getPanel() {
		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.LINE_START;
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(10, 5, 10, 5);
		panel.add(getMiddleJPanel(), c);
		c.gridx = 0;
		c.gridy = 1;
		panel.add(getButtonPanel(), c);
		return panel;
	}

	private JPanel getButtonPanel() {
		JPanel panel = new JPanel(new FlowLayout());
		btnRegister = new JButton("Register");
		btnRegister.setActionCommand(CommandConstant.REGISTER_COMMAND);
		btnRegister.addActionListener(this);

		btnUpdate = new JButton("Update");
		btnUpdate.setActionCommand(CommandConstant.UPDATE_COMMAND);
		btnUpdate.addActionListener(this);

		btnCancel = new JButton(ResourceConstant.CANCEL);
		btnCancel.setActionCommand(CommandConstant.CANCEL_COMMAND);
		btnCancel.addActionListener(this);
		panel.add(btnRegister);
		panel.add(btnUpdate);
		panel.add(btnCancel);
		return panel;
	}

	private JPanel getMiddleJPanel() {
		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.NONE;
		// 0
		JLabel lb = new JLabel("Start Date ");
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0.5;
		c.insets = new Insets(5, 5, 5, 5);
		c.anchor = GridBagConstraints.LINE_START;
		panel.add(lb, c);
		c.gridx = 1;
		c.gridy = 0;
		// c.weightx = 0.5;
		c.gridwidth = 1;
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MONTH, Calendar.JULY);
		txtStartDate = new JTextField(5);
		txtStartDate.setText(sdf.format(cal.getTime()));
		lb.setLabelFor(txtStartDate);
		panel.add(txtStartDate, c);
		c.gridx = 2;
		c.gridy = 0;
		// c.weightx = 0.5;
		panel.add(new JLabel("MM-YYYY"), c);

		// 1
		lb = new JLabel("End Date ");
		c.gridx = 0;
		c.gridy = 1;
		// c.weightx = 0.5;
		c.insets = new Insets(5, 5, 5, 5);
		c.anchor = GridBagConstraints.LINE_START;
		panel.add(lb, c);
		c.gridx = 1;
		c.gridy = 1;
		// c.weightx = 0.5;
		c.gridwidth = 1;
		txtEndDate = new JTextField(5);
		lb.setLabelFor(txtEndDate);
		cal.add(Calendar.YEAR, 1);
		cal.set(Calendar.MONTH, Calendar.JUNE);
		txtEndDate.setText(sdf.format(cal.getTime()));
		panel.add(txtEndDate, c);
		c.gridx = 2;
		c.gridy = 1;
		// c.weightx = 0.5;
		panel.add(new JLabel("MM-YYYY"), c);

		// 2
		lb = new JLabel("Display Formate ");
		c.gridx = 0;
		c.gridy = 2;
		// c.weightx = 0.5;
		c.insets = new Insets(5, 5, 5, 5);
		c.anchor = GridBagConstraints.LINE_START;
		panel.add(lb, c);
		c.gridx = 1;
		c.gridy = 2;
		// c.weightx = 0.5;
		c.gridwidth = 1;
		c.gridwidth = 2;
		txtDisplayString = new JTextField(10);
		txtDisplayString.setEnabled(false);
		String endstr = sdfYear.format(cal.getTime());
		cal.add(Calendar.YEAR, -1);
		txtDisplayString.setText(sdfYear.format(cal.getTime()) + "-" + endstr);
		lb.setLabelFor(txtDisplayString);
		panel.add(txtDisplayString, c);
		setFocusListener();
		return panel;
	}

	private void setFocusListener() {
		txtEndDate.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent arg0) {
				String error = inputValidate();
				if (error != null) {
					showWarningDialog(error);
					return;
				}
				setDisplayString();
			}

			@Override
			public void focusGained(FocusEvent arg0) {
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (CommandConstant.REGISTER_COMMAND.equals(e.getActionCommand())) {
			String error = inputValidate();
			if (error != null) {
				showWarningDialog(error);
				return;
			}
			boolean result = false;
			SessionDetails sessionDetails = null;
			try {
				sessionDetails = createSessionDetails();
				sessionDetails.setAddDate(new Date());
				result = HiebernateDboUtil.saveSessionDetails(sessionDetails);
			} catch (ParseException e1) {
				showWarningDialog("Invalid Date Format! ");
				return;
			} catch (HibernateException exp) {
				showWarningDialog("Database error occured! ");
				return;
			}
			if (result) {
				showInformationDialog("Session information has been registered Successfully!");
				dispose();
				ActionDispatcher.fireAction(
						CommandCodeConstant.SESSION_REG_CLOSE, sessionDetails);
			} else {
				showWarningDialog("Session information Already Registered Successfully!");
			}

		} else if (CommandConstant.UPDATE_COMMAND.equals(e.getActionCommand())) {
			String error = inputValidate();
			if (error != null) {
				showWarningDialog(error);
				return;
			}
			if (sessionDetails != null) {
				try {
					setSessionDetails();
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
			} else {
				return;
			}
			boolean result = false;
			try {
				sessionDetails.setUpdateDate(new Date());
				result = HiebernateDboUtil.updateSessionDetails(sessionDetails);
			} catch (HibernateException exp) {
				showWarningDialog("Database error occured! ");
				return;
			}
			if (result) {
				showInformationDialog("Session information has been updated Successfully!");
				dispose();
				ActionDispatcher.fireAction(
						CommandCodeConstant.SESSION_MOD_CLOSE, sessionDetails);
			} else {
				showWarningDialog("Session information already registered with same session name ");
			}

		} else if (CommandConstant.CANCEL_COMMAND.equals(e.getActionCommand())) {
			dispose();
			ActionDispatcher
					.removeListener(CommandCodeConstant.SESSION_REG_CLOSE);
			ActionDispatcher
					.removeListener(CommandCodeConstant.SESSION_MOD_CLOSE);
		}
	}

	private String inputValidate() {
		StringBuilder sb = new StringBuilder();
		boolean error = false;
		if (txtStartDate.getText().trim().isEmpty()) {
			sb.append("Please Enter Start Date.");
			sb.append("\n");
			error = true;
		}
		if (txtEndDate.getText().trim().isEmpty()) {
			sb.append("Please Enter Start Date.");
			sb.append("\n");
			error = true;
		}
		if (error) {
			return sb.toString();
		}
		try {
			sdf.parse(txtEndDate.getText().trim());
			sdf.parse(txtEndDate.getText().trim());
			if (sdf.parse(txtStartDate.getText().trim()).after(
					sdf.parse(txtEndDate.getText().trim()))) {
				return "Start date is greater then End Date";
			}
		} catch (ParseException e) {
			return "Invalid Date Format! ";
		}
		return null;
	}

	private void setDisplayString() {
		Calendar cal = Calendar.getInstance();
		try {
			cal.setTime(sdf.parse(txtStartDate.getText().trim()));
			cal.set(Calendar.DATE, 1);
			String yStart = sdfYear.format(cal.getTime());
			cal.setTime(sdf.parse(txtEndDate.getText().trim()));
			cal.set(Calendar.DATE, 31);
			String yEnd = sdfYear.format(cal.getTime());
			txtDisplayString.setText(yStart + "-" + yEnd);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	private SessionDetails createSessionDetails() throws ParseException {
		SessionDetails sessionDetails = new SessionDetails();
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.setTime(sdf.parse(txtStartDate.getText().trim()));
		cal.set(Calendar.DATE, 1);
		sessionDetails.setStartDate(cal.getTime());
		cal.setTime(sdf.parse(txtEndDate.getText().trim()));
		cal.set(Calendar.DATE, 1);
		cal.add(Calendar.MONTH, 1);
		cal.add(Calendar.DATE, -1);
		sessionDetails.setEndDate(cal.getTime());
		if (txtDisplayString.getText().trim().isEmpty()) {
			sessionDetails.setDisplayString(sdfYear.format(sessionDetails
					.getStartDate())
					+ "-"
					+ sdfYear.format(sessionDetails.getEndDate()));
		} else {
			sessionDetails.setDisplayString(txtDisplayString.getText().trim());
		}
		return sessionDetails;
	}

	private void setSessionDetails() throws ParseException {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.setTime(sdf.parse(txtStartDate.getText().trim()));
		cal.set(Calendar.DATE, 1);
		sessionDetails.setStartDate(cal.getTime());
		cal.setTime(sdf.parse(txtEndDate.getText().trim()));
		cal.set(Calendar.DATE, 31);
		sessionDetails.setEndDate(cal.getTime());
		if (txtDisplayString.getText().trim().isEmpty()) {
			sessionDetails.setDisplayString(sdfYear.format(sessionDetails
					.getStartDate())
					+ "-"
					+ sdfYear.format(sessionDetails.getEndDate()));
		} else {
			sessionDetails.setDisplayString(txtDisplayString.getText().trim());
		}
		// return sessionDetails;
	}

	private void showWarningDialog(String msg) {
		JOptionPane.showMessageDialog(this, msg, "Warning",
				JOptionPane.WARNING_MESSAGE);
	}

	private void showInformationDialog(String msg) {
		JOptionPane.showMessageDialog(this, msg, "Warning",
				JOptionPane.INFORMATION_MESSAGE);
	}

}
