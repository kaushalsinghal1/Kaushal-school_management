package com.banti.framework.platform.peer;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.NoSuchAlgorithmException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.banti.framework.core.Account;
import com.banti.framework.platform.LoginManager;
import com.banti.framework.theme.ToolKit;
import com.banti.framework.utils.MDEncoder;

/**
 * TODO
 */
public class PasswordChangePane extends JPanel implements ActionListener {

    protected JButton advancedButton;
    protected JTextField accountField;
    protected JPasswordField orgPasswdField;
    protected JPasswordField newPasswdField;
    protected JPasswordField rePasswdField;
 //   protected JComboBox domainField;
    protected JComboBox roleField;

    private LoginManager manager;
    protected Account currentAccount;

    private Frame parentFrame;
    private Dialog parentDialog;

    /**
     *
     */
    public PasswordChangePane(Frame frame, LoginManager manager, Account account) {
        this(frame, manager, account, false);
    }

    public PasswordChangePane(Frame frame, LoginManager manager, Account account, boolean aFlag) {
        super(new BorderLayout());

        this.manager = manager;
        this.currentAccount = account;
        this.parentFrame = frame;
        this.initComponents();

        if (advancedButton != null)
            advancedButton.setVisible(aFlag);
    }

    public PasswordChangePane(Dialog dialog, LoginManager manager, Account account) {
        this(dialog, manager, account, false);
    }

    public PasswordChangePane(Dialog dialog, LoginManager manager, Account account, boolean aFlag) {
        super(new BorderLayout());

        this.manager = manager;
        this.currentAccount = account;
        this.parentDialog = dialog;
        this.initComponents();

        if (advancedButton != null)
            advancedButton.setVisible(aFlag);
    }

