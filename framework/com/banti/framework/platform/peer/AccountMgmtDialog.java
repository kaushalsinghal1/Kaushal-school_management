package com.banti.framework.platform.peer;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import com.banti.framework.core.Account;
import com.banti.framework.platform.Application;
import com.banti.framework.platform.LoginManager;
import com.banti.framework.theme.ToolKit;
import com.banti.framework.ui.DialogEsc;
import com.banti.framework.utils.MDEncoder;

public class AccountMgmtDialog extends DialogEsc implements ActionListener,
		ListSelectionListener {
	private JButton entryButton;
	private JButton deleteButton;
	private JButton changeButton;
	private JButton closeButton;
	private JTable table;
	private ListSelectionModel rowSelect;

	private Account[] accounts;
	// private ArrayList<String> domains;
	private Collection<String> roles;
	private LoginManager manager;
	private String[] ColumnHeader = { ToolKit.getString("ACCOUNT"),
			ToolKit.getString("ROLE") };
	String selectedDomain;
	private DefaultTableModel tableModel;

	public AccountMgmtDialog(Frame frame, LoginManager manager) {
		super(frame, true);

		this.manager = manager;
	}

	public AccountMgmtDialog(Frame frame, LoginManager manager, boolean isKoban) {
		super(frame, true);
		this.manager = manager;
	}

	public AccountMgmtDialog(Dialog dialog, LoginManager manager) {
		super(dialog, true);

		this.manager = manager;
	}

	public boolean load() {
		this.accounts = manager.loadAccounts();
		this.roles = manager.loadRoles();
		if (this.accounts == null || this.roles == null) {
			return false;
		}
		return true;
	}

	private class NonEditableTableModel extends DefaultTableModel {

		public NonEditableTableModel(Object[] columnNames, int rowCount) {
			super(columnNames, rowCount);
		}

		public boolean isCellEditable(int row, int column) {

			return false;
		}

	}

	public void initComopnents() {
		super.setTitle(ToolKit.getString("ACCOUNT_MGMT"));

		// Base Layout
		GridBagLayout baseLayout = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		JPanel basePanel = new JPanel(baseLayout);
		JScrollPane tablePane = new JScrollPane();
		JPanel buttonPanel = new JPanel(new GridBagLayout());
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.fill = GridBagConstraints.BOTH;
		basePanel.add(tablePane, gbc);
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.weightx = 0.0;
		gbc.weighty = 0.0;
		gbc.anchor = GridBagConstraints.EAST;
		basePanel.add(buttonPanel, gbc);
		super.getContentPane().add(basePanel);

		// Initializes Entry Button
		entryButton = new JButton(ToolKit.getString("ADD_ACCOUNT"));
		entryButton.setActionCommand("ADD_ACCOUNT");
		entryButton.addActionListener(this);
		// Initializes Delete Button
		deleteButton = new JButton(ToolKit.getString("DELETE_ACCOUNT"));
		deleteButton.setActionCommand("DELETE_ACCOUNT");
		deleteButton.setEnabled(false);
		deleteButton.addActionListener(this);
		// Initializes Passwd Change Button
		changeButton = new JButton(ToolKit.getString("PASSWORD_CHANGE"));
		changeButton.setActionCommand("PASSWORD_CHANGE");
		changeButton.setEnabled(false);
		changeButton.addActionListener(this);
		// Initializes Close Button
		closeButton = new JButton(ToolKit.getString("CLOSE"));
		closeButton.setActionCommand("CLOSE");
		closeButton.addActionListener(this);

		// Deploys buttons on Button Panel
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;

		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(5, 5, 5, 5);
		buttonPanel.add(entryButton, c);

		c.gridx = 0;
		c.gridy = 1;
		c.insets = new Insets(5, 5, 5, 5);
		buttonPanel.add(changeButton, c);

		c.gridx = 0;
		c.gridy = 2;
		c.insets = new Insets(5, 5, 5, 5);
		buttonPanel.add(deleteButton, c);

		c.gridx = 0;
		c.gridy = 3;
		c.insets = new Insets(5, 5, 5, 5);
		buttonPanel.add(closeButton, c);

		// Initializes JTablet
		int numAccount = accounts.length;
		tableModel = new NonEditableTableModel(ColumnHeader, numAccount);
		table = new JTable(tableModel);
		table.setAutoCreateRowSorter(true);
		initTable(accounts, table, tableModel);
		table.setAutoscrolls(true);
		table.setRowSelectionAllowed(true);
		JViewport view = new JViewport();
		view.setView(table);
		tablePane.setPreferredSize(new Dimension(250, 150));
		tablePane.setViewport(view);
		tablePane
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		tablePane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		rowSelect = table.getSelectionModel();
		rowSelect.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		rowSelect.addListSelectionListener(this);
		table.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent event) {
				if (KeyEvent.VK_DELETE == event.getKeyCode()) {
					if (table.getSelectedRow() != -1) {
						actionPerformed(new ActionEvent(table, event.getID(),
								"DELETE_ACCOUNT"));
					} else {
						event.consume();
					}
				}
			}
		});

	}

	public final void actionPerformed(ActionEvent ae) {
		if (ae == null)
			return;
		String action = ae.getActionCommand();

		if (action == null)
			return;

		if (action.equals("CLOSE")) {
			super.dispose();
			return;

		} else if (action.equals("DELETE_ACCOUNT")) {

			int row = table.getSelectedRow();
			row = table.convertRowIndexToModel(row);
			String uname = (String) tableModel.getValueAt(row, 0);
			String domain = (String) tableModel.getValueAt(row, 1);
			Account acc = null;
			for (int i = 0; i < accounts.length; i++) {
				if (uname.equals(accounts[i].getName())) {
					acc = accounts[i];
				}
			}
			Account selectedAccount = acc;
			DefaultTableModel model = (DefaultTableModel) table.getModel();

			if (selectedAccount == null)
				return;
			Account activeAccount = Application.account;
			if (activeAccount.getName().equals(selectedAccount.getName())) {
				JOptionPane
						.showMessageDialog(
								this,
								ToolKit.getString("LOGGEDIN_ACCOUNT_CANNOT_BE_DELETED"),
								ToolKit.getString("WARNING"),
								JOptionPane.WARNING_MESSAGE);
				return;
			}
			String message = ToolKit.getString(
					"MSG_ACCOUNT_DELETE_CONFIRMATION", "ACCOUNT",
					selectedAccount.getName());
			int answer = JOptionPane.showConfirmDialog(this, message,
					ToolKit.getString("CONFIRMATION"),
					JOptionPane.YES_NO_OPTION);

			if (answer == JOptionPane.YES_OPTION) {

				if (manager.delete(selectedAccount)) {
					message = ToolKit.getString("MSG_ACCOUNT_DELETED",
							"ACCOUNT", selectedAccount.getName());
					JOptionPane.showMessageDialog(this, message,
							ToolKit.getString("INFORMATION"),
							JOptionPane.INFORMATION_MESSAGE);
					model.removeRow(row);
					model.fireTableDataChanged();
					accounts = manager.loadAccounts();
				} else {
					JOptionPane.showMessageDialog(this,
							ToolKit.getString("MSG_DELETE_FAILED"),
							ToolKit.getString("INFORMATION"),
							JOptionPane.WARNING_MESSAGE);
				}
			}
			return;

		} else {
			final JDialog dialog = new JDialog(this, true);
			JButton okButton;
			JButton cancelButton;

			okButton = new JButton(ToolKit.getString("OK"));

			if (action.equals("PASSWORD_CHANGE")) {
				int row = table.getSelectedRow();
				row = table.convertRowIndexToModel(row);
				String uname = (String) tableModel.getValueAt(row, 0);
				String domain = (String) tableModel.getValueAt(row, 1);
				Account acc = null;
				for (int i = 0; i < accounts.length; i++) {
					if (uname.equals(accounts[i].getName())) {
						acc = accounts[i];
					}
				}
				final PasswordChangePane pcPanel = new InnerPasswordChangePane(
						this, manager, acc);
				pcPanel.setAdvancedButtonVisible(false);

				dialog.setTitle(ToolKit.getString("PASSWORD_CHANGE"));

				okButton.setActionCommand("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent ae) {
						if (ae == null)
							return;
						String action = ae.getActionCommand();

						if ("OK".equals(action)) {
							long currentDate = System.currentTimeMillis();
							pcPanel.currentAccount.setPwUpdateDate(currentDate);
							pcPanel.actionPerformed(ae);

							// D41D8CD98F00B204E9800998ECF8427E means a blank,
							// "".
							if (pcPanel.getOrgPasswd().equals(
									"D41D8CD98F00B204E9800998ECF8427E")) {
								dialog.dispose();
							}
						}
					}
				});

				okButton.registerKeyboardAction(pcPanel,
						KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
						JComponent.WHEN_FOCUSED);

				dialog.getContentPane().add(pcPanel, BorderLayout.CENTER);
				// dialog.setSize(285, 205);

			} else if (action.equals("ADD_ACCOUNT")) {
				InnerAccountRegistrationPane arPanel = new InnerAccountRegistrationPane(
						dialog, manager);

				dialog.setTitle(ToolKit.getString("ADD_ACCOUNT"));

				okButton.setActionCommand("ADD_ACCOUNT");
				okButton.addActionListener(arPanel);
				okButton.registerKeyboardAction(arPanel,
						KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
						JComponent.WHEN_FOCUSED);

				dialog.getContentPane().add(arPanel, BorderLayout.CENTER);

			}

			cancelButton = new JButton(ToolKit.getString("CANCEL"));
			cancelButton.setActionCommand("CANCEL");
			cancelButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					dialog.dispose();
				}
			});

			cancelButton.registerKeyboardAction(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					dialog.dispose();
				}
			}, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
					JComponent.WHEN_FOCUSED);

			JPanel botPanel = new JPanel();
			botPanel.add(okButton);
			botPanel.add(cancelButton);

			dialog.getContentPane().add(botPanel, BorderLayout.SOUTH);

			dialog.setResizable(false);
			dialog.pack();
			if (dialog.getWidth() < 285) {
				dialog.setSize(285, dialog.getHeight());
			}
			dialog.setLocationRelativeTo(this);
			dialog.setVisible(true);
		}
	}

	public final void valueChanged(ListSelectionEvent e) {
		if (!e.getValueIsAdjusting()) {
			if (table.getSelectedColumnCount() >= 1) {
				deleteButton.setEnabled(true);
				changeButton.setEnabled(true);
			} else {
				deleteButton.setEnabled(false);
				changeButton.setEnabled(false);
			}
		}

	}

	private void initTable(Account[] accounts, JTable jt, DefaultTableModel dtm) {
		for (int i = 0; i < accounts.length; i++) {

			dtm.setValueAt(accounts[i].getName(), i, 0);
			dtm.setValueAt(accounts[i].getRole(), i, 1);
		}
		jt.setModel(dtm);
		DefaultTableColumnModel dtcm = (DefaultTableColumnModel) jt
				.getColumnModel();
		int colnum = dtm.getColumnCount();
		for (int i = 0; i < colnum; i++) {
			TableColumn col = dtcm.getColumn(i);
			col.setMinWidth(100);
		}
		dtm.fireTableStructureChanged();
	}

	// ---------------------InnerPassordwdChangePane class
	// ---------------------------
	private class InnerPasswordChangePane extends PasswordChangePane {

		private InnerPasswordChangePane(Dialog dialog, LoginManager manager,
				Account account) {
			super(dialog, manager, account);
		}

		// This is for checking current password of account excepting "admin"
		public String getOrgPasswd() {
			try {
				return MDEncoder.encode(super.getOrgPasswd());
			} catch (NoSuchAlgorithmException e) {
				return super.getOrgPasswd();
			}
		}
	}

	// ---------------------InnerAccountRegistrationDialog class
	// ---------------------------
	private class InnerAccountRegistrationPane extends PasswordChangePane
			implements ActionListener {
		private Dialog dialog;

		private InnerAccountRegistrationPane(Dialog dialog, LoginManager manager) {
			super(AccountMgmtDialog.this, manager, new Account("", "", null));

			this.dialog = dialog;
		}

		protected void initComponents() {
			super.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 5));
			JPanel mainPanel = new JPanel(new GridBagLayout());

			GridBagConstraints c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 0;
			c.anchor = GridBagConstraints.EAST;
			c.insets = new Insets(1, 1, 5, 0);
			mainPanel.add(new JLabel(ToolKit.getString("ACCOUNT") + " : "), c);

			c.gridx = 0;
			c.gridy = 1;
			mainPanel.add(new JLabel(ToolKit.getString("NEW_PASSWD") + " : "),
					c);

			c.gridx = 0;
			c.gridy = 2;
			mainPanel.add(new JLabel(ToolKit.getString("PASSWD_REENTRY")
					+ " : "), c);
			c.gridx = 0;
			c.gridy = 3;
			
			mainPanel.add(new JLabel(ToolKit.getString("ROLE") + " : "), c);

			c.gridx = 1;
			c.gridy = 0;
			c.weightx = 1.0;
			c.fill = GridBagConstraints.HORIZONTAL;
			c.anchor = GridBagConstraints.WEST;
			super.accountField = new JTextField(currentAccount.getName());
			mainPanel.add(accountField, c);

			c.gridx = 1;
			c.gridy = 1;
			super.newPasswdField = new JPasswordField();
			mainPanel.add(new JPanel().add(newPasswdField), c);

			c.gridx = 1;
			c.gridy = 2;
			super.rePasswdField = new JPasswordField();
			mainPanel.add(new JPanel().add(rePasswdField), c);
			c.gridx = 1;
			c.gridy = 3;
			
			Collection<String> role = (Collection<String>) roles;
			super.roleField = new JComboBox();
			for (String r : role) {
				roleField.addItem(r);
			}
			mainPanel.add(new JPanel().add(roleField), c);

			super.orgPasswdField = new JPasswordField();
			super.orgPasswdField.setText("dummy");

			super.add(mainPanel, BorderLayout.CENTER);
		}

		public void actionPerformed(ActionEvent ae) {
			if (ae == null)
				return;

			String action = ae.getActionCommand();
			if ("ADD_ACCOUNT".equals(action)) {
				String accountName = accountField.getText();

				if (accountName == null || accountName.equals("")) {
					JOptionPane.showMessageDialog(this,
							ToolKit.getString("MSG_NOACCOUNT_ENTRY"),
							ToolKit.getString("WARNING"),
							JOptionPane.WARNING_MESSAGE);

					return;
				} else if (accountName.indexOf(',') >= 0) {
					JOptionPane.showMessageDialog(this,
							ToolKit.getString("MSG_INVALID_ACCOUNT"),
							ToolKit.getString("WARNING"),
							JOptionPane.WARNING_MESSAGE);

					return;
				}

				String name = accountName.trim();
				Account tmpAcc = new Account(name, null, null);

				for (int i = 0; i < accounts.length; i++) {
					if (accounts[i].getName().equals(tmpAcc.getName())) {
						JOptionPane.showMessageDialog(this,
								ToolKit.getString("MSG_ACCOUNT_OVERLAP"),
								ToolKit.getString("WARNING"),
								JOptionPane.WARNING_MESSAGE);

						return;
					}
				}
				super.currentAccount = new Account(accountName, "dummy",
						(String) roleField.getSelectedItem());
				long currentDate = System.currentTimeMillis();
				currentAccount.setPwUpdateDate(currentDate);
				currentAccount.setAddDate(currentDate);
				currentAccount.setUpdateDate(currentDate);

				super.actionPerformed(ae);

				DefaultTableModel model = (DefaultTableModel) table.getModel();
				model.addRow(new String[] { accountName,
						(String) roleField.getSelectedItem() });

				if (super.getOrgPasswd().equals("")) {
					this.dialog.dispose();
					accounts = manager.loadAccounts();
				}
			}
		}

		public String getOrgPasswd() {
			return "dummy";
		}
	}

}
