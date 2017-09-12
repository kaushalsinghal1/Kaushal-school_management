package com.banti.framework.table;

/**
 * @author banti
 *
 */
public interface RowComparable {
    int getRowCount();

    int getColumnCount();

    int compareTo(int r1, int r2, int c);
}
