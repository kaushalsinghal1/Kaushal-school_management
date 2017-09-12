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
import java.util.List;
import java.util.logging.Level;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.hibernate.HibernateException;

import com.banti.framework.cwt.ProgressCommunicator;
import com.banti.framework.cwt.ProgressCommunicatorMonitor;
import com.banti.framework.platform.AbstractSchoolAction;
import com.banti.framework.ui.DialogEsc;
import com.school.combobox.model.ClassComboBoxModel;
import com.school.combobox.model.SessionComboBoxModel;
import com.school.console.SchoolMain;
import com.school.constant.OperationConstant;
import com.school.hiebernate.DBOFacotry;
import com.school.hiebernate.dbo.ClassDetails;
import com.school.hiebernate.dbo.SessionDetails;
import com.school.hiebernate.dbo.StudentFeeDetails;
import com.school.resource.CommandConstant;
import com.school.resource.ResourceConstant;
import com.school.resource.ResourcesUtils;
import com.school.utils.Logger;

public class FeeDetailsSearchDialog extends DialogEsc implements ActionListener {
	private JComboBox cbSession;
	private JComboBox cbClass;
	private SessionComboBoxModel sessioncomboBoxModel;
	private ClassComboBoxModel classcomboBoxModel;
	private JButton btnCancel;
	private AbstractSchoolAction searchAction;

	public FeeDetailsSearchDialog() {
		super(SchoolMain.Frame, ResourcesUtils
				.getString("STUDENT_DUE_FEE__DETAILS"), true);
		init();
		setSize(300, 180);
		setLocationRelativeTo(SchoolMain.Frame);
		setVisible(true);
	}

	private void initializeModel() {
		sessioncomboBoxModel = new SessionComboBoxModel();
		// sessioncomboBoxModel.addDefaultSession();
		classcomboBoxModel = new ClassComboBoxModel();
		classcomboBoxModel.addDefaultCLASS();
		sessioncomboBoxModel.setCurrentDefaultSelection();
	}

	private void init() {
		initializeModel();
		Container con = getContentPane();
		con.setLayout(new BorderLayout());
		con.add(getPanel(), BorderLayout.NORTH);
	}

	private JPanel getPanel() {
		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
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

	@SuppressWarnings("serial")
	private JPanel getButtonPanel() {
		JPanel panel = new JPanel(new FlowLayout());
		searchAction = new AbstractSchoolAction(
				ResourcesUtils.getString("SEARCH"),
				OperationConstant.REPORT_FEE_DETAILS) {

			@Override
			public void actionPerformed(ActionEvent e) {
				FeeDetailsSearchDialog.this.actionPerformed(e);
			}
		};
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
		// 0
		JLabel lb = new JLabel("Select Session ");
		c.gridx = 0;
		c.gridy = 0;
		// c.weightx = 0.25;
		c.insets = new Insets(5, 5, 5, 5);
		c.anchor = GridBagConstraints.LINE_START;
		panel.add(lb, c);
		c.gridx = 1;
		c.gridy = 0;
		// c.weightx = 1;
		cbSession = new JComboBox(sessioncomboBoxModel);
		cbSession.setPreferredSize(new Dimension(150, 25));
		lb.setLabelFor(cbSession);
		panel.add(cbSession, c);

		// 1
		lb = new JLabel("Select Class ");
		c.gridx = 0;
		c.gridy = 1;
		// c.weightx = 0.75;
		c.insets = new Insets(5, 5, 5, 5);
		c.anchor = GridBagConstraints.LINE_START;
		panel.add(lb, c);
		c.gridx = 1;
		c.gridy = 1;
		// c.weightx = 0.75;
		// c.gridwidth = 1;
		cbClass = new JComboBox(classcomboBoxModel);
		cbClass.setPreferredSize(new Dimension(150, 25));
		lb.setLabelFor(cbClass);
		panel.add(cbClass, c);
		return panel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (OperationConstant.REPORT_FEE_DETAILS.equals(e.getActionCommand())) {
			new SearchProgress();
		} else if (CommandConstant.CANCEL_COMMAND.equals(e.getActionCommand())) {
			dispose();
		}
	}

	class SearchProgress implements ProgressCommunicator {

		public SearchProgress() {
			ProgressCommunicatorMonitor communicatorMonitor = new ProgressCommunicatorMonitor(
					"Loading Search result...", true, SearchProgress.this);
			communicatorMonitor.start();
		}

		@Override
		public void run() {
			ClassDetails classDetails = classcomboBoxModel
					.getSelectedObject(cbClass.getSelectedIndex());
			if (classDetails != null && classDetails.getClassId() == 0) {
				classDetails = null;
			}
			SessionDetails sessionDetails = sessioncomboBoxModel
					.getSelectedObject(cbSession.getSelectedIndex());
			if (sessionDetails != null && sessionDetails.getSessionId() == 0) {
				sessionDetails = null;
			}
			try {
				List<StudentFeeDetails> studentFeeDetails = DBOFacotry
						.getSearchDBManager().getStudentFeeDetails(
								classDetails, sessionDetails);
				Logger.DEBUG.fine("Student Fee details fetched from DB");
				dispose();
				new StudentDueFeeSearchWindow(studentFeeDetails,
						ResourcesUtils
								.getString("STUDENT_DUE_FEE__DETAILS_LIST"));
			} catch (HibernateException ex) {
				Logger.EXCEPTION.log(Level.WARNING,
						"Error while getting student fee details", ex);
			}
		}

		@Override
		public void doPostProcess() {

		}

		@Override
		public void doCancel() {

		}
	}
}
