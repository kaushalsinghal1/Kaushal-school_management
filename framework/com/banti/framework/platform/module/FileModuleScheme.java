package com.banti.framework.platform.module;

import javax.swing.Icon;

import com.banti.framework.platform.Command;

/**
 * A scheme that defines file menu items of main application.
 * This absract class has some variables. Those defines the feature of
 * the respective menu items. Menu and Menu Item are constructed by 
 * <a href="../Module.html"><i>Module</i></a> and <a href="../Command.html"><i>Command</i></a>
 * classes. This scheme give initial properties to the default commands of File Module.
 *   
 * @see com.cysols.framework.platform.FileModule
 **/
public abstract class FileModuleScheme {

    /**
     * The displayed name of File Menu. This corresponds to the name of FileModule.
     * 
     */
    public String FILE_MENU_NAME;
    
    /**
     * The tool tip of File Menu. This corresponds to the strings of Tool Tip of FileModule.
     * 
     */
    public String FILE_DESCRIPTION;
    
    /**
     * Visible or not of File Menu. This corresponds to the boolean of visiblity of FileModule.
     */
    public boolean FILE_VISIBLE;
    
    /**
     * Enable or not of File Menu. This corresponds to the boolean of availability of FileModule.
     */
    public boolean FILE_ENABLE;
    
    /**
     * The key used for storing an int key code to be used as the mnemonic for FileMenu.
     * This corresponds to the MNemonic of FileModule.
     */
    public int FILE_MNEMONIC;
    
    /**
     * The displayed name of "Password Change" menu item.
     */
    public String CHAGE_PASSWD_MENU_NAME;
    
    /**
     * The tool tip of "Password Change" menu item.
     */
    public String CHANGE_PASSWD_DESCRIPTION;
    
    /**
     * The icon image of "Password Change" menu item.
     */
    public Icon CHANGE_PASSWD_ICON;
    
    /**
     * The visibility of "Password Change" menu item. If you don't set  
     * <a href="../LoginManager.html"><i>LoginManager</i></a> to your application, 
     * "Password Change" is always unvisible.
     */
    public boolean CHANGE_PASSWD_VISIBLE;
    
    /**
     * The availability of "Password Change" menu item. Even if you set true, 
     * It cannot get enable without login.
     */
    public boolean CHANGE_PASSWD_ENABLE;
    
    /**
     * An icon button of "Change Password" is put on the tool bar if it is true. 
     * No button is put if false.
     */
    public boolean CHANGE_PASSWD_BUTTON_ENABLE;
    
    /**
     * The key used for storing an int key code to be used as the mnemonic 
     * for "Password Change" menu item. 
     */
    public int CHANGE_PASSWD_MNEMONIC;
    
    /**
     * true/false whether account mgmt button is available or not from password change 
     */
    public boolean ACCOUNT_MGMT_AVAILABLE;
    
    /**
     * The displayed name of "New Tab" menu item.
     */
    public String ADD_TAB_MENU_NAME;
    
    /**
     * The tool tip of "New Tab" menu item.
     */
    public String ADD_TAB_DESCRIPTION;
    
    /**
     * The icon image of "New Tab" menu item.
     */
    public Icon ADD_TAB_ICON;
    
    /**
     * The visibility of "New Tab" menu item.
     * If you set <a href="../TabbedRemoteLoginManager.html"><i>TabbedRemoteLoginManager</i>,
     * <b>NEVER use "New Tab" menu item. Their functionalities will be conflict.</b>
     */
    public boolean ADD_TAB_VISIBLE;
    
    /**
     * The availability of "New Tab" menu item.
     */
    public boolean ADD_TAB_ENABLE;
    
    /**
     * An icon button of "New Tab" is put on the tool bar or not.
     */
    public boolean ADD_TAB_BUTTON_ENABLE;

    /**
     * The key used for storing an int key code to be used as the mnemonic 
     * for "New Tab" menu item.
     */
    public int ADD_TAB_MNEMONIC;
    
