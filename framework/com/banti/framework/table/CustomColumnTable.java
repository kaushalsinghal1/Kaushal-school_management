
package com.banti.framework.table;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

/**
 * Extend JTable class, provide to custom the table columns.
 *
 */
public class CustomColumnTable extends JTable implements ItemListener, ActionListener {

    //    private static I18NManager i18nManager = I18NManager.getI18NManager(
    //        Locale.getDefault(),
    //        "com.cysols.cwt.Resources");

    private static String SELECTION_COMMAND = "SELECTION_COMMAND";

    static String SCD_OK_COMMAND = "SCD_OK_COMMAND";

    static String SCD_CANCEL_COMMAND = "SCD_CANCEL_COMMAND";

    static ColumnShowIndexComparable columnShowIndexComparable = new ColumnShowIndexComparable();

    protected List columnList = new ArrayList();
    protected Column[] orderedColumns;

    protected List groupList;

    protected JPopupMenu popup;

    private InnerTableModel tableModel;

    private boolean isInitializing;

    private JMenuItem scMenuItem;

    private ColumnSelection csDialog;

    private Frame fOwner;

    private Dialog dOwner;

    private int width;

    private int minWidth = -1;

    protected PopupSortMouseAdapter popSortAdapter;

    protected PopupMouseAdapter popupAdapter;
    protected ColumnWidthSaveAdapter columnAdapter;

    protected SortFilterModel sortFilterModel;

    private boolean isEnabledPopup = true;

    private Map<String, Integer> columnNameWidthMap;

    private int minWidthOfColumn = 0;
    private String columnName = null;
    private int numberOfSmallColumn = 0;

    private int[] hiddenIndices;

    private final int MAX_PREF_KEY_LENGTH = Preferences.MAX_KEY_LENGTH;

    /**
     * Initializes with owner component.
     *
     * @param owner
     */
    public CustomColumnTable(Frame owner) {
        fOwner = owner;
        initialize();
    }

    /**
     * Initializes with owner component.
     *
     * @param owner
     */
    public CustomColumnTable(Dialog owner) {
        dOwner = owner;
        initialize();
    }

    public void addTableModelListener(TableModelListener l) {
        tableModel.addTableModelListener(l);
    }

    public void setPreferredColumns(int[] indexes) {
        if (popup == null) {
            return;
        }
        for (int i = 0; i < popup.getSubElements().length; i++) {
            Component com = popup.getComponent(i);
            if (!(com instanceof JCheckBoxMenuItem)) {
                continue;
            }
            JCheckBoxMenuItem item = (JCheckBoxMenuItem) com;
            boolean isDisplay = false;
            for (int c = 0; c < indexes.length; c++) {
                if (indexes[c] == i) {
                    isDisplay = true;
                    continue;
                }
            }
            if (!isDisplay) {
                item.setVisible(false);
            }
        }
    }

    private void initialize() {
        popupAdapter = new PopupMouseAdapter();
        getTableHeader().addMouseListener(popupAdapter);

        if (fOwner != null) {
            csDialog = new ColumnSelection(fOwner, getString("COLUMN_SELECTION"));
        } else if (dOwner != null) {
            csDialog = new ColumnSelection(dOwner, getString("COLUMN_SELECTION"));
        } else {
            csDialog = new ColumnSelection(getString("COLUMN_SELECTION"));
        }
    }

    /**
     * Initializes with owner component, table model.
     *
     * @param owner
     *            owner component
     * @param model
     *            table model
     */
    public CustomColumnTable(Frame owner, TableModel model) {
        this(owner);
        setModel(model);
    }

    /**
     * Initializes with owner component and table model.
     *
     * @param owner
     *            owner component
     * @param model
     *            table model
     */
    public CustomColumnTable(Dialog owner, TableModel model) {
        this(owner);
        setModel(model);
    }

    /**
     * Initializes with owner component, SortFilter model.
     *
     * @param owner
     *            owner component
     * @param model
     *            SortFilterModel
     */
    public CustomColumnTable(Frame owner, SortFilterModel model) {
        this(owner);
        setModel(model);
    }

    /**
     * Initializes with owner component and SortFilter model.
     *
     * @param owner
     *            owner component
     * @param model
     *            SortFilterModel
     */
    public CustomColumnTable(Dialog owner, SortFilterModel model) {
        this(owner);
        setModel(model);
    }

    /**
     * Initializes with owner component, table model and table renderer.
     *
     * @param owner
     *            owner component
     * @param model
     *            table model
     * @param renderer
     *            table renderer
     */
    public CustomColumnTable(Frame owner, TableModel model, TableCellRenderer renderer) {
        this(owner, model);
        setDefaultRenderer(Object.class, renderer);
    }

