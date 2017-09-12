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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import org.hibernate.HibernateException;

import com.banti.framework.cwt.ProgressCommunicator;
import com.banti.framework.cwt.ProgressCommunicatorMonitor;
import com.banti.framework.cwt.TimeChooserPane;
import com.banti.framework.platform.AbstractSchoolAction;
import com.banti.framework.ui.DialogEsc;
import com.school.combobox.model.ClassComboBoxModel;
import com.school.combobox.model.SessionComboBoxModel;
import com.school.console.SchoolMain;
import com.school.constant.OperationConstant;
import com.school.fees.search.FeeDetailsSearchDialog.SearchProgress;
import com.school.hiebernate.DBOFacotry;
import com.school.hiebernate.HiebernateDboReprotUtil;
import com.school.hiebernate.dbo.ClassDetails;
import com.school.hiebernate.dbo.DepositeFeeMaster;
import com.school.hiebernate.dbo.SessionDetails;
import com.school.hiebernate.dbo.StudentFeeDetails;
import com.school.resource.CommandConstant;
import com.school.resource.ResourceConstant;
import com.school.resource.ResourcesUtils;
import com.school.utils.Logger;
import com.school.utils.MsgDialogUtils;

public class DepositeFeeSearch extends DialogEsc implements ActionListener {
	private JComboBox cbSession;
	private JComboBox cbClass;
	private SessionComboBoxModel sessioncomboBoxModel;
	private ClassComboBoxModel classcomboBoxModel;
	private JButton btnCancel;
	private AbstractSchoolAction searchAction;
	private TimeChooserPane tcTop;
	private TimeChooserPane tcBottom;
	private JPanel priodPanel;
	SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

	public DepositeFeeSearch() {
		super(SchoolMain.Frame, ResourcesUtils
				.getString("FEE_COLLECTION_DETAILS"), true);
		init();
		setSize(350, 300);
		setLocationRelativeTo(SchoolMain.Frame);
		setVisible(true);
	}

