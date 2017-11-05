package com.school.student;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.hibernate.HibernateException;

import com.banti.framework.core.ActionDispatcher;
import com.banti.framework.core.ApplicationOpertaion;
import com.banti.framework.core.WaterMarkTextField;
import com.banti.framework.ui.DialogEsc;
import com.school.combobox.model.ClassComboBoxModel;
import com.school.combobox.model.SessionComboBoxModel;
import com.school.console.SchoolMain;
import com.school.constant.ApplicationConstant;
import com.school.constant.OperationConstant;
import com.school.hiebernate.HiebernateStudentDboUtil;
import com.school.hiebernate.dbo.StudentDetails;
import com.school.hiebernate.dbo.StudentFeeDetails;
import com.school.hiebernate.dbo.StudentImageDetails;
import com.school.resource.CommandConstant;
import com.school.resource.ResourceConstant;
import com.school.resource.ResourcesUtils;
import com.school.shared.StateDetails;
import com.school.utils.Logger;
import com.school.utils.MsgDialogUtils;
import com.school.utils.Utils;
import com.school.utils.ValidationUtils;

public class StudentRegistrationAction extends DialogEsc implements ActionListener, PropertyChangeListener {
	private JTabbedPane tabbedPane;
	private JTextField txtFirstName;
	private JTextField txtLastName;
	private JTextField txtFatherName;
	private JTextField txtMotherName;
	private JTextField txtEmailId;
	private JComboBox cbClass;
	private JComboBox cbParentOccupation;
	private JTextField txtPhone;
	private JTextField txtMobile;
	private JTextArea txtAddress;
	private JTextField txtCity;
	private JComboBox cbState;
	private JComboBox cbSession;
	private JTextField txtDOB;
	private JRadioButton rbMale;
	private JRadioButton rbFeMale;
	private ButtonGroup gender;
	private JComboBox cbCast;
	private JTextField txtAdhaar;
	private ClassComboBoxModel classComboBoxModel;
	private SessionComboBoxModel sessionComboBoxModel;
	private JButton btnRegister;
	private JButton btnCancel;
	private StudentDetails studentDetails;
	public static final String DISPOSE_DIALOG = "dispose.dialog";
	public static final String CHANGE_TAB = "change.tab";
	public static final String CHANGEANDENABLE_TAB = "change.enabled.tab";
	public static final String IMAGE_REGISTER = "image.register";
	public static final String IMAGE_UPDATE = "image.update";
	public static final String MALE = "MALE";
	public static final String FEMALE = "FEMALE";

	public StudentDetails getStudentDetails() {
		return studentDetails;
	}

	public void setStudentDetails(StudentDetails studentDetails) {
		this.studentDetails = studentDetails;
	}

	private StudentFeeDetails studentFeeDetails;
	private StudentFeesDetailsPanel feesDetailsPanel;

	private StudentPayFeesPanel payFeesPanel;
	private JButton btnUpdate;
	private static final String OCCUPATION[] = ApplicationConstant.OCCUPATION;
	private static final String CASTS[] = ApplicationConstant.CASTS;
	private static final String STATE[] = StateDetails.getInstance().getStatesArray();
	public static final String FEEDETAILS_SAVED = "FeeDetails.saved";

	StudentFeeDetails getStudentFeeDetails() {
		return studentFeeDetails;
	}

	private void setStudentFeeDetails(StudentFeeDetails studentFeeDetails) {
		this.studentFeeDetails = studentFeeDetails;
	}

	public StudentRegistrationAction() {
		this((StudentDetails) null, ResourceConstant.REGISTRATION, 0);
	}

	public StudentRegistrationAction(String title) {
		this((StudentDetails) null, title, 0);
	}

	public StudentRegistrationAction(StudentDetails studentDetails) {
		this(studentDetails, "Student Details", 0);
	}

	public StudentRegistrationAction(StudentFeeDetails studentfeeDetails) {
		this(studentfeeDetails.getStudentDetails(), studentfeeDetails, "Student Details", 0);
	}

