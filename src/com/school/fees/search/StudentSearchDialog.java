package com.school.fees.search;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.banti.framework.core.SchoolOpertaionAction;
import com.banti.framework.core.WaterMarkTextField;
import com.banti.framework.ui.DialogEsc;
import com.school.combobox.model.ClassComboBoxModel;
import com.school.combobox.model.SessionComboBoxModel;
import com.school.console.SchoolMain;
import com.school.hiebernate.dbo.ClassDetails;
import com.school.hiebernate.dbo.SessionDetails;
import com.school.resource.CommandConstant;
import com.school.resource.ResourceConstant;
import com.school.resource.ResourcesUtils;

public class StudentSearchDialog extends DialogEsc implements ActionListener {
	private JTextField txtFirstName;
	private JTextField txtLastName;
	private JComboBox cbSession;
	private JComboBox cbClass;
	private JTextField txtFatherName;
	private SchoolOpertaionAction searchAction;
	private SessionComboBoxModel sessioncomboBoxModel;
	private ClassComboBoxModel classcomboBoxModel;
	private JButton btnCancel;
	public static final String SEARCH_CMD = "Search";
	private ActionListener listener;

	public StudentSearchDialog(ActionListener listener) {
		super(SchoolMain.Frame, ResourcesUtils.getString("STUDENT_SEARCH"),
				true);
		this.listener = listener;
		init();

		setSize(400, 250);
		setLocationRelativeTo(SchoolMain.Frame);
		//setVisible(true);
	}

	private void init() {
		initializeModel();
		Container con = getContentPane();
		con.setLayout(new BorderLayout());
		con.add(getPanel(), BorderLayout.NORTH);
	}

	private void initializeModel() {
		sessioncomboBoxModel = new SessionComboBoxModel();
		sessioncomboBoxModel.addDefaultSession();
		classcomboBoxModel = new ClassComboBoxModel();
		classcomboBoxModel.addDefaultCLASS();
		sessioncomboBoxModel.setCurrentDefaultSelection();
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
		searchAction = new SchoolOpertaionAction(
				ResourcesUtils.getString("SEARCH"), SEARCH_CMD);
		searchAction.registerActionListener(this);
		JButton btnSearch = new JButton(searchAction);
		btnCancel = new JButton(ResourceConstant.CANCEL);
		btnCancel.setActionCommand(CommandConstant.CANCEL_COMMAND);
		btnCancel.addActionListener(this);
		panel.add(btnSearch);
		panel.add(btnCancel);
		return panel;
	}

	private JPanel getMiddleJPanel() {
		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		JLabel lb = new JLabel("Student Name ");
		c.insets = new Insets(5, 5, 5, 5);
		c.anchor = GridBagConstraints.LINE_START;
		panel.add(lb, c);
		c.gridx = 1;
		c.gridy = 0;
		txtFirstName = new WaterMarkTextField("First Name");
		lb.setLabelFor(txtFirstName);
		panel.add(txtFirstName, c);
		c.gridx = 2;
		c.gridy = 0;
		txtLastName = new WaterMarkTextField("Last Name");
		lb.setLabelFor(txtLastName);
		panel.add(txtLastName, c);
		// 1
		c.gridx = 0;
		c.gridy = 1;
		lb = new JLabel("Father Name ");
		c.insets = new Insets(5, 5, 5, 5);
		c.anchor = GridBagConstraints.LINE_START;
		panel.add(lb, c);
		c.gridx = 1;
		c.gridwidth = 2;
		c.weightx = 1;
		txtFatherName = new WaterMarkTextField("Father Name");
		lb.setLabelFor(txtFatherName);
		panel.add(txtFatherName, c);

		// 2
		lb = new JLabel("Select Session ");
		c.gridx = 0;
		c.gridy = 2;
		// c.weightx = 0.5;
		c.insets = new Insets(5, 5, 5, 5);
		c.anchor = GridBagConstraints.LINE_START;
		panel.add(lb, c);
		c.gridx = 1;
		// c.weightx = 0.5;
		c.gridwidth = 2;
		cbSession = new JComboBox(sessioncomboBoxModel);
		cbSession.setPreferredSize(new Dimension(200, 25));
		lb.setLabelFor(cbSession);
		panel.add(cbSession, c);

		// 3
		lb = new JLabel("Select Class ");
		c.gridx = 0;
		c.gridy = 3;
		// c.weightx = 0.75;
		c.insets = new Insets(5, 5, 5, 5);
		c.anchor = GridBagConstraints.LINE_START;
		panel.add(lb, c);
		c.gridx = 1;
		// c.weightx = 0.5;
		c.gridwidth = 2;
		cbClass = new JComboBox(classcomboBoxModel);
		cbClass.setPreferredSize(new Dimension(200, 25));
		lb.setLabelFor(cbClass);
		panel.add(cbClass, c);
		return panel;
	}

	public String getFirstName() {
		if (!txtFirstName.getText().trim().isEmpty()) {
			String name = txtFirstName.getText().replaceAll("-", "?") + "%";
			return name;
		}
		return null;
	}

	public String getLastName() {
		if (!txtLastName.getText().trim().isEmpty()) {
			String name = txtLastName.getText().replaceAll("-", "?") + "%";
			return name;
		}
		return null;
	}

	public String getFatherName() {
		if (!txtFatherName.getText().trim().isEmpty()) {
			String name = txtFatherName.getText().replaceAll("-", "?") + "%";
			return name;
		}
		return null;
	}

	public SessionDetails getSessionDetails() {
		return sessioncomboBoxModel.getSelectedObject(cbSession.getSelectedIndex());
	}
	public ClassDetails getClassDetails() {
		return classcomboBoxModel.getSelectedObject(cbClass.getSelectedIndex());
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if (CommandConstant.CANCEL_COMMAND.equals(e.getActionCommand())) {
			dispose();
		}else if(SEARCH_CMD.equals(e.getActionCommand())){
			if(listener!=null){
				listener.actionPerformed(e);
			}else{
				//vbnm,.
			}
		}
	}
}
