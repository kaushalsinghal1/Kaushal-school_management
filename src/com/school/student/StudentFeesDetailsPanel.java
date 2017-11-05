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

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.hibernate.HibernateException;

import com.banti.framework.core.ApplicationOpertaion;
import com.school.constant.OperationConstant;
import com.school.fees.SessionFeeDetailsAction;
import com.school.fees.SessionFeeRegistrationAction;
import com.school.hiebernate.HiebernateDboUtil;
import com.school.hiebernate.HiebernateStudentDboUtil;
import com.school.hiebernate.dbo.SessionClassFeeDetails;
import com.school.hiebernate.dbo.StudentDetails;
import com.school.hiebernate.dbo.StudentFeeDetails;
import com.school.resource.CommandConstant;
import com.school.resource.ResourceConstant;
import com.school.resource.ResourcesUtils;
import com.school.utils.Logger;
import com.school.utils.MsgDialogUtils;

public class StudentFeesDetailsPanel extends JPanel implements ActionListener {
	private JTextField txtAdmission;
	private JTextField txtAdmissionDiscount;
	private JTextField txtAdmissionTotal;
	private JTextField txtTution;
	private JTextField txtTutionDiscount;
	private JTextField txtTutionDiscountTotal;
	private JTextField txtOther;
	private JTextField txtOtherDiscount;
	private JTextField txtOtherDiscountTotal;
	private JTextField txtBus;
	private JTextField txtBusDiscount;
	private JTextField txtBusDiscountTotal;
	private JTextField txtGrandTotal;
	private JButton btnRegister;
	private static NumberFormat numberFormat = NumberFormat.getInstance();
	private StudentDetails studentDetails;
	// private StudentRegistrationAction registrationAction;
	private JButton btnUpdate;
	private StudentFeeDetails studentFeeDetails;
	private PropertyChangeListener listener;

	public StudentFeesDetailsPanel(PropertyChangeListener listener,
			StudentFeeDetails studentFeeDetails) {
		init();
		this.listener = listener;
		if (studentFeeDetails != null) {
			setStudentFeeDetails(studentFeeDetails);
			setRegisterEnable(false);
		} else {
			setRegisterEnable(true);
		}
	}

	public void setPropertyChangeListener(
			PropertyChangeListener propertyChangeListener) {
		this.listener = propertyChangeListener;
	}

	private void init() {
		setLayout(new BorderLayout());
		add(getPanel(), BorderLayout.NORTH);
		// add(getFeeDetailsPanel(), BorderLayout.SOUTH);
	}

	public void setPropertyListener(PropertyChangeListener listener) {
		this.listener = listener;
	}

	private JPanel getPanel() {
		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.PAGE_START;
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(10, 10, 10, 10);
		panel.add(getMiddlePanel(), c);
		c.gridx = 0;
		c.gridy = 1;
		panel.add(getButtonPanel(), c);
		// c.gridx = 0;
		// c.gridy = 2;
		// c.insets = new Insets(20, 20, 10, 20);
		// c.fill = GridBagConstraints.HORIZONTAL;
		// panel.add(getFeeDetailsPanel(), c);
		return panel;
	}

