package com.school.console.admin;

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
import com.school.hiebernate.dbo.SessionDetails;

public class SessionDetailsTableModel extends SortFilterModel implements
		TableCellRenderer {

	private final int[] COL_INDEX = new int[] { 0, 1, 2, 3, 4, 5 };
	private final int[] SET_PREF_COL_INDEX = new int[] { 0, 1, 2, 3, 4 };
	private final int[] SET_PREF_DIS_COL_INDEX = new int[] { 1, 2, 3 };
	private final String[] columnNames = new String[] { "Session id",
			"Session", "Start Date", "End Date", "Added Date", "Modify Date" };

	private final SimpleDateFormat sdf = ApplicationConstant.sdf_fulldate;
	private SimpleDateFormat sdfDate = ApplicationConstant.sdf_date;

	private List<SessionDetails> dataList;

	public SessionDetailsTableModel(List<SessionDetails> classList) {
		this.dataList = classList;
		if (dataList == null) {
			dataList = new ArrayList<SessionDetails>();
		}
	}

	public int[] getTableColIndex() {
		return COL_INDEX;
	}

	public int[] getSetPrefColIndex() {
		return SET_PREF_COL_INDEX;
	}

	public List<SessionDetails> getDataList() {
		return dataList;
	}

	public void setDataList(List<SessionDetails> dataListNew) {
		dataList = dataListNew;
		fireTableDataChanged();
	}

	public void setDataElement(int index, SessionDetails SessionDetails) {
		dataList.set(getUnSortedIndex(index), SessionDetails);
		fireTableDataChanged();
	}

	public void removeDataElement(int index) {
		dataList.remove(getUnSortedIndex(index));
		fireTableDataChanged();
	}

	public void addDataElement(SessionDetails SessionDetails) {
		dataList.add(SessionDetails);
		fireTableDataChanged();
	}

	public SessionDetails get(int row) {
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
		SessionDetails sessionDetails = dataList.get(rowIndex);
		switch (columnIndex) {
		case 0:
			return sessionDetails.getSessionId() + "";
		case 1:
			return sessionDetails.getDisplayString();
		case 2:
			return sessionDetails.getStartDate() == null ? "-" : sdfDate
					.format(sessionDetails.getStartDate().getTime());
		case 3:
			return sessionDetails.getEndDate() == null ? "-" : sdfDate
					.format(sessionDetails.getEndDate());
		case 4:
			return sessionDetails.getAddDate() == null ? "-" : sdf
					.format(sessionDetails.getAddDate());
		case 5:
			return sessionDetails.getUpdateDate() == null ? "-" : sdf
					.format(sessionDetails.getUpdateDate());
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
