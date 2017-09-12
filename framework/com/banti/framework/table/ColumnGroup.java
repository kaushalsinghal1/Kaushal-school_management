
package com.banti.framework.table;
public class ColumnGroup {

    private String name;
    private int[] indexes;

    public ColumnGroup(String displayname, int[] columnsIndex) {
        name = displayname;
        indexes = columnsIndex;
    }

    public String getName() {
        return name;
    }

    public int[] getSelectedIndexes() {
        return indexes;
    }
}
