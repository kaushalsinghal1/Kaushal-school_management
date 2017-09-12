package com.banti.framework.cwt;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.banti.framework.utils.DateUtils;
import com.banti.framework.utils.I18NManager;


public class CalendarUI extends JPanel {

    private static I18NManager i18nManager;
    static {
        i18nManager = 
            I18NManager.getI18NManager(Locale.getDefault(), "com.banti.framework.cwt.Resources");
    }

    private static  final String[] MONTHS = {
            getString("JANUARY"),
            getString("FEBRUARY"),
            getString("MARCH"),
            getString("APRIL"),
            getString("MAY"),
            getString("JUNE"),
            getString("JULY"),
            getString("AUGUST"),
            getString("SEPTEMBER"),
            getString("OCTOBER"),
            getString("NOVEMBER"),
            getString("DECEMBER")
         };
    

	private MonthComboBox monthComboBox;
	private Spinner yearSpinner;
	private DateTable dateTable;
	private NumberSpinner hourSpinner;
	private NumberSpinner minSpinner;
	private NumberSpinner secSpinner;
	private JLabel timeZoneLabel;

	private Calendar cal;

	public CalendarUI() {
		super(new BorderLayout());
		cal = Calendar.getInstance();
		this.initComponents();
		this.setupComponents();
		this.initListeners();
		
		dateTable.setMonth(monthComboBox.getSelectedIndex());
	}

	private void initComponents() {
		monthComboBox = new MonthComboBox();
		monthComboBox.setSelectedIndex(cal.get(Calendar.MONTH));

		yearSpinner = new Spinner(cal.get(Calendar.YEAR),1900, 9999,1);
      yearSpinner.applyPattern("0000");
		yearSpinner.setInt(cal.get(Calendar.YEAR));
		dateTable = new DateTable();
		dateTable.getTableHeader().setReorderingAllowed(false);
		dateTable.setYear(cal.get(Calendar.YEAR));
		dateTable.setMonth(cal.get(Calendar.MONTH));
		dateTable.setDate(cal.get(Calendar.DAY_OF_MONTH));
		dateTable.repaint();
		
		hourSpinner = new NumberSpinner(0, 23, 2);
		minSpinner = new NumberSpinner(0, 59, 2);
		secSpinner = new NumberSpinner(0, 59, 2);
		timeZoneLabel = new JLabel(getString("TIME_ZONE") + ": "
                + cal.getTimeZone().getDisplayName(true, TimeZone.LONG));
		timeZoneLabel.setBorder(BorderFactory.createLoweredBevelBorder());
	}

	private void setupComponents() {
		JPanel northPanel = new JPanel(new GridLayout(1, 2));
		northPanel.add(monthComboBox);
		northPanel.add(yearSpinner);
		northPanel.setBorder(
			BorderFactory.createTitledBorder(
				BorderFactory.createCompoundBorder(
					BorderFactory.createEmptyBorder(2, 2, 2, 2),
					BorderFactory.createLineBorder(Color.gray)),
				getString("DATE")));

		JPanel hourPanel = new JPanel(new GridLayout(1, 6));
		hourPanel.setBorder(BorderFactory.createRaisedBevelBorder());
		hourPanel.add(new JLabel("  " + getString("HRS") + ": "));
		hourPanel.add(hourSpinner);
		hourPanel.add(new JLabel("  " + getString("MIN") + ": "));
		hourPanel.add(minSpinner);

		hourPanel.add(new JLabel("  " + getString("SEC") + ": "));
		hourPanel.add(secSpinner);

		JPanel southPanel = new JPanel(new GridLayout(2, 1));
		southPanel.add(hourPanel);
		southPanel.add(timeZoneLabel);

		add(northPanel, BorderLayout.NORTH);
		add(new JScrollPane(dateTable), BorderLayout.CENTER);
		add(southPanel, BorderLayout.SOUTH);
	}

	private static String getString(String key) {
		return i18nManager.getString(key);
	}