	private JPanel getButtonPanel() {
		JPanel panel = new JPanel(new FlowLayout());

		btnRegister = new JButton(ResourcesUtils.getString("REGISTER"));
		btnRegister.setActionCommand(OperationConstant.STUDENT_FEE_REGISTER);
		btnRegister.addActionListener(this);

		btnUpdate = new JButton(ResourcesUtils.getString("UPDATE"));
		btnUpdate.setActionCommand(OperationConstant.STUDENT_FEE_UPDATE);
		btnUpdate.addActionListener(this);

		JButton cancelBtn = new JButton(ResourceConstant.CANCEL);
		cancelBtn.setActionCommand(CommandConstant.CANCEL_COMMAND);
		cancelBtn.addActionListener(this);
		panel.add(btnRegister);
		panel.add(btnUpdate);
		panel.add(cancelBtn);
		btnRegister.setFocusable(true);
		return panel;
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
		lb = new JLabel("Discount");
		panel.add(lb, c);
		c.gridx = 3;
		txtAdmissionDiscount = new JFormattedTextField(numberFormat);
		lb.setLabelFor(txtAdmissionDiscount);
		txtAdmissionDiscount.setColumns(4);
		panel.add(txtAdmissionDiscount, c);
		c.gridx = 4;
		lb = new JLabel("Total");
		panel.add(lb, c);
		c.gridx = 5;
		txtAdmissionTotal = new JFormattedTextField(numberFormat);
		lb.setLabelFor(txtAdmissionTotal);
		txtAdmissionTotal.setColumns(5);
		panel.add(txtAdmissionTotal, c);
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
		lb = new JLabel("Discount");
		panel.add(lb, c);
		c.gridx = 3;
		txtTutionDiscount = new JFormattedTextField(numberFormat);
		lb.setLabelFor(txtTutionDiscount);
		txtTutionDiscount.setColumns(4);
		panel.add(txtTutionDiscount, c);
		c.gridx = 4;
		lb = new JLabel("Total");
		panel.add(lb, c);
		c.gridx = 5;
		txtTutionDiscountTotal = new JFormattedTextField(numberFormat);
		lb.setLabelFor(txtTutionDiscountTotal);
		txtTutionDiscountTotal.setColumns(5);
		panel.add(txtTutionDiscountTotal, c);
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
		lb = new JLabel("Discount");
		panel.add(lb, c);
		c.gridx = 3;
		txtOtherDiscount = new JFormattedTextField(numberFormat);
		lb.setLabelFor(txtOtherDiscount);
		txtOtherDiscount.setColumns(4);
		panel.add(txtOtherDiscount, c);
		c.gridx = 4;
		lb = new JLabel("Total");
		panel.add(lb, c);
		c.gridx = 5;
		txtOtherDiscountTotal = new JFormattedTextField(numberFormat);
		lb.setLabelFor(txtOtherDiscountTotal);
		txtOtherDiscountTotal.setColumns(5);
		panel.add(txtOtherDiscountTotal, c);
		// 3
		c.gridy = 3;
		c.gridx = 0;
		lb = new JLabel("Transportation");
		panel.add(lb, c);
		c.gridx = 1;
		txtBus = new JFormattedTextField(numberFormat);
		lb.setLabelFor(txtBus);
		txtBus.setColumns(5);
		panel.add(txtBus, c);

		c.gridx = 2;
		lb = new JLabel("Discount");
		panel.add(lb, c);
		c.gridx = 3;
		txtBusDiscount = new JFormattedTextField(numberFormat);
		lb.setLabelFor(txtBusDiscount);
		txtBusDiscount.setColumns(4);
		panel.add(txtBusDiscount, c);
		c.gridx = 4;
		lb = new JLabel("Total");
		panel.add(lb, c);
		c.gridx = 5;
		txtBusDiscountTotal = new JFormattedTextField(numberFormat);
		lb.setLabelFor(txtBusDiscountTotal);
		txtBusDiscountTotal.setColumns(5);
		panel.add(txtBusDiscountTotal, c);

		// 4
		c.gridy = 4;
		c.gridx = 2;
		c.gridwidth = 2;
		c.anchor = GridBagConstraints.LINE_END;
		lb = new JLabel("Grand Total");
		panel.add(lb, c);
		c.gridx = 5;
		c.anchor = GridBagConstraints.LINE_START;
		txtGrandTotal = new JFormattedTextField(numberFormat);
		lb.setLabelFor(txtGrandTotal);
		txtGrandTotal.setColumns(5);
		panel.add(txtGrandTotal, c);
		setDiscountEnable(true);
		setFeeEnable(false);
		setDiscountFocusListener();
		setInitialDiscountValue();
		return panel;
	}

	private void setDiscountFocusListener() {
		MyFocusListemer focusListemer = new MyFocusListemer();
		txtAdmissionDiscount.addFocusListener(focusListemer);
		txtTutionDiscount.addFocusListener(focusListemer);
		txtOtherDiscount.addFocusListener(focusListemer);
		txtBusDiscount.addFocusListener(focusListemer);
	}

	private void setInitialDiscountValue() {
		txtAdmissionDiscount.setText("0");
		txtTutionDiscount.setText("0");
		txtOtherDiscount.setText("0");
		txtBusDiscount.setText("0");
	}

	private void setFeeEnable(boolean enable) {
		txtAdmission.setEditable(enable);
		txtTution.setEditable(enable);
		txtOther.setEditable(enable);
		txtBus.setEditable(enable);
		txtAdmissionTotal.setEditable(enable);
		txtTutionDiscountTotal.setEditable(enable);
		txtOtherDiscountTotal.setEditable(enable);
		txtBusDiscountTotal.setEditable(enable);
		txtGrandTotal.setEditable(enable);
	}

