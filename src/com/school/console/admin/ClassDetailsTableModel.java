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
import com.school.hiebernate.dbo.ClassDetails;

public class ClassDetailsTableModel extends SortFilterModel implements
		TableCellRenderer {

	private final int[] COL_INDEX = new int[] { 0, 1, 2, 3, 4 };
	private final int[] SET_PREF_COL_INDEX = new int[] { 1, 2, 3 };
	private final String[] columnNames = new String[] { "Class Id", "Session",
			"Class Name", "Added Date", "Modify Date" };

	private SimpleDateFormat sdf = ApplicationConstant.sdf_fulldate;

	private List<ClassDetails> dataList;

	public ClassDetailsTableModel(List<ClassDetails> classList) {
		this.dataList = classList;
		if (dataList == null) {
			dataList = new ArrayList<ClassDetails>();
		}
	}

	public int[] getTableColIndex() {
		return COL_INDEX;
	}

	public int[] getSetPrefColIndex() {
		return SET_PREF_COL_INDEX;
	}

	public List<ClassDetails> getDataList() {
		return dataList;
	}

	public void setDataList(List<ClassDetails> dataListNew) {
		dataList = dataListNew;
		fireTableDataChanged();
	}

	public void setDataElement(int index, ClassDetails classDetails) {
		System.out.println("set called----");
		dataList.set(getUnSortedIndex(index), classDetails);
		fireTableDataChanged();
	}

	public void removeDataElement(int index) {
		System.out.println("remove called----");
		dataList.remove(getUnSortedIndex(index));
		fireTableDataChanged();
	}

	public void addDataElement(ClassDetails classDetails) {
		System.out.println("add called----");
		dataList.add(classDetails);
		fireTableDataChanged();
	}

	public ClassDetails get(int row) {
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
		ClassDetails classsDetails = dataList.get(rowIndex);
		switch (columnIndex) {
		case 0:
			return classsDetails.getClassId() + "";
		case 1:
			return classsDetails.getDisplaySession();
		case 2:
			return classsDetails.getClassName();
		case 3:
			return classsDetails.getAddDate() == null ? "-" : sdf
					.format(classsDetails.getAddDate());
		case 4:
			return classsDetails.getUpdateDate() == null ? "-" : sdf
					.format(classsDetails.getUpdateDate());
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
		if (c == 3 || c == 4) {
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
		} else if (c == 0) {
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