	private void initListeners() {
		monthComboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent ie) {
				dateTable.setMonth(monthComboBox.getSelectedIndex());
			}
		});

		yearSpinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent ce) {
				dateTable.setYear(
					Integer.parseInt(yearSpinner.getValue() + ""));
			}
		});

	}

	public Calendar getDate() {
		cal.set(Calendar.YEAR, yearSpinner.getInt());
		cal.set(Calendar.MONTH, monthComboBox.getMonth());
		int date = dateTable.getDate();
		if (date > 0) {
			cal.set(Calendar.DATE, date);
		}

		cal.set(Calendar.HOUR_OF_DAY, hourSpinner.getInt());
		cal.set(Calendar.AM_PM, hourSpinner.getInt());
		cal.set(Calendar.MINUTE, minSpinner.getInt());
		cal.set(Calendar.SECOND, secSpinner.getInt());
		return cal;
	}

	public String getDateInString() {
	    cal.set(Calendar.YEAR, yearSpinner.getInt());
	    cal.set(Calendar.MONTH, monthComboBox.getMonth());
	    int date = dateTable.getDate();
	    if (date > 0) {
	        cal.set(Calendar.DATE, date);
       	} else {
       	    return "";
       	}

	    cal.set(Calendar.HOUR_OF_DAY, hourSpinner.getInt());
	    cal.set(Calendar.MINUTE, minSpinner.getInt());
	    cal.set(Calendar.SECOND, secSpinner.getInt());
	    SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	    format.setCalendar(cal);
	    return format.format(cal.getTime());
	}

	public void setDateStr(String time) {
		if (time == null) {
			return;
		}
        if (time.trim().length() == 0) {
            time = DateUtils.getSystemTime();
        }
		String DATE_FORMAT = "yyyy/MM/dd HH:mm:ss";
		
		SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
        cal.setTime(formatter.parse(time, new ParsePosition(0)));
        monthComboBox.setSelectedIndex(cal.get(Calendar.MONTH));
        yearSpinner.setInt(cal.get(Calendar.YEAR));
        dateTable.setDate(cal.get(Calendar.DATE));
        dateTable.setYear(cal.get(Calendar.YEAR));
        dateTable.setMonth(cal.get(Calendar.MONTH));
        hourSpinner.setInt(cal.get(Calendar.HOUR_OF_DAY));
        minSpinner.setInt(cal.get(Calendar.MINUTE));
        secSpinner.setInt(cal.get(Calendar.SECOND));
	}

	
	// ------------------ Inner Classes ----------------------------------------------------------------

	// ----------------- Inner MonthComboBox class --------------------------------------------
	private class MonthComboBox extends JComboBox {

        public MonthComboBox() {
            super(MONTHS);
        }

        public int getMonth() {
            return getSelectedIndex();
        }

        public void setMonth(int index) {
            setSelectedIndex(index);
        }
    }

	// ----------------- Inner Date Table class --------------------------------------------
	private class DateTable extends JTable {

        private DateTableModel dtm;
        private int selRow;
        private int selCol;
        private DateTableCellRenderer dtcr;

        public DateTable() {
            dtm = new DateTableModel();
            setModel(dtm);
            dtcr = new DateTableCellRenderer();
            setDefaultRenderer(Object.class, dtcr);
        }

        public void setMonth(int month) {
            selRow = getSelectedRow();
            selCol = getSelectedColumn();
            dtm.setMonth(month);

            changeSelection(selRow, selCol, false, false);
        }

        public void setYear(int year) {
            selRow = getSelectedRow();
            selCol = getSelectedColumn();
            dtm.setYear(year);

            changeSelection(selRow, selCol, false, false);
        }

        public void setDate(int date) {
            dtm.setDate(date);
            changeSelection(dtm.getCurrentRow(), dtm.getCurrentCol(), false, false);
        }

        public int getDate() {
            try {
                return Integer.parseInt(dtm.getValueAt(getSelectedRow(), getSelectedColumn()) + "");
            } catch (NumberFormatException e) {
                return -1;
            }
        }
    }

	// ----------------- Inner DateTableModel class --------------------------------------------
	private class DateTableModel extends DefaultTableModel {

        private int ROWS = 6;
        private int COLS = 7;
        private String[] COLUMN = new String[7];

        private Calendar calendar;
        private int startCol;

        public DateTableModel() {
            COLUMN[0] = getString("SUNDAY");
            COLUMN[1] = getString("MONDAY");
            COLUMN[2] = getString("TUESDAY");
            COLUMN[3] = getString("WEDNESDAY");
            COLUMN[4] = getString("THURSDAY");
            COLUMN[5] = getString("FRIDAY");
            COLUMN[6] = getString("SATURDAY");

            calendar = Calendar.getInstance();
        }

        public void setMonth(int month) {
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
            startCol = calendar.get(Calendar.DAY_OF_WEEK) - 1;
            fireTableDataChanged();
        }

        public void setYear(int year) {
            calendar.set(Calendar.YEAR, year);
            startCol = calendar.get(Calendar.DAY_OF_WEEK) - 1;
            fireTableDataChanged();
        }

        public void setDate(int date) {
            calendar.set(Calendar.DATE, date);
            startCol = calendar.get(Calendar.DAY_OF_WEEK) - 1;
            fireTableDataChanged();
        }

        public int getCurrentRow() {
            int date = calendar.get(Calendar.DATE);
            calendar.set(Calendar.DATE, 1);
            int offset = calendar.get(Calendar.DAY_OF_WEEK) - 1;
            calendar.set(Calendar.DATE, date);

            int dom = calendar.get(Calendar.DAY_OF_MONTH) - 1;
            int col = (offset + dom) / 7;

            return col;
        }

        public int getCurrentCol() {
            return calendar.get(Calendar.DAY_OF_WEEK) - 1;
        }

        public int getColumnCount() {
            return COLS;
        }

        public String getColumnName(int col) {
            return COLUMN[col];
        }

        public int getRowCount() {
            return ROWS;
        }

        public Object getValueAt(int row, int col) {
            if ((row == 0 && col < startCol)) {
                return "";
            }

            int val = ((row * COLS) + (COLS - startCol - (COLS - col - 1)));
            if (val > calendar.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                return "";
            }
            return "" + (val < 1 ? -1 : val);
        }

        public void setValueAt(Object object, int row, int col) {
        }

        public boolean isCellEditable(int row, int col) {
            return false;
        }
    }

	// ----------------- Inner DateTableCellRender class -------------------------------
	private class DateTableCellRenderer extends DefaultTableCellRenderer {

        private Font simpleFont;
        private Font boldFont;

        public DateTableCellRenderer() {
            Font defaultFont = getFont();
            simpleFont = new Font(defaultFont.getName(), Font.PLAIN, defaultFont.getSize());
            boldFont = new Font(defaultFont.getName(), Font.BOLD, defaultFont.getSize());
        }

        public Component getTableCellRendererComponent(JTable table, Object value, 
                	boolean isSelected, boolean hasFocus, int row, int column) {
            
            if (value.toString().trim().length() == 0) {
                hasFocus = false;
                isSelected = false;
            }

            if (table.getSelectedRow() == row && table.getSelectedColumn() == column) {
                setFont(boldFont);
                setBackground(Color.gray);
            } else {
                setBackground(Color.white);
                setFont(simpleFont);
            }

            setHorizontalAlignment(JLabel.CENTER);
            setText(value.toString());
            return this;
        }
    }


}