	public StudentRegistrationAction(StudentDetails studentDetails, StudentFeeDetails studentFeeDetails, String title,
			int i) {
		super(SchoolMain.Frame, title, true);
		this.studentDetails = studentDetails;
		init();
		if (studentDetails != null) {
			if (studentFeeDetails != null) {
				this.studentFeeDetails = studentFeeDetails;
			} else {
				try {
					List<StudentFeeDetails> list = HiebernateStudentDboUtil.getStudentFeeDetails(studentDetails);
					this.studentFeeDetails = list.size() > 0 ? list.get(0) : null;
				} catch (HibernateException e) {
					Logger.DEBUG.log(Level.WARNING, "Database Error while getting Student Fee details");
					Logger.EXCEPTION.log(Level.WARNING, "Database Error while getting Student Fee details", e);
				}
			}
			feesDetailsPanel.setStudentFeeDetails(studentFeeDetails);
			setRegisterButtonEnabled(false);
			setData(studentDetails);
			setTabEnabled(true);
			setTabEnabled(2);
		}
		setSelectedTab(i);
		setSize(420, 450);
		// setResizable(false);
		setLocationRelativeTo(SchoolMain.Frame);
		// setVisible(true);
	}

	public StudentRegistrationAction(StudentDetails studentDetails, String title, int i) {
		this(studentDetails, null, title, i);
		// super(SchoolMain.Frame, ResourceConstant.REGISTRATION, true);
		this.studentDetails = studentDetails;
		init();
		if (studentDetails != null) {
			try {
				List<StudentFeeDetails> list = HiebernateStudentDboUtil.getStudentFeeDetails(studentDetails);
				this.studentFeeDetails = list.size() > 0 ? list.get(0) : null;
				feesDetailsPanel.setStudentFeeDetails(studentFeeDetails);
			} catch (HibernateException e) {
				Logger.DEBUG.log(Level.WARNING, "Database Error while getting Student Fee details");
				Logger.EXCEPTION.log(Level.WARNING, "Database Error while getting Student Fee details", e);
			}
			setRegisterButtonEnabled(false);
			setData(studentDetails);
			setTabEnabled(true);
			setTabEnabled(2);
		}
		setSelectedTab(i);
		setSize(420, 540);
		setResizable(false);
		setLocationRelativeTo(com.school.console.SchoolMain.Frame);
		setVisible(true);
	}

	private void init() {
		classComboBoxModel = new ClassComboBoxModel();
		sessionComboBoxModel = new SessionComboBoxModel();
		Container con = getContentPane();
		con.setLayout(new BorderLayout());
		con.add(tabbedPanel(), BorderLayout.NORTH);
	}

