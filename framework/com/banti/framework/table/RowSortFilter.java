package com.banti.framework.table;
import java.util.Arrays;

/**
 * @author banti
 */
public class RowSortFilter {

    private int sortColumn = -1;

    private Row[] rows;

    private boolean sortAscending;

    private RowComparable sortable;

    public RowSortFilter(RowComparable sortable) {

        this.sortable = sortable;

        rows = new Row[(sortable.getRowCount())];
        for (int i = 0; i < rows.length; i++) {
            rows[i] = new Row();
            rows[i].index = i;

        }
    }

    public RowSortFilter(RowComparable sortable, int rowCount) {
        this.sortable = sortable;

        rows = new Row[rowCount];
        for (int i = 0; i < rows.length; i++) {
            rows[i] = new Row();
            rows[i].index = i;
        }
    }

    public synchronized void setSortColumn(int c) {
        setColumn(c);
        Arrays.sort(rows);
    }

    public void setColumn(int c) {
        if (c == sortColumn) {
            if (sortAscending) {
                sortAscending = false;
            } else {
                sortAscending = true;
            }
        } else {
            sortAscending = true;
            sortColumn = c;
        }
    }

    public synchronized int getRowIndex(int r) {
        if (rows.length <= r || r < 0) {
            return -1;
        }
        if (rows[r] == null) {
            return -1;
        }
        return rows[r].index;
    }

    protected void setRow() {
        for (int i = 0; i < rows.length; i++) {
            rows[i] = new Row();
            rows[i].index = i;
        }
    }

    public synchronized int getReverseRowIndex(int r) {

        for (int i = 0; i < rows.length; i++) {
            if (r == rows[i].index)
                return i;
        }

        return -1;
    }

    private class Row implements Comparable {

        public int index;

        public int compareTo(Object other) {

            if (sortAscending) {
                return compareToPriv(other);
            } else {
                return -compareToPriv(other);
            }
        }

        private int compareToPriv(Object other) {

            Row otherRow = (Row) other;
            return sortable.compareTo(index, otherRow.index, sortColumn);
        }
    }

    public void update() {
        update(sortColumn);
    }

    public synchronized void update(int c) {
        if (rows == null || rows.length != sortable.getRowCount()) {
            rows = new Row[sortable.getRowCount()];
            for (int i = 0; i < rows.length; i++) {
                rows[i] = new Row();
                rows[i].index = i;
            }
        }
        if (c < 0 || c >= sortable.getColumnCount()) {
            return;
        }
        sortColumn = c;
        Arrays.sort(rows);

    }

}
