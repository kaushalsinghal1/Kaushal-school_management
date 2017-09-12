package com.banti.framework.platform;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;

public abstract class AbstractSchoolAction extends AbstractAction {
    private String commandKey;
    private boolean isSuperUserOnly;

    public AbstractSchoolAction(String commandKey) {
        super();
        super.putValue(Action.ACTION_COMMAND_KEY, commandKey);
        this.commandKey = commandKey;
    }

    public AbstractSchoolAction(String text, String commandKey) {
        super(text);
        super.putValue(Action.ACTION_COMMAND_KEY, commandKey);
        this.commandKey = commandKey;
    }

    public AbstractSchoolAction(String text, Icon icon, String commandKey) {
        super(text, icon);
        super.putValue(Action.ACTION_COMMAND_KEY, commandKey);
        this.commandKey = commandKey;
    }

    public String getCommandKey() {
        return commandKey;
    }

    public void setSuperUserOnly(boolean superUser) {
        isSuperUserOnly = superUser;
    }

    public boolean getSuperUserOnly() {
        return isSuperUserOnly;
    }
    
    public final void setEnabled(boolean enable) {
       /* if (isSuperUserOnly
            && Application.getSession() != null
            && Application.getSession().getActiveAccount() != null) {
            super.setEnabled(Application.getSession().getActiveAccount().isSuperUser() && enable);

            return;
        }*/
        if (commandKey == null) {
            super.setEnabled(enable);
            return;
        }
        super.setEnabled(enable ? Application.isActionPermitted(commandKey, false) : false);
    }
}
