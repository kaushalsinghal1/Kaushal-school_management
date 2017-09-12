package com.banti.framework.platform;

import java.awt.event.ActionEvent;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.KeyStroke;

/**
 * An abstract Command provides the interface to realize the respective functions on the platform. 
 * This class has setter/getter methods for its attributes and an abstract method, 
 * "<a href="Command.html#entryPoint(java.awt.event.ActionEvent)">entryPoint(ActionEvent)</a>".
 * It is necessary to implement your concrete entryPoint method to perform your own function.
 * Your application constructs the menu bar and the tool bar based on Command instances, and
 * it calls their entryPoints when the corresponding menu item or icon button is selected.
 * <br><br>
 *  
 * The attributes of Command are as follows. These attributes give configured paramters
 * to <i>Application</i>. <br>
 * <ul>
 * <li>Name: it is initialized by only constructor. The name is shown as the menu item.
 * <li>Description: is used as ToolTip on the menu item or icon button when a mouse cursor is on it.
 * <li>Separator: is put before the menu item or icon button if it is true.
 * <li>Icon: is shown in the menu item, and means the icon button itself.
 * <li>Mnemonic: the key used for storing an int key code to be used as the mnemonic for the action.
 * <li>Accelerator: the key used for storing a KeyStroke to be used as the accelerator for the action.
 * <li>Visible: the corresponding menu item and icon button are displayed if it is true. Not displayed if false.
 * <li>Enabled: the corresponding menu item and icon button gets enabled if it is true. disable otherwise.
 * <li>ButtonEnabled: the icon button is deployed on the tool bar if it is true. No button is displayed if false.
 *  <li>Login: means the corresponding menu item and icon button cannot be enabled without login.
 * </ul>
 * 
 */
public abstract class Command {

    private final RuntimeAction action;

    private boolean separator = false;
    private String name;
    private String commandKey;
    private boolean isSuperUserOnly;

    /**
     * A constructor, a name of the command is given only by this constructor.
     * 
     * @param name the name of the command, which is displayed as the menu item.
     */
    public Command(String name) {
        this.name = name;
        action = new RuntimeAction(this);
        action.putValue(Action.NAME, name);
        isSuperUserOnly = false;
    }

    public Command(String name, String cmdKey) {
        this.name = name;
        this.commandKey = cmdKey;
        action = new RuntimeAction(this);
        action.putValue(Action.NAME, name);
        action.putValue(Action.ACTION_COMMAND_KEY, cmdKey);
        isSuperUserOnly = false;
    }

    public void setSuperUserOnly(boolean superUser) {
        isSuperUserOnly = superUser;
    }

    public boolean getSuperUserOnly() {
        return isSuperUserOnly;
    }

    /**
     * The entry point of this command is given by this method.
     * This is an abstract method. It is necessary to implement a operation
     * of this Command in this method of your concrete class. 
     * 
     * @param ae ActionEvent, "getSource()" method returns your Command
     * concrete instance itself.
     */
    public abstract void entryPoint(ActionEvent ae);

    /**
     * Returns the name of the command.
     * 
     * @return Command Name.
     */
    public final String getName() {
        //return (String) action.getValue(Action.NAME);
        return this.name;
    }

    protected final void setName(String name) {
        this.name = name;
        action.putValue(Action.NAME, name);
    }

    protected final void setCommandKey(String commandKey) {
        this.commandKey = commandKey;
        action.putValue(Action.ACTION_COMMAND_KEY, commandKey);
    }

    public String getCommandKey() {
        return commandKey;
    }

    /**
     * Returns whether a separator is needed or not.
     *  
     * @return true if a separator is needed before the command, false otherwise.
     */
    public final boolean needsSeparator() {
        return separator;
    }

    /**
     * Returns the description of the command.
     * 
     * @return description, which is used for ToolTip.
     */
    public final String getDescription() {
        return (String) action.getValue(Action.SHORT_DESCRIPTION);
    }

    /**
     * Return Icon instance.
     * 
     * @return Icon that is used on the menu item, or Icon button itself.
     */
    public final Icon getIcon() {
        return (Icon) action.getValue(Action.SMALL_ICON);
    }

    /**
     * Returns an int key code to be used as the mnemonic for the action.
     *   
     * @return Key code as integer.
     */
    public final int getMnemonic() {
        return ((Integer) action.getValue(Action.MNEMONIC_KEY)).intValue();
    }

    /**
     * Returns a KeyStroke to be used as the accelerator for the action. 
     * 
     * @return KeyStorike instance as a short cut key.
     */
    public final KeyStroke getAccelerator() {
        return (KeyStroke) action.getValue(Action.ACCELERATOR_KEY);
    }

    /**
     * Returns whether the command is visible on menu item and tool bar or not.
     * 
     * @return true if the command is visible, otherwise false.
     */
    public boolean isVisiable() {
        return action.isVisible();
    }

    /**
     * Returns whether the command is available or not.
     * 
     * @return true if the menu item or icon button are enabled.
     */
    public boolean isEnabled() {
        return action.isEnabled();
    }

    /**
     * Returns whether an icon button of the command is deployed on the tool bar or not.
     * 
     * @return an icon button is put on the tool bar only when it is true.
     */
    public boolean isButtonEnabled() {
        return action.isButtonEnabled();
    }

