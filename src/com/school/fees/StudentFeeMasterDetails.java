package com.school.fees;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import org.hibernate.HibernateException;

import com.banti.framework.table.CustomMaintainTable;
import com.school.console.SchoolMain;
import com.school.fees.report.ReciptModel;
import com.school.fees.report.ReciptWindow;
import com.school.hiebernate.HiebernateStudentDboUtil;
import com.school.hiebernate.dbo.DepositeFeeMaster;
import com.school.hiebernate.dbo.StudentDetails;
import com.school.resource.CommandConstant;
import com.school.utils.Logger;

public class StudentFeeMasterDetails extends JPanel implements ActionListener {
	private StudentFeesMasterDetailsTableModel feesMasterDetailsTableModel;
	private CustomMaintainTable table;
	private JPopupMenu popupMenu;
	private AbstractAction viewReciept;

	public StudentFeeMasterDetails(List<DepositeFeeMaster> depositeFeeMaster) {
		feesMasterDetailsTableModel = new StudentFeesMasterDetailsTableModel(
				depositeFeeMaster);
		init();
	}

	public StudentFeeMasterDetails(StudentDetails studentDetails) {
		List<DepositeFeeMaster> feeMasters = null;
		try {
			feeMasters = HiebernateStudentDboUtil
					.getDepositeFeeMasterDetails(studentDetails);
		} catch (HibernateException ex) {
			Logger.DEBUG.log(Level.WARNING,
					"DB Error occured while get Fee Master Details for Student: "
							+ studentDetails.getRegistrationNumber());
			Logger.EXCEPTION.log(Level.WARNING,
					"DB Error occured while get Fee Master Details for Student: "
							+ studentDetails.getRegistrationNumber(), ex);
			feeMasters = new ArrayList<DepositeFeeMaster>();
		}
		feesMasterDetailsTableModel = new StudentFeesMasterDetailsTableModel(
				feeMasters);
		init();
	}

	public void setStudentFeeDetails(StudentDetails studentDetails) {
		List<DepositeFeeMaster> feeMasters = null;
		try {
			feeMasters = HiebernateStudentDboUtil
					.getDepositeFeeMasterDetails(studentDetails);
		} catch (HibernateException ex) {
			Logger.DEBUG.log(Level.WARNING,
					"DB Error occured while get Fee Master Details for Student: "
							+ studentDetails.getRegistrationNumber());
			Logger.EXCEPTION.log(Level.WARNING,
					"DB Error occured while get Fee Master Details for Student: "
							+ studentDetails.getRegistrationNumber(), ex);
			feeMasters = new ArrayList<DepositeFeeMaster>();
		}
		setFeeMasterDetails(feeMasters);
	}

	public List<DepositeFeeMaster> getStudentFeeDetails(
			StudentDetails studentDetails) {
		List<DepositeFeeMaster> feeMasters = null;
		try {
			feeMasters = HiebernateStudentDboUtil
					.getDepositeFeeMasterDetails(studentDetails);
		} catch (HibernateException ex) {
			Logger.DEBUG.log(Level.WARNING,
					"DB Error occured while get Fee Master Details for Student: "
							+ studentDetails.getRegistrationNumber());
			Logger.EXCEPTION.log(Level.WARNING,
					"DB Error occured while get Fee Master Details for Student: "
							+ studentDetails.getRegistrationNumber(), ex);
			feeMasters = new ArrayList<DepositeFeeMaster>();
		}
		return feeMasters;
	}

	public void setStudentFeeDetails(List<StudentDetails> studentDetailsList) {
		List<DepositeFeeMaster> feeMasters = new ArrayList<DepositeFeeMaster>();
		for (Iterator<StudentDetails> iterator = studentDetailsList.iterator(); iterator
				.hasNext();) {
			StudentDetails studentDetails = iterator.next();
			List<DepositeFeeMaster> feeMaster = getStudentFeeDetails(studentDetails);
			if (feeMaster != null) {
				feeMasters.addAll(feeMaster);
			}
		}
		setFeeMasterDetails(feeMasters);
		/*
		 * try { feeMasters = HiebernateStudentDboUtil
		 * .getDepositeFeeMasterDetails(studentDetails); } catch
		 * (HibernateException ex) { Logger.DEBUG.log(Level.WARNING,
		 * "DB Error occured while get Fee Master Details for Student: " +
		 * studentDetails.getRegistrationNumber());
		 * Logger.EXCEPTION.log(Level.WARNING,
		 * "DB Error occured while get Fee Master Details for Student: " +
		 * studentDetails.getRegistrationNumber(), ex); feeMasters = new
		 * ArrayList<DepositeFeeMaster>(); } setFeeMasterDetails(feeMasters);
		 */
	}

	public void setFeeMasterDetails(List<DepositeFeeMaster> depositeFeeMaster) {
		feesMasterDetailsTableModel.setDataList(depositeFeeMaster);
	}

	private void init() {
		createPopupMenu();

		table = new CustomMaintainTable(SchoolMain.Frame,
				feesMasterDetailsTableModel, feesMasterDetailsTableModel);
		table.setDefaultRenderer(Object.class, feesMasterDetailsTableModel);
		table.getTableHeader().setReorderingAllowed(false);
		table.setDefaultSelectedColumns(feesMasterDetailsTableModel
				.getSetPrefColIndex());
		table.setPreferredColumns(feesMasterDetailsTableModel
				.getSetPrefColIndex());
		table.setMinimumSize(new Dimension(150, 150));
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		// table.setLocation(100, 100);
		table.setColumnWidth(350, 100);
		table.setOpaque(true);
		feesMasterDetailsTableModel.fireTableDataChanged();

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

		setLayout(new BorderLayout());
		add(scrollPane, BorderLayout.CENTER);
		add(getButtonPanel(), BorderLayout.SOUTH);
		setButtonEnable(false);
	}

	private void updateButton() {
		if (table.getSelectedRow() != -1) {
			viewReciept.setEnabled(true);

		}
	}

	private void setButtonEnable(boolean enable) {
		// btnRegister.setEnabled(enable);
		viewReciept.setEnabled(enable);
	}

	private JPanel getButtonPanel() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JButton reciept = new JButton(viewReciept);
		panel.add(reciept);
		reciept.setFocusable(true);
		return panel;
	}

	private void createPopupMenu() {
		viewReciept = new AbstractAction("Deposite Fees Reciept") {

			@Override
			public void actionPerformed(ActionEvent e) {
				new ReciptWindow(
						new ReciptModel(feesMasterDetailsTableModel.get(table
								.getSelectedRow())));

			}
		};
		popupMenu = new JPopupMenu();
		JMenuItem reciept = new JMenuItem(viewReciept);
		popupMenu.add(reciept);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (CommandConstant.REGISTER_COMMAND.equals(e.getActionCommand())) {
			new ReciptWindow(new ReciptModel(
					feesMasterDetailsTableModel.get(table.getSelectedRow())));
		} else if (CommandConstant.UPDATE_COMMAND.equals(e.getActionCommand())) {

		}

	}

}