    /**
     * Initializes with owner component, table model and table renderer.
     *
     * @param owner
     *            owner component
     * @param model
     *            table model
     * @param renderer
     *            table renderer
     */
    public CustomColumnTable(Dialog owner, TableModel model, TableCellRenderer renderer) {
        this(owner, model);
        setDefaultRenderer(Object.class, renderer);
    }

    /**
     * Initializes with owner component, SortFilterModel and table renderer.
     *
     * @param owner
     *            owner component
     * @param model
     *            SortFilterModel
     * @param renderer
     *            table renderer
     */
    public CustomColumnTable(Frame owner, SortFilterModel model, TableCellRenderer renderer) {
        this(owner, model);
        setDefaultRenderer(Object.class, renderer);
    }

    /**
     * Initializes with owner component, SortFilterModel and table renderer.
     *
     * @param owner
     *            owner component
     * @param model
     *            SortFilterModel
     * @param renderer
     *            table renderer
     */
    public CustomColumnTable(Dialog owner, SortFilterModel model, TableCellRenderer renderer) {
        this(owner, model);
        setDefaultRenderer(Object.class, renderer);
    }

    /**
     * Sets the data model for this table to <code>newModel</code> and
     * registers with it for listener notifications from the new data model.
     */
    public void setModel(TableModel model) {
        arrangColumnList(model);

        super.setModel(tableModel = new InnerTableModel(model));
        if (getTableHeader() != null) {
            getTableHeader().removeMouseListener(popupAdapter);
            getTableHeader().removeMouseListener(popSortAdapter);
            if (popupAdapter != null) {
                getTableHeader().addMouseListener(popupAdapter);
            } else {
                getTableHeader().addMouseListener(new PopupMouseAdapter());
            }
        }
        tableModel.fireTableDataChanged();
    }

    public void setModel(SortFilterModel model) {
        sortFilterModel = model;
        arrangColumnList(model);
        super.setModel(tableModel = new InnerTableModel(model));
        if (getTableHeader() != null) {
            getTableHeader().removeMouseListener(popSortAdapter);
            getTableHeader().removeMouseListener(popupAdapter);
            getTableHeader().removeMouseListener(columnAdapter);
            if (popSortAdapter != null) {
                popSortAdapter.setModel(model);
                getTableHeader().addMouseListener(popSortAdapter);
            } else {
                popSortAdapter = new PopupSortMouseAdapter();
                popSortAdapter.setModel(model);
                getTableHeader().addMouseListener(popSortAdapter);
            }
            if (columnAdapter == null) {
                columnAdapter = this.new ColumnWidthSaveAdapter();
                getTableHeader().addMouseListener(columnAdapter);
            } else {
                getTableHeader().addMouseListener(columnAdapter);
            }
        }

        if (CustomColumnTable.this.sortFilterModel != null) {
            Map<String, Integer> widthMap = new HashMap<String, Integer>(0);
            Class c = CustomColumnTable.this.sortFilterModel.getClass();
            //TODO use println when you check which preference path is used to save/load table header column width.
            //System.out.println(c.getName());
            if (!c.getName().startsWith("com.banti.framework")) {
                Preferences prefs = Preferences.userNodeForPackage(c);
                for (int i = 0; i < getColumnCount(); i++) {
                    TableColumn column = getColumnModel().getColumn(i);
                    String columnName = column.getHeaderValue().toString();
                    String prefix = "";
                    String prefKey = "";
                    if (c != null) {
                        prefix = c.getSimpleName();
                    }
                    prefKey = prefix + "_" + columnName;

                    int width = 0;

                    if (isValidKeyLength(prefKey, columnName)) {
                        try {
                            width = prefs.getInt(prefKey, 0);
                        } catch (Exception e) {
                            width = 0;
                        }
                    }

                    if (width > 0) {
                        widthMap.put(columnName, width);
                    }
                }
                if (widthMap.size() > 0) {
                    columnNameWidthMap = widthMap;
                }
            }
        }
        tableModel.fireTableDataChanged();
    }

    public void setDefaultRenderer(Class columnClass, TableCellRenderer renderer) {
        super.setDefaultRenderer(columnClass, new InnerTableRenderer(renderer));
    }

    public void addSelectedColumns(int[] indexes) {
        for (int i = 0; i < indexes.length; i++) {
            if (indexes[i] >= columnList.size()) {
                continue;
            }
            Component com = popup.getComponent(i);
            if (com instanceof JCheckBoxMenuItem) {
                JCheckBoxMenuItem item = (JCheckBoxMenuItem) com;
                item.setSelected(true);
            }
        }
    }

