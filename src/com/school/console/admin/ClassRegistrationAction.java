package com.school.console.admin;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.hibernate.HibernateException;

import com.banti.framework.core.ActionDispatcher;
import com.banti.framework.ui.DialogEsc;
import com.school.combobox.model.SessionComboBoxModel;
import com.school.hiebernate.HiebernateDboUtil;
import com.school.hiebernate.dbo.ClassDetails;
import com.school.hiebernate.dbo.SessionDetails;
import com.school.resource.CommandConstant;
import com.school.resource.ResourceConstant;
import com.school.shared.CommandCodeConstant;

public class ClassRegistrationAction extends DialogEsc implements ActionListener {
	// private List<SessionDetails> sessionDetails;
	private JComboBox cbSession;
	private JTextField txtClass;
	private SessionComboBoxModel comboBoxModel;
	private JButton btnRegister;
	private JButton btnUpdate;
	private JButton btnCancel;
	private boolean isUpdate = false;
	private ClassDetails classDetails;

	// private String sessions[] = { "2012-2013", "2013-2014" };

	public ClassRegistrationAction(JDialog parent,
			List<SessionDetails> dbSessionList) throws HibernateException {
		this(parent, null, dbSessionList,"Class Registration");
	}
	public ClassRegistrationAction(JDialog parent, ClassDetails classdetails,
			List<SessionDetails> dbSessionLists) throws HibernateException {
		this(parent, classdetails, dbSessionLists, "Class Updation ");
	}
	public ClassRegistrationAction(JDialog parent, ClassDetails classdetails,
			List<SessionDetails> dbSessionLists,String title) throws HibernateException {
		super(parent, title, true);

		comboBoxModel = new SessionComboBoxModel(dbSessionLists);
		init();
		if (classdetails != null) {
			this.classDetails = classdetails;
			isUpdate = true;
			setData();
		}
		setButtonState(isUpdate);
		setSize(300, 250);
		setLocationRelativeTo(parent);
	}

	private void setData() {
		comboBoxModel.setSelectedItem(classDetails.getDisplaySession());
		txtClass.setText(classDetails.getClassName());
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
		c.anchor = GridBagConstraints.PAGE_START;
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(10, 10, 10, 10);
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

	private void setButtonState(boolean isUpdate) {
		if (isUpdate) {
			btnUpdate.setFocusable(true);
		} else {
			btnRegister.setFocusable(true);
		}
		btnRegister.setEnabled(!isUpdate);
		btnUpdate.setEnabled(isUpdate);
	}

	private JPanel getMiddleJPanel() {
		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.NONE;
		// 0
		JLabel lb = new JLabel("Select Session ");
		c.gridx = 0;
		c.gridy = 0;
		// c.weightx = 0.5;
		c.insets = new Insets(5, 5, 5, 5);
		c.anchor = GridBagConstraints.LINE_START;
		panel.add(lb, c);
		c.gridx = 1;
		c.gridy = 0;
		// c.weightx = 0.5;
		c.gridwidth = 1;
		cbSession = new JComboBox(comboBoxModel);
		lb.setLabelFor(cbSession);
		panel.add(cbSession, c);

		// 1
		lb = new JLabel("Class Name ");
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
		txtClass = new JTextField(10);
		lb.setLabelFor(txtClass);
		panel.add(txtClass, c);
		return panel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (CommandConstant.REGISTER_COMMAND.equals(e.getActionCommand())) {
			String error = inputValidate();
			if (error != null) {
				showWarningDialog(error);
				return;
			}
			ClassDetails classDetails = createClassDetails();
			boolean result = false;
			try {
				result = HiebernateDboUtil.saveClassDetails(classDetails);
			} catch (HibernateException exp) {
				showWarningDialog("Database error occured! ");
				return;
			}
			if (result) {
				showInformationDialog("Class has been registered Successfully!");
				dispose();
				ActionDispatcher.fireAction(
						CommandCodeConstant.CLASS_REG_CLOSE, classDetails);
			} else {
				showWarningDialog("Class already registered with same name !");
			}

		} else if (CommandConstant.UPDATE_COMMAND.equals(e.getActionCommand())) {
			String error = inputValidate();
			if (error != null) {
				showWarningDialog(error);
				return;
			}
			if (classDetails != null) {
				classDetails.setClassName(txtClass.getText().trim());
				classDetails.setDisplaySession((String) cbSession
						.getSelectedItem());
				classDetails.setUpdateDate(new Date());
			}
			boolean result = false;
			try {
				result = HiebernateDboUtil.updateClassDetails(classDetails);
			} catch (HibernateException exp) {
				showWarningDialog("Database error occured! ");
				return;
			}
			if (result) {
				showInformationDialog("Class has been updated Successfully!");
				dispose();
				ActionDispatcher.fireAction(
						CommandCodeConstant.CLASS_MOD_CLOSE, classDetails);
			} else {
				showWarningDialog("Class already registered with same name ");
			}

		} else if (CommandConstant.CANCEL_COMMAND.equals(e.getActionCommand())) {
			dispose();
			ActionDispatcher
					.removeListener(CommandCodeConstant.CLASS_REG_CLOSE);
			ActionDispatcher
					.removeListener(CommandCodeConstant.CLASS_MOD_CLOSE);
		}
	}

	private String inputValidate() {
		StringBuilder sb = new StringBuilder();
		boolean error = false;
		if (cbSession.getSelectedIndex() < 0) {
			sb.append("Please Select Session.");
			sb.append("\n");
			error = true;
		}
		if (txtClass.getText().trim().isEmpty()) {
			sb.append("Please Enter Class Name.");
			sb.append("\n");
			error = true;
		}
		if (error) {
			return sb.toString();
		}
		return null;
	}

	private ClassDetails createClassDetails() {
		ClassDetails classDetails = new ClassDetails();
		classDetails.setDisplaySession((String) cbSession.getSelectedItem());
		classDetails.setClassName(txtClass.getText().trim());
		classDetails.setAddDate(new Date());
		return classDetails;
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
