package com.banti.framework.cwt;

import java.util.ListResourceBundle;

public class Resources extends ListResourceBundle {

    public Object[][] getContents() {
        return contents;
    }

    private Object[][] contents = {
        { "DATE_SELECTOR", "Date Selector" },
        { "SELECT_DATE", "Please select the date also" },
        { "CAL_MSG", "Calendar Message" },
        { "OK", "Ok" },
        { "CANCEL", "Cancel" },
        { "APPLY", "Apply" },
        {"CLOSE", "Close"},
        {"ABORT", "Abort"},
        { "DATE", "Date" },
        { "TIME_ZONE", "Time Zone" },

        { "JANUARY", "January" },
        { "FEBRUARY", "February" },
        { "MARCH", "March" },
        { "APRIL", "April" },
        { "MAY", "May" },
        { "JUNE", "June" },
        { "JULY", "July" },
        { "AUGUST", "August" },
        { "SEPTEMBER", "September" },
        { "OCTOBER", "October" },
        { "NOVEMBER", "November" },
        { "DECEMBER", "December" },

        { "HRS", "Hrs" },
        { "MIN", "Min" },
        { "SEC", "Sec" },

        { "SUNDAY", "S" },
        { "MONDAY", "M" },
        { "TUESDAY", "T" },
        { "WEDNESDAY", "W" },
        { "THURSDAY", "T" },
        { "FRIDAY", "F" },
        { "SATURDAY", "S" },

        { "CC_CHOOSE_COLOR", "Choose Color" },
        { "CC_GRAPH_COLOR", "Graph" },
        { "CC_BACKGROUND_COLOR", "Background" },
        { "CC_GRID_COLOR", "Grid" },
        { "CC_XYLABELS_COLOR", "X-Y Labels" },
        { "CC_XYVALUES_COLOR", "X-Y Values" },

        { "CC_ITEMS", "Items" },
        { "SELECT_COLUMN", "etc.." },
        { "SAVE", "Save" },
        { "COLUMN_SELECTION", "Column customize" },
        { "COLUMN_SELECTION_MSG", "Please select showing columns and order of column for table." },

        { "MOVE", "Move" },
        { "LOCATION_ADDRESS", "Address?F" },
        { "UP", "Up" },
        { "DOWN", "Down" } };
}