    public void setSelectedColumns(int[] indexes) {
        if (indexes == null) {
            return;
        }
        isInitializing = true;
        try {

            // Clear selecting all menus.
            for (int i = 0; i < columnList.size(); i++) {
                Component com = popup.getComponent(i);
                if (com instanceof JCheckBoxMenuItem) {
                    JCheckBoxMenuItem item = (JCheckBoxMenuItem) com;
                    item.setSelected(false);
                }
            }

            for (int i = 0; i < indexes.length; i++) {
                if (indexes[i] >= columnList.size()) {
                    continue;
                }
                Component com = popup.getComponent(indexes[i]);
                if (com instanceof JCheckBoxMenuItem) {
                    JCheckBoxMenuItem item = (JCheckBoxMenuItem) com;
                    item.setSelected(true);
                }
            }
        } finally {
            isInitializing = false;
        }
        fireTableStructureChanged();
        if (width != 0) {
            setColumnWidth(width);
        } else {
            if (this.columnNameWidthMap != null) {
                TableColumn column = null;
                for (int i = 0; i < getColumnCount(); i++) {
                    column = getColumnModel().getColumn(i);
                    Integer prefferedwitdth = this.columnNameWidthMap.get(column.getHeaderValue().toString());
                    if (prefferedwitdth != null) {
                        column.setPreferredWidth(prefferedwitdth);
                    }
                }
            }
        }
    }

    public int[] getDisplayedColumns() {
        Column[] columns = (Column[]) columnList.toArray(new Column[columnList.size()]);
        ArrayList list = new ArrayList();
        for (int i = 0; i < columns.length && i < columns.length; i++) {
            if (columns[i].isSelected()) {
                list.add(new Integer(columns[i].getIndex()));
            }
        }
        if (list.size() < 1) {
            return null;
        }
        int[] indexes = new int[list.size()];
        int counter = 0;
        for (Iterator ite = list.iterator(); ite.hasNext(); counter++) {
            Integer integer = (Integer) ite.next();
            indexes[counter] = integer.intValue();
        }
        return indexes;
    }

