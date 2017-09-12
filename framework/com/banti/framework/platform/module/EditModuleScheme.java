package com.banti.framework.platform.module;

import javax.swing.Icon;
import javax.swing.KeyStroke;

import com.banti.framework.platform.Command;

public abstract class EditModuleScheme {

    public String EDIT_MENU_NAME;
    public String EDIT_DESCRIPTION;
    public boolean EDIT_VISIBLE;
    public boolean EDIT_ENABLE;
    public int EDIT_MNEMONIC;    
    
    public String COPY_MENU_NAME;
    public String COPY_DESCRIPTION;
    public Icon COPY_ICON;
    public boolean COPY_VISIBLE;
    public boolean COPY_ENABLE;
    public boolean COPY_BUTTON_ENABLE;
    public int COPY_MNEMONIC;
    public KeyStroke COPY_ACCELERATOR;
    
    public String CUT_MENU_NAME;
    public String CUT_DESCRIPTION;
    public Icon CUT_ICON;
    public boolean CUT_VISIBLE;
    public boolean CUT_ENABLE;
    public boolean CUT_BUTTON_ENABLE;
    public int CUT_MNEMONIC;
    public KeyStroke CUT_ACCELERATOR; 
    
    public String PASTE_MENU_NAME;
    public String PASTE_DESCRIPTION;
    public Icon PASTE_ICON;
    public boolean PASTE_VISIBLE;
    public boolean PASTE_ENABLE;
    public boolean PASTE_BUTTON_ENABLE;
    public int PASTE_MNEMONIC;
    public KeyStroke PASTE_ACCELERATOR; 

    public String SELECTALL_MENU_NAME;
    public String SELECTALL_DESCRIPTION;
    public Icon SELECTALL_ICON;
    public boolean SELECTALL_VISIBLE;
    public boolean SELECTALL_ENABLE;
    public boolean SELECTALL_BUTTON_ENABLE;
    public int SELECTALL_MNEMONIC;
    public KeyStroke SELECTALL_ACCELERATOR; 

    public String DELETEALL_MENU_NAME;
    public String DELETEALL_DESCRIPTION;
    public Icon DELETEALL_ICON;
    public boolean DELETEALL_VISIBLE;
    public boolean DELETEALL_ENABLE;
    public boolean DELETEALL_BUTTON_ENABLE;
    public int DELETEALL_MNEMONIC;
    public KeyStroke DELETEALL_ACCELERATOR; 

    public Command[] EDIT_COMMANDS;
}
