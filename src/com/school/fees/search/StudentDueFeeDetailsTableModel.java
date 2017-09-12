package com.school.fees.search;

import java.awt.Color;
import java.awt.Component;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import com.banti.framework.table.SortFilterModel;
import com.school.constant.ApplicationConstant;
import com.school.hiebernate.dbo.StudentFeeDetails;
import com.school.utils.Logger;

public class StudentDueFeeDetailsTableModel extends SortFilterModel implements
		TableCellRenderer {

	private final int[] COL_INDEX = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9,
			10 };
	private final int[] SET_PREF_COL_INDEX = new int[] { 0, 1, 2, 3, 4, 5, 6,
			7, 8, 9, 10 };
	private final String[] columnNames = new String[] { "Student Name",
			"Session", "Class Name", "Last Deposited Fee Date",
			"Admissaion Due Fee", "Tuition Due Fee",
			"Activity & others Due Fee", "Bus Due Fee", "Total Due Fee",
			"Total Fee", "Total Deposited Fee" };

	private SimpleDateFormat sdf = ApplicationConstant.sdf_fulldate;
	private SimpleDateFormat sdf_date = ApplicationConstant.sdf_date;

	private List<StudentFeeDetails> dataList;

	public StudentDueFeeDetailsTableModel(List<StudentFeeDetails> feeMasterList) {
		this.dataList = feeMasterList;
		if (dataList == null) {
			dataList = new ArrayList<StudentFeeDetails>();
		}
	}

	public int[] getTableColIndex() {
		return COL_INDEX;
	}

	public int[] getSetPrefColIndex() {
		return SET_PREF_COL_INDEX;
	}

	public List<StudentFeeDetails> getDataList() {
		return dataList;
	}

	public void setDataList(List<StudentFeeDetails> dataListNew) {
		dataList = dataListNew;
		fireTableDataChanged();
	}

	public void setDataElement(int index, StudentFeeDetails feeMaster) {
		dataList.set(getUnSortedIndex(index), feeMaster);
		fireTableDataChanged();
	}

	public void removeDataElement(int index) {
		dataList.remove(getUnSortedIndex(index));
		fireTableDataChanged();
	}

	public void addDataElement(StudentFeeDetails feeMaster) {
		dataList.add(feeMaster);
		fireTableDataChanged();
	}

	public StudentFeeDetails get(int row) {
		if (getUnSortedIndex(row) < 0 || dataList == null)
			return null;
		return dataList.get(getUnSortedIndex(row));
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	public String getColumnName(int index) {
		return columnNames[index];
	}

	public Class getColumnClass(int index) {
		return columnNames[index].getClass();
	}

	@Override
	public int getRowCount() {
		if (dataList == null) {
			return 0;
		}
		return dataList.size();
	}

	@Override
	protected Object getUnSortedValueAt(int rowIndex, int columnIndex) {
		if (rowIndex < 0) {
			return "-" + rowIndex;
		}
		if (dataList == null || dataList.size() < rowIndex || rowIndex < 0) {
			return "-";
		}
		StudentFeeDetails feeMaster = dataList.get(rowIndex);
		switch (columnIndex) {
		case 0:
			StringBuffer sb = new StringBuffer();
			if (feeMaster.getStudentDetails().getFirstName() != null) {
				sb.append(feeMaster.getStudentDetails().getFirstName());
				sb.append(" ");
			}

			if (feeMaster.getStudentDetails().getLastName() != null) {
				sb.append(feeMaster.getStudentDetails().getLastName());
			}

			return sb.toString();
		case 1:
			return feeMaster.getSessionDetails().getDisplayString();
		case 2:
			return feeMaster.getClassDetails().getClassName();
		case 3:
			return feeMaster.getLastDepositeDate() == null ? "-" : sdf_date
					.format(feeMaster.getLastDepositeDate());
		case 4:
			return feeMaster.getTotalAdmissionDue() + "";
		case 5:
			return feeMaster.getTotalTutionDue() + "";
		case 6:
			return feeMaster.getTotalActivityDue() + "";
		case 7:
			return feeMaster.getTotalBusDue() + "";
		case 8:
			return feeMaster.getTotalDueFee() + "";
		case 9:
			return feeMaster.getTotalFees() + "";
		case 10:
			return feeMaster.getTotalDepositedFee();
		}
		return "-";
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		JComponent label = null;
		if (value != null) {
			label = new JLabel(value.toString());
			if (!value.equals("")) {
				label.setToolTipText(value.toString());
			}
		} else {
			label = new JLabel("");
		}
		label.setOpaque(true);
		if (isSelected) {
			label.setBackground(table.getSelectionBackground());
			label.setForeground(Color.white);
		} else {
			label.setBackground(Color.white);
			label.setForeground(Color.black);
		}
		label.setFont(table.getFont());
		return label;
	}

	@SuppressWarnings("unused")
	public int compareTo(int r1, int r2, int c) {
		Object a = getUnSortedValueAt(r1, c);
		Object b = getUnSortedValueAt(r2, c);
		if (c == 3) {
			Date dA = null;
			Date dB = null;
			try {
				dA = sdf.parse((String) a);
			} catch (ParseException e) {
			}
			try {
				dB = sdf.parse((String) b);
			} catch (ParseException e) {
			}

			int comp = 0;
			if (dA == null && dB == null) {
				comp = super.compareTo(r1, r2, c);
			} else if (dA == null) {
				comp = -1;
			} else if (dB == null) {
				comp = 1;
			} else {
				comp = dA.compareTo(dB);
			}
			return comp;
		} else if (c == 4 || c == 5 ||c == 6 ||  c == 7 || c == 8 || c == 9 || c == 10 || c == 11) {
			Integer intA = null;
			Integer intB = null;
			try {
				intA = Integer.parseInt((String) a);
			} catch (NumberFormatException e) {
			}
			try {
				intB = Integer.parseInt((String) b);
			} catch (NumberFormatException e) {
			}

			int comp = 0;
			if (intA == null && intB == null) {
				comp = super.compareTo(r1, r2, c);
			} else if (intA == null) {
				comp = -1;
			} else if (intB == null) {
				comp = 1;
			} else {
				comp = intA.compareTo(intB);
			}
			return comp;
		}
		return super.compareTo(r1, r2, c);
	}

	public boolean writeToFile(File file) {
		int[] columns = SET_PREF_COL_INDEX;
		BufferedWriter bw = null;
		try {
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			bw = new BufferedWriter(new FileWriter(file));
			bw.write(getHeader(columns));
			bw.newLine();
			for (int i = 0; i < dataList.size(); i++) {
				bw.write(getCSVData(columns, i));
				bw.newLine();
			}
		} catch (IOException e) {
			Logger.EXCEPTION.log(Level.WARNING,
					"Error occured while writing file", e);
			return false;
		} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException e) {
				}
			}
		}
		return true;
	}

	public String getCSVData(int[] columns, int row) {
		int sortedIndex = getUnSortedIndex(row);
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < columns.length; i++) {
			sb.append(getUnSortedValueAt(sortedIndex, columns[i]));
			if (i != columns.length - 1)
				sb.append(",");
		}
		return sb.toString();

	}

	public String getHeader(int[] columns) {
		StringBuffer sb = new StringBuffer("#");
		for (int i = 0; i < columns.length; i++) {
			sb.append(columnNames[columns[i]]);
			if (i != columns.length - 1)
				sb.append(",");
		}
		return sb.toString();
	}

}
