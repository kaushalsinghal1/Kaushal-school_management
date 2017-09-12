package com.school.student;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.logging.Level;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import org.hibernate.HibernateException;

import com.banti.framework.core.ApplicationOpertaion;
import com.school.console.SchoolMain;
import com.school.constant.OperationConstant;
import com.school.fees.report.ReciptModel;
import com.school.fees.report.ReciptWindow;
import com.school.hiebernate.HiebernateStudentDboUtil;
import com.school.hiebernate.dbo.DepositeFeeMaster;
import com.school.hiebernate.dbo.StudentFeeDetails;
import com.school.resource.CommandConstant;
import com.school.resource.ResourcesUtils;
import com.school.utils.Logger;
import com.school.utils.MsgDialogUtils;

public class StudentPayFeesPanel extends JPanel implements ActionListener {
	private JTextField txtAdmission;
	private JTextField txtAdmissionPaid;
	private JTextField txtAdmissionPaying;
	private JTextField txtTution;
	private JTextField txtTutionPaid;
	private JTextField txtTutionPaying;
	private JTextField txtOther;
	private JTextField txtOtherPaid;
	private JTextField txtOtherPaying;
	private JTextField txtGrandTotal;
	private JTextField txtBus;
	private JTextField txtBusPaid;
	private JTextField txtBusPaying;
	private JButton btnRegister;
	private static NumberFormat numberFormat = NumberFormat.getInstance();
	private StudentFeeDetails studentFeeDetails;
	private PropertyChangeListener listener;

	public StudentPayFeesPanel(StudentFeeDetails studentFeeDetails) {
		this();
		this.studentFeeDetails = studentFeeDetails;
		fillInitialData(studentFeeDetails);
	}

	public StudentPayFeesPanel() {
		init();
		setEditable();
	}

	public void setPropertyListener(PropertyChangeListener listener) {
		this.listener = listener;
	}

	private static final String RB_GROUP_STRING[] = { "Admission",
			"Admission and 1 month", "Monthly", "2 month", "Quarterly",
			"Halfyearly", "Yearly", "Manually" };

	private void init() {
		setLayout(new BorderLayout());
		add(getPanel(), BorderLayout.NORTH);
		setDepositeEditable(false);
		setButtonEnable(true);
		setDocumentListener();
	}

	private void fillInitialData(StudentFeeDetails feeDetails) {
		txtAdmission
				.setText(numberFormat.format(feeDetails.getTotalAdmission()));
		txtTution.setText(numberFormat.format(feeDetails.getTotalTutionFee()));
		txtOther.setText(numberFormat.format(feeDetails.getTotalActivity()));
		txtBus.setText(numberFormat.format(feeDetails.getTotalBus()));
		txtAdmissionPaid.setText(numberFormat.format(feeDetails
				.getAdmissionFeeDeposite()));
		txtTutionPaid.setText(numberFormat.format(feeDetails
				.getTuitionFeeDeposite()));
		txtOtherPaid.setText(numberFormat.format(feeDetails
				.getActivityFeeDeposite()));
		txtBusPaid.setText(numberFormat.format(feeDetails.getBusFeeDeposite()));
	}

	private void setDepositeEditable(boolean enable) {
		txtAdmissionPaying.setEditable(enable);
		txtTutionPaying.setEditable(enable);
		txtOtherPaying.setEditable(enable);
		txtBusPaying.setEditable(enable);
		txtGrandTotal.setEditable(enable);
	}

	private JPanel getPanel() {
		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.PAGE_START;
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(10, 10, 10, 10);
		panel.add(getPayOptionPanel(), c);
		c.gridx = 0;
		c.gridy = 1;
		panel.add(getMiddlePanel(), c);
		c.gridx = 0;
		c.gridy = 2;
		panel.add(getButtonPanel(), c);
		return panel;
	}