    protected void initComponents() {
        super.setBorder(BorderFactory.createEmptyBorder(10, 10, 15, 5));

        JPanel mainPanel = new JPanel(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.EAST;
        c.insets = new Insets(1, 1, 5, 0);
        mainPanel.add(new JLabel(ToolKit.getString("ACCOUNT") + " : "), c);

        c.gridx = 0;
        c.gridy = 1;
        mainPanel.add(new JLabel(ToolKit.getString("CURRENT_PASSWD") + " : "), c);

        c.gridx = 0;
        c.gridy = 2;
        mainPanel.add(new JLabel(ToolKit.getString("NEW_PASSWD") + " : "), c);

        c.gridx = 0;
        c.gridy = 3;
        mainPanel.add(new JLabel(ToolKit.getString("PASSWD_REENTRY") + " : "), c);

        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 1.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.WEST;
        accountField = new JTextField(currentAccount.getName());
        accountField.setEditable(false);
        mainPanel.add(accountField, c);

        c.gridx = 1;
        c.gridy = 1;
        orgPasswdField = new JPasswordField();
        mainPanel.add(new JPanel().add(orgPasswdField), c);

        c.gridx = 1;
        c.gridy = 2;
        newPasswdField = new JPasswordField();
        mainPanel.add(new JPanel().add(newPasswdField), c);

        c.gridx = 1;
        c.gridy = 3;
        rePasswdField = new JPasswordField();
        mainPanel.add(new JPanel().add(rePasswdField), c);

        // Advanced Button is available only when an account is "admin";
        advancedButton = new JButton(ToolKit.getString("ACCOUNT_MGMT"));
        advancedButton.addActionListener(new AccountMgmtAction());
        if (currentAccount.isSuperUser()) {
            advancedButton.setEnabled(true);
        } else {
            advancedButton.setEnabled(false);
        }

        c.gridx = 1;
        c.gridy = 4;
        c.weightx = 1.0;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.EAST;
        mainPanel.add(advancedButton, c);

        super.add(mainPanel, BorderLayout.CENTER);
    }

    public String getOrgPasswd() {
        return this.encodedPasswd(orgPasswdField.getPassword());
    }

    public String getNewPasswd() {
        return this.encodedPasswd(newPasswdField.getPassword());
    }

    public String getRePasswd() {
        return this.encodedPasswd(rePasswdField.getPassword());
    }

    public void clearAll() {
        orgPasswdField.setText("");
        newPasswdField.setText("");
        rePasswdField.setText("");
    }

    public void clearOrgPasswd() {
        orgPasswdField.setText("");
    }

    public void clearNewPasswd() {
        newPasswdField.setText("");
        rePasswdField.setText("");
    }

    /**
     * Validates the entered parameters.
     */
    public boolean validateEntry(String currentPasswd) {
        String orgPasswd = this.getOrgPasswd();

        // Checks an original password is empty or not.
        if (orgPasswd.equals("")) {
            JOptionPane.showMessageDialog(
                this,
                ToolKit.getString("MSG_NOPASSWD_ENTRY"),
                ToolKit.getString("WARNING"),
                JOptionPane.WARNING_MESSAGE);

            this.clearOrgPasswd();

            return false;
        }

        // Checks an original password is correct or not.
        if (!currentPasswd.equals(orgPasswd)) {
            JOptionPane.showMessageDialog(
                this,
                ToolKit.getString("MSG_WRONG_PASSWD"),
                ToolKit.getString("WARNING"),
                JOptionPane.WARNING_MESSAGE);

            this.clearOrgPasswd();

            return false;
        }

        String newPasswd = this.getNewPasswd();
        String rePasswd = this.getRePasswd();

        // Checks an new password is empty or not.
        if (newPasswd.equals("")) {
            JOptionPane.showMessageDialog(
                this,
                ToolKit.getString("MSG_NONEWPASSWD_ENTRY"),
                ToolKit.getString("WARNING"),
                JOptionPane.WARNING_MESSAGE);

            this.clearNewPasswd();

            return false;
        }

        // Checks an re-entered password is empty or not.
        if (rePasswd.equals("")) {
            JOptionPane.showMessageDialog(
                this,
                ToolKit.getString("MSG_NOREPASSWD_ENTRY"),
                ToolKit.getString("WARNING"),
                JOptionPane.WARNING_MESSAGE);

            this.clearNewPasswd();

            return false;
        }

        // Checks a new password matches the re-entered password.
        if (!newPasswd.equals(rePasswd)) {
            JOptionPane.showMessageDialog(
                this,
                ToolKit.getString("MSG_PASSWD_UNMATCH"),
                ToolKit.getString("WARNING"),
                JOptionPane.WARNING_MESSAGE);

            this.clearNewPasswd();
            return false;
        }

        return true;
    }

    void setAdvancedButtonVisible(boolean aFlag) {
        this.advancedButton.setVisible(aFlag);
    }

    private String encodedPasswd(char[] charPasswd) {
        String passwd = new String(charPasswd);
        if (passwd == null || passwd.trim().equals("")) {
            return "";
        }

        try {
            String encoded = MDEncoder.encode(passwd);
            return encoded;
        } catch (NoSuchAlgorithmException e) {
            return passwd;
        }
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae == null)
            return;

        String action = ae.getActionCommand();

        if ("OK".equals(action)) {
            boolean pass = (orgPasswdField.getPassword() == null || "".equals(new String(
                orgPasswdField.getPassword())));
            boolean newPass = (newPasswdField.getPassword() == null || "".equals(new String(
                newPasswdField.getPassword())));
            boolean rePass = (rePasswdField.getPassword() == null || "".equals(new String(
                rePasswdField.getPassword())));

            if (pass && newPass && rePass)
                return;
        }

        if ("OK".equals(action) || "APPLY".equals(action) || "ADD_ACCOUNT".equals(action)) {

            // Validates the entered parameters.
            if (!this.validateEntry(currentAccount.getPasswd())) {
                return;
            }

            // Calls LoginManager.changePassword method.
            if (this.manager.changePassword(currentAccount, this.getNewPasswd())) {
                // Success
                if ("OK".equals(action) || "APPLY".equals(action)) {
                    JOptionPane.showMessageDialog(
                        this,
                        ToolKit.getString("MSG_PASSWD_CHANGED"),
                        ToolKit.getString("INFORMATION"),
                        JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(
                        this,
                        ToolKit.getString("MSG_ACCOUNT_ADDED", "ACCOUNT", currentAccount.getName()),
                        ToolKit.getString("INFORMATION"),
                        JOptionPane.INFORMATION_MESSAGE);
                }

                try {
                    currentAccount.setPasswd(MDEncoder.encode(getNewPasswd()));
                } catch (NoSuchAlgorithmException e) {
                    currentAccount.setPasswd(getNewPasswd());
                }

                this.clearAll();

            } else {
                // Failed
                if ("OK".equals(action) || "APPLY".equals(action)) {
                    JOptionPane.showMessageDialog(
                        this,
                        ToolKit.getString("MSG_PASSWD_CHANGED_FAILED"),
                        ToolKit.getString("WARNING"),
                        JOptionPane.WARNING_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(
                        this,
                        ToolKit.getString("MSG_ADD_FAILED"),
                        ToolKit.getString("WARNING"),
                        JOptionPane.WARNING_MESSAGE);
                }

                this.clearAll();
            }

        }
    }

    private class AccountMgmtAction implements ActionListener {

        private AccountMgmtAction() {
            super();
        }

        /* (non-Javadoc)
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent ae) {
            if (ae == null)
                return;
            AccountMgmtDialog dialog = null;
            if (parentFrame != null) {
                dialog = new AccountMgmtDialog(parentFrame, manager);
            } else {
                dialog = new AccountMgmtDialog(parentDialog, manager);
            }

            if (dialog.load()) {
                dialog.initComopnents();
                dialog.pack();
                if (dialog.getWidth() < 320) {
                    dialog.setSize(320, dialog.getHeight());
                }
                dialog.setLocationRelativeTo(PasswordChangePane.this);
                dialog.setVisible(true);
            } else {
                if (parentFrame != null) {
                    parentFrame.dispose();
                } else {
                    parentDialog.dispose();
                }
            }
        }
    }
}
