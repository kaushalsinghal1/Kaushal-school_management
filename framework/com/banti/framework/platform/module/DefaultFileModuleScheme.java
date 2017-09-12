package com.banti.framework.platform.module;

import java.awt.event.KeyEvent;

import com.banti.framework.theme.ToolKit;

/**
 * This class defines a default File Module Scheme.
 */
public class DefaultFileModuleScheme extends FileModuleScheme {

    public DefaultFileModuleScheme() {
        ToolKit tool = ToolKit.getInstance();
        String mapImagePath = "/com/banti/framework/platform/images/world.gif";
        String exitImagePath = "/com/banti/framework/platform/images/exit.jpg";

        FILE_MENU_NAME = tool.getString("FILE");
        FILE_DESCRIPTION = null;
        FILE_VISIBLE = true;
        FILE_ENABLE = true;
        FILE_MNEMONIC = KeyEvent.VK_F;
        if (FILE_MNEMONIC != -1 && FILE_MENU_NAME.indexOf(FILE_MNEMONIC) == -1) {
        	FILE_MENU_NAME = FILE_MENU_NAME + " (" + (char) FILE_MNEMONIC + ")";
        }

        CHAGE_PASSWD_MENU_NAME = tool.getString("PASSWORD_CHANGE");
        CHANGE_PASSWD_DESCRIPTION = null;
        CHANGE_PASSWD_ICON = null;
        CHANGE_PASSWD_VISIBLE = true;
        CHANGE_PASSWD_ENABLE = true;
        CHANGE_PASSWD_BUTTON_ENABLE = false;
        CHANGE_PASSWD_MNEMONIC = -1;
        ACCOUNT_MGMT_AVAILABLE = true;
        
      /*  OPEN_MAP_MENU_NAME = tool.getString("OPEN_MAP");
        OPEN_MAP_DESCRIPTION = tool.getString("OPEN_MAP_TOOLTIP");
        OPEN_MAP_ICON = tool.createImageIcon(mapImagePath);
        OPEN_MAP_VISIBLE = true;
        OPEN_MAP_ENABLE = true;
        OPEN_MAP_BUTTON_ENABLE = false;
        OPEN_MAP_MNEMONIC = -1;
        DEFAULT_MAP_FILE_PATH = "config/map/map.cf";
        MAP_WIN_TITLE = tool.getString("MAIN_MAP_TITLE");
        if (OPEN_MAP_MNEMONIC != -1 && OPEN_MAP_MENU_NAME.indexOf(OPEN_MAP_MNEMONIC) == -1) {
        	OPEN_MAP_MENU_NAME = OPEN_MAP_MENU_NAME + " (" + (char) OPEN_MAP_MNEMONIC + ")";
        }
        
        ADD_TAB_MENU_NAME = tool.getString("ADD_TAB");
        ADD_TAB_DESCRIPTION = tool.getString("ADD_TAB_TOOL_TIP");
        ADD_TAB_ICON = tool.createImageIcon("/com/cysols/framework/platform/images/addtab.gif");
        ADD_TAB_VISIBLE = true;
        ADD_TAB_ENABLE = true;
        ADD_TAB_BUTTON_ENABLE = false;
        ADD_TAB_MNEMONIC = KeyEvent.VK_T;
        if (ADD_TAB_MNEMONIC != -1 && ADD_TAB_MENU_NAME.indexOf(ADD_TAB_MNEMONIC) == -1) {
        	ADD_TAB_MENU_NAME = ADD_TAB_MENU_NAME + " (" + (char) ADD_TAB_MNEMONIC + ")";
        }
        
        REMOVE_TAB_MENU_NAME = tool.getString("REMOVE_TAB");
        REMOVE_TAB_DESCRIPTION = tool.getString("REMOVE_TAB_TOOL_TIP");
        REMOVE_TAB_ICON = tool.createImageIcon("/com/cysols/framework/platform/images/removetab.gif");;
        REMOVE_TAB_VISIBLE = true;
        REMOVE_TAB_ENABLE = false;
        REMOVE_TAB_BUTTON_ENABLE = false;
        REMOVE_TAB_MNEMONIC = KeyEvent.VK_L;
        if (REMOVE_TAB_MNEMONIC != -1 && REMOVE_TAB_MENU_NAME.indexOf(REMOVE_TAB_MNEMONIC) == -1) {
        	REMOVE_TAB_MENU_NAME = REMOVE_TAB_MENU_NAME + " (" + (char) REMOVE_TAB_MNEMONIC + ")";
        }*/
        
        PRINT_VISIBLE = true;
        PREVIEW_VISIBLE = true;
        
        EXIT_MENU_NAME = tool.getString("EXIT");
        EXIT_DESCRIPTION = tool.getString("EXIT");
        EXIT_ICON = tool.createImageIcon(exitImagePath);
        EXIT_VISIBLE = true;
        EXIT_ENABLE = true;
        EXIT_BUTTON_ENABLE = true;
        EXIT_MNEMONIC = KeyEvent.VK_X;
        if (EXIT_MNEMONIC != -1 && EXIT_MENU_NAME.indexOf(EXIT_MNEMONIC) == -1) {
        	EXIT_MENU_NAME = EXIT_MENU_NAME + " (" + (char) EXIT_MNEMONIC + ")";
        }

        FILE_COMMANDS = null;

    }
}