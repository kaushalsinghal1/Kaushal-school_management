package com.banti.framework.platform;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.management.relation.Role;
import javax.swing.Icon;

import com.banti.framework.core.Account;
import com.banti.framework.platform.ViewMediator.DefaultCommand.Login;
import com.banti.framework.platform.ViewMediator.DefaultCommand.Logout;
import com.banti.framework.theme.ToolKit;

/**
 * LoginManager abstract class, whose responsibility is to manage Login/Logout
 * Functionalities. Three concrete classes are already provided,
 * <a href="LocalLoginManager.htm"><i>LocalLoginManager</i></a>,
 * <a href="RemoteLoginManager.html"><i>RemoteLoginManager</i></a> and
 * <a href="TabbedRemoteLoginManager.html"><i>TabbedRemoteLoginManager</i></a>.
 * Those can manage account authentication when a user tries to login to your concrete
 * Application and to logout. Then, LoginManagers control Login/Logout menus and provides
 * Password Change function according to the respective LoginManagers.<br>
 * If you set one of LoginManagers to your application, Those menu items get available.
 */
public abstract class LoginManager {

    /**
     * The displayed name of Login men item.
     */
    public String LOGIN_MENU_NAME;

    /**
     * The tool tip of Login menu item.
     */
    public String LOGIN_DESCRIPTION;

    /**
     * The icon image of Login menu item.
     */
    public Icon LOGIN_ICON;

    /**
     * An icon button of Login is put on the tool bar if it is true;
     * No button is put if false.
     */
    public boolean LOGIN_BUTTON_ENABLE;

    /**
     * The key used for string an int key code to be used as the mnemonic
     * for Login menu item.  -1 means no definition.
     */
    public int LOGIN_MNEMONIC;

    /**
     * The title string of Login Dialog.
     */
    public String LOGIN_DIALOG_TITLE;

    /**
     * The displayed name of Logout men item.
     */
    public String LOGOUT_MENU_NAME;

    /**
     * The tool tip of Logout menu item.
     */
    public String LOGOUT_DESCRIPTION;

    /**
     * The icon image of Logout menu item.
     */
    public Icon LOGOUT_ICON;

    /**
     * An icon button of Login is put on the tool bar if it is true;
     * No button is put if false.
     */
    public boolean LOGOUT_BUTTON_ENABLE;

    /**
     * The key used for string an int key code to be used as the mnemonic
     * for Logout menu item. -1 means no definition.
     */
    public int LOGOUT_MNEMONIC;

    /**
     * a variable of Login command, which can be re-used by the sub class.
     */
    protected Command loginCommand;

    /**
     * a variable of Logout command, which can be re-used by the sub class.
     */
    protected Command logoutCommand;

    private ArrayList commandList;

    private List loginListeners;
    private List logoutListeners;
    String loginImagePath = "/com/banti/framework/platform/images/login.gif";
    String logoutImagePath = "/com/banti/framework/platform/images/logout.jpg";

    /**
     * Public Constructor shared by the sub class.
     * Your sub class of LoginManager must call this constructor by "super()".
     *
     */
    public LoginManager() {
        super();
        commandList = new  ArrayList();
        loginListeners = Collections.synchronizedList(new  ArrayList());
        logoutListeners = Collections.synchronizedList(new  ArrayList());

        ToolKit tool = ToolKit.getInstance();
        this.LOGIN_MENU_NAME = tool.getString("LOGIN");
        this.LOGIN_DESCRIPTION = null;
        this.LOGIN_ICON = tool.createImageIcon(loginImagePath);
        this.LOGIN_BUTTON_ENABLE = false;
        this.LOGIN_MNEMONIC = -1;
        this.LOGIN_DIALOG_TITLE = tool.getString("LOGIN");

        this.LOGOUT_MENU_NAME = tool.getString("LOGOUT");
        this.LOGOUT_DESCRIPTION = null;
        this.LOGOUT_ICON = tool.createImageIcon(logoutImagePath);;
        this.LOGOUT_BUTTON_ENABLE = false;
        this.LOGOUT_MNEMONIC = -1;
    }


    // --------------- protected methods for the extended classes --------------------------
    /**
     * This method will be called when a user requests his login operation to your Application.
     * Only a message of the status bar and status indicator are changed by this method.
     * Each sub classes of LoginManager has thier own login operations. This method is
     * called by them after the respecitive login operations are performed.
     *
     * @param application your concrete application implementation.
     */
    protected void login(Application application) {
    	
     //   AccountSession session = Application.getSession();
        if (application.account != null) {
          //  Account loginAccount = session.getActiveAccount();
            ToolKit tool = ToolKit.getInstance();
            String message = tool.getString("LOGGED_IN") + "    "
            	+ tool.getString("ACCOUNT") + " : " + application.account.getName();

            application.View.setStatusMessage(message);
            application.View.changeStatus(Color.BLUE);

            this.fireLoginSucceeded(application.account);
            this.notifyLogin();
        } else {
            this.fireLoginFailed();
        }
    }

    /**
     * Authenticates a login account. This method has a logic of authentication for a login user
     * account. This method itself maintains session maintenance for the account and always
     * returns true. Each sub class of LoginManager authenticates the account actually. It is
     * like a default implementation
     *
     * @param account Account instance who want to login into your application.
     * @return true if the account is authenticated; false otherwise.
     *
     * @see com.cysols.framework.platform.core.Account
     */
    protected boolean authenticate(Account account) {
        Application.account=account;
        return true;
    }

