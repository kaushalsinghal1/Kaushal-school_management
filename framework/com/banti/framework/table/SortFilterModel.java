package com.banti.framework.table;
import java.awt.Color;

import javax.swing.JComponent;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

/**
 * @author banti
 * 
 * 
 */
public abstract class SortFilterModel extends AbstractTableModel implements RowComparable {

    protected RowSortFilter rowSortFilter;

    private UpdateListener listener;

    public SortFilterModel() {
        rowSortFilter = new RowSortFilter(this);
        listener = new UpdateListener();
        super.addTableModelListener(listener);
    }

    public SortFilterModel(int buffer) {
        rowSortFilter = new RowSortFilter(this, buffer);
        listener = new UpdateListener();
        super.addTableModelListener(listener);
    }

    public int getSortedIndex(int row) {
        return rowSortFilter.getReverseRowIndex(row);
    }

    public int getUnSortedIndex(int row) {
        return rowSortFilter.getRowIndex(row);
    }

    public void sort(int column) {
        //sortColumn = column;
        rowSortFilter.setSortColumn(column);

        listener.setEnabled(false);

        this.fireTableDataChanged();

        listener.setEnabled(true);

    }

    /**
     * Adds a listener to the list that's notified each time a change to the
     * data model occurs.
     * 
     * @param l
     *            the TableModelListener
     */
    public void addTableModelListener(TableModelListener l) {
        super.addTableModelListener(l);

        super.removeTableModelListener(listener);
        super.addTableModelListener(listener);
    }

    /*
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    public abstract int getColumnCount();

    /*
     * @see javax.swing.table.TableModel#getRowCount()
     */
    public abstract int getRowCount();

    /*
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    public Object getValueAt(int rowIndex, int columnIndex) {

        return getUnSortedValueAt(rowSortFilter.getRowIndex(rowIndex), columnIndex);
    }

    protected abstract Object getUnSortedValueAt(int rowIndex, int columnIndex);

    private class UpdateListener implements TableModelListener {

        boolean updateEnabled = true;

        public void setEnabled(boolean enable) {

            updateEnabled = enable;
        }

        public void tableChanged(TableModelEvent e) {
            if (updateEnabled)
                rowSortFilter.update();
        }
    }

    public int compareTo(int r1, int r2, int c) {
        Object a = getUnSortedValueAt(r1, c);
        Object b = getUnSortedValueAt(r2, c);

        if (a instanceof Comparable) {
            if (a != null) {
                if (b != null) {
                    return ((Comparable) a).compareTo(b);
                } else {
                    return 1;
                }
            } else if (b != null) {
                return -1;
            }
            return 0;
        } else {
            String strA = (a == null) ? "" : a.toString();
            String strB = (b == null) ? "" : b.toString();
            return strA.compareTo(strB);
        }
    }

    public JComponent changeSelectedColor(boolean isSelected, JComponent label, Color fgColor) {
        if (isSelected) {
            label.setOpaque(true);
            if (fgColor == null) {
                label.setForeground(Color.WHITE);
            } else {
                label.setForeground(fgColor);
            }
        }
        return label;
    }

    protected void setColumn(int column) {
        rowSortFilter.setColumn(column);
    }

    protected void updateRow() {
        rowSortFilter.setRow();
    }

}