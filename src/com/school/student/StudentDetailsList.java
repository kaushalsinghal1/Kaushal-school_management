package com.school.student;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import org.hibernate.HibernateException;

import com.banti.framework.core.ActionDispatcher;
import com.banti.framework.core.ApplicationOpertaion;
import com.banti.framework.core.ClientListener;
import com.banti.framework.cwt.ProgressCommunicator;
import com.banti.framework.cwt.ProgressCommunicatorMonitor;
import com.banti.framework.platform.AbstractSchoolAction;
import com.banti.framework.table.CustomMaintainTable;
import com.banti.framework.tree.AbstractTreeModel;
import com.banti.framework.tree.SearchNode;
import com.banti.framework.ui.MasterBrowser;
import com.school.combobox.model.SessionComboBoxModel;
import com.school.console.SchoolMain;
import com.school.constant.OperationConstant;
import com.school.fees.StudentFeeMasterDetails;
import com.school.fees.search.DefaultSearchCommandSchema;
import com.school.fees.search.StudentSearchDialog;
import com.school.hiebernate.DBOFacotry;
import com.school.hiebernate.HiebernateDboUtil;
import com.school.hiebernate.HiebernateStudentDboUtil;
import com.school.hiebernate.dbo.ClassDetails;
import com.school.hiebernate.dbo.SessionDetails;
import com.school.hiebernate.dbo.StudentDetails;
import com.school.hiebernate.dbo.StudentFeeDetails;
import com.school.hiebernate.dbo.StudentImageDetails;
import com.school.resource.ResourcesUtils;
import com.school.utils.Logger;
import com.school.utils.MsgDialogUtils;