	private JPanel tabbedPanel() {
		JPanel panel = new JPanel();
		tabbedPane = new JTabbedPane();
		tabbedPane.addTab("Personal Details-1", getRegistrationMainPanel());
		feesDetailsPanel = new StudentFeesDetailsPanel(this, studentFeeDetails);
		payFeesPanel = new StudentPayFeesPanel();
		payFeesPanel.setPropertyListener(this);
		StudentImagePanel imagePanel = new StudentImagePanel(
				studentDetails == null ? null : studentDetails.getStudentImageDetails(), this);
		imagePanel.setPropertyChangeListener(this);
		tabbedPane.addTab("Personal Details-2 ", imagePanel);
		tabbedPane.addTab("Fee Details", feesDetailsPanel);
		tabbedPane.addTab("Deposit Fees", payFeesPanel);
		tabbedPane.setEnabledAt(1, false);
		tabbedPane.setEnabledAt(2, false);
		tabbedPane.setEnabledAt(3, false);
		tabbedPane.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				if (studentDetails == null && tabbedPane.getSelectedIndex() >= 1) {
					MsgDialogUtils.showWarningDialog(StudentRegistrationAction.this, "First Register Student Details");
					tabbedPane.setSelectedIndex(0);
				} else {
					if (tabbedPane.getSelectedIndex() == 2) {
						if (studentFeeDetails == null) {
							feesDetailsPanel.setStudentDetails(studentDetails);
						}
					} else if (tabbedPane.getSelectedIndex() == 3) {
						if (studentFeeDetails != null) {
							payFeesPanel.setFeeDetails(studentFeeDetails);
						} else {
							MsgDialogUtils.showWarningDialog(StudentRegistrationAction.this,
									"First Register Student Fee Details");
							tabbedPane.setSelectedIndex(2);
						}
					}

					btnRegister.setEnabled(false);
				}
			}
		});
		panel.add(tabbedPane);
		return panel;
	}

	private void setTabEnabled(boolean enable) {

		if (studentDetails != null) {
			tabbedPane.setEnabledAt(1, true);
			// tabbedPane.setEnabledAt(2, true);
		}
		if (studentFeeDetails != null) {
			tabbedPane.setEnabledAt(3, true);
		}
	}

	void setTabEnabled(boolean enable, int index) {

		if (studentDetails != null) {
			tabbedPane.setEnabledAt(2, true);
		}
		if (studentFeeDetails != null) {
			tabbedPane.setEnabledAt(3, true);
			tabbedPane.setSelectedIndex(3);
		}
	}

	void setTabEnabled(int index) {

		if (index == 1 || index == 2) {
			if (studentDetails != null) {
				tabbedPane.setEnabledAt(index, true);
			}
		} else {
			if (studentFeeDetails != null) {
				tabbedPane.setEnabledAt(3, true);
				tabbedPane.setSelectedIndex(3);
			}
		}
	}

	void setSelectedTab(int index) {
		if (tabbedPane.getTabCount() > index) {
			tabbedPane.setSelectedIndex(index);
		}

	}

	private JPanel getRegistrationMainPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(getRegistraionPanel(), BorderLayout.NORTH);
		panel.add(getButtonPanel(), BorderLayout.SOUTH);
		return panel;

	}

	private JPanel getRegistraionPanel() {
		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		int row = 0;
		// 0
		JLabel lb = new JLabel("Select Class");
		c.gridx = 0;
		c.gridy = row;
		// c.weightx = 0.5;
		c.insets = new Insets(5, 5, 2, 5);
		c.anchor = GridBagConstraints.LINE_START;
		panel.add(lb, c);
		c.gridx = 1;
		c.gridy = row;
		// c.weightx = 0.5;
		c.gridwidth = 1;
		// c.insets = new Insets(5, 5, 5, 5);
		cbClass = new JComboBox(classComboBoxModel);
		// cbClass.setSize(280, 20);
		lb.setLabelFor(cbClass);
		panel.add(cbClass, c);

		c.gridx = 2;
		c.gridy = row;
		// c.weightx = 0.5;
		c.gridwidth = 1;
		// c.insets = new Insets(5, 5, 5, 5);
		cbSession = new JComboBox(sessionComboBoxModel);
		// cbSession.setSize(280, 20);
		lb.setLabelFor(cbSession);
		panel.add(cbSession, c);
		row++;
		// 1
		c.gridx = 0;
		c.gridy = row;
		// c.weightx = 0.5;
		c.gridwidth = 1;
		// c.insets = new Insets(5, 5, 5, 5);
		c.anchor = GridBagConstraints.LINE_START;
		lb = new JLabel("Name");
		panel.add(lb, c);
		c.gridx = 1;
		c.gridy = row;
		// c.weightx = 0.5;
		// c.insets = new Insets(5, 5, 5, 5);
		c.anchor = GridBagConstraints.LINE_START;
		txtFirstName = new WaterMarkTextField("First Name", 12);
		txtFirstName.setToolTipText("First Name");
		lb.setLabelFor(txtFirstName);
		panel.add(txtFirstName, c);
		c.gridx = 2;
		c.gridy = row;
		// c.weightx = 0.5;
		// c.insets = new Insets(5, 5, 5, 5);
		txtLastName = new WaterMarkTextField("Last Name", 10);
		txtLastName.setToolTipText("Last Name");
		panel.add(txtLastName, c);

		row++;
		// 2

		lb = new JLabel("Father Name");
		panel.add(lb, c);
		c.gridx = 0;
		c.gridy = row;
		// c.weightx = 0.5;
		c.anchor = GridBagConstraints.LINE_START;
		panel.add(lb, c);
		c.gridx = 1;
		c.gridy = row;
		// c.weightx = 0.5;
		c.gridwidth = 2;
		c.anchor = GridBagConstraints.LINE_START;
		txtFatherName = new WaterMarkTextField("Father Name", 12);
		txtFatherName.setToolTipText("Father Name");
		lb.setLabelFor(txtFatherName);
		panel.add(txtFatherName, c);
		row++;
		// 3
		c.gridx = 0;
		c.gridy = row;
		// c.weightx = 0.5;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.LINE_START;
		lb = new JLabel("Mother Name");
		panel.add(lb, c);

		c.gridx = 1;
		c.gridy = row;
		// c.weightx = 0.5;
		c.gridwidth = 2;
		c.anchor = GridBagConstraints.LINE_START;
		txtMotherName = new WaterMarkTextField("Mother Name", 12);
		txtMotherName.setToolTipText("Mother Name");
		lb.setLabelFor(txtMotherName);
		panel.add(txtMotherName, c);
		row++;
		// 4
		c.gridx = 0;
		c.gridy = row;
		// c.weightx = 0.5;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.LINE_START;
		lb = new JLabel("Date of Birth");
		panel.add(lb, c);

		c.gridx = 1;
		c.gridy = row;
		// c.weightx = 0.5;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.LINE_START;
		txtDOB = new WaterMarkTextField("Date Of Birth", 12);
		txtDOB.setToolTipText("Date Of Birth");
		lb.setLabelFor(txtDOB);
		panel.add(txtDOB, c);
		c.gridx = 2;
		c.gridy = row;
		// c.weightx = 0.5;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.LINE_START;
		lb = new JLabel("DD-MM-YYYY");
		panel.add(lb, c);

		row++;
		// 05 Additional row for Gender
		lb = new JLabel("Gender");
		c.gridx = 0;
		c.gridy = row;
		// c.weightx = 0.5;
		c.insets = new Insets(5, 5, 2, 5);
		c.anchor = GridBagConstraints.LINE_START;
		panel.add(lb, c);
		c.gridx = 1;
		c.gridy = row;
		// c.weightx = 0.5;
		c.gridwidth = 1;
		// c.insets = new Insets(5, 5, 5, 5);
		rbMale = new JRadioButton("Male", true);
		// cbClass.setSize(280, 20);
		lb.setLabelFor(rbMale);
		panel.add(rbMale, c);

		c.gridx = 2;
		c.gridy = row;
		// c.weightx = 0.5;
		c.gridwidth = 1;
		// c.insets = new Insets(5, 5, 5, 5);
		rbFeMale = new JRadioButton("Female");
		// cbClass.setSize(280, 20);
		panel.add(rbFeMale, c);
		panel.add(rbFeMale, c);
		gender = new ButtonGroup();
		gender.add(rbMale);
		gender.add(rbFeMale);

		row++;
		// 5
		c.gridx = 0;
		c.gridy = row;
		// c.weightx = 0.5;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.LINE_START;
		lb = new JLabel("Cast");
		panel.add(lb, c);

		c.gridx = 1;
		c.gridy = row;
		// c.weightx = 0.5;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.LINE_START;
		cbCast = new JComboBox(CASTS);
		lb.setLabelFor(cbCast);
		panel.add(cbCast, c);

		row++;
		// 2

		lb = new JLabel("Aadhaar No:");
		panel.add(lb, c);
		c.gridx = 0;
		c.gridy = row;
		// c.weightx = 0.5;
		c.anchor = GridBagConstraints.LINE_START;
		panel.add(lb, c);
		c.gridx = 1;
		c.gridy = row;
		// c.weightx = 0.5;
		c.gridwidth = 2;
		c.anchor = GridBagConstraints.LINE_START;
		txtAdhaar = new WaterMarkTextField("Aadhaar Number", 12);
		txtAdhaar.setToolTipText("Adhaar Number");
		lb.setLabelFor(txtAdhaar);
		panel.add(txtAdhaar, c);

		row++;
		// 5
		c.gridx = 0;
		c.gridy = row;
		// c.weightx = 0.5;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.LINE_START;
		lb = new JLabel("Parents Occupation");
		panel.add(lb, c);

		c.gridx = 1;
		c.gridy = row;
		// c.weightx = 0.5;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.LINE_START;
		cbParentOccupation = new JComboBox(OCCUPATION);
		lb.setLabelFor(cbParentOccupation);
		panel.add(cbParentOccupation, c);
		row++;
		// 6
		c.gridx = 0;
		c.gridy = row;
		// c.weightx = 0.5;
		c.gridwidth = 1;

		c.anchor = GridBagConstraints.LINE_START;
		lb = new JLabel("Mobile");
		panel.add(lb, c);
		c.gridx = 1;
		c.gridy = row;
		// c.weightx = 0.5;
		c.gridwidth = 2;
		c.anchor = GridBagConstraints.LINE_START;
		txtMobile = new WaterMarkTextField("Mobile Number", 12);
		txtMobile.setToolTipText("Mobile Number");
		lb.setLabelFor(txtMobile);
		panel.add(txtMobile, c);

		row++;
		// 6
		c.gridx = 0;
		c.gridy = row;
		// c.weightx = 0.5;
		c.gridwidth = 1;

		c.anchor = GridBagConstraints.LINE_START;
		lb = new JLabel("Phone Number");
		panel.add(lb, c);
		c.gridx = 1;
		c.gridy = row;
		// c.weightx = 0.5;
		c.gridwidth = 2;
		c.anchor = GridBagConstraints.LINE_START;
		txtPhone = new WaterMarkTextField("Phone Number", 12);
		txtPhone.setToolTipText("Phone Number");
		lb.setLabelFor(txtPhone);
		panel.add(txtPhone, c);

		row++;
		// 7
		c.gridx = 0;
		c.gridy = row;
		// c.weightx = 0.5;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.LINE_START;
		lb = new JLabel("Email");
		panel.add(lb, c);
		c.gridx = 1;
		c.gridy = row;
		// c.weightx = 0.5;
		c.gridwidth = 2;
		c.anchor = GridBagConstraints.LINE_START;
		txtEmailId = new WaterMarkTextField("Email",20);
		lb.setLabelFor(txtEmailId);
		panel.add(txtEmailId, c);
		txtEmailId.setToolTipText("Email");

		row++;
		// 8

		c.gridx = 0;
		c.gridy = row;
		// c.weightx = 0.5;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.LINE_START;
		lb = new JLabel("Address");
		panel.add(lb, c);
		c.gridx = 1;
		c.gridy = row;
		// c.weightx = 0.5;
		c.gridwidth = 2;
		c.anchor = GridBagConstraints.LINE_START;
		txtAddress = new JTextArea(4, 20);
		JScrollPane txtScroll = new JScrollPane(txtAddress);
		txtScroll.setMaximumSize(new Dimension(200, 120));
		txtScroll.setSize(200, 90);
		txtAddress.setAutoscrolls(true);
		lb.setLabelFor(txtScroll);
		panel.add(txtScroll, c);
		row++;
		// 9
		c.gridx = 0;
		c.gridy = row;
		// c.weightx = 0.5;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.LINE_START;
		lb = new JLabel("City");
		panel.add(lb, c);
		c.gridx = 1;
		c.gridy = row;
		// c.weightx = 0.5;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.LINE_START;
		txtCity = new WaterMarkTextField("City", 12);
		lb.setLabelFor(txtCity);
		panel.add(txtCity, c);
		txtCity.setToolTipText("City");
		c.gridx = 2;
		c.gridy = row;
		// c.weightx = 0.5;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.LINE_START;
		cbState = new JComboBox(STATE);
		lb.setLabelFor(cbState);
		panel.add(cbState, c);

		return panel;
	}

	private JPanel getButtonPanel() {
		JPanel panel = new JPanel(new FlowLayout());
		btnRegister = new JButton(ResourcesUtils.getString("REGISTER"));
		btnRegister.setActionCommand(OperationConstant.STUDENT_REGISTER);
		btnRegister.addActionListener(this);
		btnUpdate = new JButton(ResourcesUtils.getString("UPDATE"));
		btnUpdate.setActionCommand(OperationConstant.STUDENT_UPDATE);
		btnUpdate.addActionListener(this);
		btnCancel = new JButton(ResourceConstant.CANCEL);
		btnCancel.setActionCommand(CommandConstant.CANCEL_COMMAND);
		btnCancel.addActionListener(this);
		panel.add(btnRegister);
		panel.add(btnUpdate);
		panel.add(btnCancel);
		btnRegister.setFocusable(true);
		setRegisterButtonEnabled(true);
		return panel;
	}

	private void setRegisterButtonEnabled(boolean enable) {
		btnRegister.setEnabled(enable && ApplicationOpertaion.isAllowed(OperationConstant.STUDENT_REGISTER));
		btnUpdate.setEnabled(!enable && ApplicationOpertaion.isAllowed(OperationConstant.STUDENT_UPDATE));

	}

	private void clearValue() {

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (OperationConstant.STUDENT_REGISTER.equals(e.getActionCommand())) {
			String error = validateInput();
			if (error != null) {
				MsgDialogUtils.showWarningDialog(this, error);
				return;
			}
			boolean result = false;
			StudentDetails studentDetails = null;
			try {
				studentDetails = createStudentObject();
				studentDetails.setAddDate(new Date());
				this.studentDetails = HiebernateStudentDboUtil.saveStudentDetails(studentDetails);
				result = true;
			} catch (ParseException e1) {
				MsgDialogUtils.showWarningDialog(this, "Invalid Date Format! ");
				return;
			} catch (HibernateException exp) {
				MsgDialogUtils.showWarningDialog(this, "Database error occured! ");
				Logger.EXCEPTION.log(Level.WARNING, "Error Occured while save student Details", exp);
				return;
			}
			if (result) {
				ActionDispatcher.fireAction(OperationConstant.STUDENT_REGISTER, studentDetails);
				MsgDialogUtils.showInformationDialog(this, "Student Details has been registered Successfully!");
				Logger.DEBUG.info("Student Details has been registered Successfully");
				setTabEnabled(true);
				tabbedPane.setSelectedIndex(1);
				// ActionDispatcher.fireAction(
				// CommandCodeConstant.SESSION_REG_CLOSE, sessionDetails);
			} else {
				MsgDialogUtils.showWarningDialog(this, "Student Details  Already Registered Successfully!");
			}

		} else if (OperationConstant.STUDENT_UPDATE.equals(e.getActionCommand())) {

			String error = validateInput();
			if (error != null) {
				MsgDialogUtils.showWarningDialog(this, error);
				return;
			}
			boolean result = false;
			StudentDetails studentDetails = null;
			try {
				studentDetails = createStudentObject(this.studentDetails);
				studentDetails.setUpdateDate(new Date());
				result = HiebernateStudentDboUtil.updateStudentDetails(studentDetails);
			} catch (ParseException e1) {
				MsgDialogUtils.showWarningDialog(this, "Invalid Date Format! ");
				return;
			} catch (HibernateException exp) {
				MsgDialogUtils.showWarningDialog(this, "Database error occured! ");
				Logger.EXCEPTION.log(Level.WARNING, "Error Occured while updating student Details", exp);
				return;
			}
			if (result) {
				ActionDispatcher.fireAction(OperationConstant.STUDENT_UPDATE, studentDetails);
				MsgDialogUtils.showInformationDialog(this, "Student Details has been updated Successfully!");
				Logger.DEBUG.info("Student Details has been updated Successfully");
				tabbedPane.setSelectedIndex(1);
			} else {
				MsgDialogUtils.showWarningDialog(this, "Student Details  Already Registered Successfully!");
			}

		} else if (CommandConstant.CANCEL_COMMAND.equals(e.getActionCommand())) {
			dispose();
		}
	}

	private void setData(StudentDetails student) {
		txtFirstName.setText(student.getFirstName());
		txtLastName.setText(student.getLastName());
		txtFatherName.setText(student.getFatherName());
		txtMotherName.setText(student.getMotherName());
		txtDOB.setText(ApplicationConstant.sdf_date.format(student.getDateOfBirth()));
		cbParentOccupation.setSelectedItem(student.getParentsOccupation());
		txtPhone.setText(student.getPhone());
		txtMobile.setText(student.getMobile());
		txtEmailId.setText(student.getEmailId());
		txtAddress.setText(student.getAddress());
		txtCity.setText(student.getCity());
		cbState.setSelectedItem(student.getState());
		cbClass.setSelectedItem(student.getClassDetails().getClassName());
		cbSession.setSelectedItem(student.getSessionDetails().getDisplayString());
		if (student.getCast() != null && !student.getCast().isEmpty()) {
			cbCast.setSelectedItem(student.getCast());
		}
		if (FEMALE.equals(student.getGender())) {
			rbFeMale.setSelected(true);
		} else {
			rbMale.setSelected(true);
		}
		txtAdhaar.setText(student.getAadhar());
	}

	private StudentDetails createStudentObject(StudentDetails student) throws ParseException {
		if (student == null) {
			student = new StudentDetails();
		}
		student.setFirstName(txtFirstName.getText().trim());
		student.setLastName(txtLastName.getText().trim());
		student.setFatherName(txtFatherName.getText().trim());
		student.setMotherName(txtMotherName.getText().trim());
		student.setClassDetails(classComboBoxModel.getSelectedObject(cbClass.getSelectedIndex()));
		student.setSessionDetails(sessionComboBoxModel.getSelectedObject(cbSession.getSelectedIndex()));
		student.setDateOfBirth(ApplicationConstant.sdf_date.parse(txtDOB.getText().trim()));
		student.setParentsOccupation(cbParentOccupation.getSelectedItem().toString());
		student.setPhone(txtPhone.getText().trim());
		student.setEmailId(txtEmailId.getText().trim());
		student.setAddress(txtAddress.getText());
		student.setCity(txtCity.getText().trim());
		student.setState(cbState.getSelectedItem().toString());
		student.setRegistrationNumber(Utils.generateRegistrationNumber(student.getSessionDetails(),
				student.getClassDetails(), student.getFirstName()));
		student.setAadhar(txtAdhaar.getText().trim().isEmpty() ? null : txtAdhaar.getText().trim());
		student.setCast(cbCast.getSelectedItem().toString());
		student.setGender(rbMale.isSelected() ? MALE : FEMALE);
		student.setMobile(txtMobile.getText().trim());
		return student;
	}

	private StudentDetails createStudentObject() throws ParseException {
		return createStudentObject(null);
	}

	private String validateInput() {
		StringBuilder sb = new StringBuilder();
		boolean error = false;
		if ("".equals(txtFirstName.getText().trim())) {
			error = true;
			sb.append("Please Enter First Name");
			sb.append("\n");
		}
		if ("".equals(txtLastName.getText().trim())) {
			error = true;
			sb.append("Please Enter Last Name");
			sb.append("\n");
		}
		if ("".equals(txtFatherName.getText().trim()) && "".equals(txtMotherName.getText().trim())) {
			error = true;
			sb.append("Please Enter at least Father or Mother Name");
			sb.append("\n");
		}
		if ("".equals(txtDOB.getText().trim())) {
			error = true;
			sb.append("Please Enter Date Of Birth");
			sb.append("\n");
		} else {
			try {
				ApplicationConstant.sdf_date.parse(txtDOB.getText().trim());
			} catch (ParseException e) {
				error = true;
				sb.append("Please Enter Date Of Birth in dd-MM-yyyy format");
				sb.append("\n");
			}
		}
		if (!"".equals(txtAdhaar.getText().trim()) && !ValidationUtils.isValidAdhaar(txtAdhaar.getText().trim())) {
			error = true;
			sb.append("Please Enter Valid 12 digit Aadhar number");
			sb.append("\n");
		}
		if (!"".equals(txtPhone.getText().trim())) {
			if (!ValidationUtils.isValidPhone(txtPhone.getText().trim())
					|| !ValidationUtils.isValidMobile(txtPhone.getText().trim())) {
				error = true;
				sb.append("Please Enter Valid Phone Number format and start with 0 or +91 ");
				sb.append("\n");
			}
		}
		if (!"".equals(txtMobile.getText().trim()) && !ValidationUtils.isValidMobile(txtMobile.getText().trim())) {
			error = true;
			sb.append("Please Enter Valid Mobile Number format and start with 0 or +91 ");
			sb.append("\n");
		}

		if (!"".equals(txtEmailId.getText().trim())
				&& !ValidationUtils.isValidEmailAddress(txtEmailId.getText().trim())) {
			error = true;
			sb.append("Please Enter Valid Email address");
			sb.append("\n");
		}
		if (!error) {
			return null;
		}
		return sb.toString();
	}

	private void studentUpdate(StudentDetails studentDetails) {
		try {
			studentDetails.setUpdateDate(new Date());
			HiebernateStudentDboUtil.updateStudentDetails(studentDetails);
		} catch (HibernateException exp) {
			MsgDialogUtils.showWarningDialog(this, "Database error occured! " + exp.getLocalizedMessage());
			Logger.EXCEPTION.log(Level.WARNING, "Error Occured while updating student Details", exp);
			return;
		}
		/*
		 * if (result) { ActionDispatcher.fireAction(OperationConstant.STUDENT_UPDATE,
		 * studentDetails); MsgDialogUtils.showInformationDialog(this,
		 * "Student Details has been updated Successfully!");
		 * Logger.DEBUG.info("Student Details has been updated Successfully");
		 * tabbedPane.setSelectedIndex(1); } else {
		 * MsgDialogUtils.showWarningDialog(this,
		 * "Student Details  Already Registered Successfully!"); }
		 */
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (DISPOSE_DIALOG.equals(evt.getPropertyName())) {
			dispose();
		} else if (CHANGE_TAB.equals(evt.getPropertyName())) {
			int i = (Integer) evt.getNewValue();
			setSelectedTab(i);
		} else if (CHANGEANDENABLE_TAB.equals(evt.getPropertyName())) {
			int i = (Integer) evt.getNewValue();
			setTabEnabled(i);
			setSelectedTab(i);
		} else if (FEEDETAILS_SAVED.equals(evt.getPropertyName())) {
			setStudentFeeDetails((StudentFeeDetails) evt.getOldValue());
			int i = (Integer) evt.getNewValue();
			setTabEnabled(i);
			setSelectedTab(i);
		} else if (IMAGE_REGISTER.equals(evt.getPropertyName())) {
			StudentImageDetails studentImageDetails = (StudentImageDetails) evt.getNewValue();
			if (studentImageDetails != null) {
				studentDetails.setStudentImageDetails(studentImageDetails);
				studentUpdate(studentDetails);
				setTabEnabled(2);
				setSelectedTab(2);
			}
		} else if (IMAGE_UPDATE.equals(evt.getPropertyName())) {
			StudentImageDetails studentImageDetails = (StudentImageDetails) evt.getNewValue();
			if (studentImageDetails != null) {
				studentDetails.setStudentImageDetails(studentImageDetails);
				studentUpdate(studentDetails);
			}
		}
	}

}
