package com.school.fees;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import org.hibernate.HibernateException;

import com.banti.framework.core.ActionDispatcher;
import com.banti.framework.core.ClientListener;
import com.banti.framework.table.CustomMaintainTable;
import com.banti.framework.ui.DialogEsc;
import com.school.console.SchoolMain;
import com.school.hiebernate.HiebernateDboUtil;
import com.school.hiebernate.dbo.ClassDetails;
import com.school.hiebernate.dbo.SessionClassFeeDetails;
import com.school.hiebernate.dbo.SessionDetails;
import com.school.resource.CommandConstant;
import com.school.shared.CommandCodeConstant;
import com.school.utils.MsgDialogUtils;

public class SessionFeeDetailsAction extends DialogEsc  implements ActionListener,
		ClientListener {
	private SessionFeesDetailsTableModel sessionFeeDetailsTableModel;
	private CustomMaintainTable table;
	private JPopupMenu popupMenu;
	private JButton btnRegister;
	private JButton btnUpdate;
	private JButton btnDelete;
	private JMenuItem registerMenu;
	private JMenuItem updateMenu;
	private JMenuItem deleteMenu;
	private JDialog registerDialog;

	public SessionFeeDetailsAction() throws HibernateException {
		super(SchoolMain.Frame, true);
		setTitle("Session Class Fee Details");
		setSize(700, 350);
		setLocation(150, 150);
		setMinimumSize(new Dimension(400, 200));
		// this.dataList = getSessionList();
		List<SessionClassFeeDetails> classList = HiebernateDboUtil
				.getSessionFeeDetails();
		sessionFeeDetailsTableModel = new SessionFeesDetailsTableModel(
				classList);
		init();
	}

	private void init() {
		createPopupMenu();
		table = new CustomMaintainTable(SchoolMain.Frame,
				sessionFeeDetailsTableModel, sessionFeeDetailsTableModel);
		table.setDefaultRenderer(Object.class, sessionFeeDetailsTableModel);
		table.getTableHeader().setReorderingAllowed(false);
		table.setDefaultSelectedColumns(sessionFeeDetailsTableModel
				.getSetPrefColIndex());
		table.setPreferredColumns(sessionFeeDetailsTableModel
				.getSetPrefColIndex());
		table.setMinimumSize(new Dimension(150, 150));
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		// table.setLocation(100, 100);
		table.setColumnWidth(350, 100);
		table.setOpaque(true);
		sessionFeeDetailsTableModel.fireTableDataChanged();

		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3) {
					popupMenu.show(table, e.getX(), e.getY());
				}
				updateButton();
			}
		});
		JScrollPane scrollPane = new JScrollPane(table);

		Container con = getContentPane();
		con.setLayout(new BorderLayout());
		con.add(scrollPane, BorderLayout.CENTER);
		con.add(getButtonPanel(), BorderLayout.SOUTH);
		setButtonEnable(false);

	}

	private void updateButton() {
		if (table.getSelectedRow() != -1) {
			btnUpdate.setEnabled(true);
			btnDelete.setEnabled(true);
			updateMenu.setEnabled(true);
			deleteMenu.setEnabled(true);

		}
	}

	private void setButtonEnable(boolean enable) {
		// btnRegister.setEnabled(enable);
		btnUpdate.setEnabled(enable);
		btnDelete.setEnabled(enable);
		updateMenu.setEnabled(enable);
		deleteMenu.setEnabled(enable);
	}

	private JPanel getButtonPanel() {
		JPanel panel = new JPanel(new FlowLayout());
		btnRegister = new JButton("Register");
		btnRegister.setActionCommand(CommandConstant.REGISTER_COMMAND);
		btnRegister.addActionListener(this);

		btnUpdate = new JButton("Update");
		btnUpdate.setActionCommand(CommandConstant.UPDATE_COMMAND);
		btnUpdate.addActionListener(this);

		btnDelete = new JButton("Delete");
		btnDelete.setActionCommand(CommandConstant.DELETE_COMMAND);
		btnDelete.addActionListener(this);

		panel.add(btnRegister);
		panel.add(btnUpdate);
		panel.add(btnDelete);
		btnRegister.setFocusable(true);
		return panel;
	}

	private void createPopupMenu() {
		popupMenu = new JPopupMenu();
		registerMenu = new JMenuItem("Register");
		registerMenu.setActionCommand(CommandConstant.REGISTER_COMMAND);
		registerMenu.addActionListener(this);
		popupMenu.add(registerMenu);
		updateMenu = new JMenuItem("Update");
		updateMenu.setActionCommand(CommandConstant.UPDATE_COMMAND);
		updateMenu.addActionListener(this);
		popupMenu.add(updateMenu);
		deleteMenu = new JMenuItem("Delete");
		deleteMenu.setActionCommand(CommandConstant.DELETE_COMMAND);
		deleteMenu.addActionListener(this);
		popupMenu.add(deleteMenu);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (CommandConstant.REGISTER_COMMAND.equals(e.getActionCommand())) {
			try {
				List<SessionDetails> dbSessionList = HiebernateDboUtil
						.getSessionDetails();
				List<ClassDetails> dbClassList = HiebernateDboUtil
						.getClassDetails();

				if (dbSessionList.size() == 0) {
					SchoolMain
							.showWarningDialog("Session is not registered, First registered Session info. ");
					return;
				}
				if (dbClassList.size() == 0) {
					SchoolMain
							.showWarningDialog("Class is not registered, First registered Class info. ");
					return;
				}
				ActionDispatcher.registeredListener(
						CommandCodeConstant.SESSION_FEE_REG_CLOSE, this);
				registerDialog = new SessionFeeRegistrationAction(this,
						dbClassList, dbSessionList);
				registerDialog.setLocationRelativeTo(this);
				registerDialog.setVisible(true);
			} catch (HibernateException ex) {
				SchoolMain.showWarningDialog("Database is down !");
			}
		} else if (CommandConstant.UPDATE_COMMAND.equals(e.getActionCommand())) {
			List<SessionDetails> dbSessionList = HiebernateDboUtil
					.getSessionDetails();
			List<ClassDetails> dbClassList = HiebernateDboUtil
					.getClassDetails();

			if (dbSessionList.size() == 0) {
				SchoolMain
						.showWarningDialog("Session is not registered, First registered Session info. ");
				return;
			}
			if (dbClassList.size() == 0) {
				SchoolMain
						.showWarningDialog("Class is not registered, First registered Class info. ");
				return;
			}

			ActionDispatcher.registeredListener(
					CommandCodeConstant.SESSION_FEE_MOD_CLOSE, this);
			SessionClassFeeDetails c = sessionFeeDetailsTableModel.get(table
					.getSelectedRow());
			registerDialog = new SessionFeeRegistrationAction(this,
					dbClassList, dbSessionList, c);
			registerDialog.setLocationRelativeTo(this);
			registerDialog.setVisible(true);
		} else if (CommandConstant.DELETE_COMMAND.equals(e.getActionCommand())) {
			deleteRecord();
		}
	}

	private void deleteRecord() {
		int i = JOptionPane.showConfirmDialog(this, "Do You want to delete ? ",
				"Confirmation", JOptionPane.YES_NO_OPTION,
				JOptionPane.INFORMATION_MESSAGE);
		if (JOptionPane.YES_OPTION == i) {
			SessionClassFeeDetails sessionClassFeeDetails = sessionFeeDetailsTableModel
					.get(table.getSelectedRow());
			sessionClassFeeDetails.setDeleteDate(new Date());
			boolean result = false;
			try {
				result = HiebernateDboUtil
						.deleteSessionFeeDetails(sessionClassFeeDetails);
			} catch (HibernateException exp) {
				MsgDialogUtils.showWarningDialog(this,
						"Database error occured! ");
				return;
			}
			if (result) {
				MsgDialogUtils.showInformationDialog(this,
						"Session Fee Details has been deleted Successfully!");
				if (table.getSelectedRow() != -1) {
					sessionFeeDetailsTableModel.removeDataElement(table
							.getSelectedRow());
				} else {
					sessionFeeDetailsTableModel.setDataList(HiebernateDboUtil
							.getSessionFeeDetails());
				}

			} else {
				MsgDialogUtils
						.showWarningDialog(this, "Unknown Error occured!");
			}
		}
	}

	@Override
	public void fireAction(String actionCMD, Object obj) {
		if (CommandCodeConstant.SESSION_FEE_REG_CLOSE.equals(actionCMD)) {
			if (registerDialog != null) {
				registerDialog.dispose();
				sessionFeeDetailsTableModel
						.addDataElement((SessionClassFeeDetails) obj);
			}
		} else if (CommandCodeConstant.SESSION_FEE_MOD_CLOSE.equals(actionCMD)) {
			if (registerDialog != null) {
				registerDialog.dispose();
				if (table.getSelectedRow() != -1) {
					sessionFeeDetailsTableModel.setDataElement(
							table.getSelectedRow(),
							(SessionClassFeeDetails) obj);
				} else {
					sessionFeeDetailsTableModel.setDataList(HiebernateDboUtil
							.getSessionFeeDetails());
				}
			}
		}
	}

}