	private void setDiscountEnable(boolean enable) {
		txtAdmissionDiscount.setEnabled(enable
				&& ApplicationOpertaion
						.isAllowed(OperationConstant.ADMISSION_FEE_DISCOUNT));
		txtTutionDiscount.setEnabled(enable
				&& ApplicationOpertaion
						.isAllowed(OperationConstant.TUTION_FEE_DISCOUNT));
		txtOtherDiscount.setEnabled(enable
				&& ApplicationOpertaion
						.isAllowed(OperationConstant.OTHER_FEE_DISCOUNT));
		txtBusDiscount.setEnabled(enable
				&& ApplicationOpertaion
						.isAllowed(OperationConstant.BUS_FEE_DISCOUNT));
	}

	class MyFocusListemer implements FocusListener {
		@Override
		public void focusLost(FocusEvent e) {
			String error = null;
			try {
				error = discountValidate();
			} catch (ParseException e1) {
				error = "Please Enter Fee amount Numeric";
			}
			if (error != null) {
				MsgDialogUtils.showWarningDialog(StudentFeesDetailsPanel.this,
						error);
			}
		}

		@Override
		public void focusGained(FocusEvent e) {

		}
	}

	private String discountValidate() throws ParseException {
		boolean error = false;
		StringBuilder sb = new StringBuilder();
		int admission = numberFormat.parse(txtAdmission.getText().trim())
				.intValue();
		int tution = numberFormat.parse(txtTution.getText().trim()).intValue();
		int other = numberFormat.parse(txtOther.getText().trim()).intValue();
		int bus = numberFormat.parse(txtBus.getText().trim()).intValue();
		int admissaionDis = "".equals(txtAdmissionDiscount.getText().trim()) ? 0
				: numberFormat.parse(txtAdmissionDiscount.getText().trim())
						.intValue();
		int tutionDis = "".equals(txtTutionDiscount.getText().trim()) ? 0
				: numberFormat.parse(txtTutionDiscount.getText().trim())
						.intValue();
		int otherDis = "".equals(txtOtherDiscount.getText().trim()) ? 0
				: numberFormat.parse(txtOtherDiscount.getText().trim())
						.intValue();
		int busDis = "".equals(txtBusDiscount.getText().trim()) ? 0
				: numberFormat.parse(txtBusDiscount.getText().trim())
						.intValue();
		if (admission - admissaionDis >= 0) {
			txtAdmissionTotal.setText(numberFormat
					.format((admission - admissaionDis)));
		} else {
			error = true;
			sb.append("Admission Fee discount should less then fee amount");
			sb.append("\n");
		}
		if (tution - tutionDis >= 0) {
			txtTutionDiscountTotal.setText(numberFormat
					.format((tution - tutionDis)));
		} else {
			error = true;
			sb.append("Tution Fee discount should less then fee amount");
			sb.append("\n");
		}
		if (other - otherDis >= 0) {
			txtOtherDiscountTotal.setText(numberFormat
					.format((other - otherDis)));
		} else {
			error = true;
			sb.append("Activity & others Fee discount should less then fee amount");
			sb.append("\n");
		}
		if (bus - busDis >= 0) {
			txtBusDiscountTotal.setText(numberFormat.format((bus - busDis)));
		} else {
			error = true;
			sb.append("Transportaion Fee discount should less then fee amount");
			sb.append("\n");
		}
		int total = admission + tution + other - admissaionDis - tutionDis
				- otherDis + bus - busDis;
		txtGrandTotal.setText(numberFormat.format(total));

		if (!error) {
			return null;
		}

		return sb.toString();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (OperationConstant.STUDENT_FEE_REGISTER.equals(e.getActionCommand())) {
			try {
				String error = discountValidate();
				if (error != null) {
					MsgDialogUtils.showWarningDialog(this, error);
					return;
				}
			} catch (ParseException ex) {
				MsgDialogUtils.showWarningDialog(this,
						"Please enter valid nubers");
				return;
			}
			boolean result = false;
			StudentFeeDetails studentFeeDetails = null;
			try {
				studentFeeDetails = createStudentFeeObject();
				studentFeeDetails.setAddDate(new Date());
				studentFeeDetails = HiebernateStudentDboUtil
						.saveStudentFeeDetails(studentFeeDetails);
				result = true;
				if (studentFeeDetails == null) {
					result = false;
				}
			} catch (ParseException e1) {
				MsgDialogUtils.showWarningDialog(this, "Invalid input! ");
				return;
			} catch (HibernateException exp) {
				MsgDialogUtils.showWarningDialog(this,
						"Database error occured! ");
				Logger.EXCEPTION.log(Level.WARNING,
						"Error Occured while save student fee Details", exp);
				return;
			}
			if (result) {
				MsgDialogUtils
						.showInformationDialog(this,
								"Student Fee Details has been registered Successfully!");
				Logger.DEBUG
						.info("Student Fee Details has been registered Successfully");
				listener.propertyChange(new PropertyChangeEvent(this,
						StudentRegistrationAction.FEEDETAILS_SAVED,
						studentFeeDetails, 3));
				setRegisterEnable(false);
			} else {
				MsgDialogUtils.showWarningDialog(this,
						"Student Fee Details Already Registered !");
			}
		} else if (OperationConstant.STUDENT_FEE_UPDATE.equals(e
				.getActionCommand())) {
			try {
				String error = discountValidate();
				if (error != null) {
					MsgDialogUtils.showWarningDialog(this, error);
					return;
				}
			} catch (ParseException ex) {
				MsgDialogUtils.showWarningDialog(this,
						"Please enter valid nubers");
				return;
			}
			boolean result = false;
			StudentFeeDetails studentFeeDetails = null;
			try {
				studentFeeDetails = createStudentFeeObject(this.studentFeeDetails);
				studentFeeDetails.setUpdateDate(new Date());
				result = HiebernateStudentDboUtil
						.updateStudentFeeDetails(studentFeeDetails);
				result = true;
			} catch (ParseException e1) {
				MsgDialogUtils.showWarningDialog(this, "Invalid input! ");
				return;
			} catch (HibernateException exp) {
				MsgDialogUtils.showWarningDialog(this,
						"Database error occured! ");
				Logger.EXCEPTION.log(Level.WARNING,
						"Error Occured while update student fee Details", exp);
				return;
			}
			if (result) {
				MsgDialogUtils.showInformationDialog(this,
						"Student Fee Details has been updated Successfully!");
				Logger.DEBUG
						.info("Student Fee Details has been updated Successfully");
				listener.propertyChange(new PropertyChangeEvent(this,
						StudentRegistrationAction.FEEDETAILS_SAVED,
						studentFeeDetails, 3));
				// setRegisterEnable(false);
			} else {
				MsgDialogUtils.showWarningDialog(this,
						"Student Fee Details Already Registered !");
			}

		} else if (CommandConstant.CANCEL_COMMAND.equals(e.getActionCommand())) {
			listener.propertyChange(new PropertyChangeEvent(this,
					StudentRegistrationAction.CHANGE_TAB, null, 0));
		}
	}

