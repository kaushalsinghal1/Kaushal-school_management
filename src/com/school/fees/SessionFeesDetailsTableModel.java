package com.school.fees;

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
import com.school.hiebernate.dbo.SessionClassFeeDetails;

public class SessionFeesDetailsTableModel extends SortFilterModel implements
		TableCellRenderer {

	private final int[] COL_INDEX = new int[] { 0, 1, 2, 3, 4, 5, 6, 7,8 };
	private final int[] SET_PREF_COL_INDEX = new int[] { 1, 2, 3, 4, 5, 6,7 };
	private final String[] columnNames = new String[] { "SessionFee Id",
			"Session", "Class Name", "Admissaion Fee", "Tuition Fee",
			"Activity & others Fee","Transportaion Fee", "Added Date", "Modify Date" };

	private SimpleDateFormat sdf = ApplicationConstant.sdf_fulldate;

	private List<SessionClassFeeDetails> dataList;

	public SessionFeesDetailsTableModel(List<SessionClassFeeDetails> classList) {
		this.dataList = classList;
		if (dataList == null) {
			dataList = new ArrayList<SessionClassFeeDetails>();
		}
	}

	public int[] getTableColIndex() {
		return COL_INDEX;
	}

	public int[] getSetPrefColIndex() {
		return SET_PREF_COL_INDEX;
	}

	public List<SessionClassFeeDetails> getDataList() {
		return dataList;
	}

	public void setDataList(List<SessionClassFeeDetails> dataListNew) {
		dataList = dataListNew;
		fireTableDataChanged();
	}

	public void setDataElement(int index, SessionClassFeeDetails classDetails) {
		dataList.set(getUnSortedIndex(index), classDetails);
		fireTableDataChanged();
	}

	public void removeDataElement(int index) {
		dataList.remove(getUnSortedIndex(index));
		fireTableDataChanged();
	}

	public void addDataElement(SessionClassFeeDetails classDetails) {
		dataList.add(classDetails);
		fireTableDataChanged();
	}

	public SessionClassFeeDetails get(int row) {
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
		// { "SessionFee Id",
		// "Session", "Class Name", "Admissaion Fee", "Tuition Fee",
		// "Activity & others Fee", "Added Date", "Modify Date" };
		SessionClassFeeDetails sessionFeeDetails = dataList.get(rowIndex);
		switch (columnIndex) {
		case 0:
			return sessionFeeDetails.getSessionFeeId() + "";
		case 1:
			return sessionFeeDetails.getSessionDetails().getDisplayString();
		case 2:
			return sessionFeeDetails.getClassDetails().getClassName();
		case 3:
			return sessionFeeDetails.getAdmissionFee() + "";
		case 4:
			return sessionFeeDetails.getTuitionFee() + "";
		case 5:
			return sessionFeeDetails.getActivityFee() + "";
		case 6:
			return sessionFeeDetails.getBusFee() + "";
		case 7:
			return sessionFeeDetails.getAddDate() == null ? "-" : sdf
					.format(sessionFeeDetails.getAddDate());
		case 8:
			return sessionFeeDetails.getUpdateDate() == null ? "-" : sdf
					.format(sessionFeeDetails.getUpdateDate());
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
		if (c == 6 || c == 7) {
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
		} else if (c == 0 || c == 3 || c == 4 || c == 5) {
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

}
