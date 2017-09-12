package com.school.fees.search;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.banti.framework.core.SchoolOpertaionAction;
import com.banti.framework.platform.InternalWindow;
import com.banti.framework.table.CustomMaintainTable;
import com.school.console.SchoolMain;
import com.school.constant.OperationConstant;
import com.school.fees.StudentFeesMasterDetailsTableModel;
import com.school.fees.report.ReciptModel;
import com.school.fees.report.ReciptWindow;
import com.school.hiebernate.dbo.DepositeFeeMaster;
import com.school.resource.CommandConstant;
import com.school.resource.ResourcesUtils;
import com.school.utils.MsgDialogUtils;

public class DepositeFeeSearchWindow extends InternalWindow implements
		ActionListener {
	private StudentFeesMasterDetailsTableModel feesMasterDetailsTableModel;
	private CustomMaintainTable table;
	private JPopupMenu popupMenu;
	private AbstractAction viewReciept;
	private SchoolOpertaionAction csvOutput;
	private long totalamount = 0;

	public DepositeFeeSearchWindow(List<DepositeFeeMaster> list, long amount,
			String title) {
		super(title, true, true, true, true);
		feesMasterDetailsTableModel = new StudentFeesMasterDetailsTableModel(
				list);
		this.totalamount = amount;
		init();
		setSize(900, 600);
		setVisible(true);
	}

	private void init() {
		createPopupMenu();

		table = new CustomMaintainTable(SchoolMain.Frame,
				feesMasterDetailsTableModel, feesMasterDetailsTableModel);
		table.setDefaultRenderer(Object.class, feesMasterDetailsTableModel);
		table.getTableHeader().setReorderingAllowed(false);
		table.setDefaultSelectedColumns(feesMasterDetailsTableModel
				.getSetPrefColIndex());
		table.setPreferredColumns(feesMasterDetailsTableModel
				.getSetPrefColIndex());
		table.setMinimumSize(new Dimension(150, 150));
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		// table.setLocation(100, 100);
		table.setColumnWidth(350, 100);
		table.setOpaque(true);
		feesMasterDetailsTableModel.fireTableDataChanged();

		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		table.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					@Override
					public void valueChanged(ListSelectionEvent arg0) {
						updateButton();
					}
				});
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3) {
					popupMenu.show(table, e.getX(), e.getY());
				}
			}
		});
		JScrollPane scrollPane = new JScrollPane(table);

		setLayout(new BorderLayout());
		add(scrollPane, BorderLayout.CENTER);
		add(getButtonPanel(), BorderLayout.SOUTH);
		setButtonEnable(false);
	}

	private void updateButton() {
		if (table.getSelectedRow() != -1) {
			viewReciept.setEnabled(true);

		}
	}

	private void setButtonEnable(boolean enable) {
		// btnRegister.setEnabled(enable);
		viewReciept.setEnabled(enable);
	}

	private JPanel getButtonPanel() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JButton btnCSV = new JButton(csvOutput);
		JButton reciept = new JButton(viewReciept);
		panel.add(btnCSV);
		panel.add(reciept);
		reciept.setFocusable(true);
		panel.add(new JLabel("Total Deposited Amount: "));
		JLabel lb = new JLabel(totalamount + "  ");
		lb.setBackground(Color.pink);
		lb.setOpaque(true);
		panel.add(lb);
		return panel;
	}

	private void createPopupMenu() {
		viewReciept = new AbstractAction("Deposit Fees Receipt") {
			@Override
			public void actionPerformed(ActionEvent e) {
				new ReciptWindow(
						new ReciptModel(feesMasterDetailsTableModel.get(table
								.getSelectedRow())));

			}
		};
		csvOutput = new SchoolOpertaionAction(
				ResourcesUtils.getString("CSV_OUTPUT"),
				OperationConstant.DEPOSITE_FEE_CSV_OUTPUT);
		csvOutput.setEnabled(true);
		csvOutput.registerActionListener(this);
		popupMenu = new JPopupMenu();
		JMenuItem reciept = new JMenuItem(viewReciept);
		popupMenu.add(reciept);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (CommandConstant.REGISTER_COMMAND.equals(e.getActionCommand())) {
			new ReciptWindow(new ReciptModel(
					feesMasterDetailsTableModel.get(table.getSelectedRow())));
		} else if (CommandConstant.UPDATE_COMMAND.equals(e.getActionCommand())) {

		} else if (OperationConstant.DEPOSITE_FEE_CSV_OUTPUT.equals(e
				.getActionCommand())) {
			JFileChooser fc = new JFileChooser();
			fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
			int returnVal = fc.showDialog(this, "SAVE");

			// Process the results.
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				if (!(file.getName().endsWith(".csv") || file.getName()
						.endsWith(".CSV"))) {
					file = new File(file.getAbsolutePath() + ".csv");
				}
				table.getColumnCount();
				if (feesMasterDetailsTableModel.writeToFile(file)) { 
					MsgDialogUtils.showInformationDialog(this, "CSV Output",
							"Fee Collection details written successfully \n File: "
									+ file.getAbsolutePath());
				}
			}
			}
		}
		/*
		 * public boolean writeToFile(File file) { int[] columns =
		 * feesMasterDetailsTableModel.getSetPrefColIndex(); BufferedWriter bw =
		 * null; try { if (!file.getParentFile().exists()) {
		 * file.getParentFile().mkdirs(); } bw = new BufferedWriter(new
		 * FileWriter(file));
		 * bw.write(feesMasterDetailsTableModel.getHeader(columns));
		 * bw.newLine(); for (int i = 0; i < dataList.size(); i++) {
		 * bw.write(getCSVData(columns, i)); bw.newLine(); } } catch
		 * (IOException e) { Logger.EXCEPTION.log(Level.WARNING,
		 * "Error occured while writing file", e); return false; } finally { if
		 * (bw != null) { try { bw.close(); } catch (IOException e) { } } }
		 * return true; }
		 */

	
}