	private StudentFeeDetails createStudentFeeObject() throws ParseException {
		return createStudentFeeObject(null);
	}

	private StudentFeeDetails createStudentFeeObject(
			StudentFeeDetails feeDetails) throws ParseException {
		if (feeDetails == null) {
			feeDetails = new StudentFeeDetails();
		}
		int admission = numberFormat.parse(txtAdmission.getText().trim())
				.intValue();
		int tution = numberFormat.parse(txtTution.getText().trim()).intValue();
		int other = numberFormat.parse(txtOther.getText().trim()).intValue();
		int bus = numberFormat.parse(txtBus.getText().trim()).intValue();
		int admissaionDis = "".equals(txtAdmissionDiscount.getText().trim()) ? 0
				: numberFormat.parse(txtAdmissionDiscount.getText().trim())
						.intValue();
		int tutionDis = "".equals(txtTutionDiscount.getText().trim()) ? 0
				: numberFormat.parse(txtTutionDiscount.getText().trim())
						.intValue();
		int otherDis = "".equals(txtOtherDiscount.getText().trim()) ? 0
				: numberFormat.parse(txtOtherDiscount.getText().trim())
						.intValue();
		int busDis = "".equals(txtBusDiscount.getText().trim()) ? 0
				: numberFormat.parse(txtBusDiscount.getText().trim())
						.intValue();
		feeDetails.setAdmissionFee(admission);
		feeDetails.setAdmissionFeeDiscount(admissaionDis);
		feeDetails.setAdmissionFeeDeposite(0);
		feeDetails.setTuitionFee(tution);
		feeDetails.setTuitionFeeDiscount(tutionDis);
		feeDetails.setTuitionFeeDeposite(0);
		feeDetails.setActivityFee(other);
		feeDetails.setActivityFeeDiscount(otherDis);
		feeDetails.setActivityFeeDeposite(0);
		feeDetails.setBusFee(bus);
		feeDetails.setBusFeeDiscount(busDis);
		feeDetails.setBusFeeDeposite(0);
		feeDetails.setStudentDetails(studentDetails);
		feeDetails.setSessionDetails(studentDetails.getSessionDetails());
		feeDetails.setClassDetails(studentDetails.getClassDetails());
		return feeDetails;
	}

	private void setRegisterEnable(boolean enable) {
		btnRegister.setEnabled(enable
				&& ApplicationOpertaion.isAllowed(btnRegister
						.getActionCommand()));
		btnUpdate
				.setEnabled(!enable
						&& ApplicationOpertaion.isAllowed(btnUpdate
								.getActionCommand()));
	}