    /**
     * The displayed name of "Close Tab" menu item.
     */
    public String REMOVE_TAB_MENU_NAME;
    
    /**
     * The tool tip of "Close Tab" menu item.
     */
    public String REMOVE_TAB_DESCRIPTION;
    
    /**
     * The icon image of "Close Tab" menu item.
     */
    public Icon REMOVE_TAB_ICON;
    
    /**
     * The visibility of "Close Tab" menu item.
     * If you set <a href="../TabbedRemoteLoginManager.html"><i>TabbedRemoteLoginManager</i>,
     * <b>NEVER use "Close Tab" menu item. Their functionalities will be conflict.</b>
     */
    public boolean REMOVE_TAB_VISIBLE;
    
    /**
     * The availability of "Close Tab" menu item.
     */
    public boolean REMOVE_TAB_ENABLE;
    
    /**
     * An icon button of "Close Tab" is put on the tool bar or not.
     */
    public boolean REMOVE_TAB_BUTTON_ENABLE;
    
    /**
     * The key used for storing an int key code to be used as the mnemonic 
     * for "Close Tab" menu item.
     */    
    public int REMOVE_TAB_MNEMONIC;

    /**
     * The dispayed name of "Open Map" menu item. The "Open Map " menu item
     * is for a map.cf on the Local Host. 
     * See <a href="RemoteOpenMapCommand.html">Remote Open Map command</a> 
     * if you want to manage map.cf on the server side.
     */
    public String OPEN_MAP_MENU_NAME;
    
    /**
     * The tool tip of "Open Map" menu item.
     */
    public String OPEN_MAP_DESCRIPTION;
    
    /**
     * The icon image of "Open Map" menu item.
     */
    public Icon OPEN_MAP_ICON;
    
    /**
     * The visibility of "Open Map" menu item.
     */
    public boolean OPEN_MAP_VISIBLE;
    
    /**
     * The availability of "Open Map" menu item.
     */
    public boolean OPEN_MAP_ENABLE;
    
    /**
     * An icon button of "Open Map" is put on the tool bar or not.
     */
    public boolean OPEN_MAP_BUTTON_ENABLE;
    
    /**
     * The key used for storing an int key code to be used as the mnemonic 
     * for "Open Map" menu item.
     */
    public int OPEN_MAP_MNEMONIC;
    
    /**
     * The file path of <b>map.cf</b> is specified by this variable. "Open Map" displays
     * Internal Map Window from this file path.
     */
    public String DEFAULT_MAP_FILE_PATH;
    
    /**
     * The tile name of Internal Map Window. The specified name is used for a title bar.
     */
    public String MAP_WIN_TITLE;;    
    
    /**
     * The visibility of "Print" menu item.
     */
    public boolean PRINT_VISIBLE;

    /**
     * The visibility of "Preview" menu item.
     */
    public boolean PREVIEW_VISIBLE;    
    
    /**
     * The displayed name of "Exit" menu item. 
     */
    public String EXIT_MENU_NAME;
    
    /**
     * The tool tip of "Exit" menu item.
     */
    public String EXIT_DESCRIPTION;
    
    /**
     * The icon image of "Exit" menu item.
     */
    public Icon EXIT_ICON;
    
    /**
     * The visibility of "Exit" menu item.
     */
    public boolean EXIT_VISIBLE;
    
    /**
     * The availability of "Exit" menu item.
     */
    public boolean EXIT_ENABLE;
    
    /**
     * An icon button of "Exit" is put on the tool bar or not.
     */
    public boolean EXIT_BUTTON_ENABLE;
    
    /**
     * The key used for storing an int key code to be used as the mnemonic 
     * for "Exit" menu item.
     */
    public int EXIT_MNEMONIC;

    /**
     * An array of Command instances. If you want to list other menu items in File Menu, 
     * you use this variable and give concrete commands.
     */
    public Command[] FILE_COMMANDS;
}