	private JPanel getPayOptionPanel() {
		JPanel panel = new JPanel(new GridBagLayout());
		JRadioButton rbBtn;
		ButtonGroup btnGrp = new ButtonGroup();
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.LINE_START;
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(5, 5, 5, 5);
		RBAction action = new RBAction();
		for (int i = 0; i < RB_GROUP_STRING.length; i++) {
			c.gridx = i % 3;
			c.gridy = i / 3;
			rbBtn = new JRadioButton(RB_GROUP_STRING[i]);
			rbBtn.setActionCommand(i + "");
			rbBtn.addActionListener(action);
			btnGrp.add(rbBtn);
			panel.add(rbBtn, c);
		}

		panel.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(1),
				"Select Fee Deposit Option"));
		return panel;
	}

	private JPanel getButtonPanel() {
		JPanel panel = new JPanel(new FlowLayout());
		btnRegister = new JButton(ResourcesUtils.getString("SUBMIT"));
		btnRegister.setActionCommand(OperationConstant.STUDENT_FEE_DIPOSITE);
		btnRegister.addActionListener(this);
		JButton cancelBtn = new JButton(ResourcesUtils.getString("CANCEL"));
		cancelBtn.setActionCommand(CommandConstant.CANCEL_COMMAND);
		cancelBtn.addActionListener(this);
		panel.add(btnRegister);
		panel.add(cancelBtn);
		btnRegister.setFocusable(true);
		return panel;
	}

	private void setEditable() {
		txtAdmission.setEditable(false);
		txtAdmissionPaid.setEditable(false);
		txtTution.setEditable(false);
		txtTutionPaid.setEditable(false);
		txtOther.setEditable(false);
		txtOtherPaid.setEditable(false);
		txtBus.setEditable(false);
		txtBusPaid.setEditable(false);
	}

	private JPanel getMiddlePanel() {
		JPanel panel = new JPanel();
		panel.setSize(new Dimension(100, 100));
		panel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.NONE;
		c.insets = new Insets(5, 5, 5, 5);
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0.5;
		c.anchor = GridBagConstraints.LINE_START;
		JLabel lb = new JLabel("Admission Fees");
		panel.add(lb, c);
		c.gridx = 1;
		txtAdmission = new JFormattedTextField(numberFormat);
		lb.setLabelFor(txtAdmission);
		txtAdmission.setColumns(5);
		panel.add(txtAdmission, c);

		c.gridx = 2;
		lb = new JLabel("Paid");
		panel.add(lb, c);
		c.gridx = 3;
		txtAdmissionPaid = new JFormattedTextField(numberFormat);
		lb.setLabelFor(txtAdmissionPaid);
		txtAdmissionPaid.setColumns(4);
		panel.add(txtAdmissionPaid, c);
		c.gridx = 4;
		lb = new JLabel("Deposite");
		panel.add(lb, c);
		c.gridx = 5;
		txtAdmissionPaying = new JFormattedTextField(numberFormat);
		lb.setLabelFor(txtAdmissionPaying);
		txtAdmissionPaying.setColumns(5);
		panel.add(txtAdmissionPaying, c);
		// 1
		c.gridx = 0;
		c.gridy = 1;
		lb = new JLabel("Tution Fees");
		panel.add(lb, c);
		c.gridx = 1;
		txtTution = new JFormattedTextField(numberFormat);
		lb.setLabelFor(txtTution);
		txtTution.setColumns(5);
		panel.add(txtTution, c);

		c.gridx = 2;
		lb = new JLabel("Paid");
		panel.add(lb, c);
		c.gridx = 3;
		txtTutionPaid = new JFormattedTextField(numberFormat);
		lb.setLabelFor(txtTutionPaid);
		txtTutionPaid.setColumns(4);
		panel.add(txtTutionPaid, c);
		c.gridx = 4;
		lb = new JLabel("Deposite");
		panel.add(lb, c);
		c.gridx = 5;
		txtTutionPaying = new JFormattedTextField(numberFormat);
		lb.setLabelFor(txtTutionPaying);
		txtTutionPaying.setColumns(5);
		panel.add(txtTutionPaying, c);
		// 2
		c.gridy = 2;
		c.gridx = 0;
		lb = new JLabel("Activity & Other");
		panel.add(lb, c);
		c.gridx = 1;
		txtOther = new JFormattedTextField(numberFormat);
		lb.setLabelFor(txtOther);
		txtOther.setColumns(5);
		panel.add(txtOther, c);

		c.gridx = 2;
		lb = new JLabel("Paid");
		panel.add(lb, c);
		c.gridx = 3;
		txtOtherPaid = new JFormattedTextField(numberFormat);
		lb.setLabelFor(txtOtherPaid);
		txtOtherPaid.setColumns(4);
		panel.add(txtOtherPaid, c);
		c.gridx = 4;
		lb = new JLabel("Deposite");
		panel.add(lb, c);
		c.gridx = 5;
		txtOtherPaying = new JFormattedTextField(numberFormat);
		lb.setLabelFor(txtOtherPaying);
		txtOtherPaying.setColumns(5);
		panel.add(txtOtherPaying, c);
		// 3
		c.gridy = 3;
		c.gridx = 0;
		lb = new JLabel("Transportaion");
		panel.add(lb, c);
		c.gridx = 1;
		txtBus = new JFormattedTextField(numberFormat);
		lb.setLabelFor(txtBus);
		txtBus.setColumns(5);
		panel.add(txtBus, c);

		c.gridx = 2;
		lb = new JLabel("Paid");
		panel.add(lb, c);
		c.gridx = 3;
		txtBusPaid = new JFormattedTextField(numberFormat);
		lb.setLabelFor(txtBusPaid);
		txtBusPaid.setColumns(4);
		panel.add(txtBusPaid, c);
		c.gridx = 4;
		lb = new JLabel("Deposite");
		panel.add(lb, c);
		c.gridx = 5;
		txtBusPaying = new JFormattedTextField(numberFormat);
		lb.setLabelFor(txtBusPaying);
		txtBusPaying.setColumns(5);
		panel.add(txtBusPaying, c);

		// 4
		c.gridy = 4;
		c.gridx = 2;
		c.gridwidth = 2;
		c.anchor = GridBagConstraints.LINE_END;
		lb = new JLabel("Total Amount");
		panel.add(lb, c);
		c.gridx = 5;
		c.anchor = GridBagConstraints.LINE_START;
		txtGrandTotal = new JFormattedTextField(numberFormat);
		lb.setLabelFor(txtGrandTotal);
		txtGrandTotal.setColumns(5);
		panel.add(txtGrandTotal, c);
		return panel;
	}

	private void setButtonEnable(boolean enable) {
		btnRegister.setEnabled(enable
				&& ApplicationOpertaion.isAllowed(btnRegister
						.getActionCommand()));
	}

	public void setFeeDetails(StudentFeeDetails feeDetails) {
		if (feeDetails != null) {
			this.studentFeeDetails = feeDetails;
			fillInitialData(feeDetails);
		}

	}

	class RBAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			int i = Integer.parseInt(e.getActionCommand());
			fillPayFeeFromSelectedOption(i);
		}
	}

	private String validateInput() throws ParseException {
		StringBuffer sb = new StringBuffer();
		boolean error = false;
		int admissionDeposite = numberFormat.parse(
				txtAdmissionPaying.getText().trim()).intValue();
		int tutionDeposite = numberFormat.parse(
				txtTutionPaying.getText().trim()).intValue();
		int activityDeposite = numberFormat.parse(
				txtOtherPaying.getText().trim()).intValue();
		int busDeposite = numberFormat.parse(txtBusPaying.getText().trim())
				.intValue();
		if (studentFeeDetails.getTotalAdmissionDue() - admissionDeposite < 0) {
			error = true;
			sb.append("Admissaion Deposite amount more than Due amount");
			sb.append("\n");

		}
		if (studentFeeDetails.getTotalTutionDue() - tutionDeposite < 0) {
			error = true;
			sb.append("Tution Deposite amount more than Due amount");
			sb.append("\n");

		}
		if (studentFeeDetails.getTotalActivityDue() - activityDeposite < 0) {
			error = true;
			sb.append("Activity & Other Deposite amount more than Due amount");
			sb.append("\n");

		}
		if (studentFeeDetails.getTotalBusDue() - busDeposite < 0) {
			error = true;
			sb.append("Transportaion Deposite amount more than Due amount");
			sb.append("\n");

		}
		if (error)
			return sb.toString();
		return null;
	}

	private void setDocumentListener() {
		FocusListener focusListener = new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {
				try {
					int admissionDeposite = numberFormat.parse(
							txtAdmissionPaying.getText().trim()).intValue();
					int tutionDeposite = numberFormat.parse(
							txtTutionPaying.getText().trim()).intValue();
					int activityDeposite = numberFormat.parse(
							txtOtherPaying.getText().trim()).intValue();
					int busDeposite = numberFormat.parse(
							txtBusPaying.getText().trim()).intValue();
					txtGrandTotal.setText(admissionDeposite + tutionDeposite
							+ activityDeposite + busDeposite + "");
				} catch (ParseException ex) {

				}

			}

			@Override
			public void focusGained(FocusEvent e) {

			}
		};

		txtAdmissionPaying.addFocusListener(focusListener);
		txtTutionPaying.addFocusListener(focusListener);
		txtOtherPaying.addFocusListener(focusListener);
		txtBusPaying.addFocusListener(focusListener);
		/*
		 * DocumentListener documentListener = new DocumentListener() {
		 * 
		 * @Override public void removeUpdate(DocumentEvent e) {
		 * 
		 * }
		 * 
		 * @Override public void insertUpdate(DocumentEvent e) {
		 * 
		 * }
		 * 
		 * @Override public void changedUpdate(DocumentEvent e) { try { int
		 * admissionDeposite = numberFormat.parse(
		 * txtAdmissionPaying.getText().trim()).intValue(); int tutionDeposite =
		 * numberFormat.parse( txtTutionPaying.getText().trim()).intValue(); int
		 * activityDeposite = numberFormat.parse(
		 * txtOtherPaying.getText().trim()).intValue();
		 * txtGrandTotal.setText(admissionDeposite + tutionDeposite +
		 * activityDeposite + ""); } catch (ParseException ex) {
		 * 
		 * } } }; txtAdmissionPaying.getDocumadminent().addDocumentListener(
		 * documentListener);
		 * txtTutionPaying.getDocument().addDocumentListener(documentListener);
		 * txtOtherPaying.getDocument().addDocumentListener(documentListener);
		 */
	}

	private DepositeFeeMaster createDepositeFeeMaster() throws ParseException {
		DepositeFeeMaster depositeFeeMaster = new DepositeFeeMaster();
		int admissionDeposite = numberFormat.parse(
				txtAdmissionPaying.getText().trim()).intValue();
		int tutionDeposite = numberFormat.parse(
				txtTutionPaying.getText().trim()).intValue();
		int activityDeposite = numberFormat.parse(
				txtOtherPaying.getText().trim()).intValue();
		int busDeposite = numberFormat.parse(txtBusPaying.getText().trim())
				.intValue();
		depositeFeeMaster.setAdmissionFee(admissionDeposite);
		depositeFeeMaster.setTuitionFee(tutionDeposite);
		depositeFeeMaster.setActivityFee(activityDeposite);
		depositeFeeMaster.setBusFee(busDeposite);
		depositeFeeMaster.setTotalAmount(admissionDeposite + tutionDeposite
				+ activityDeposite);
		depositeFeeMaster.setClassDetails(studentFeeDetails.getClassDetails());
		depositeFeeMaster.setSessionDetails(studentFeeDetails
				.getSessionDetails());
		depositeFeeMaster.setStudentDetails(studentFeeDetails
				.getStudentDetails());
		depositeFeeMaster.setCachierName(SchoolMain.getInstance().account
				.getName());
		studentFeeDetails.setAdmissionFeeDeposite(studentFeeDetails
				.getAdmissionFeeDeposite() + admissionDeposite);
		studentFeeDetails.setTuitionFeeDeposite(studentFeeDetails
				.getTuitionFeeDeposite() + tutionDeposite);
		studentFeeDetails.setActivityFeeDeposite(studentFeeDetails
				.getActivityFeeDeposite() + activityDeposite);
		return depositeFeeMaster;
	}

	private void fillPayFeeFromSelectedOption(int i) {
		boolean manual = false;
		if (studentFeeDetails == null)
			return;
		int[] fees = { studentFeeDetails.getTotalAdmission(), 0, 0, 0 };
		switch (i) {
		case 0:
			txtAdmissionPaying.setText(numberFormat.format(fees[0]));
			txtTutionPaying.setText(numberFormat.format(fees[1]));
			txtOtherPaying.setText(numberFormat.format(fees[2]));
			txtBusPaying.setText(numberFormat.format(fees[3]));
			break;
		case 1:
			fees = studentFeeDetails.getPerMonthWithoutAdmission();
			fees[0] = studentFeeDetails.getTotalAdmission();
			txtAdmissionPaying.setText(numberFormat.format(fees[0]));
			txtTutionPaying.setText(numberFormat.format(fees[1]));
			txtOtherPaying.setText(numberFormat.format(fees[2]));
			txtBusPaying.setText(numberFormat.format(fees[3]));
			break;
		case 2:
			fees = studentFeeDetails.getPerMonthWithoutAdmission();
			txtAdmissionPaying.setText(numberFormat.format(fees[0]));
			txtTutionPaying.setText(numberFormat.format(fees[1]));
			txtOtherPaying.setText(numberFormat.format(fees[2]));
			txtBusPaying.setText(numberFormat.format(fees[3]));
			break;
		case 3:
			fees = studentFeeDetails.getTwoMonthWithoutAdmission();
			txtAdmissionPaying.setText(numberFormat.format(fees[0]));
			txtTutionPaying.setText(numberFormat.format(fees[1]));
			txtOtherPaying.setText(numberFormat.format(fees[2]));
			txtBusPaying.setText(numberFormat.format(fees[3]));
			break;
		case 4:
			fees = studentFeeDetails.getQuaterlyWithoutAdmission();
			txtAdmissionPaying.setText(numberFormat.format(fees[0]));
			txtTutionPaying.setText(numberFormat.format(fees[1]));
			txtOtherPaying.setText(numberFormat.format(fees[2]));
			txtBusPaying.setText(numberFormat.format(fees[3]));
			break;
		case 5:
			fees = studentFeeDetails.getHalfYearlyWithoutAdmission();
			txtAdmissionPaying.setText(numberFormat.format(fees[0]));
			txtTutionPaying.setText(numberFormat.format(fees[1]));
			txtOtherPaying.setText(numberFormat.format(fees[2]));
			txtBusPaying.setText(numberFormat.format(fees[3]));
			break;
		case 6:
			fees = studentFeeDetails.getYearlyWithoutAdmission();
			txtAdmissionPaying.setText(numberFormat.format(fees[0]));
			txtTutionPaying.setText(numberFormat.format(fees[1]));
			txtOtherPaying.setText(numberFormat.format(fees[2]));
			txtBusPaying.setText(numberFormat.format(fees[3]));
			break;
		case 7:
			fees = new int[4];
			txtAdmissionPaying.setText(numberFormat.format(fees[0]));
			txtTutionPaying.setText(numberFormat.format(fees[1]));
			txtOtherPaying.setText(numberFormat.format(fees[2]));
			txtBusPaying.setText(numberFormat.format(fees[3]));
			manual = true;
			break;
		}
		if (manual) {
			setDepositeEditable(true);
		} else {
			setDepositeEditable(false);
		}
		txtGrandTotal.setEditable(false);
		txtGrandTotal.setText(numberFormat.format(fees[0] + fees[1] + fees[2]
				+ fees[3]));

	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (OperationConstant.STUDENT_FEE_DIPOSITE.equals(e.getActionCommand())) {
			try {
				String error = validateInput();
				if (error != null) {
					MsgDialogUtils.showWarningDialog(this, error);
					return;
				}
			} catch (ParseException ex) {
				MsgDialogUtils.showWarningDialog(this,
						"Please enter valid numbers");
				return;
			}
			boolean result = false;
			DepositeFeeMaster depositeFeeMaster = null;
			try {
				depositeFeeMaster = createDepositeFeeMaster();
				// studentFeeDetails = createStudentFeeObject();
				depositeFeeMaster.setDepositeDate(new Date());
				studentFeeDetails.setLastDepositeDate(new Date());
				depositeFeeMaster = HiebernateStudentDboUtil
						.saveDepositeFeeMasterDetails(depositeFeeMaster,
								studentFeeDetails);
				result = true;
			} catch (ParseException e1) {
				MsgDialogUtils.showWarningDialog(this, "Invalid input! ");
				return;
			} catch (HibernateException exp) {
				MsgDialogUtils.showWarningDialog(this,
						"Database error occured and transaction aborted ! ");
				Logger.EXCEPTION
						.log(Level.WARNING,
								"Error Occured while Deposite Fess and transaction aborted",
								exp);
				return;
			}
			if (result) {
				MsgDialogUtils.showInformationDialog(this,
						"Fee is deposited Successfully!");
				Logger.DEBUG.info("Fee is deposited  Successfully");
				fillInitialData(studentFeeDetails);
				new ReciptWindow(new ReciptModel(depositeFeeMaster));
				// fire action

				// registrationAction.setStudentFeeDetails(studentFeeDetails);
			}
		} else if (CommandConstant.CANCEL_COMMAND.equals(e.getActionCommand())) {
			listener.propertyChange(new PropertyChangeEvent(this,
					StudentRegistrationAction.DISPOSE_DIALOG, null, null));

		}

	}

}
