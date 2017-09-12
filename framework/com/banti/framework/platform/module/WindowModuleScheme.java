package com.banti.framework.platform.module;

import javax.swing.Icon;
import javax.swing.KeyStroke;

import com.banti.framework.platform.Command;

/**
 *
 */
public class WindowModuleScheme {

    public String WINDOW_MENU_NAME;
    public String WINDOW_DESCRIPTION;
    public boolean WINDOW_VISIBLE;
    public boolean WINDOW_ENABLE;
    public int WINDOW_MNEMONIC;
    
    public String CASCADE_MENU_NAME;
    public String CASCADE_DESCRIPTION;
    public Icon CASCADE_ICON;
    public boolean CASCADE_VISIBLE;
    public boolean CASCADE_ENABLE;
    public boolean CASCADE_BUTTON_ENABLE;
    public int CASCADE_MNEMONIC;
    public KeyStroke CASCADE_ACCELERATOR; 
       
    public String TILE_MENU_NAME;
    public String TILE_DESCRIPTION;
    public Icon TILE_ICON;
    public boolean TILE_VISIBLE;
    public boolean TILE_ENABLE;
    public boolean TILE_BUTTON_ENABLE;
    public int TILE_MNEMONIC;
    public KeyStroke TILE_ACCELERATOR;

    public String ICONIFY_MENU_NAME;
    public String ICONIFY_DESCRIPTION;
    public Icon ICONIFY_ICON;
    public boolean ICONIFY_VISIBLE;
    public boolean ICONIFY_ENABLE;
    public boolean ICONIFY_BUTTON_ENABLE;
    public int ICONIFY_MNEMONIC;
    public KeyStroke ICONIFY_ACCELERATOR;

    public String DEICONIFY_MENU_NAME;
    public String DEICONIFY_DESCRIPTION;
    public Icon DEICONIFY_ICON;
    public boolean DEICONIFY_VISIBLE;
    public boolean DEICONIFY_ENABLE;
    public boolean DEICONIFY_BUTTON_ENABLE;
    public int DEICONIFY_MNEMONIC;
    public KeyStroke DEICONIFY_ACCELERATOR;

    public String MAXIMIZE_MENU_NAME;
    public String MAXIMIZE_DESCRIPTION;
    public Icon MAXIMIZE_ICON;
    public boolean MAXIMIZE_VISIBLE;
    public boolean MAXIMIZE_ENABLE;
    public boolean MAXIMIZE_BUTTON_ENABLE;
    public int MAXIMIZE_MNEMONIC;
    public KeyStroke MAXIMIZE_ACCELERATOR;

    public String ALL_CLOSE_MENU_NAME;
    public String ALL_CLOSE_DESCRIPTION;
    public Icon ALL_CLOSE_ICON;
    public boolean ALL_CLOSE_VISIBLE;
    public boolean ALL_CLOSE_ENABLE;
    public boolean ALL_CLOSE_BUTTON_ENABLE;
    public int ALL_CLOSE_MNEMONIC;
    public KeyStroke ALL_CLOSE_ACCELERATOR;
    
    /**
     * An array of Command instances. If you want to list other menu items in Window Menu, 
     * you use this variable and give concrete commands.
     */
    public Command[] WINDOW_COMMANDS;

}