    /**
     * Returns the command needs Login for getting available.
     * 
     * @return true if a user needs to login for using the command.
     */
    public final boolean needsLogin() {
        return action.needsLogin();
    }

    // ------ Setter Method --------------------------------------------------------------------

    /**
     * Sets a separator is necessary or not.
     * 
     * @param aFlag true or false.
     */
    public final void setSeparator(boolean aFlag) {
        separator = aFlag;
    }

    /**
     * Sets the description phrase for the Tool Tip.
     * 
     * @param text ToolTip phrase. null means no phrase.
     */
    public final void setDescription(String text) {
        action.putValue(Action.SHORT_DESCRIPTION, text);
    }

    /**
     * Sets Icon object used on the menu item and Icon button.
     * 
     * @param icon Icon Image object. null means icon is unnecessary.
     */
    public final void setIcon(Icon icon) {
        action.putValue(Action.SMALL_ICON, icon);
    }

    /**
     * Sets key code for mnemonic as integer.
     * 
     * @param mnemonic integer of the key code. <u><b>-1</b> means no mnemonic</u>.
     */
    public final void setMnemonic(int mnemonic) {
        if (mnemonic != -1) {
            action.putValue(Action.MNEMONIC_KEY, new Integer(mnemonic));

            String name = this.getName();
            if (name.indexOf(mnemonic) == -1) {
                name = name + " (" + (char) mnemonic + ")";
                action.putValue(Action.NAME, name);
            }
        }
    }

    /**
     * Sets the key stroke object as the accelerator.
     * 
     * @param keystroke KeyStroke object. null means no setting..
     */
    public final void setAccelerator(KeyStroke keystroke) {
        action.putValue(Action.ACCELERATOR_KEY, keystroke);
    }

    /**
     * Sets visible or not.
     * 
     * @param aFlag true or false.
     */
    public final void setVisible(boolean aFlag) {
        action.setVisible(aFlag);
    }

    /**
     * Sets enable or not.
     * 
     * @param aFlag true or not.
     */
    public final void setEnabled(boolean aFlag) {
       /* if (isSuperUserOnly
            && Application.getSession() != null
            && Application.getSession().getActiveAccount() != null) {
            action.setEnabled(Application.getSession().getActiveAccount().isSuperUser());

            return;
        }*/
        if (commandKey == null) {
            action.setEnabled(aFlag);
            return;
        }
        action.setEnabled(aFlag ? Application.isActionPermitted(commandKey, false) : false);
    }

    /**
     * Sets true you want to show an icon button of the command. 
     * 
     * @param aFlag true or false.
     */
    public final void setButtonEnabled(boolean aFlag) {
        action.setButtonEnabled(aFlag);
    }

    /**
     * Sets true if you want to enable the command after login.
     * 
     * @param aFlag true or false.
     */
    public final void setLoginNeeds(boolean aFlag) {
        action.setLoginNeeds(aFlag);
    }

    // ------- non-public methods ------------------------------------------------------------
    final void setCmdSelected(boolean aFlag) {
        action.setSelected(aFlag);
    }

    final boolean isCmdSelected() {
        return action.isSelected();
    }

    /**
     * This is a method for Visitor pattern.
     */
    void accept(MenuBar bar) {
        bar.visit(this);
    }

    final RuntimeAction getRuntimeAction() {
        return action;
    }

    //---------Inner RuntimeAction class------------------------------------------------------------------
    final class RuntimeAction extends AbstractSchoolAction {

        private final String visible_key = "VISIBLED";
        private final String select_key = "SELECTED";
        private final String button_key = "BUTTON_ENABLED";
        private final String login_key = "NEEDS_LOGIN";

        private boolean visible = true;
        private boolean selected = false;
        private boolean button_enabled = false;
        private boolean login = false;

        private Command command;

        private RuntimeEngine runtimeEngine;

        RuntimeAction(Command cmd) {
            super(cmd.commandKey);
            command = cmd;
        }

        public boolean isEnabled() {
            if (!this.isVisible())
                return false;

          /*  if (isSuperUserOnly
                && Application.getSession() != null
                && Application.getSession().getActiveAccount() != null) {
                return Application.getSession().getActiveAccount().isSuperUser();
            }*/
            return super.isEnabled();
        }

        boolean isVisible() {
            return visible;
        }

        void setVisible(boolean aFlag) {
            visible = aFlag;
            super.putValue(visible_key, new Boolean(visible));
        }

        boolean isSelected() {
            return selected;
        }

        void setSelected(boolean aFlag) {
            selected = aFlag;
            super.putValue(select_key, new Boolean(selected));
        }

        boolean isButtonEnabled() {
            return button_enabled;
        }

        void setButtonEnabled(boolean aFlag) {
            button_enabled = aFlag;
            super.putValue(button_key, new Boolean(button_enabled));
        }

        boolean needsLogin() {
            return login;
        }

        void setLoginNeeds(boolean aFlag) {
            login = aFlag;
            super.putValue(login_key, new Boolean(login));
        }

        void plug(RuntimeEngine engine) {
            runtimeEngine = engine;
        }

        public void actionPerformed(ActionEvent ae) {
            if (ae == null)
                return;
            ae.setSource(command);
            if (command.commandKey != null && !Application.isActionPermitted(command.commandKey)) {
                return;
            }
            if (runtimeEngine != null) {
                runtimeEngine.execute(ae);
            } else {
                command.entryPoint(ae);
            }
        }
    }
}
