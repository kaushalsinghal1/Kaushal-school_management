package com.banti.framework.platform;

import java.util.ArrayList;

import com.banti.framework.platform.module.FileModuleScheme;

/**
 *  
 */
final class FileModule extends Module {
    
    Command removeTab;    
    Command Exit;
    boolean Printable;
    boolean Previewable;
    
    private Command passwdChange;
//    private Command addTab;
//    private Command openMap;
    private ArrayList cmdList;
    
    FileModule(FileModuleScheme fileScheme, ViewMediator view) {
        super( fileScheme.FILE_MENU_NAME);
        super.setDescription(fileScheme.FILE_DESCRIPTION);
        super.setVisible(fileScheme.FILE_VISIBLE);
        super.setEnabled(fileScheme.FILE_ENABLE);
        super.setMnemonic(fileScheme.FILE_MNEMONIC);

        passwdChange = view.CommandBuilder.new PasswordChange(fileScheme.CHAGE_PASSWD_MENU_NAME, fileScheme.ACCOUNT_MGMT_AVAILABLE);
        passwdChange.setDescription(fileScheme.CHANGE_PASSWD_DESCRIPTION);
        passwdChange.setIcon(fileScheme.CHANGE_PASSWD_ICON);
        passwdChange.setVisible(fileScheme.CHANGE_PASSWD_VISIBLE);
        passwdChange.setEnabled(fileScheme.CHANGE_PASSWD_ENABLE);
        passwdChange.setButtonEnabled(fileScheme.CHANGE_PASSWD_BUTTON_ENABLE);
        passwdChange.setMnemonic(fileScheme.CHANGE_PASSWD_MNEMONIC);
        
        if (Application.Login == null) {
            passwdChange.setVisible(false);
        } else {
            passwdChange.setSeparator(true);
            passwdChange.setLoginNeeds(true);
        }
        
        
        
        Printable = fileScheme.PRINT_VISIBLE;
        Previewable = fileScheme.PREVIEW_VISIBLE;
        
        Exit  = view.CommandBuilder.new Exit(fileScheme.EXIT_MENU_NAME);
        Exit.setDescription(fileScheme.EXIT_DESCRIPTION);
        Exit.setIcon(fileScheme.EXIT_ICON);
        Exit.setVisible(fileScheme.EXIT_VISIBLE);
        Exit.setEnabled(fileScheme.EXIT_ENABLE);
        Exit.setButtonEnabled(fileScheme.EXIT_BUTTON_ENABLE);
        Exit.setMnemonic(fileScheme.EXIT_MNEMONIC);

        cmdList = new ArrayList(4);
        cmdList.add(passwdChange);
        cmdList.add(removeTab);
        
        if (fileScheme.FILE_COMMANDS != null) {
            for (int i = 0; i < fileScheme.FILE_COMMANDS.length; i++) {
                Command cmd = fileScheme.FILE_COMMANDS[i];
                cmdList.add(cmd);
            }
        }
    }

    /* (non-Javadoc)
     * @see com.cysols.framework.platform.Module#getCommands()
     */
    public Command[] getCommands() {
        Command[] cmds = new Command[cmdList.size()];
        for (int i = 0; i < cmdList.size(); i++) {
            cmds[i] = (Command) cmdList.get(i);
        }
        
        return cmds;
    }
    
    void accept(MenuBar bar) {
        bar.visit(this);
    }
}
