package com.school.fees.search;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.banti.framework.core.SchoolOpertaionAction;
import com.banti.framework.platform.InternalWindow;
import com.banti.framework.table.CustomMaintainTable;
import com.school.console.SchoolMain;
import com.school.constant.OperationConstant;
import com.school.hiebernate.dbo.StudentFeeDetails;
import com.school.resource.ResourcesUtils;
import com.school.student.FeeDetailsDialog;
import com.school.student.StudentRegistrationAction;
import com.school.utils.MsgDialogUtils;

public class StudentDueFeeSearchWindow extends InternalWindow implements
		ActionListener {
	private StudentDueFeeDetailsTableModel dueFeesDetailsTableModel;
	private CustomMaintainTable table;
	private JPopupMenu popupMenu;
	private SchoolOpertaionAction viewdetails;
	private SchoolOpertaionAction viewStudentdetails;
	private SchoolOpertaionAction csvOutput;
	private long totalamount = 0;
	private final String FEE_DETAILS = "FEE_DETAILS";
	private final String STUDENT_DETAILS = "STUDENT_DETAILS";

	public StudentDueFeeSearchWindow(List<StudentFeeDetails> list, String title) {
		super(title, true, true, true, true);
		dueFeesDetailsTableModel = new StudentDueFeeDetailsTableModel(list);
		this.totalamount = calculateamount(list);
		init();
		setSize(1100, 600);
		setVisible(true);
	}

	private int calculateamount(List<StudentFeeDetails> list) {
		int amount = 0;
		if (list != null) {
			for (StudentFeeDetails feeDetails : list) {
				amount += feeDetails.getTotalDueFee();
			}
		}
		return amount;
	}

	private void init() {
		createPopupMenu();

		table = new CustomMaintainTable(SchoolMain.Frame,
				dueFeesDetailsTableModel, dueFeesDetailsTableModel);
		table.setDefaultRenderer(Object.class, dueFeesDetailsTableModel);
		table.getTableHeader().setReorderingAllowed(false);
		table.setDefaultSelectedColumns(dueFeesDetailsTableModel
				.getSetPrefColIndex());
		table.setPreferredColumns(dueFeesDetailsTableModel.getSetPrefColIndex());
		table.setMinimumSize(new Dimension(150, 150));
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		// table.setLocation(100, 100);
		table.setColumnWidth(350, 100);
		table.setOpaque(true);
		dueFeesDetailsTableModel.fireTableDataChanged();

		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		table.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					@Override
					public void valueChanged(ListSelectionEvent arg0) {
						updateButton();
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

		setLayout(new BorderLayout());
		add(scrollPane, BorderLayout.CENTER);
		add(getButtonPanel(), BorderLayout.SOUTH);
		setButtonEnable(false);
	}

	private void updateButton() {
		if (table.getSelectedRow() != -1) {
			viewdetails.setEnabled(true);
			viewStudentdetails.setEnabled(true);

		}
	}

	private void setButtonEnable(boolean enable) {
		// btnRegister.setEnabled(enable);
		viewdetails.setEnabled(enable);
	}

	private JPanel getButtonPanel() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JButton btnCSV = new JButton(csvOutput);
		JButton reciept = new JButton(viewdetails);
		panel.add(btnCSV);
		panel.add(reciept);
		panel.add(new JButton(viewStudentdetails));
		reciept.setFocusable(true);
		panel.add(new JLabel("Total Due Amount: "));
		JLabel lb = new JLabel(totalamount + "  ");
		lb.setBackground(Color.pink);
		lb.setOpaque(true);
		panel.add(lb);
		return panel;
	}

	private void createPopupMenu() {
		viewdetails = new SchoolOpertaionAction("View Details", FEE_DETAILS);
		viewdetails.registerActionListener(this);

		viewStudentdetails = new SchoolOpertaionAction("Student Details",
				STUDENT_DETAILS);
		viewStudentdetails.registerActionListener(this);
		viewStudentdetails.setEnabled(false);
		csvOutput = new SchoolOpertaionAction(
				ResourcesUtils.getString("CSV_OUTPUT"),
				OperationConstant.DUE_FEE_CSV_OUTPUT);
		csvOutput.setEnabled(true);
		csvOutput.registerActionListener(this);
		popupMenu = new JPopupMenu();
		popupMenu.add(viewdetails);
		popupMenu.add(viewStudentdetails);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (FEE_DETAILS.equals(e.getActionCommand())) {
			new FeeDetailsDialog(dueFeesDetailsTableModel.get(table
					.getSelectedRow()));
		} else if (STUDENT_DETAILS.equals(e.getActionCommand())) {
			new StudentRegistrationAction(dueFeesDetailsTableModel.get(table
					.getSelectedRow())).setVisible(true);
		} else if (OperationConstant.DUE_FEE_CSV_OUTPUT.equals(e

		.getActionCommand())) {
			JFileChooser fc = new JFileChooser();
			fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
			int returnVal = fc.showDialog(this, "SAVE");

			// Process the results.
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				if (!(file.getName().endsWith(".csv") || file.getName()
						.endsWith(".CSV"))) {
					file = new File(file.getAbsolutePath() + ".csv");
				}
				table.getColumnCount();
				if (dueFeesDetailsTableModel.writeToFile(file)) {
					MsgDialogUtils.showInformationDialog(this, "CSV Output",
							"Due Fee details written successfully \n File: "
									+ file.getAbsolutePath());
				}
			}
		}
	}

}
