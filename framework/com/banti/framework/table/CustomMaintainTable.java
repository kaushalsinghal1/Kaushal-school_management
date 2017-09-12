package com.banti.framework.table;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

/**
 * Maintain the column data of customized table.
 *
 */
public class CustomMaintainTable extends CustomColumnTable {

    private static final String SAVE_COMMAND = "SAVE_COMMAND";
    private String tablename;
    private JMenuItem saveMenuItem;
    private boolean loaded = false;
    private boolean isInitialized = false;

    private int[] savedselecting;

    /**
     * Initializes with owner component.
     * @param owner
     */
    public CustomMaintainTable(Frame owner) {
        super(owner);
        isInitialized = true;
    }

    /**
     * Initializes with owner component.
     * @param owner
     */
    public CustomMaintainTable(Dialog owner) {
        super(owner);
        isInitialized = true;
    }

    /**
     * Initializes with owner component, table model.
     * @param owner owner component
     * @param model table model
     */
    public CustomMaintainTable(Frame owner, TableModel model) {
        super(owner, model);
        isInitialized = true;
        load(model.getClass().getName());
    }

    /**
     * Initializes with owner component, table model.
     * @param owner owner component
     * @param model table model
     * @param name table name to maintain to file.
     */
    public CustomMaintainTable(Frame owner, String name, TableModel model) {
        super(owner, model);
        isInitialized = true;
        load(name);
    }

    /**
     * Initializes with owner component and table model.
     * @param owner owner component
     * @param model table model
     */
    public CustomMaintainTable(Dialog owner, TableModel model) {
        super(owner, model);
        isInitialized = true;
        load(model.getClass().getName());
    }

    /**
     * Initializes with owner component and table model.
     * @param owner owner component
     * @param name table name to maintain to file.
     * @param model table model
     */
    public CustomMaintainTable(Dialog owner, String name, TableModel model) {
        super(owner, model);
        isInitialized = true;
        load(name);
    }

    /**
     * Initializes with owner component, SortFilterModel.
     * @param owner owner component
     * @param model table model
     */
    public CustomMaintainTable(Frame owner, SortFilterModel model) {
        super(owner, model);
        isInitialized = true;
        load(model.getClass().getName());
    }

    /**
     * Initializes with owner component, SortFilterModel.
     * @param owner owner component
     * @param model table model
     * @param name table name to maintain to file.
     */
    public CustomMaintainTable(Frame owner, String name, SortFilterModel model) {
        super(owner, model);
        isInitialized = true;
        load(name);
    }

    /**
     * Initializes with owner component and SortFilterModel.
     * @param owner owner component
     * @param model table model
     */
    public CustomMaintainTable(Dialog owner, SortFilterModel model) {
        super(owner, model);
        isInitialized = true;
        load(model.getClass().getName());
    }

    /**
     * Initializes with owner component and SortFilterModel.
     * @param owner owner component
     * @param name table name to maintain to file.
     * @param model table model
     */
    public CustomMaintainTable(Dialog owner, String name, SortFilterModel model) {
        super(owner, model);
        isInitialized = true;
        load(name);
    }

    /**
     * Initializes with owner component, table model and table renderer.
     * @param owner owner component
     * @param model table model
     * @param renderer table renderer
     */
    public CustomMaintainTable(Frame owner, TableModel model, TableCellRenderer renderer) {
        super(owner, model, renderer);
        isInitialized = true;
        load(model.getClass().getName());
    }

    /**
     * Initializes with owner component, table model and table renderer.
     * @param owner owner component
     * @param model table model
     * @param renderer table renderer
     * @param name table name to maintain to file.
     */
    public CustomMaintainTable(
        Frame owner,
        String name,
        TableModel model,
        TableCellRenderer renderer) {
        super(owner, model, renderer);
        isInitialized = true;
        load(name);
    }

    /**
     * Initializes with owner component, table model and table renderer.
     * @param owner owner component
     * @param model table model
     * @param renderer table renderer
     */
    public CustomMaintainTable(Dialog owner, TableModel model, TableCellRenderer renderer) {
        super(owner, model, renderer);
        isInitialized = true;
        load(model.getClass().getName());
    }

    /**
     * Initializes with owner component, table model and table renderer.
     * @param owner owner component
     * @param model table model
     * @param renderer table renderer
     * @param name table name to maintain to file.
     */
    public CustomMaintainTable(
        Dialog owner,
        String name,
        TableModel model,
        TableCellRenderer renderer) {
        super(owner, model, renderer);
        isInitialized = true;
        load(name);
    }

    /**
     * Initializes with owner component, table model and table renderer.
     * @param owner owner component
     * @param model SortFilterModel
     * @param renderer table renderer
     */
    public CustomMaintainTable(Frame owner, SortFilterModel model, TableCellRenderer renderer) {
        super(owner, model, renderer);
        isInitialized = true;
        load(model.getClass().getName());
    }

    /**
     * Initializes with owner component, table model and table renderer.
     * @param owner owner component
     * @param model SortFilterModel
     * @param renderer table renderer
     * @param name table name to maintain to file.
     */
    public CustomMaintainTable(
        Frame owner,
        String name,
        SortFilterModel model,
        TableCellRenderer renderer) {
        super(owner, model, renderer);
        isInitialized = true;
        load(name);
    }