	private void setData(SessionClassFeeDetails feeDetails) {
		txtAdmission.setText(numberFormat.format(feeDetails.getAdmissionFee()));
		txtTution.setText(numberFormat.format(feeDetails.getTuitionFee()));
		txtOther.setText(numberFormat.format(feeDetails.getActivityFee()));
		txtBus.setText(numberFormat.format(feeDetails.getBusFee()));
		try {
			discountValidate();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public void setStudentFeeDetails(StudentFeeDetails studentFeeDetails) {
		if (studentFeeDetails == null) {
			return;
		}
		this.studentFeeDetails = studentFeeDetails;
		this.studentDetails = studentFeeDetails.getStudentDetails();
		txtAdmission.setText(numberFormat.format(studentFeeDetails
				.getAdmissionFee()));
		txtTution
				.setText(numberFormat.format(studentFeeDetails.getTuitionFee()));
		txtOther.setText(numberFormat.format(studentFeeDetails.getActivityFee()));
		txtBus.setText(numberFormat.format(studentFeeDetails.getBusFee()));
		txtAdmissionDiscount.setText(numberFormat.format(studentFeeDetails
				.getAdmissionFeeDiscount()));
		txtTutionDiscount.setText(numberFormat.format(studentFeeDetails
				.getTuitionFeeDiscount()));
		txtOtherDiscount.setText(numberFormat.format(studentFeeDetails
				.getActivityFeeDiscount()));
		txtBusDiscount.setText(numberFormat.format(studentFeeDetails
				.getBusFeeDiscount()));
		txtAdmissionTotal.setText(numberFormat.format(studentFeeDetails
				.getAdmissionFee()
				- studentFeeDetails.getAdmissionFeeDiscount()));
		txtTutionDiscountTotal.setText(numberFormat.format(studentFeeDetails
				.getTuitionFee() - studentFeeDetails.getTuitionFeeDiscount()));
		txtOtherDiscountTotal
				.setText(numberFormat.format(studentFeeDetails.getActivityFee()
						- studentFeeDetails.getActivityFeeDiscount()));
		txtBusDiscountTotal.setText(numberFormat.format(studentFeeDetails
				.getBusFee() - studentFeeDetails.getBusFeeDiscount()));
		int total = studentFeeDetails.getAdmissionFee()
				+ studentFeeDetails.getTuitionFee()
				+ studentFeeDetails.getActivityFee()
				+ studentFeeDetails.getBusFee()
				- studentFeeDetails.getAdmissionFeeDiscount()
				- studentFeeDetails.getTuitionFeeDiscount()
				- studentFeeDetails.getActivityFeeDiscount()
				- studentFeeDetails.getBusFeeDiscount();
		txtGrandTotal.setText(numberFormat.format(total));
		setRegisterEnable(false);

	}

	public StudentDetails getStudentDetails() {
		return studentDetails;
	}

	public void setStudentDetails(StudentDetails studentDetails) {
		this.studentDetails = studentDetails;
		try {
			SessionClassFeeDetails classFeeDetails = HiebernateDboUtil
					.getSessionFeeDetails(studentDetails.getSessionDetails(),
							studentDetails.getClassDetails());
			if (classFeeDetails == null) {
				MsgDialogUtils.showWarningDialog(this,
						"Fee details is not registered for class: "
								+ studentDetails.getClassDetails()
										.getClassName()
								+ " and Session:"
								+ studentDetails.getSessionDetails()
										.getDisplayString());
				new SessionFeeRegistrationAction();
				classFeeDetails = HiebernateDboUtil.getSessionFeeDetails(
						studentDetails.getSessionDetails(),
						studentDetails.getClassDetails());
				if (classFeeDetails == null) {
					MsgDialogUtils.showWarningDialog(this,
							"Fee details is not registered for class: "
									+ studentDetails.getClassDetails()
											.getClassName()
									+ " and Session:"
									+ studentDetails.getSessionDetails()
											.getDisplayString());
					return;
				}
			}

			setData(classFeeDetails);
			Logger.DEBUG
					.info("Session class fee details got from student session and class details: "
							+ studentDetails.getSessionDetails()
									.getDisplayString()
							+ " , "
							+ studentDetails.getClassDetails().getClassName());
		} catch (HibernateException e) {
			Logger.EXCEPTION
					.log(Level.WARNING,
							"Error while getting session class fee details from student session and class details: "
									+ studentDetails.getSessionDetails()
											.getDisplayString()
									+ " , "
									+ studentDetails.getClassDetails()
											.getClassName(), e);
		}
	}

}
