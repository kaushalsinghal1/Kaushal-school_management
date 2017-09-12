package com.school.student;

import java.awt.Color;
import java.awt.Component;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import com.banti.framework.table.SortFilterModel;
import com.school.constant.ApplicationConstant;
import com.school.hiebernate.dbo.StudentDetails;

public class StudentDetailsTableModel extends SortFilterModel implements
		TableCellRenderer {

	private final int[] COL_INDEX = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9,
			10, 11, 12, 13, 14, 15, 16, 17 };
	private final int[] SET_PREF_COL_INDEX = new int[] { 1, 2, 3, 4, 5, 6, 7,
			8, 9, 10, 11, 13, 14 };
	private final int[] SET_PREF_DIS_COL_INDEX = new int[] { 1, 2, 3, 4, 5, 6,
			7, 8, 9, 10, 11, 13, 14 };
	private final String[] columnNames = new String[] { "Student id",
			"Registration Number", "First Name", "Last Name", "Father Name",
			"Mother Name", "Date Of Birth", "Class Name", "Session", "Phone",
			"Mobile", "Email Id", "Parents Occupation", "Address", "City",
			"State", "Added Date", "Modify Date" };

	private final SimpleDateFormat sdf = ApplicationConstant.sdf_fulldate;
	private SimpleDateFormat sdfDate = ApplicationConstant.sdf_date;

	private List<StudentDetails> dataList;

	public StudentDetailsTableModel(List<StudentDetails> dataList) {
		this.dataList = dataList;
		if (dataList == null) {
			dataList = new ArrayList<StudentDetails>();
		}
	}

	public int[] getTableColIndex() {
		return COL_INDEX;
	}

	public int[] getSetPrefColIndex() {
		return SET_PREF_COL_INDEX;
	}

	public List<StudentDetails> getDataList() {
		return dataList;
	}

	public void setDataList(List<StudentDetails> dataListNew) {
		dataList = dataListNew;
		fireTableDataChanged();
	}

	public void setDataElement(int index, StudentDetails studentDetails) {
		dataList.set(getUnSortedIndex(index), studentDetails);
		fireTableDataChanged();
	}

	public void removeDataElement(int index) {
		dataList.remove(getUnSortedIndex(index));
		fireTableDataChanged();
	}

	public void addDataElement(StudentDetails studentDetails) {
		dataList.add(studentDetails);
		fireTableDataChanged();
	}

	public StudentDetails get(int row) {
		if (getUnSortedIndex(row) < 0 || dataList == null)
			return null;
		return dataList.get(getUnSortedIndex(row));
	}

	public List<StudentDetails> getList(int[] rows) {
		List<StudentDetails> list = new ArrayList<StudentDetails>();
		if (rows.length < 1 || dataList == null)
			return list;
		for (int i = 0; i < rows.length; i++) {
			list.add(dataList.get(getUnSortedIndex(rows[i])));
		}
		return list;
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
		StudentDetails studentDetails = dataList.get(rowIndex);
		switch (columnIndex) {
		case 0:
			return studentDetails.getStudentId() + "";
		case 1:
			return studentDetails.getRegistrationNumber();
		case 2:
			return studentDetails.getFirstName() == null ? "-" : studentDetails
					.getFirstName();
		case 3:
			return studentDetails.getLastName() == null ? "-" : studentDetails
					.getLastName();

		case 4:
			return studentDetails.getFatherName() == null ? "-"
					: studentDetails.getFatherName();
		case 5:
			return studentDetails.getMotherName() == null ? "-"
					: studentDetails.getMotherName();
		case 6:
			return studentDetails.getDateOfBirth() == null ? "-" : sdfDate
					.format(studentDetails.getDateOfBirth().getTime());
		case 7:
			return studentDetails.getClassDetails().getClassName();
		case 8:
			return studentDetails.getSessionDetails().getDisplayString();
		case 9:
			return studentDetails.getPhone() == null ? "-" : studentDetails
					.getPhone();
		case 10:
			return studentDetails.getMobile() == null ? "-" : studentDetails
					.getMobile();
		case 11:
			return studentDetails.getEmailId() == null ? "-" : studentDetails
					.getEmailId();
		case 12:
			return studentDetails.getParentsOccupation();
		case 13:
			return studentDetails.getAddress() == null ? "-" : studentDetails
					.getAddress();
		case 14:
			return studentDetails.getCity() == null ? "-" : studentDetails
					.getCity();
		case 15:
			return studentDetails.getState() == null ? "-" : studentDetails
					.getState();
		case 16:
			return studentDetails.getAddDate() == null ? "-" : sdf
					.format(studentDetails.getAddDate());
		case 17:
			return studentDetails.getUpdateDate() == null ? "-" : sdf
					.format(studentDetails.getUpdateDate());
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
		if (c == 0) {
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
		} else if (c == 2 || c == 3) {
			Date dA = null;
			Date dB = null;
			try {
				dA = sdfDate.parse((String) a);
			} catch (ParseException e) {
			}
			try {
				dB = sdfDate.parse((String) b);
			} catch (ParseException e) {
			}

			int comp = 0;
			if (dA == null && dB == null) {
				comp = super.compareTo(r1, r2, c);
			} else if (dA == null) {
				comp = -1;
			} else if (dA == null) {
				comp = 1;
			} else {
				comp = dA.compareTo(dB);
			}
			return comp;
		} else if (c == 4 || c == 5) {
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
		}
		return super.compareTo(r1, r2, c);
	}

	public int[] getSET_PREF_DIS_COL_INDEX() {
		return SET_PREF_DIS_COL_INDEX;
	}

}
