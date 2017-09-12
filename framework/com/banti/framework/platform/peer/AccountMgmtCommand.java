package com.banti.framework.platform.peer;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import com.banti.framework.core.Account;
import com.banti.framework.platform.Application;
import com.banti.framework.platform.Command;
import com.banti.framework.platform.LoginManager;
import com.banti.framework.theme.ToolKit;

public class AccountMgmtCommand extends Command {

    private LoginManager loginManager;
    private boolean userEnabled;

    public AccountMgmtCommand(LoginManager loginManager) {
        this(ToolKit.getInstance().getString("ACCOUNT_MGMT"), loginManager);
    }

    public AccountMgmtCommand(String name, LoginManager loginManager) {
        super(name);
        super.setLoginNeeds(true);
        super.setSuperUserOnly(true);
        this.loginManager = loginManager;
        userEnabled = false;
    }
    public void setUserEnabled(boolean aFlag) {
        userEnabled = aFlag;
    }

    public void entryPoint(ActionEvent ae) {
        if (ae == null)
            return;
        AccountMgmtDialog dialog = null;

        Account account =Application.account;
        if (account == null) {
            ToolKit tool = ToolKit.getInstance();
            JOptionPane.showMessageDialog(
                Application.Frame,
                tool.getString("MSG_NOT_LOGIN"),
                tool.getString("WARNING"),
                Application.WARNING);

            return;
        }

        if (!account.isSuperUser() && !userEnabled) {
            ToolKit tool = ToolKit.getInstance();
            JOptionPane.showMessageDialog(
                Application.Frame,
                tool.getString("MSG_ACCOUNG_MGMT_UNAVAILABLE"),
                tool.getString("WARNING"),
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        dialog = createAccountMgmtDialog(Application.Frame, loginManager);
        if (dialog == null)
            return;

        dialog.pack();
        dialog.setSize(500, 200);
        dialog.setMinimumSize(new Dimension(500, 200));
        dialog.setLocationRelativeTo(Application.Frame);
        dialog.setVisible(true);
    }

    protected AccountMgmtDialog createAccountMgmtDialog(Frame owner, LoginManager manager) {
        AccountMgmtDialog dialog = new AccountMgmtDialog(Application.Frame, loginManager);
        if (!dialog.load()) {
            return null;
        }
        dialog.initComopnents();
        return dialog;
    }
}