    public void addColumnGroup(final ColumnGroup group) {
        if (group == null) {
            return;
        }
        if (groupList == null) {
            groupList = new ArrayList();
            popup.addSeparator();
        }
        JMenuItem item = new JMenuItem(group.getName());
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setSelectedColumns(group.getSelectedIndexes());
            }
        });
        groupList.add(group);
        popup.add(item);
    }

    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (SELECTION_COMMAND.equals(command)) {

            Column[] columns = new Column[columnList.size()];

            Column co;
            for (int i = 0; i < columns.length; i++) {
                co = (Column) columnList.get(i);
                if (!co.isHidden()) {
                    columns[i] = (Column) co.clone();
                }
            }
            csDialog.show(columns);

        } else if (SCD_OK_COMMAND.equals(command)) {
            Column[] columns = csDialog.getCoulmn();
            List list = new ArrayList();

            Column org;
            for (int i = 0; i < columns.length; i++) {
                if (columns[i].getIndex() >= columnList.size()) {
                    continue;
                }
                columns[i].setShowIndex(i);
                org = (Column) columnList.get(columns[i].getIndex());
                org.setSelected(columns[i].isSelected());
                org.setShowIndex(columns[i].getShowIndex());
                org.setWeight(columns[i].getWeight());
                if (columns[i].isSelected()) {
                    list.add(new Integer(org.getIndex()));
                }
            }
            synctOrderedColumns();
            int[] indexes = new int[list.size()];
            for (int i = 0; i < indexes.length; i++) {
                indexes[i] = ((Integer) list.get(i)).intValue();
            }

            this.setSelectedColumns(indexes);
            csDialog.setVisible(false);
            if (width != 0) {
                setColumnWidth(width);
            }
        } else if (SCD_CANCEL_COMMAND.equals(command)) {
            csDialog.setVisible(false);
        }
    }

    protected void arrangColumnList(TableModel model) {
        columnList = new ArrayList(model.getColumnCount());
        popup = new JPopupMenu();
        for (int i = 0; i < model.getColumnCount(); i++) {
            String name = model.getColumnName(i);
            Column column = new Column(i, name);
            column.setSelected(true);
            columnList.add(column);
            synctOrderedColumns();
            JCheckBoxMenuItem item = new JCheckBoxMenuItem(column.getName(), true);
            item.setActionCommand("" + i);
            item.addItemListener(this);
            popup.add(item);
        }
        popup.addSeparator();
        if (scMenuItem == null) {
            scMenuItem = new JMenuItem(getString("Select Column"));
            scMenuItem.setActionCommand(SELECTION_COMMAND);
            scMenuItem.addActionListener(this);
        }
        popup.add(scMenuItem);
    }

    protected String getString(String key) {
        String converted = null;//i18nManager.getString(key);

        return (converted == null) ? key : converted;
    }

    public void setEnabled(boolean b) {
        super.setEnabled(b);
        if (b) {
            if (popSortAdapter != null) {
                getTableHeader().removeMouseListener(popSortAdapter);
                getTableHeader().addMouseListener(popSortAdapter);
            } else {
                getTableHeader().removeMouseListener(popupAdapter);
                getTableHeader().addMouseListener(popupAdapter);
            }
        } else {
            if (popSortAdapter != null) {
                getTableHeader().removeMouseListener(popSortAdapter);
            } else {
                getTableHeader().removeMouseListener(popupAdapter);
            }
        }
    }

    public void setEnabledPopup(boolean flag) {
        isEnabledPopup = flag;
    }

    Column[] synctOrderedColumns() {
        orderedColumns = (Column[]) columnList.toArray(new Column[columnList.size()]);
        Arrays.sort(orderedColumns, columnShowIndexComparable);
        return orderedColumns;
    }

    class InnerTableRenderer extends DefaultTableCellRenderer {

        private TableCellRenderer renderer;

        public InnerTableRenderer(TableCellRenderer myrenderer) {
            renderer = myrenderer;
        }

        public Component getTableCellRendererComponent(
            JTable table,
            Object value,
            boolean isSelected,
            boolean hasFocus,
            int row,
            int columnIndex) {

            int counter = 0;

            Column[] columns = CustomColumnTable.this.orderedColumns;
            for (int i = 0; i < columns.length; i++) {
                if (columns[i].isSelected()) {
                    if (counter == columnIndex) {
                        return renderer.getTableCellRendererComponent(
                            table,
                            value,
                            isSelected,
                            hasFocus,
                            row,
                            columns[i].getIndex());
                    }
                    counter++;
                }
            }
            return this;
        }
    }

    class InnerTableModel extends AbstractTableModel implements TableModelListener {

        private TableModel model;

        InnerTableModel(TableModel mymodel) {
            model = mymodel;
            model.addTableModelListener(this);
        }

        public boolean isCellEditable(int row, int column) {
            return model.isCellEditable(row, column);
        }

        public int getColumnCount() {
            int count = 0;
            for (int i = 0; i < CustomColumnTable.this.columnList.size(); i++) {
                Column co = (Column) CustomColumnTable.this.columnList.get(i);
                if (co.isSelected()) {
                    count++;
                }
            }
            return count;
        }

        public int getRowCount() {
            return model.getRowCount();
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            int counter = 0;

            Column[] columns = CustomColumnTable.this.orderedColumns;

            for (int i = 0; i < columns.length; i++) {
                if (columns[i].isSelected()) {
                    if (counter == columnIndex) {
                        return model.getValueAt(rowIndex, columns[i].getIndex());
                    }
                    counter++;
                }
            }
            return "";
        }

        public void setValueAt(Object value, int row, int column) {
            model.setValueAt(value, row, column);
        }

        public String getColumnName(int index) {
            int counter = 0;

            Column[] columns = CustomColumnTable.this.orderedColumns;
            for (int i = 0; i < columns.length; i++) {
                if (columns[i].isSelected()) {
                    if (counter == index) {
                        return model.getColumnName(columns[i].getIndex());
                    }
                    counter++;
                }
            }
            return "";
        }

        public void tableChanged(TableModelEvent e) {
            super.fireTableChanged(e);
        }
    }

    public void itemStateChanged(ItemEvent e) {
        if (!(e.getSource() instanceof JCheckBoxMenuItem)) {
            return;
        }
        JCheckBoxMenuItem item = (JCheckBoxMenuItem) e.getSource();
        try {
            int selectedItem = Integer.parseInt(item.getActionCommand());
            if (columnList == null || columnList.size() <= selectedItem) {
                return;
            }
            Column column = (Column) columnList.get(selectedItem);
            if ((e.getStateChange() == ItemEvent.DESELECTED) && column.isSelected()) {
                if (!isInitializing && tableModel.getColumnCount() == 1) {
                    item.setSelected(true);
                    return;
                }
                column.setSelected((e.getStateChange() == ItemEvent.SELECTED));
                fireTableStructureChanged();
                if (width != 0) {
                    setColumnWidth(width);
                }

            } else if ((e.getStateChange() == ItemEvent.SELECTED) && !column.isSelected()) {

                column.setSelected((e.getStateChange() == ItemEvent.SELECTED));
                fireTableStructureChanged();
                if (width != 0) {
                    setColumnWidth(width);
                }
            }
        } catch (NumberFormatException nfe) {
        }
    }

    private void fireTableStructureChanged() {
        if (!isInitializing) {
            tableModel.fireTableStructureChanged();
        }
    }

    static class ColumnShowIndexComparable implements Comparator {

        public int compare(Object arg0, Object arg1) {
            if (!(arg0 instanceof Column)) {
                if (!(arg1 instanceof Column)) {
                    return 0;
                }
                return 1;
            }

            if (!(arg1 instanceof Column)) {
                return -1;
            }

            Column c0 = (Column) arg0;
            Column c1 = (Column) arg1;
            return c0.getShowIndex() - c1.getShowIndex();
        }

    }

    static class Column implements Comparable, Cloneable {

        private int index;

        private String name;

        private boolean selected;

        private int weight;

        private int showIndex;

        private boolean hidden;

        Column(int columnIndex, String columnName) {
            index = columnIndex;
            showIndex = index;
            name = columnName;
        }

        int getIndex() {
            return index;
        }

        String getName() {
            return name;
        }

        void setName(String name) {
            this.name = name;
        }

        boolean isSelected() {
            return selected;
        }

        void setSelected(boolean selected) {
            this.selected = selected;
        }

        int getWeight() {
            return weight;
        }

        void setWeight(int weight) {
            this.weight = weight;
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof Column)) {
                return false;
            }
            Column column = (Column) obj;
            if ((column.getIndex() == this.index)
                && (column.getName().equals(this.name))
                && column.isSelected() == this.selected) {
                return true;
            }
            return false;
        }

        public int getShowIndex() {
            return showIndex;
        }

        public void setShowIndex(int showIndex) {
            this.showIndex = showIndex;
        }

        public int compareTo(Object arg) {
            if (!(arg instanceof Column)) {
                return 1;
            }
            Column other = (Column) arg;
            return index - other.index;
        }

        public Object clone() {
            Column clone = new Column(index, name);
            clone.setSelected(selected);
            clone.setShowIndex(showIndex);
            clone.setWeight(weight);
            return clone;
        }

        /**
         * @return the hidden
         */
        public boolean isHidden() {
            return hidden;
        }

        /**
         * @param hidden the hidden to set
         */
        public void setHidden(boolean hidden) {
            this.hidden = hidden;
        }

    }

    static class SelectionListModel extends DefaultListModel implements ChangeListener {

        private List columnList = new ArrayList();

        synchronized Column[] getColumns() {
            JCheckBox jcb;
            Column column;
            for (int i = 0; i < getSize() && i < columnList.size(); i++) {
                jcb = (JCheckBox) getElementAt(i);
                column = (Column) columnList.get(i);
                column.setSelected(jcb.isSelected());
            }
            return (Column[]) columnList.toArray(new Column[columnList.size()]);
        }

        public synchronized void addElement(Column column) {
            if (column != null) {
                columnList.add(column);
                JCheckBox checkbox = new JCheckBox(column.getName(), column.isSelected());
                checkbox.addChangeListener(this);
                super.addElement(checkbox);
            }
        }

        public synchronized void removeAllElements() {
            super.removeAllElements();
            columnList.clear();
        }

        public synchronized void stateChanged(ChangeEvent e) {
            if (!(e.getSource() instanceof JCheckBox)) {
                return;
            }
            JCheckBox item = (JCheckBox) e.getSource();
            if (item.isSelected()) {
                return;
            }
            for (int i = 0; i < getSize(); i++) {
                JCheckBox tmp = (JCheckBox) super.getElementAt(i);
                if (tmp.isSelected()) {
                    return;
                }
            }
            item.setSelected(true);
        }

        synchronized int up(int index) {
            if (index < 1) {
                return index;
            }

            Column column = (Column) columnList.remove(index);
            columnList.add(index - 1, column);

            JCheckBox jcb = (JCheckBox) super.remove(index);
            super.add(index - 1, jcb);
            return index - 1;
        }

        synchronized int down(int index) {
            if (index < 0 || index > getSize() - 2) {
                return index;
            }
            Column column = (Column) columnList.remove(index);
            columnList.add(index + 1, column);

            JCheckBox jcb = (JCheckBox) super.remove(index);
            super.add(index + 1, jcb);
            return index + 1;
        }
    }

    class ColumnSelection extends JDialog implements ActionListener, ListSelectionListener {

        private Component owner;

        private CheckBoxList cbl;
        private SelectionListModel listModel = new SelectionListModel();

        private JButton up_JB;
        private JButton down_JB;

        ColumnSelection(String title) {
            initialize();
            super.setTitle(title);
        }

        ColumnSelection(Dialog dialog, String title) {
            super(dialog, title, true);
            owner = dialog;
            initialize();
        }

        ColumnSelection(Frame dialog, String title) {
            super(dialog, title, true);
            owner = dialog;
            initialize();
        }

        private void initialize() {

            Container container = this.getContentPane();
            container.setLayout(new GridBagLayout());

            GridBagConstraints gbc = new GridBagConstraints();

            gbc.weightx = 1;
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            JLabel label = new JLabel("<html>" + getString("COLUMN_SELECTION_MSG") + "</html>");
            container.add(label, gbc);

            cbl = new CheckBoxList(listModel);
            cbl.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            cbl.addListSelectionListener(this);
            JScrollPane jsp = new JScrollPane(cbl);
            gbc.gridy = 1;
            gbc.weighty = 5;
            gbc.fill = GridBagConstraints.BOTH;
            container.add(jsp, gbc);

            JPanel movebuttonJP = new JPanel(new GridBagLayout());
            up_JB = new JButton(getString("UP"));
            up_JB.setEnabled(false);
            up_JB.setActionCommand("UP");
            up_JB.addActionListener(this);
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.weightx = 1;
            gbc.weighty = 1;
            gbc.insets = new Insets(0, 0, 5, 0);
            gbc.fill = GridBagConstraints.NONE;
            movebuttonJP.add(up_JB, gbc);

            down_JB = new JButton(getString("DOWN"));
            down_JB.setEnabled(false);
            down_JB.setActionCommand("DOWN");
            down_JB.addActionListener(this);
            up_JB.setPreferredSize(down_JB.getPreferredSize());
            gbc.gridy = 1;
            movebuttonJP.add(down_JB, gbc);

            gbc.gridx = 1;
            gbc.gridy = 1;
            gbc.weightx = 0;
            gbc.insets = new Insets(5, 0, 5, 5);
            gbc.anchor = GridBagConstraints.NORTH;
            container.add(movebuttonJP, gbc);

            JButton ok_JB = new JButton(getString("OK"));
            ok_JB.setActionCommand(CustomColumnTable.SCD_OK_COMMAND);
            ok_JB.addActionListener(CustomColumnTable.this);
            JButton cancel_JB = new JButton(getString("CANCEL"));
            ok_JB.setPreferredSize(cancel_JB.getPreferredSize());
            cancel_JB.setActionCommand(CustomColumnTable.SCD_CANCEL_COMMAND);
            cancel_JB.addActionListener(CustomColumnTable.this);

            JPanel okbuttonJP = new JPanel(new GridBagLayout());
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.weighty = 1;
            gbc.insets = new Insets(0, 0, 0, 5);
            gbc.fill = GridBagConstraints.NONE;
            gbc.anchor = GridBagConstraints.EAST;
            okbuttonJP.add(ok_JB, gbc);
            gbc.gridx = 1;
            gbc.insets.right = 0;
            gbc.anchor = GridBagConstraints.WEST;
            okbuttonJP.add(cancel_JB, gbc);

            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.gridwidth = 2;
            gbc.weighty = 0;
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.anchor = GridBagConstraints.EAST;
            container.add(okbuttonJP, gbc);
        }

        public void show(Column[] columns) {
            valueChanged(null);
            Arrays.sort(columns, columnShowIndexComparable);
            listModel.removeAllElements();
            for (int i = 0; i < columns.length; i++) {
                listModel.addElement(columns[i]);
            }
            super.pack();
            super.setLocationRelativeTo(owner);
            super.setVisible(true);
        }

        Column[] getCoulmn() {
            return listModel.getColumns();
        }

        //        public int[] getSelectedIndex() {
        //            List indexList = new ArrayList();
        //            for (int i = 0; i < listModel.getSize(); i++) {
        //                JCheckBox checkbox = (JCheckBox) listModel.getElementAt(i);
        //                if (checkbox.isSelected()) {
        //                    indexList.add(new Integer(i));
        //                }
        //            }
        //            int counter = 0;
        //            int[] indexes = new int[indexList.size()];
        //            for (Iterator ite = indexList.iterator(); ite.hasNext();) {
        //                Integer index = (Integer) ite.next();
        //                indexes[counter++] = index.intValue();
        //            }
        //            return indexes;
        //        }

        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            if (command == null) {
                return;
            }
            if ("UP".equals(command)) {
                int selectedIndex = cbl.getSelectedIndex();
                if (selectedIndex < 1) {
                    return;
                }
                int afterindex = listModel.up(selectedIndex);
                cbl.setSelectedIndex(afterindex);
                cbl.scrollRectToVisible(cbl.getCellBounds(afterindex, afterindex));

            } else if ("DOWN".equals(command)) {
                int selectedIndex = cbl.getSelectedIndex();
                if (selectedIndex < 0) {
                    return;
                }
                int afterindex = listModel.down(selectedIndex);
                cbl.setSelectedIndex(afterindex);
                cbl.scrollRectToVisible(cbl.getCellBounds(afterindex, afterindex));

            }
        }

        public void valueChanged(ListSelectionEvent e) {
            int index = cbl.getSelectedIndex();
            if (index < 0) {
                up_JB.setEnabled(false);
                down_JB.setEnabled(false);
                return;
            }
            up_JB.setEnabled(index > 0);
            down_JB.setEnabled(index < listModel.size() - 1);
        }

    }

    public class PopupSortMouseAdapter extends MouseAdapter {

        private SortFilterModel model;

        public void mouseClicked(MouseEvent event) {
            if (javax.swing.SwingUtilities.isRightMouseButton(event)) {
                if (popup == null) {
                    return;
                }
                if (CustomColumnTable.this.isEnabledPopup) {
                    popup.show(CustomColumnTable.this.getTableHeader(), event.getX(), event.getY());
                }
            } else if (javax.swing.SwingUtilities.isLeftMouseButton(event)) {
                if (event.getClickCount() < 1) {
                    return;
                }
                // if mouse cursor is on the border of columns, the it is not for sorting.
                Cursor cur = CustomColumnTable.this.getTableHeader().getCursor();
                if (cur.getType() == Cursor.E_RESIZE_CURSOR) {
                    return;
                }

                int tableColumn = columnAtPoint(event.getPoint());
                int modelColumn = convertColumnIndexToModel(tableColumn);

                int[] items = getDisplayedColumns();
                if (items == null || items.length < modelColumn) {
                    return;
                }

                int counter = 0;

                Column[] columns = orderedColumns;
                for (int i = 0; i < columns.length; i++) {
                    if (columns[i].isSelected()) {
                        if (counter == modelColumn) {
                            model.sort(columns[i].getIndex());
                        }
                        counter++;
                    }
                }

                clearSelection();
            } else {
                return;
            }
        }

        public void setModel(SortFilterModel model) {
            this.model = model;
        }
    }

    private class ColumnWidthSaveAdapter extends MouseAdapter {

        private boolean started;

        public ColumnWidthSaveAdapter() {
            super();
            started = false;
        }

        @Override
        public void mousePressed(MouseEvent event) {
            if (javax.swing.SwingUtilities.isLeftMouseButton(event)) {
                started = false;

                Cursor cur = CustomColumnTable.this.getTableHeader().getCursor();
                if (cur.getType() == Cursor.E_RESIZE_CURSOR) {
                    started = true;
                }
            }
        }

        @Override
        public void mouseReleased(MouseEvent event) {
            if (javax.swing.SwingUtilities.isLeftMouseButton(event)) {
                if (!started)
                    return;
                if (CustomColumnTable.this.sortFilterModel == null)
                    return;

                Point pt = new Point(event.getX() - 3, event.getY());
                int vc = CustomColumnTable.this.columnAtPoint(pt);
                if (vc >= 0) {
                    TableColumn tc = CustomColumnTable.this.getColumnModel().getColumn(vc);
                    Class c = CustomColumnTable.this.sortFilterModel.getClass();
                    //TODO use println when you check which preference path is used to save/load table header column width.
                    if (!c.getName().startsWith("com.cysols.cwt")) {
                        for (int i = 0; i < CustomColumnTable.this.getColumnCount(); i++) {
                            TableColumn column = CustomColumnTable.this.getColumnModel().getColumn(
                                i);
                            String columnName = column.getHeaderValue().toString();
                            if (tc.getHeaderValue().toString().equals(columnName)) {
                                Preferences prefs = Preferences.userNodeForPackage(c);
                                String prefix = "";
                                String prefKey = "";
                                if (c != null) {
                                    prefix = c.getSimpleName();
                                }
                                prefKey = prefix + "_" + columnName;
                                if (isValidKeyLength(prefKey, columnName)) {
                                    try {
                                        prefs.putInt(prefKey, column.getWidth());
                                    } catch (Exception e) {

                                    }
                                }

                                try {
                                    prefs.flush();
                                } catch (BackingStoreException e) {
                                }
                                break;
                            }
                        }
                    }
                    event.consume();
                }
            }
        }
    }

    public class PopupMouseAdapter extends MouseAdapter {

        public void mouseClicked(MouseEvent event) {
            if (!javax.swing.SwingUtilities.isRightMouseButton(event)) {
                return;
            }
            if (popup == null) {
                return;
            }
            if (CustomColumnTable.this.isEnabledPopup) {
                popup.show(CustomColumnTable.this.getTableHeader(), event.getX(), event.getY());
            }
        }
    }

    public void setColumnWidth(int width, int minWidth) {
        setColumnWidth(width, minWidth, null);
    }

    public void setMinWidthOfColumn(int width, String columnName) {
        this.minWidthOfColumn = width;
        this.columnName = columnName;
        numberOfSmallColumn = 1;

    }

    public void setColumnWidth(int width, int minWidth, Map<String, Integer> columnNameWidthMap) {
        this.minWidth = minWidth;
        if (this.columnNameWidthMap == null) {
            this.columnNameWidthMap = columnNameWidthMap;
        }

        this.width = width;
        setAutoResizeMode(AUTO_RESIZE_OFF);
        int preWidth = (this.width) / getColumnCount();
        this.width = preWidth * getColumnCount();
        if (columnName != null && getColumnCount() > 1) {
            preWidth += (preWidth - minWidthOfColumn + 5)
                / (getColumnCount() - numberOfSmallColumn);
        }
        if (minWidth > 0 && preWidth < minWidth) {
            preWidth = minWidth;
        }

        TableColumn column = null;
        for (int i = 0; i < getColumnCount(); i++) {
            column = getColumnModel().getColumn(i);
            int columnWidth = column.getWidth();
            if (getColumnCount() > 1
                && columnName != null
                && columnName.equals(column.getHeaderValue().toString())) {
                columnWidth = minWidthOfColumn;
                if (this.columnNameWidthMap != null) {
                    Integer prefferedwitdth = this.columnNameWidthMap.get(column.getHeaderValue().toString());
                    if (prefferedwitdth != null && columnWidth < prefferedwitdth) {
                        columnWidth = prefferedwitdth;
                    }
                }
                column.setPreferredWidth(columnWidth);
                column.setMinWidth(minWidthOfColumn);
                column.setMaxWidth(this.width);
                continue;
            }
            if (preWidth > columnWidth) {
                columnWidth = preWidth;
            }
            if (this.columnNameWidthMap != null) {
                Integer prefferedwitdth = this.columnNameWidthMap.get(column.getHeaderValue().toString());
                if (prefferedwitdth != null && columnWidth < prefferedwitdth) {
                    columnWidth = prefferedwitdth;
                }
            }

            column.setPreferredWidth(columnWidth);
            if (minWidth >= 0) {
                column.setMinWidth(minWidth);
            } else {
                column.setMinWidth(preWidth);
            }
            column.setMaxWidth(this.width);
        }
        this.doLayout();
    }

    public void setColumnWidth(int width) {
        this.setColumnWidth(width, this.minWidth);
    }

    public SortFilterModel getSortFilterModel() {
        return sortFilterModel;
    }

    public void setHiddenColumns() {
        setHiddenColumns(hiddenIndices);
    }

    public void setHiddenColumns(int[] indexes) {
        hiddenIndices = indexes;
        if (indexes == null || indexes.length <= 0) {
            return;
        }
        for (int i = 0; i < indexes.length; i++) {
            for (Iterator iterator = columnList.iterator(); iterator.hasNext();) {
                Column column = (Column) iterator.next();
                if (column.getIndex() == indexes[i]) {
                    column.setHidden(true);
                    Component com = popup.getComponent(indexes[i]);
                    if (com instanceof JCheckBoxMenuItem) {
                        JCheckBoxMenuItem item = (JCheckBoxMenuItem) com;
                        item.setSelected(false);
                    }
                }
            }
        }

        for (int i = 0; i < popup.getSubElements().length; i++) {
            Component com = popup.getComponent(i);
            if (!(com instanceof JCheckBoxMenuItem)) {
                continue;
            }
            JCheckBoxMenuItem item = (JCheckBoxMenuItem) com;
            boolean isDisplay = false;
            for (int c = 0; c < indexes.length; c++) {
                if (indexes[c] == i) {
                    isDisplay = true;
                    continue;
                }
            }
            if (isDisplay) {
                item.setVisible(false);
            }
        }
    }

    public static void main(String[] args) {
        AbstractTableModel model = new AbstractTableModel() {

            String[] columns = { "A", "B", "C", "D", "E" };

            public int getColumnCount() {
                return columns.length;
            }

            public int getRowCount() {
                return 20;
            }

            public Object getValueAt(int rowIndex, int columnIndex) {
                return "(" + columns[columnIndex] + "," + rowIndex + ") " + "DATA";
            }

            public String getColumnName(int index) {
                return columns[index];
            }
        };

        TableCellRenderer renderer = new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(
                JTable table,
                Object value,
                boolean isSelected,
                boolean hasFocus,
                int row,
                int column) {

                setText(value.toString());
                if (column == 3) {
                    setBackground(Color.RED);
                } else {
                    setBackground(Color.YELLOW);
                }
                return this;
            }
        };
        JFrame frame = new JFrame();
        CustomColumnTable table = new CustomColumnTable(frame, model, renderer);
        table.setSelectedColumns(new int[] { 1, 2 });
        ColumnGroup cg = new ColumnGroup("Default", new int[] { 3, 4 });
        table.addColumnGroup(cg);
        table.addColumnGroup(new ColumnGroup("Group1", new int[] { 0, 2 }));
        // table.setColumnWith();
        JScrollPane panel = new JScrollPane(table);
        frame.getContentPane().add(panel, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
    }

    private boolean isValidKeyLength(String key, String columnName) {
        if (key != null && key.length() < MAX_PREF_KEY_LENGTH) {
            return true;
        } else {
            System.err.println("columnName : "
                + columnName
                + " is too long. Unable to save Column width.");
            return false;
        }
    }

}