public class StudentDetailsList extends MasterBrowser implements
		TreeSelectionListener, ActionListener, ClientListener,
		ProgressCommunicator {
	private StudentDetailsTableModel studentDetailsTableModel;
	private SessionComboBoxModel sessionComboBoxModel;
	private JButton btnRegister;
	private JButton btnUpdate;
	private AbstractButton btnDelete;
	private AbstractSchoolAction registerMenuAction;
	private AbstractSchoolAction updateMenuAction;
	private AbstractSchoolAction deleteMenuAction;
	private JPopupMenu popupMenu;
	private CustomMaintainTable table;
	private JComboBox cbSession;
	private JTree tree;
	private ClassDetails selectedClass;
	private SessionDetails selectedSession;
	private boolean first = false;
	private StudentFeeMasterDetails feeMasterDetails;
	private AbstractTreeModel treeModel;
	private JButton btnServiceButton;
	private JPopupMenu servicePopUp;
	private AbstractSchoolAction depositeFeeDetailsAction;
	private AbstractSchoolAction depositeFeeAction;
	private AbstractSchoolAction studentIdCardAction;
	private AbstractSchoolAction searchMenuAction;
	private StudentSearchDialog studentSearchDialog;
	private static final String SEARCH_DIALOG = "student.search";
	private List<StudentDetails> searchResult;
	private ProgressCommunicatorMonitor monitor;
	private AdmissionFromExistingAction admissionFromExistingAction;

	public StudentDetailsList() {
		this(false);
	}

	public StudentDetailsList(boolean search) {
		super(ResourcesUtils.getString("STUDENT_DETAILS_LIST"));
		first = true;
		monitor = new ProgressCommunicatorMonitor("Students Details loading..",
				true, this);
		if (search) {
			setFramesize(SchoolMain.Frame);
			setVisible(true);
			studentSearchDialog = new StudentSearchDialog(this);
			studentSearchDialog.setVisible(true);
		} else {
			loadList(search);
		}
	}

	public void showSearchDialog() {
		studentSearchDialog = new StudentSearchDialog(this);
		studentSearchDialog.setVisible(true);
	}

	@Override
	protected JScrollPane getTableScrollPane() {
		createPopupMenu();
		List<StudentDetails> studentDetails = new ArrayList<StudentDetails>();
		studentDetailsTableModel = new StudentDetailsTableModel(studentDetails);
		table = new CustomMaintainTable(SchoolMain.Frame,
				studentDetailsTableModel, studentDetailsTableModel);
		table.setDefaultRenderer(Object.class, studentDetailsTableModel);
		table.getTableHeader().setReorderingAllowed(false);
		table.setDefaultSelectedColumns(studentDetailsTableModel
				.getSetPrefColIndex());
		table.setPreferredColumns(studentDetailsTableModel
				.getSET_PREF_DIS_COL_INDEX());
		table.setMinimumSize(new Dimension(250, 150));
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		// table.setLocation(100, 100);
		table.setColumnWidth(300, 100);

		studentDetailsTableModel.fireTableDataChanged();

		table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		table.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					@Override
					public void valueChanged(ListSelectionEvent arg0) {
						updateButton();
						if (secondLayed && feeMasterDetails != null) {
							int rows[] = table.getSelectedRows();
							if (rows.length > 0) {
								feeMasterDetails
										.setStudentFeeDetails(studentDetailsTableModel
												.getList(table
														.getSelectedRows()));
							}
						}
					}
				});

		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3) {
					popupMenu.show(table, e.getX(), e.getY());
				}
			}
		});
		JScrollPane scrollPane = new JScrollPane(table);
		loadList();
		return scrollPane;

	}

	@Override
	protected Action getSearchAction() {
		DefaultSearchCommandSchema commandSchema = new DefaultSearchCommandSchema(
				ResourcesUtils.getString("SEARCH"), SEARCH_DIALOG);
		searchMenuAction = new AbstractSchoolAction(
				commandSchema.getDISPLAY_NAME(), commandSchema.getIcon(),
				commandSchema.getCOMMANDSTRING()) {

			@Override
			public void actionPerformed(ActionEvent e) {
				StudentDetailsList.this.actionPerformed(e);

			}
		};
		return searchMenuAction;
	}

	/*
	 * private void setSearchMenuAction() { DefaultSearchCommandSchema
	 * commandSchema = new DefaultSearchCommandSchema(
	 * ResourcesUtils.getString("SEARCH"), SEARCH_DIALOG); searchMenuAction =
	 * new AbstractSchoolAction( commandSchema.getDISPLAY_NAME(),
	 * commandSchema.getIcon(), commandSchema.getCOMMANDSTRING()) {
	 * 
	 * @Override public void actionPerformed(ActionEvent e) {
	 * StudentDetailsList.this.actionPerformed(e);
	 * 
	 * } }; super.setSearchMenuAction(searchMenuAction); }
	 */

	@Override
	protected JScrollPane getTreeScrollPane() {
		List classList = null;
		try {
			classList = HiebernateDboUtil.getClassDetails();
		} catch (HibernateException ex) {
			classList = new ArrayList<ClassDetails>();
			Logger.DEBUG.log(Level.WARNING,
					"Error Occured while getting class list");
			Logger.EXCEPTION.log(Level.WARNING,
					"Error Occured while getting class list", ex);
		}
		treeModel = new ClassTreeModel(classList, this);
		tree = treeModel.getTree();
		treeModel.setDefaultSelectionRow(1);
		return new JScrollPane(tree);
	}

	@Override
	protected JPanel getTopPanel() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel lb = new JLabel("Select Session");
		sessionComboBoxModel = new SessionComboBoxModel();
		cbSession = new JComboBox(sessionComboBoxModel);
		cbSession
				.setActionCommand(OperationConstant.STUDENT_LIST_SESSION_CHANGE);
		cbSession.addActionListener(this);
		cbSession.setPreferredSize(new Dimension(200, 25));
		lb.setLabelFor(cbSession);
		panel.add(lb);
		panel.add(cbSession);
		return panel;
	}

	@Override
	protected JPanel getButtonPanel() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		btnRegister = new JButton(ResourcesUtils.getString("REGISTER"));
		btnRegister.setActionCommand(OperationConstant.STUDENT_REGISTER);
		btnRegister.addActionListener(this);

		btnUpdate = new JButton(ResourcesUtils.getString("UPDATE"));
		btnUpdate.setActionCommand(OperationConstant.STUDENT_UPDATE);
		btnUpdate.addActionListener(this);

		btnDelete = new JButton(ResourcesUtils.getString("DELETE"));
		btnDelete.setActionCommand(OperationConstant.STUDENT_DELETE);
		btnDelete.addActionListener(this);
		btnServiceButton = new JButton(
				ResourcesUtils.getString("SERVICE_BUTTON"));

		btnServiceButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					servicePopUp.show(btnServiceButton, e.getX(), e.getY());
				}
			}
		});

		panel.add(btnRegister);
		panel.add(btnUpdate);
		panel.add(btnDelete);
		panel.add(btnServiceButton);
		btnRegister.setFocusable(true);
		return panel;
	}

	private void initExecuteMenu() {
		List<Action> actions = new ArrayList<Action>();
		actions.add(registerMenuAction);
		actions.add(updateMenuAction);
		actions.add(deleteMenuAction);
		actions.add(depositeFeeAction);
		actions.add(depositeFeeDetailsAction);
		actions.add(admissionFromExistingAction);
		actions.add(studentIdCardAction);
		setExecuteMenuItem(actions);
	}

	@SuppressWarnings("serial")
	private void createPopupMenu() {
		popupMenu = new JPopupMenu();
		registerMenuAction = new AbstractSchoolAction(
				ResourcesUtils.getString("REGISTER"),
				OperationConstant.STUDENT_REGISTER) {

			@Override
			public void actionPerformed(ActionEvent e) {
				StudentDetailsList.this.actionPerformed(e);

			}
		};
		registerMenuAction.setEnabled(true);
		popupMenu.add(registerMenuAction);
		updateMenuAction = new AbstractSchoolAction(
				ResourcesUtils.getString("UPDATE"),
				OperationConstant.STUDENT_UPDATE) {

			@Override
			public void actionPerformed(ActionEvent e) {
				StudentDetailsList.this.actionPerformed(e);

			}
		};
		popupMenu.add(updateMenuAction);
		deleteMenuAction = new AbstractSchoolAction(
				ResourcesUtils.getString("DELETE"),
				OperationConstant.STUDENT_DELETE) {

			@Override
			public void actionPerformed(ActionEvent e) {
				StudentDetailsList.this.actionPerformed(e);

			}
		};
		popupMenu.add(deleteMenuAction);

		servicePopUp = new JPopupMenu();
		// depositeFee.setAction(new)
		depositeFeeDetailsAction = new AbstractSchoolAction(
				ResourcesUtils.getString("OPEN_DEPOSITED_FEE_DETAILS"),
				OperationConstant.STUDENT_FEE_DIPOSITE_LIST) {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (secondLayed) {
					removeSecondLayed();
					depositeFeeDetailsAction.putValue(Action.NAME,
							ResourcesUtils
									.getString("OPEN_DEPOSITED_FEE_DETAILS"));
				} else {
					feeMasterDetails = new StudentFeeMasterDetails(
							studentDetailsTableModel.get(table.getSelectedRow()));
					addSecondLayed(feeMasterDetails);
					depositeFeeDetailsAction.putValue(Action.NAME,
							ResourcesUtils
									.getString("CLOSE_DEPOSITED_FEE_DETAILS"));
				}
			}
		};
		depositeFeeDetailsAction.setEnabled(true);
		depositeFeeAction = new AbstractSchoolAction(
				ResourcesUtils.getString("DEPOSITE_FEE"),
				OperationConstant.STUDENT_FEE_DIPOSITE) {

			@Override
			public void actionPerformed(ActionEvent e) {
				StudentDetailsList.this.actionPerformed(e);
			}
		};
		studentIdCardAction = new AbstractSchoolAction(
				ResourcesUtils.getString("ID_CARD"),
				OperationConstant.STUDENT_ID_CARD) {
			@Override
			public void actionPerformed(ActionEvent e) {
				StudentDetailsList.this.actionPerformed(e);
			}
		};
		admissionFromExistingAction = new AdmissionFromExistingAction();
		admissionFromExistingAction.setEnabled(false);
		studentIdCardAction.setEnabled(false);
		depositeFeeAction.setEnabled(true);
		popupMenu.add(depositeFeeAction);
		popupMenu.add(depositeFeeDetailsAction);
		popupMenu.add(admissionFromExistingAction);
		servicePopUp.add(depositeFeeAction);
		servicePopUp.add(depositeFeeDetailsAction);
		servicePopUp.add(admissionFromExistingAction);
		popupMenu.add(studentIdCardAction);
		initExecuteMenu();
	}

	private void setButtonEnable(boolean enable) {
		boolean enable1 = enable;
		if (!(enable && table.getSelectedRowCount() == 1)) {
			enable = false;
		}
		btnUpdate
				.setEnabled(enable
						&& ApplicationOpertaion.isAllowed(btnUpdate
								.getActionCommand()));
		btnDelete
				.setEnabled(enable
						&& ApplicationOpertaion.isAllowed(btnDelete
								.getActionCommand()));
		updateMenuAction.setEnabled(enable);
		deleteMenuAction.setEnabled(enable);
		depositeFeeAction.setEnabled(enable);
		studentIdCardAction.setEnabled(enable);
		depositeFeeDetailsAction.setEnabled(enable1);
	}

	private void updateButton() {
		if (table.getSelectedRow() != -1) {
			setButtonEnable(true);
		}
		admissionFromExistingAction.setStudents(studentDetailsTableModel
				.getList(table.getSelectedRows()));
	}

	@Override
	public void valueChanged(TreeSelectionEvent e) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree
				.getLastSelectedPathComponent();

		if (node == null)
			return;
		Object nodeInfo = node.getUserObject();
		if (nodeInfo instanceof ClassDetails) {
			selectedClass = (ClassDetails) nodeInfo;
			Logger.DEBUG.info("Selected node -->" + nodeInfo.toString());
		} else if (nodeInfo instanceof SearchNode) {
			if (searchResult != null) {
				studentDetailsTableModel.setDataList(searchResult);
				return;
			}
		} else {
			Logger.DEBUG.info("Unkown type of node selected");
		}
		loadList();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (OperationConstant.STUDENT_LIST_SESSION_CHANGE.equals(e
				.getActionCommand())) {
			selectedSession = sessionComboBoxModel.getSelectedObject(cbSession
					.getSelectedIndex());
			loadList();
			treeModel.removeSearchNode();
			searchResult = null;
		} else if (OperationConstant.STUDENT_REGISTER.equals(e
				.getActionCommand())) {
			ActionDispatcher.registeredListener(
					OperationConstant.STUDENT_REGISTER, this);
			new StudentRegistrationAction();

		} else if (OperationConstant.STUDENT_UPDATE
				.equals(e.getActionCommand())) {
			ActionDispatcher.registeredListener(
					OperationConstant.STUDENT_UPDATE, this);
			new StudentRegistrationAction(studentDetailsTableModel.get(table
					.getSelectedRow()));
		} else if (OperationConstant.STUDENT_DELETE
				.equals(e.getActionCommand())) {
			if (table.getSelectedRow() > -1) {
				int i = JOptionPane.showConfirmDialog(this,
						"Do yout really want to Delete ?");
				if (JOptionPane.OK_OPTION == i) {
					try {
						StudentDetails s = studentDetailsTableModel.get(table
								.getSelectedRow());
						s.setDeleteDate(new Date());
						HiebernateStudentDboUtil.deleteStudentDetails(s);
						List<StudentFeeDetails> list = HiebernateStudentDboUtil
								.getStudentFeeDetails(s);
						StudentFeeDetails studentFeeDetails = list.size() > 0 ? list
								.get(0) : null;
						if (studentFeeDetails != null) {
							studentFeeDetails.setDeleteDate(new Date());
							HiebernateStudentDboUtil
									.deleteStudentFeeDetails(studentFeeDetails);
						}
						studentDetailsTableModel.removeDataElement(table
								.getSelectedRow());
						MsgDialogUtils.showInformationDialog(this,
								"Student is deleted successfully");
					} catch (HibernateException ex) {
						MsgDialogUtils.showWarningDialog(
								this,
								"Data base Error occured"
										+ ex.getLocalizedMessage());
						Logger.EXCEPTION.log(Level.WARNING,
								"Error Occured while deleting student", ex);
					}
				}
			}
		} else if (OperationConstant.STUDENT_FEE_DIPOSITE.equals(e
				.getActionCommand())) {
			new StudentRegistrationAction(studentDetailsTableModel.get(table
					.getSelectedRow()), "Student Deposit Fee", 3);
		} else if (OperationConstant.STUDENT_ID_CARD.equals(e
				.getActionCommand())) {
			StudentImageDetails imageDetails = studentDetailsTableModel.get(
					table.getSelectedRow()).getStudentImageDetails();
			if (imageDetails != null && imageDetails.getPhoto() != null) {
				StudentIdCardImage cardImage = new StudentIdCardImage(
						studentDetailsTableModel.get(table.getSelectedRow()));
			} else {
				MsgDialogUtils.showInformationDialog(this,
						"First Upload photo for the student");
			}
		} else if (SEARCH_DIALOG.equals(e.getActionCommand())) {
			if (studentSearchDialog == null) {
				studentSearchDialog = new StudentSearchDialog(this);
				studentSearchDialog.setVisible(true);
			} else {
				studentSearchDialog.setVisible(true);
			}

		} else if (StudentSearchDialog.SEARCH_CMD.equals(e.getActionCommand())) {
			if (studentSearchDialog != null) {
				new SearchProgress();
			}
		}
	}

	public void loadList(boolean search) {
		if (!search) {
			loadList();
		}
	}

	@Override
	public void loadList() {
		if (!first) {
			return;
		}
		monitor.start();
		/*
		 * removeSecondLayed(); depositeFeeDetailsAction.putValue(Action.NAME,
		 * ResourcesUtils.getString("OPEN_DEPOSITED_FEE_DETAILS"));
		 * setButtonEnable(false); if (selectedClass == null && treeModel !=
		 * null) { selectedClass = (ClassDetails) treeModel.defaultObject(); }
		 * if (selectedSession == null) { selectedSession =
		 * sessionComboBoxModel.getSelectedObject(cbSession
		 * .getSelectedIndex()); } List<StudentDetails> studentDetails =
		 * HiebernateStudentDboUtil .getStudentDetails(selectedClass,
		 * selectedSession);
		 * studentDetailsTableModel.setDataList(studentDetails);
		 */
	}

	@Override
	public void fireAction(String actionCMD, Object obj) {
		if (OperationConstant.STUDENT_REGISTER.equals(actionCMD)) {
			studentDetailsTableModel.addDataElement((StudentDetails) obj);
		} else if (OperationConstant.STUDENT_UPDATE.equals(actionCMD)) {
			studentDetailsTableModel.setDataElement(table.getSelectedRow(),
					((StudentDetails) obj));
		}
	}

	@Override
	public void run() {
		removeSecondLayed();
		depositeFeeDetailsAction.putValue(Action.NAME,
				ResourcesUtils.getString("OPEN_DEPOSITED_FEE_DETAILS"));
		setButtonEnable(false);
		if (selectedClass == null && treeModel != null) {
			selectedClass = (ClassDetails) treeModel.defaultObject();
		}
		if (selectedSession == null && cbSession.getSelectedIndex() != -1) {
			selectedSession = sessionComboBoxModel.getSelectedObject(cbSession
					.getSelectedIndex());
		}
		List<StudentDetails> studentDetails = HiebernateStudentDboUtil
				.getStudentDetails(selectedClass, selectedSession);
		studentDetailsTableModel.setDataList(studentDetails);
	}

	@Override
	public void doPostProcess() {

	}

	@Override
	public void doCancel() {

	}

	class SearchProgress implements ProgressCommunicator {

		public SearchProgress() {
			ProgressCommunicatorMonitor communicatorMonitor = new ProgressCommunicatorMonitor(
					"Loading Search result...", true, SearchProgress.this);
			communicatorMonitor.start();
		}

		@Override
		public void run() {
			if (studentSearchDialog != null) {
				try {
					String fName = studentSearchDialog.getFirstName();
					String lName = studentSearchDialog.getLastName();
					String fatherName = studentSearchDialog.getFatherName();
					ClassDetails c = studentSearchDialog.getClassDetails();
					SessionDetails s = studentSearchDialog.getSessionDetails();
					studentSearchDialog.dispose();
					List<StudentDetails> studentDetails = DBOFacotry
							.getSearchDBManager().getStudentDetails(fName,
									lName, fatherName, c, s);
					if (studentDetails == null || studentDetails.size() == 0) {
						MsgDialogUtils.showInformationDialog(
								StudentDetailsList.this,
								"No Records found for the search condition");
					}
					searchResult = studentDetails;
					treeModel.addSearchNode();
				} catch (RuntimeException exception) {
					MsgDialogUtils.showWarningDialog(StudentDetailsList.this,
							"DB Error occred While Searching");
					Logger.EXCEPTION.log(Level.WARNING,
							"DB Error while searching", exception);
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