    /**
     * Initializes with owner component, table model and table renderer.
     * @param owner owner component
     * @param model SortFilterModel
     * @param renderer table renderer
     */
    public CustomMaintainTable(Dialog owner, SortFilterModel model, TableCellRenderer renderer) {
        super(owner, model, renderer);
        isInitialized = true;
        load(model.getClass().getName());
    }

    /**
     * Initializes with owner component, table model and table renderer.
     * @param owner owner component
     * @param model SortFilterModel
     * @param renderer table renderer
     * @param name table name to maintain to file.
     */
    public CustomMaintainTable(
        Dialog owner,
        String name,
        SortFilterModel model,
        TableCellRenderer renderer) {
        super(owner, model, renderer);
        isInitialized = true;
        load(name);
    }

    /**
     * Sets the data model for this table to <code>newModel</code> and registers
     * with it for listener notifications from the new data model.
     */
    public void setModel(TableModel model) {
        super.setModel(model);

        if (model instanceof DefaultTableModel) {
            return;
        }
        load(model.getClass().getName());
    }

    public void setModel(SortFilterModel model) {
        super.setModel(model);
        load(model.getClass().getName());
    }

    public void setEnabled(boolean b) {
        super.setEnabled(b);
    }

    /**
     * Sets the data model for this table to <code>newModel</code> and registers
     * with it for listener notifications from the new data model.
     * 
     * @param name table name to maintain to file.
     * @param model table model
     */
    public void setModel(String name, TableModel model) {
        super.setModel(model);
        load(name);
    }

    protected void arrangColumnList(TableModel model) {
        super.arrangColumnList(model);
        if (saveMenuItem == null) {
            saveMenuItem = new JMenuItem(getString("SAVE"));
            saveMenuItem.setActionCommand(SAVE_COMMAND);
            saveMenuItem.addActionListener(this);
        }
        popup.add(saveMenuItem);
    }

    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (SAVE_COMMAND.equals(command)) {
        	  savedselecting = super.getDisplayedColumns();
            /* Column[] columns = (Column[]) columnList.toArray(new Column[columnList.size()]);
             ColumnDataParser parser = new ColumnDataParser();
             savedselecting = super.getDisplayedColumns();
             try {
                 parser.setColumnData(tablename, columns);
             } catch (XMLWriteException e1) {
             }*/

        } else {
            super.actionPerformed(e);
        }
    }

    private void load(String name) {
        if (!isInitialized) {
            //Executes nothing because not initialize yet.
            return;
        }
       /*  tablename = name;
             ColumnDataParser parser = new ColumnDataParser();
         Column[] columns = parser.getColumnData(tablename);
         if (columns == null) {
             return;
         }

         ArrayList list = new ArrayList();
         Column[] org_columns = (Column[]) columnList.toArray(new Column[columnList.size()]);
         for (int i = 0; i < org_columns.length && i < columns.length; i++) {
             if (columns[i].getName().equals(org_columns[i].getName())
                 && columns[i].getIndex() == org_columns[i].getIndex()) {
                 if (columns[i].isSelected()) {
                     list.add(new Integer(columns[i].getIndex()));
                 }
                 org_columns[i].setShowIndex(columns[i].getShowIndex());
             }
         }
         super.synctOrderedColumns();
         if (list.size() < 1) {
             return;
         }
         int[] indexes = new int[list.size()];
         int counter = 0;
         for (Iterator ite = list.iterator(); ite.hasNext(); counter++) {
             Integer integer = (Integer) ite.next();
             indexes[counter] = integer.intValue();
         }
         savedselecting = indexes;
         super.setSelectedColumns(indexes);
         loaded = true;*/
    }

    public void changeToSavedColumns() {
        if (savedselecting == null) {
            return;
        }
        super.setSelectedColumns(savedselecting);
    }

    public boolean isExistsSavedColumns() {
        if (!loaded || savedselecting == null) {
            return false;
        }
        return true;
    }

    public void setDefaultSelectedColumns(int[] indexes) {
        if (indexes == null || indexes.length < 1) {
            return;
        }
        if (!loaded) {
            super.setSelectedColumns(indexes);
            savedselecting = indexes;
        }
    }

    public static void main(String[] args) {
        final SortFilterModel model = new SortFilterModel() {

            String[] columns = { "A", "B", "C", "D", "E" };

            public int getColumnCount() {
                return columns.length;
            }

            public int getRowCount() {
                return 20;
            }

            public String getColumnName(int index) {
                return columns[index];
            }

            protected Object getUnSortedValueAt(int rowIndex, int columnIndex) {
                return "(" + columns[columnIndex] + "," + rowIndex + ") " + "DATA";
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
        final CustomMaintainTable table = new CustomMaintainTable(frame, model, renderer);
        table.setDefaultSelectedColumns(new int[] { 1, 2 });
        table.setPreferredColumns(new int[] { 3, 4 });
        ColumnGroup cg = new ColumnGroup("Default", new int[] { 3, 4 });
        table.addColumnGroup(cg);
        table.addColumnGroup(new ColumnGroup("Group1", new int[] { 0, 2 }));
        JScrollPane panel = new JScrollPane(table);
        JButton jb = new JButton("Reset");
        jb.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Calls the changeToSavedColumns method.");
                table.changeToSavedColumns();
            }
        });
        frame.getContentPane().add(panel, BorderLayout.CENTER);
        frame.getContentPane().add(jb, BorderLayout.SOUTH);
        frame.pack();
        frame.setVisible(true);
        int[] displaying = table.getDisplayedColumns();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < displaying.length; i++) {
            sb.append(displaying[i] + ", ");
        }
    }
}