    /**
     * This method will be called when a user requests his logout operation from your Application.
     * This method itself works for removing the account from the session, changing a message on
     * the status bar and changing status indicator on the status bar. The concrete operation of logout
     * is implemented in the respective sub classes of LoginManager.
     *
     * @param application application your concrete application implementation.
     */
    protected void logout(Application application) {
        //AccountSession session = Application.getSession();
        Account loginAccount = application.account;

        if (loginAccount != null) {
            this.fireLogoutSucceeded(loginAccount);
            application.account=null;
            application.View.setStatusMessage("");
            application.View.changeStatus(null);

            this.notifyLogout();
        } else {
            this.fireLogoutFailed();
        }
    }

    /**
     * Changes account's password. True is returned if his password is changed correctly.
     * Concrete implementation of Password Change logic is implemented in the respective
     * sub class of LoginManager. This method always returns false.
     *
     * @param account Account instance who want to change his password.
     * @param newPasswd a new pasword.
     * @return true if the password is changed successfully, false otherwise.
     */
    public boolean changePassword(Account account, String newPasswd) {
        return false;
    }

    /**
     * Deletes a specified Account.
     *
     * @param account Account to be deleted.
     * @return true if the Account is deleted successfully, false otherwise.
     */
    public boolean delete(Account account) {
        return false;
    }

    /**
     * Loads all accounts managed by LoginManager and Returns its array.
     * This array of Account will be displayed on Account management called
     * from Password Change Pane.
     *
     * @return an array of Account instance managed by LoginManager.
     */
    public Account[] loadAccounts() {
        return null;
    }


    public ArrayList<String> loadDomains() {
        return null;
    }


    public Collection<String> loadRoles(){
        return null;
    }


    /**
     * Adds LoginListener concrete instance to this LoginManager.
     * The added LoginListener is called after Login process.
     *
     * @param listener
     */
    public void addLoginListener(LoginListener listener) {
        if (!loginListeners.contains(listener)) {
            loginListeners.add(listener);
        }
    }

    /**
     * Adds LogoutListener concrete instance to this LoginManager.
     * The added LogoutListener is called after Logout process.
     *
     * @param listener
     */
    public void addLogoutListener(LogoutListener listener) {
        if (!logoutListeners.contains(listener)) {
            logoutListeners.add(listener);
        }
    }

    protected final void fireLoginSucceeded(Account account) {
        for (int i = 0; i < loginListeners.size(); i++) {
            LoginListener listener = (LoginListener) loginListeners.get(i);
            listener.loginSucceeded(account);
        }
    }

    protected final void fireLoginFailed() {
        for (int i = 0; i < loginListeners.size(); i++) {
            LoginListener listener = (LoginListener) loginListeners.get(i);
            listener.loginFailed();
        }
    }

    protected final void fireLogoutSucceeded(Account account) {
        for (int i = 0; i < logoutListeners.size(); i++) {
            LogoutListener listener = (LogoutListener) logoutListeners.get(i);
            listener.logoutSucceeded(account);
        }
    }
    protected final void fireLogoutFailed() {
        for (int i = 0; i < logoutListeners.size(); i++) {
            LogoutListener listener = (LogoutListener) logoutListeners.get(i);
            listener.logoutFailed();
        }
    }

    /**
     * Notifies Login Status to all commands/modules.
     * Their commands/modules get available if "login enabled" properites is true.
     *
     */
    protected final void notifyLogin() {
        for (int i = 0; i < commandList.size(); i++) {
            Command cmd = (Command) commandList.get(i);
            cmd.setEnabled(true);
        }

        loginCommand.setEnabled(false);
        logoutCommand.setEnabled(true);
    }

    /**
     * Notifies Logout Status to all commands/modules.
     * Their commands/modules get unavailable if "login enabled" properites is true.
     *
     */
    protected final void notifyLogout() {
        for (int i = 0; i < commandList.size(); i++) {
            Command cmd = (Command) commandList.get(i);
            cmd.setEnabled(false);
        }

        loginCommand.setEnabled(true);
        logoutCommand.setEnabled(false);
    }

    /**
     * This is a mark used by Exit command when Exit is performed.
     * The implementation of <i>LoginManager</i> does nothing.
     * If your concrete <i>LoginManager</i> manages multple login session,
     * then you should disconnect all login sessions if this flag is true.
     *
     * @param aFlag true will be set before Exit command is performed.
     */
    protected void setAllLogout(boolean aFlag) {
        // do nothng.
    }

    // --------------- private methods -----------------------------------------------------------------

    final void registerCommand(Command command) {
        if (command == null) return;

        commandList.add(command);
        command.setEnabled(false);
    }

    final void registerCommand(Module module) {
        if (module == null) return;
        Command[] cmds = module.getCommands();

        for (int i = 0; i < cmds.length; i++) {
            this.registerCommand(cmds[i]);
        }
    }

    final void plug(Login command) {
        this.loginCommand = command;

        this.loginCommand.setDescription(this.LOGIN_DESCRIPTION);
        this.loginCommand.setIcon(this.LOGIN_ICON);
        this.loginCommand.setButtonEnabled(this.LOGIN_BUTTON_ENABLE);
        this.loginCommand.setMnemonic(this.LOGIN_MNEMONIC);

        this.loginCommand.setVisible(true);
        this.loginCommand.setEnabled(true);
    }

    final void plug(Logout command) {
        this.logoutCommand = command;

        this.logoutCommand.setDescription(this.LOGOUT_DESCRIPTION);
        this.logoutCommand.setIcon(this.LOGOUT_ICON);
        this.logoutCommand.setButtonEnabled(this.LOGOUT_BUTTON_ENABLE);
        this.logoutCommand.setMnemonic(this.LOGOUT_MNEMONIC);

        this.logoutCommand.setVisible(true);
        this.logoutCommand.setEnabled(false);
    }
}
