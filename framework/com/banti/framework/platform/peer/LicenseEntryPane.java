package com.banti.framework.platform.peer;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import com.banti.framework.platform.Activator;
import com.banti.framework.platform.Application;
import com.banti.framework.platform.Command;
import com.banti.framework.platform.LicenseManager;
import com.banti.framework.platform.PluginCommand;
import com.banti.framework.platform.PluginModule;
import com.banti.framework.theme.ToolKit;

/**
 * TODO
 */
public class LicenseEntryPane extends JPanel implements ActionListener,
		ListSelectionListener {

	private List pluggedCommands;
	private Set activatorSet;

	private JButton entryButton;
	private JButton deleteButton;
	private JButton closeButton;
	private JTable table;

	private LicenseManager manager;

	public LicenseEntryPane(LicenseManager manager) {
		super(new BorderLayout());

		this.manager = manager;
		this.pluggedCommands = Collections.synchronizedList(new ArrayList());
		this.activatorSet = new HashSet();

		ToolKit tool = ToolKit.getInstance();
		// Initializes Entry Button
		entryButton = new JButton(tool.getString("ENTRY"));
		entryButton.setEnabled(false);
		entryButton.setActionCommand("ENTRY");
		entryButton.addActionListener(this);
		// Initializes Delete Button
		deleteButton = new JButton(tool.getString("DELETE"));
		deleteButton.setEnabled(false);
		deleteButton.setActionCommand("DELETE");
		deleteButton.addActionListener(this);
		// Initializes Close Button
		closeButton = new JButton(tool.getString("CLOSE"));
		closeButton.setActionCommand("CLOSE");
		closeButton.addActionListener(this);

		// Deploys buttons on Button Panel
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(entryButton);
		buttonPanel.add(deleteButton);
		buttonPanel.add(closeButton);

		super.add(buttonPanel, BorderLayout.SOUTH);
	}

	public void addCloseActionListener(ActionListener al) {
		closeButton.addActionListener(al);
	}

	private void addPluggedCommand(Command[] commands) {
		for (int i = 0; i < commands.length; i++) {
			if (commands[i] instanceof PluginModule) {
				PluginModule module = (PluginModule) commands[i];
				Activator activator = module.getActivator();
				if (activator.getModuleName() != null
						&& !activatorSet.contains(activator)) {
					pluggedCommands.add(commands[i]);
					activatorSet.add(activator);
				}
				this.addPluggedCommand(module.getCommands());

			} else if (commands[i] instanceof PluginCommand) {
				PluginCommand cmd = (PluginCommand) commands[i];
				Activator activator = cmd.getActivator();
				if (activator.getModuleName() != null
						&& !activatorSet.contains(activator)) {
					pluggedCommands.add(commands[i]);
					activatorSet.add(activator);
				}
			}
		}
	}

	private void addPluggedModule(PluginModule[] modules) {
		for (int i = 0; i < modules.length; i++) {
			Activator activator = modules[i].getActivator();
			if (activator.getModuleName() != null
					&& !activatorSet.contains(activator)) {
				pluggedCommands.add(modules[i]);
				activatorSet.add(activator);
			}
			this.addPluggedCommand(modules[i].getCommands());
		}
	}

	public final void initialize(PluginCommand[] pluggedCommands,
			PluginModule[] pluggedModules) {
		this.addPluggedCommand(pluggedCommands);
		this.addPluggedModule(pluggedModules);

		// Initializes JTable
		table = new JTable(new InnerTableModel());
		table.getTableHeader().setReorderingAllowed(false);

		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		ListSelectionModel lsm = table.getSelectionModel();
		lsm.addListSelectionListener(this);
		table.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent event) {
				if (KeyEvent.VK_DELETE == event.getKeyCode()) {
					if (deleteButton.isEnabled()) {
						actionPerformed(new ActionEvent(table, event.getID(),
								"DELETE"));
					} else {
						event.consume();
					}
				}
			}
		});
		super.add(new JScrollPane(table), BorderLayout.CENTER);
	}

	public final void actionPerformed(ActionEvent ae) {
		if (ae == null)
			return;
		String action = ae.getActionCommand();

		if (action == null)
			return;

		ToolKit tool = ToolKit.getInstance();
		TableModel tm = table.getModel();

		if (action.equals("ENTRY")) {
			String enteredCode = JOptionPane.showInputDialog(this,
					tool.getString("LICENSE_ENTRY"));
			if (enteredCode == null) {
				return;
			}

			/*
			 * Calls LicenseManager.register method. The registration is
			 * delegated to LicenseManager.
			 */
			boolean entered = manager
					.register((PluginCommand) pluggedCommands.get(table
							.getSelectedRow()), enteredCode);
			if (manager.isResponseNull()) {
				ActionListener[] al = closeButton.getActionListeners();
				ActionEvent event = new ActionEvent(ae, ae.getID(), "CLOSE");
				for (int i = 0; i < al.length; i++) {
					al[i].actionPerformed(event);
				}
				return;
			}

			if (entered) {
				table.repaint();

				JOptionPane.showMessageDialog(Application.Frame,
						ToolKit.getString("MSG_LICENSE_VALID"),
						ToolKit.getString("INFORMATION"),
						JOptionPane.INFORMATION_MESSAGE);
				String selectedLicence = (String) tm.getValueAt(
						table.getSelectedRow(), 0);
				if (manager.MAIN_LICENCE.equals(selectedLicence)) {
					entryButton.setEnabled(true);
					deleteButton.setEnabled(false);
				} else {
					entryButton.setEnabled(false);
					deleteButton.setEnabled(true);
				}

			} else {
				JOptionPane.showMessageDialog(Application.Frame,
						tool.getString("MSG_LICENSE_INVALID"),
						tool.getString("WARNING"), JOptionPane.WARNING_MESSAGE);

			}

		} else if (action.equals("DELETE")) {
			int answer = JOptionPane.showConfirmDialog(Application.Frame,
					tool.getString("MSG_LICENSE_DELETE_CONFIRMATION"),
					tool.getString("CONFIRMATION"), JOptionPane.YES_NO_OPTION);

			if (answer == JOptionPane.YES_OPTION) {
				/*
				 * Calls LicenseManager.delete method. The deletionis delegated
				 * to LicenseManager.
				 */
				boolean deleted = manager
						.delete((PluginCommand) pluggedCommands.get(table
								.getSelectedRow()));
				if (manager.isResponseNull()) {
					ActionListener[] al = closeButton.getActionListeners();
					ActionEvent event = new ActionEvent(ae, ae.getID(), "CLOSE");
					for (int i = 0; i < al.length; i++) {
						al[i].actionPerformed(event);
					}
					return;
				}

				if (deleted) {
					tm.setValueAt(new String(), table.getSelectedRow(), 1);
					table.repaint();

					JOptionPane.showMessageDialog(Application.Frame,
							ToolKit.getString("MSG_LICENSE_DELETE_COMPLETED"),
							ToolKit.getString("INFORMATION"),
							JOptionPane.INFORMATION_MESSAGE);

					entryButton.setEnabled(true);
					deleteButton.setEnabled(false);

				} else {
					JOptionPane.showMessageDialog(Application.Frame,
							tool.getString("MSG_DELETE_FAILED"),
							tool.getString("WARNING"),
							JOptionPane.WARNING_MESSAGE);
				}
			}

		} else if (action.equals("CLOSE")) {
			super.setVisible(false);
		}
	} /*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event
	 * .ListSelectionEvent)
	 */

	public final void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting())
			return;

		ListSelectionModel lsm = (ListSelectionModel) e.getSource();
		if (lsm.isSelectionEmpty()) {
			// ignore
		} else {
			int selectedRow = table.getSelectedRow();
			TableModel tm = table.getModel();
			String selectedCode = (String) tm.getValueAt(selectedRow, 1);
			String selectedLicence = (String) tm.getValueAt(selectedRow, 0);
			if (manager.MAIN_LICENCE.equals(selectedLicence)) {
				entryButton.setEnabled(true);
				//deleteButton.setEnabled(true);
				deleteButton.setEnabled(false);
				return;
			}
			if (selectedCode == null || selectedCode.trim().equals("")) {
				entryButton.setEnabled(true);
				deleteButton.setEnabled(false);
			} else {
				Activator activator = ((PluginCommand) pluggedCommands
						.get(selectedRow)).getActivator();
				if (activator.isNoLicense()) {
					entryButton.setEnabled(false);
					deleteButton.setEnabled(false);
				} else {
					entryButton.setEnabled(false);
					deleteButton.setEnabled(true);
				}
			}
		}

	}

	// ---------------------InnerTableModel class
	// -----------------------------------------------
	private class InnerTableModel extends AbstractTableModel {

		private String[] columnNames;

		private InnerTableModel() {
			super();

			ToolKit tool = ToolKit.getInstance();
			columnNames = new String[2];
			columnNames[0] = tool.getString("PLUGGED_MODULE");
			columnNames[1] = tool.getString("LICENSE_CODE");
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.table.TableModel#getColumnCount()
		 */
		public int getColumnCount() {
			return columnNames.length;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.table.TableModel#getRowCount()
		 */
		public int getRowCount() {
			return pluggedCommands.size();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.table.TableModel#getColumnName(int column)
		 */
		public String getColumnName(int column) {
			return columnNames[column];
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.table.TableModel#getValueAt(int, int)
		 */
		public Object getValueAt(int rowIndex, int columnIndex) {
			Activator activator = ((PluginCommand) pluggedCommands
					.get(rowIndex)).getActivator();
			switch (columnIndex) {
			case 0:
				return activator.getModuleName();

			case 1:
				String value;
				if (activator.isNoLicense()) {
					ToolKit tool = ToolKit.getInstance();
					value = tool.getString("NO_LICENSE");
				} else {
					/*
					 * Calls LicenseManager.read method. The license code is
					 * obtained through LicenseManager.
					 */
					value = manager.read((PluginCommand) pluggedCommands
							.get(rowIndex));
					if (value.startsWith(LicenseManager.trialCodePreFix)) {
						value = value.substring(LicenseManager.trialCodePreFix
								.length());
					}
				}

				return value;

			default:
				return null;
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.table.TableModel#getColumnClass(int)
		 */
		public Class getColumnClass(int columnIndex) {
			return String.class;
		}

	} // End of InnerTableModel class

}
