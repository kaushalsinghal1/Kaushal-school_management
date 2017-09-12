package com.school.student;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import com.banti.framework.ui.DialogEsc;
import com.school.console.SchoolMain;
import com.school.hiebernate.dbo.StudentFeeDetails;

public class FeeDetailsDialog extends DialogEsc {
	private StudentFeeDetails studentFeeDetails;

	public FeeDetailsDialog(StudentFeeDetails studentFeeDetails) {
		super(SchoolMain.Frame, "Student Fee Details");
		this.studentFeeDetails = studentFeeDetails;
		init(SchoolMain.Frame);
	}

	private void init(Component parent) {
		add(getFeeDetailsPanel());
		setSize(480, 200);
		setLocationRelativeTo(parent);
		setVisible(true);
	}

	private JPanel getFeeDetailsPanel() {
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createEtchedBorder());

		JTable table = new JTable(new AbstractTableModel() {

			@Override
			public Object getValueAt(int r, int c) {
				if (r == 0) {
					switch (c) {
					case 0:
						return "Admission Fee";
					case 1:
						return studentFeeDetails.getTotalAdmission();
					case 2:
						return studentFeeDetails.getAdmissionFeeDeposite();
					case 3:
						return studentFeeDetails.getTotalAdmissionDue();
					}
				} else if (r == 1) {
					switch (c) {
					case 0:
						return "Tution Fee";
					case 1:
						return studentFeeDetails.getTotalTutionFee();
					case 2:
						return studentFeeDetails.getTuitionFeeDeposite();
					case 3:
						return studentFeeDetails.getTotalTutionDue();
					}
				} else if (r == 2) {
					switch (c) {
					case 0:
						return "Activity & Others Fee";
					case 1:
						return studentFeeDetails.getTotalActivity();
					case 2:
						return studentFeeDetails.getActivityFeeDeposite();
					case 3:
						return studentFeeDetails.getTotalActivityDue();
					}
				} else if (r == 3) {
					switch (c) {
					case 0:
						return "Bus Fee";
					case 1:
						return studentFeeDetails.getTotalBus();
					case 2:
						return studentFeeDetails.getBusFeeDeposite();
					case 3:
						return studentFeeDetails.getTotalBusDue();
					}
				} else if (r == 4) {
					switch (c) {
					case 0:
						return "Grand Total";
					case 1:
						return studentFeeDetails.getTotalFees();
					case 2:
						return studentFeeDetails.getTotalDepositedFee();
					case 3:
						return studentFeeDetails.getTotalDueFee();
					}
				}
				return "-";
			}

			@Override
			public int getRowCount() {
				return 5;
			}

			@Override
			public int getColumnCount() {
				return 4;
			}

			@Override
			public String getColumnName(int c) {
				switch (c) {
				case 0:
					return "Description";
				case 1:
					return "Fee";
				case 2:
					return "Deposited Fee";
				case 3:
					return "Due Fee";
				}
				return super.getColumnName(c);
			}
		});

		JScrollPane scrollPane = new JScrollPane(table);
		table.setMinimumSize(new Dimension(250, 150));
		table.setMaximumSize(new Dimension(300, 200));
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		// table.setLocation(100, 100);
		// table.setColumnWidth(300, 100);
		table.setAutoscrolls(true);
		panel.add(scrollPane);
		return panel;
	}
}
