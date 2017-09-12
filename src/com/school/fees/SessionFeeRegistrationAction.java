package com.school.fees;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.hibernate.HibernateException;

import com.banti.framework.core.ActionDispatcher;
import com.banti.framework.ui.DialogEsc;
import com.school.combobox.model.ClassComboBoxModel;
import com.school.combobox.model.SessionComboBoxModel;
import com.school.console.SchoolMain;
import com.school.hiebernate.HiebernateDboUtil;
import com.school.hiebernate.dbo.ClassDetails;
import com.school.hiebernate.dbo.SessionClassFeeDetails;
import com.school.hiebernate.dbo.SessionDetails;
import com.school.resource.CommandConstant;
import com.school.resource.ResourceConstant;
import com.school.shared.CommandCodeConstant;

public class SessionFeeRegistrationAction extends DialogEsc implements
		ActionListener {

	private JTextField txtAdmission;
	private JTextField txtTution;
	private JTextField txtOther;
	private JTextField txtBus;
	private JTextField txtGrandTotal;
	private JComboBox cbSession;
	private JComboBox cbClass;
	private ClassComboBoxModel classComboBoxModel;
	private SessionComboBoxModel sessionComboBoxModel;
	private static NumberFormat numberFormat = NumberFormat.getInstance();
	private SessionClassFeeDetails sessionClassFeeDetails;
	private boolean isUpdate = false;
	private JButton btnRegister;
	private JButton btnUpdate;
	private JButton btnCancel;

	public SessionFeeRegistrationAction(JDialog parent,
			List<ClassDetails> classDetailsList,
			List<SessionDetails> sessionDetailsList) {
		this(parent, classDetailsList, sessionDetailsList, null);
	}

	public SessionFeeRegistrationAction(JDialog parent,
			List<ClassDetails> classDetailsList,
			List<SessionDetails> sessionDetailsList,
			SessionClassFeeDetails sessionClassFeeDetails) {
		super(SchoolMain.Frame, "Session Class Fees Registration", true);
		classComboBoxModel = new ClassComboBoxModel(classDetailsList);
		sessionComboBoxModel = new SessionComboBoxModel(sessionDetailsList);
		init();
		setInitialValue();
		if (sessionClassFeeDetails != null) {
			this.sessionClassFeeDetails = sessionClassFeeDetails;
			isUpdate = true;
			setData();

		}
		setButtonState(isUpdate);
		setSize(350,300);
		setResizable(false);
		// setVisible(true);
	}

	private void setData() {
		cbClass.setSelectedItem(sessionClassFeeDetails.getClassDetails()
				.getClassName());
		cbSession.setSelectedItem(sessionClassFeeDetails.getSessionDetails()
				.getDisplayString());
		txtAdmission.setText(sessionClassFeeDetails.getAdmissionFee() + "");
		txtTution.setText(sessionClassFeeDetails.getTuitionFee() + "");
		txtOther.setText(sessionClassFeeDetails.getActivityFee() + "");
		txtBus.setText(sessionClassFeeDetails.getBusFee() + "");
		setGrandTotal();

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

	public SessionFeeRegistrationAction() throws HibernateException {
		super(SchoolMain.Frame, "Fees Registration", true);
		List<ClassDetails> classDetailsList = HiebernateDboUtil
				.getClassDetails();
		List<SessionDetails> sessionDetailsList = HiebernateDboUtil
				.getSessionDetails();
		classComboBoxModel = new ClassComboBoxModel(classDetailsList);
		sessionComboBoxModel = new SessionComboBoxModel(sessionDetailsList);
		init();
		setInitialValue();
		setSize(350, 300);
		setLocationRelativeTo(SchoolMain.Frame);
		setResizable(false);
		setVisible(true);
	}
	
	

	private void init() {
		Container con = getContentPane();
		con.setLayout(new BorderLayout());
		con.add(getMiddlePanel(), BorderLayout.NORTH);
		con.add(getButtonPanel(), BorderLayout.SOUTH);
		// .add(getPanel());

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
		// panel.add(btnCalculate);
		panel.add(btnUpdate);
		panel.add(btnRegister);
		panel.add(btnCancel);
		btnRegister.setFocusable(true);
		return panel;
	}

	private JPanel getMiddlePanel() {
		JPanel panel = new JPanel();
		panel.setSize(new Dimension(100, 100));
		panel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(5, 5, 5, 15);
		c.gridwidth = 1;
		c.weightx = 0.5;
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.LINE_START;
		JLabel lb = new JLabel("Select Session");
		panel.add(lb, c);
		c.gridx = 1;
		cbSession = new JComboBox(sessionComboBoxModel);
		lb.setLabelFor(cbSession);
		panel.add(cbSession, c);
		// 1
		c.gridx = 0;
		c.gridy = 1;
		c.anchor = GridBagConstraints.LINE_START;
		lb = new JLabel("Select Class");
		panel.add(lb, c);
		c.gridx = 1;
		cbClass = new JComboBox(classComboBoxModel);
		lb.setLabelFor(cbClass);
		panel.add(cbClass, c);

		// 2
		c.gridx = 0;
		c.gridy = 2;
		c.anchor = GridBagConstraints.LINE_START;
		lb = new JLabel("Admission Fees");
		panel.add(lb, c);
		c.gridx = 1;
		txtAdmission = new JFormattedTextField(numberFormat);
		lb.setLabelFor(txtAdmission);
		txtAdmission.setColumns(7);
		panel.add(txtAdmission, c);

		// 3
		c.gridx = 0;
		c.gridy = 3;
		lb = new JLabel("Tution Fees");
		panel.add(lb, c);
		c.gridx = 1;
		txtTution = new JFormattedTextField(numberFormat);
		lb.setLabelFor(txtTution);
		txtTution.setColumns(7);
		panel.add(txtTution, c);

		// 4
		c.gridy = 4;
		c.gridx = 0;
		lb = new JLabel("Activity & Other");
		panel.add(lb, c);
		c.gridx = 1;
		txtOther = new JFormattedTextField(numberFormat);
		lb.setLabelFor(txtOther);
		txtOther.setColumns(7);
		panel.add(txtOther, c);
		// 5
		c.gridy = 5;
		c.gridx = 0;
		lb = new JLabel("Transportaion");
		panel.add(lb, c);
		c.gridx = 1;
		txtBus = new JFormattedTextField(numberFormat);
		lb.setLabelFor(txtBus);
		txtOther.setColumns(7);
		panel.add(txtBus, c);

		// 6
		c.gridy = 6;
		c.gridx = 0;
		c.gridwidth = 1;

		lb = new JLabel("Grand Total");
		panel.add(lb, c);
		c.gridx = 1;
		txtGrandTotal = new JFormattedTextField(numberFormat);
		lb.setLabelFor(txtGrandTotal);
		txtGrandTotal.setEditable(false);
		txtGrandTotal.setColumns(7);
		panel.add(txtGrandTotal, c);
		setFocusListener();
		return panel;

	}

	private void setInitialValue() {
		txtAdmission.setText("0");
		txtTution.setText("0");
		txtOther.setText("0");
		txtBus.setText("0");
		txtGrandTotal.setText("0");
	}

	private void setFocusListener() {
		FocusListener focusListener = new FocusListener() {

			@Override
			public void focusLost(FocusEvent arg0) {
				String error = inputValidate();
				if (error != null) {
					showWarningDialog(error);
					return;
				}
				setGrandTotal();
			}

			@Override
			public void focusGained(FocusEvent arg0) {
			}
		};
		txtOther.addFocusListener(focusListener);
		txtBus.addFocusListener(focusListener);
	}

	protected void setGrandTotal() {
		try {
			int admission = numberFormat.parse(txtAdmission.getText().trim())
					.intValue();
			int tuition = numberFormat.parse(txtTution.getText().trim())
					.intValue();
			int other = numberFormat.parse(txtOther.getText().trim())
					.intValue();
			int bus = numberFormat.parse(txtBus.getText().trim()).intValue();
			int gtotal = admission + tuition + other + bus;
			txtGrandTotal.setText(numberFormat.format(gtotal));
		} catch (ParseException e) {
		}
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
			SessionClassFeeDetails sessionFeeDetails = null;
			try {
				if (JOptionPane.YES_OPTION != zeroCheck()) {
					return;
				}
				sessionFeeDetails = getSessionClassFeeDetails();
				sessionFeeDetails.setAddDate(new Date());
				result = HiebernateDboUtil
						.saveSessionFeeDetails(sessionFeeDetails);
			} catch (ParseException e1) {
				showWarningDialog("Invalid input numbers ! ");
				return;
			} catch (HibernateException exp) {
				showWarningDialog("Database error occured! ");
				return;
			}
			if (result) {
				showInformationDialog("Session Class Fee information has been registered Successfully!");
				dispose();
				ActionDispatcher.fireAction(
						CommandCodeConstant.SESSION_FEE_REG_CLOSE,
						sessionFeeDetails);
			} else {
				showWarningDialog("Session Class Fee information Already Registered Successfully!");
			}

		} else if (CommandConstant.UPDATE_COMMAND.equals(e.getActionCommand())) {

			String error = inputValidate();
			if (error != null) {
				showWarningDialog(error);
				return;
			}
			if (sessionClassFeeDetails == null) {
				return;
			}
			boolean result = false;
			try {
				if (JOptionPane.YES_OPTION != zeroCheck()) {
					return;
				}
				setSessionFeeDetails();
				sessionClassFeeDetails.setUpdateDate(new Date());
				result = HiebernateDboUtil
						.updateSessionFeeDetails(sessionClassFeeDetails);
			} catch (ParseException e1) {
				showWarningDialog("Invalid input numbers ! ");
				return;
			} catch (HibernateException exp) {
				showWarningDialog("Database error occured! ");
				return;
			}
			if (result) {
				showInformationDialog("Session Class Fee information has been updated Successfully!");
				dispose();
				ActionDispatcher.fireAction(
						CommandCodeConstant.SESSION_MOD_CLOSE,
						sessionClassFeeDetails);
			} else {
				showWarningDialog("Session Class Fee information already registered with same session name ");
			}

		} else if (CommandConstant.CANCEL_COMMAND.equals(e.getActionCommand())) {
			dispose();
		}
	}
	private SessionClassFeeDetails getSessionClassFeeDetails()
			throws ParseException {
		int admission = numberFormat.parse(txtAdmission.getText().trim())
				.intValue();
		int tuition = numberFormat.parse(txtTution.getText().trim()).intValue();
		int other = numberFormat.parse(txtOther.getText().trim()).intValue();
		int bus = numberFormat.parse(txtBus.getText().trim()).intValue();

		SessionClassFeeDetails sessionFeeDetails = new SessionClassFeeDetails();
		sessionFeeDetails.setAdmissionFee(admission);
		sessionFeeDetails.setTuitionFee(tuition);
		sessionFeeDetails.setActivityFee(other);
		sessionFeeDetails.setBusFee(bus);
		sessionFeeDetails.setSessionDetails(sessionComboBoxModel
				.getSelectedObject(cbSession.getSelectedIndex()));
		sessionFeeDetails.setClassDetails(classComboBoxModel
				.getSelectedObject(cbClass.getSelectedIndex()));
		
		return sessionFeeDetails;
	}
	private void setSessionFeeDetails() throws ParseException {
		int admission = numberFormat.parse(txtAdmission.getText().trim())
				.intValue();
		int tuition = numberFormat.parse(txtTution.getText().trim()).intValue();
		int other = numberFormat.parse(txtOther.getText().trim()).intValue();
		int bus = numberFormat.parse(txtBus.getText().trim()).intValue();

		sessionClassFeeDetails.setAdmissionFee(admission);
		sessionClassFeeDetails.setTuitionFee(tuition);
		sessionClassFeeDetails.setActivityFee(other);
		sessionClassFeeDetails.setBusFee(bus);
		sessionClassFeeDetails.setSessionDetails(sessionComboBoxModel
				.getSelectedObject(cbSession.getSelectedIndex()));
		sessionClassFeeDetails.setClassDetails(classComboBoxModel
				.getSelectedObject(cbClass.getSelectedIndex()));
		// return sessionFeeDetails;

	}

	

	private int zeroCheck() throws ParseException {
		StringBuilder sb = new StringBuilder();
		boolean error = false;
		int admission = numberFormat.parse(txtAdmission.getText().trim())
				.intValue();
		int tuition = numberFormat.parse(txtTution.getText().trim()).intValue();
		int other = numberFormat.parse(txtOther.getText().trim()).intValue();
		if (admission <= 0) {
			sb.append("Admission Fee Amount is 0");
			sb.append("\n");
			error = true;
		}
		if (tuition <= 0) {
			sb.append("Tuition Fee Amount is 0");
			sb.append("\n");
			error = true;
		}
		if (other <= 0) {
			sb.append("Activity & others Fee Amount is 0");
			sb.append("\n");
			error = true;
		}
		if (error) {
			sb.append("Do you want to save/update.");
			sb.append("\n");
			return JOptionPane.showConfirmDialog(this, sb.toString(),
					"Confirmation", JOptionPane.YES_NO_OPTION,
					JOptionPane.INFORMATION_MESSAGE);
		}
		return JOptionPane.YES_OPTION;
	}

	private String inputValidate() {
		StringBuilder sb = new StringBuilder();
		boolean error = false;
		try {
			if (txtAdmission.getText().trim().isEmpty()) {
				sb.append("Please Enter Admission Fee in positive number.");
				sb.append("\n");
				error = true;
			}
			int admission = numberFormat.parse(txtAdmission.getText().trim())
					.intValue();
			if (admission < 0) {
				sb.append("Please Enter Admission Fee in positive number.");
				sb.append("\n");
				error = true;
			}
			if (txtTution.getText().trim().isEmpty()) {
				sb.append("Please Enter Tution Fee in positive number.");
				sb.append("\n");
				error = true;
			}

			int tuition = numberFormat.parse(txtTution.getText().trim())
					.intValue();

			if (tuition < 0) {
				sb.append("Please Enter Tution Fee in positive number.");
				sb.append("\n");
				error = true;
			}
			if (txtOther.getText().trim().isEmpty()) {
				sb.append("Please Enter Activity & others Fee in positive number.");
				sb.append("\n");
				error = true;
			}
			int other = numberFormat.parse(txtOther.getText().trim())
					.intValue();
			if (other < 0) {
				sb.append("Please Enter Activity & others Fee in positive number.");
				sb.append("\n");
				error = true;
			}
			int bus = numberFormat.parse(txtBus.getText().trim())
					.intValue();
			if (bus < 0) {
				sb.append("Please Enter Transportaion Fee in positive number.");
				sb.append("\n");
				error = true;
			}
		} catch (ParseException ex) {
			sb.append("Invalid numbers entered.");
			sb.append("\n");
			error = true;
		}
		if (error) {
			return sb.toString();
		}
		return null;
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
