package com.banti.framework.platform.module;

import javax.swing.Icon;
import javax.swing.KeyStroke;

import com.banti.framework.platform.Command;

/**
 * TODO
 */
public abstract class ViewModuleScheme {

    public String VIEW_MENU_NAME;
    public String VIEW_DESCRIPTION;
    public boolean VIEW_VISIBLE;
    public boolean VIEW_ENABLE;
    public int VIEW_MNEMONIC;
    
    public String TOOL_BAR_MENU_NAME;
    public String TOOL_BAR_DESCRIPTION;
    public Icon TOOL_BAR_ICON;
    public boolean TOOL_BAR_VISIBLE;
    public boolean TOOL_BAR_ENABLE;
    public int TOOL_BAR_MNEMONIC;
    public boolean TOOL_BAR_SELECTED;

    public String STATUS_BAR_MENU_NAME;
    public String STATUS_BAR_DESCRIPTION;
    public Icon STATUS_BAR_ICON;
    public boolean STATUS_BAR_VISIBLE;
    public boolean STATUS_BAR_ENABLE;
    public int STATUS_BAR_MNEMONIC;
    public boolean STATUS_BAR_SELECTED;
    
    public String WINDOW_BAR_MENU_NAME;
    public String WINDOW_BAR_DESCRIPTION;
    public Icon WINDOW_BAR_ICON;
    public boolean WINDOW_BAR_VISIBLE;
    public boolean WINDOW_BAR_ENABLE;
    public int WINDOW_BAR_MNEMONIC;
    public boolean WINDOW_BAR_SELECTED;

    public String PREFERENCE_MENU_NAME;
    public String PREFERENCE_DESCRIPTION;
    public Icon PREFERENCE_ICON;
    public boolean PREFERENCE_VISIBLE;
    public boolean PREFERENCE_ENABLE;
    public boolean PREFERENCE_BUTTON_ENABLE;
    public int PREFERENCE_MNEMONIC;
    public KeyStroke PREFERENCE_ACCELERATOR;
    
    public Command[] VIEW_COMMANDS;
}
