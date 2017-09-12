package com.banti.framework.platform.peer;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.security.NoSuchAlgorithmException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import com.banti.framework.theme.ToolKit;
import com.banti.framework.ui.DialogEsc;
import com.banti.framework.utils.MDEncoder;


/**
 * TODO
 */
public class LoginDialog extends DialogEsc implements ActionListener {

    protected JButton loginButton;
    protected JButton cancelButton;
    protected JTextField jtextAccount;
    protected JPasswordField jtextPassword;
    
    protected JPanel upPanel;
    
    protected Frame parent;
    /**
     * 
     */
    public LoginDialog(Frame parent, String name) {
        super(parent, name, true);
        this.parent = parent;
        
        ToolKit tool = ToolKit.getInstance();
        
        loginButton = new JButton(tool.getString("OK"));
        cancelButton = new JButton(tool.getString("CANCEL"));
        cancelButton.setActionCommand("CANCEL");
        cancelButton.addActionListener(this);
        cancelButton.registerKeyboardAction(this, 
                KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
                JComponent.WHEN_FOCUSED);

        
        jtextAccount = new JTextField();
        jtextPassword = new JPasswordField();
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel botPanel = new JPanel();
        this.initUpPanel();

        botPanel.add(loginButton);
        botPanel.add(cancelButton);

        mainPanel.add(BorderLayout.CENTER, upPanel);
        mainPanel.add(BorderLayout.SOUTH, botPanel);
        super.getContentPane().add(mainPanel);
        
        super.setSize(220, 145);
        super.setResizable(false);
        super.setLocationRelativeTo(parent);

    }
    
    protected void initUpPanel() {
        ToolKit tool = ToolKit.getInstance();
        
        upPanel = new JPanel(new GridBagLayout());
        upPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 15, 5));
        
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.EAST;
        c.insets = new Insets(1, 0, 1, 0);
        upPanel.add(new JLabel(tool.getString("ACCOUNT") + " : "), c);

        c.gridx = 0;
        c.gridy = 1;
        upPanel.add(new JLabel(tool.getString("PASSWD") + " : "), c);

        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 1.0;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        upPanel.add(jtextAccount, c);        
        
        c.gridx = 1;
        c.gridy = 1;
        upPanel.add(jtextPassword, c);
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae == null) return;
        
        String action = ae.getActionCommand();
        
        if (action.equals("CANCEL")) {
            this.dispose();
        }
    }
    
    public void setActionListener(ActionListener al) {
        loginButton.addActionListener(al);
        loginButton.registerKeyboardAction(al, 
                KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
                JComponent.WHEN_FOCUSED);

        jtextAccount.registerKeyboardAction(al, 
                KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
                JComponent.WHEN_FOCUSED);

        jtextPassword.registerKeyboardAction(al, 
                KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
                JComponent.WHEN_FOCUSED);
    }
        
    public String getAccount() {
        return jtextAccount.getText();
    }

    public String getPasswd() {
        String passwd = new String(jtextPassword.getPassword());

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
    
    public void clearPasswd() {
        jtextPassword.setText("");
    }
}