	private void initializeModel() {
		sessioncomboBoxModel = new SessionComboBoxModel();
		sessioncomboBoxModel.addDefaultSession();
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

	@SuppressWarnings("serial")
	private JPanel getButtonPanel() {
		JPanel panel = new JPanel(new FlowLayout());
		searchAction = new AbstractSchoolAction(
				ResourcesUtils.getString("SEARCH"),
				OperationConstant.REPORT_DEPOSITE_FEE) {

			@Override
			public void actionPerformed(ActionEvent e) {
				DepositeFeeSearch.this.actionPerformed(e);
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
		// c.weightx = 0.5;
		c.insets = new Insets(5, 5, 5, 5);
		c.anchor = GridBagConstraints.LINE_START;
		panel.add(lb, c);
		c.gridx = 1;
		c.gridy = 0;
		// c.weightx = 0.5;
		// c.gridwidth = 1;
		cbSession = new JComboBox(sessioncomboBoxModel);
		cbSession.setPreferredSize(new Dimension(200, 25));
		lb.setLabelFor(cbSession);
		panel.add(cbSession, c);

		// 1
		lb = new JLabel("Select Class ");
		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 0.75;
		c.insets = new Insets(5, 5, 5, 5);
		c.anchor = GridBagConstraints.LINE_START;
		panel.add(lb, c);
		c.gridx = 1;
		c.gridy = 1;
		// c.weightx = 0.5;
		// c.gridwidth = 1;
		cbClass = new JComboBox(classcomboBoxModel);
		cbClass.setPreferredSize(new Dimension(200, 25));
		lb.setLabelFor(cbClass);
		panel.add(cbClass, c);
		// 2
		lb = new JLabel("Select Period ");
		c.gridx = 0;
		c.gridy = 2;
		// c.weightx = 0.5;
		c.insets = new Insets(5, 5, 5, 5);
		c.anchor = GridBagConstraints.LINE_START;
		// panel.add(lb, c);
		// c.gridx = 1;
		// c.gridy = 2;
		// c.weightx = 0.75;
		c.gridwidth = 2;
		// cbsearchOption = new JComboBox(periodComboBoxModel);
		// cbsearchOption.setPreferredSize(new Dimension(200, 25));
		createTimePanel();
		lb.setLabelFor(priodPanel);
		panel.add(priodPanel, c);
		return panel;
	}

	protected void createTimePanel() {
		priodPanel = new JPanel();
		Border border = BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),
				ResourcesUtils.getString("CHECK_TIMESPECIFY"),
				TitledBorder.LEFT, TitledBorder.TOP);

		priodPanel.setBorder(border);

		final JLabel dateJL = new JLabel(ResourcesUtils.getString("FROM")
				+ " : ");
		final JLabel toJL = new JLabel(ResourcesUtils.getString("TO"));
		GridBagConstraints c = new GridBagConstraints();
		priodPanel.setLayout(new GridBagLayout());
		tcTop = new TimeChooserPane(this, false);
		tcBottom = new TimeChooserPane(this, true);

		JPanel panel = new JPanel(new GridBagLayout()) {
			public void setEnabled(boolean b) {
				tcTop.setEnabled(b);
				tcBottom.setEnabled(b);
				dateJL.setEnabled(b);
				toJL.setEnabled(b);
			}
		};
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(3, 3, 3, 3);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.WEST;
		c.gridx = 0;
		panel.add(dateJL, c);

		c.gridx = 1;
		c.weightx = 1;
		Dimension tcSize = new Dimension(160, 29);
		tcTop.setMinimumSize(tcSize);
		tcTop.setPreferredSize(tcSize);
		panel.add(tcTop, c);

		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 0;
		c.insets.top = 5;
		panel.add(toJL, c);

		c.gridx = 1;
		c.weightx = 1;
		tcBottom.setMinimumSize(tcSize);
		tcBottom.setPreferredSize(tcSize);
		panel.add(tcBottom, c);

		c.gridx = 0;
		c.gridy = 0;
		c.fill = GridBagConstraints.BOTH;
		priodPanel.add(panel, c);
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		cal.add(Calendar.SECOND, 0);
		setStartTime(cal);
	}

	public String getStartTime() {
		return tcTop.getTimeStr();
	}

	public String getEndTime() {
		return tcBottom.getTimeStr();
	}

	public void setStartTime(String time) {
		if (time.indexOf('-') > 0) {
			time = time.replaceAll("-", "/");
		}
		tcTop.setTimeStr(time);
	}

	public void setStartTime(Calendar cal) {
		String time = format.format(cal.getTime());
		if (time.indexOf('-') > 0) {
			time = time.replaceAll("-", "/");
		}
		tcTop.setTimeStr(time);
	}

	public void setEndTime(Calendar cal) {
		String time = format.format(cal.getTime());
		if (time.indexOf('-') > 0) {
			time = time.replaceAll("-", "/");
		}
		tcBottom.setTimeStr(time);
	}

	public void setEndTime(String time) {
		if (time.indexOf('-') > 0) {
			time = time.replaceAll("-", "/");
		}
		tcBottom.setTimeStr(time);
	}

	private String validateInput() throws ParseException {
		StringBuilder sb = new StringBuilder();
		SessionDetails s = sessioncomboBoxModel.getSelectedObject(cbSession
				.getSelectedIndex());
		boolean error = false;
		if (s == null) {
			sb.append("Please Select Session");
			error = true;
		} else {
			Date from = format.parse(tcTop.getTimeStr());
			Date to = format.parse(tcBottom.getTimeStr());
			if (from.after(to)) {
				sb.append("Invalid Search Period, To date should be graeter then from date");
				error = true;
			} else if (to.after(new Date())) {
				sb.append("Invalid Search Period, To date time should be less then from current date time");
				error = true;
			}
			/*
			 * else if (from.before(s.getStartDate()) ||
			 * to.after(s.getEndDate())) {
			 * sb.append("Search Period is Out of range of selected Session");
			 * sb.append("\n Selected Session: " + s.getDisplayString());
			 * sb.append("Start :" +
			 * ApplicationConstant.sdf_date.format(s.getStartDate()));
			 * sb.append("End :" +
			 * ApplicationConstant.sdf_date.format(s.getEndDate())); error =
			 * true; }
			 */
		}
		if (error) {
			return sb.toString();
		}
		return null;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (OperationConstant.REPORT_DEPOSITE_FEE.equals(e.getActionCommand())) {
			new SearchProgress();
		} else if (CommandConstant.CANCEL_COMMAND.equals(e.getActionCommand())) {
			dispose();
		}
	}

	class SearchProgress implements ProgressCommunicator {

		public SearchProgress() {
			ProgressCommunicatorMonitor communicatorMonitor = new ProgressCommunicatorMonitor(
					"Deposite Fee Search result loading...", true, SearchProgress.this);
			communicatorMonitor.start();
		}

		@Override
		public void run() {
			String error = null;
			try {
				error = validateInput();
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
			if (error != null) {
				MsgDialogUtils.showWarningDialog(DepositeFeeSearch.this, error);
			} else {
				try {
					Calendar fCal = Calendar.getInstance();
					Calendar toCal = Calendar.getInstance();
					fCal.setTime(format.parse(tcTop.getTimeStr()));
					toCal.setTime(format.parse(tcBottom.getTimeStr()));
					fCal.set(Calendar.HOUR, 0);
					fCal.set(Calendar.MINUTE, 0);
					fCal.set(Calendar.SECOND, 0);
					toCal.set(Calendar.HOUR, 23);
					toCal.set(Calendar.MINUTE, 59);
					toCal.set(Calendar.SECOND, 59);

					ClassDetails classDetails = classcomboBoxModel
							.getSelectedObject(cbClass.getSelectedIndex());
					if (classDetails != null && classDetails.getClassId() == 0) {
						classDetails = null;
					}
					SessionDetails sessionDetails = sessioncomboBoxModel
							.getSelectedObject(cbSession.getSelectedIndex());
					if (sessionDetails != null
							&& sessionDetails.getSessionId() == 0) {
						sessionDetails = null;
					}
					List<DepositeFeeMaster> feeMasters = HiebernateDboReprotUtil
							.getFeeMasterDetails(fCal.getTime(),
									toCal.getTime(), sessionDetails,
									classDetails);
					long amount = HiebernateDboReprotUtil.getTotalDepositeFee(
							fCal.getTime(), toCal.getTime(), sessionDetails,
							classDetails);
					new DepositeFeeSearchWindow(feeMasters, amount,
							ResourcesUtils.getString("FEE_COLLECTION_DETAILS")
									+ " From " + getStartTime() + " To "
									+ getEndTime());
					dispose();
				} catch (HibernateException ex) {

				} catch (ParseException ex) {

				}
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
