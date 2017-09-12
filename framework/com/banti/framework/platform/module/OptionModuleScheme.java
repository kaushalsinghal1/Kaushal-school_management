package com.banti.framework.platform.module;

import javax.swing.Icon;

import com.banti.framework.platform.Command;
import com.banti.framework.platform.peer.AccountMgmtCommand;

/**
 * TODO
 */
public abstract class OptionModuleScheme {
    
    public String OPTION_MENU_NAME;
    public String OPTION_DESCRIPTION;
    public boolean OPTION_VISIBLE;
    public boolean OPTION_ENABLE;
    public int OPTION_MNEMONIC;
    
    public String ACCOUNT_MGNT_NAME;
    public String ACCOUNT_MGNT_DESCRIPTION;
    public boolean ACCOUNT_MGNT_VISIBLE;
    public boolean ACCOUNT_MGNT_ENABLE;
    public boolean ACCOUNT_MGNT_USER_ENABLE;
    public int ACCOUNT_MGNT_MNEMONIC;
    public Icon ACCOUNT_MGNT_ICON;
    

    public Command[] OPTION_COMMANDS;
    
    public OptionModuleScheme() {
	//AccountMgmtCommand command=new 
	}
    
}
