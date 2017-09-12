package com.banti.framework.platform.module;

import java.awt.event.KeyEvent;

import com.banti.framework.theme.ToolKit;

/**
 * TODO
 */
public class DefaultWindowModuleScheme extends WindowModuleScheme {

    public DefaultWindowModuleScheme() {
        ToolKit tool = ToolKit.getInstance();
        
        WINDOW_MENU_NAME = tool.getString("WINDOWS");
        WINDOW_DESCRIPTION = null;
        WINDOW_VISIBLE = true;
        WINDOW_ENABLE = true;
        WINDOW_MNEMONIC = KeyEvent.VK_W;
        if (WINDOW_MNEMONIC != -1 && WINDOW_MENU_NAME.indexOf(WINDOW_MNEMONIC) == -1) {
        	WINDOW_MENU_NAME = WINDOW_MENU_NAME + " (" + (char) WINDOW_MNEMONIC + ")";
        }
        
        CASCADE_MENU_NAME = tool.getString("CASCADE");
        CASCADE_DESCRIPTION = null;
        CASCADE_ICON = tool.createImageIcon("/com/cysols/framework/platform/images/cascade.gif");
        CASCADE_VISIBLE = true;
        CASCADE_ENABLE = false;
        CASCADE_BUTTON_ENABLE = false;
        CASCADE_MNEMONIC = KeyEvent.VK_C;
        CASCADE_ACCELERATOR = null; 
        if (CASCADE_MNEMONIC != -1 && CASCADE_MENU_NAME.indexOf(CASCADE_MNEMONIC) == -1) {
        	CASCADE_MENU_NAME = CASCADE_MENU_NAME + " (" + (char) CASCADE_MNEMONIC + ")";
        }

        TILE_MENU_NAME = tool.getString("TILE");
        TILE_DESCRIPTION = null;
        TILE_ICON = tool.createImageIcon("/com/cysols/framework/platform/images/tile.gif");
        TILE_VISIBLE = true;
        TILE_ENABLE = false;
        TILE_BUTTON_ENABLE = false;
        TILE_MNEMONIC = KeyEvent.VK_T;
        TILE_ACCELERATOR = null;
        if (TILE_MNEMONIC != -1 && TILE_MENU_NAME.indexOf(TILE_MNEMONIC) == -1) {
        	TILE_MENU_NAME = TILE_MENU_NAME + " (" + (char) TILE_MNEMONIC + ")";
        }
        
        ICONIFY_MENU_NAME = tool.getString("ICONIFY");
        ICONIFY_DESCRIPTION = null;
        ICONIFY_ICON = tool.createImageIcon("/com/cysols/framework/platform/images/empty.gif");
        ICONIFY_VISIBLE = true;
        ICONIFY_ENABLE = false;
        ICONIFY_BUTTON_ENABLE = false;
        ICONIFY_MNEMONIC = KeyEvent.VK_M;
        ICONIFY_ACCELERATOR = null;
        if (ICONIFY_MNEMONIC != -1 && ICONIFY_MENU_NAME.indexOf(ICONIFY_MNEMONIC) == -1) {
        	ICONIFY_MENU_NAME = ICONIFY_MENU_NAME + " (" + (char) ICONIFY_MNEMONIC + ")";
        }
        
        DEICONIFY_MENU_NAME = tool.getString("DEICONIFY");
        DEICONIFY_DESCRIPTION = null;
        DEICONIFY_ICON = tool.createImageIcon("/com/cysols/framework/platform/images/empty.gif");
        DEICONIFY_VISIBLE = true;
        DEICONIFY_ENABLE = false;
        DEICONIFY_BUTTON_ENABLE = false;
        DEICONIFY_MNEMONIC = KeyEvent.VK_E;
        DEICONIFY_ACCELERATOR = null;
        if (DEICONIFY_MNEMONIC != -1 && DEICONIFY_MENU_NAME.indexOf(DEICONIFY_MNEMONIC) == -1) {
        	DEICONIFY_MENU_NAME = DEICONIFY_MENU_NAME + " (" + (char) DEICONIFY_MNEMONIC + ")";
        }

        MAXIMIZE_MENU_NAME = tool.getString("MAXMIZE");
        MAXIMIZE_DESCRIPTION = null;
        MAXIMIZE_ICON = tool.createImageIcon("/com/cysols/framework/platform/images/empty.gif");
        MAXIMIZE_VISIBLE = true;
        MAXIMIZE_ENABLE = false;
        MAXIMIZE_BUTTON_ENABLE = false;
        MAXIMIZE_MNEMONIC = KeyEvent.VK_X;
        MAXIMIZE_ACCELERATOR = null;
        if (MAXIMIZE_MNEMONIC != -1 && MAXIMIZE_MENU_NAME.indexOf(MAXIMIZE_MNEMONIC) == -1) {
        	MAXIMIZE_MENU_NAME = MAXIMIZE_MENU_NAME + " (" + (char) MAXIMIZE_MNEMONIC + ")";
        }

        ALL_CLOSE_MENU_NAME = tool.getString("ALL_CLOSE");
        ALL_CLOSE_DESCRIPTION = null;
        ALL_CLOSE_ICON = tool.createImageIcon("/com/cysols/framework/platform/images/empty.gif");
        ALL_CLOSE_VISIBLE = true;
        ALL_CLOSE_ENABLE = false;
        ALL_CLOSE_BUTTON_ENABLE = false;
        ALL_CLOSE_MNEMONIC = KeyEvent.VK_B;
        ALL_CLOSE_ACCELERATOR = null;
        if (ALL_CLOSE_MNEMONIC != -1 && ALL_CLOSE_MENU_NAME.indexOf(ALL_CLOSE_MNEMONIC) == -1) {
        	ALL_CLOSE_MENU_NAME = ALL_CLOSE_MENU_NAME + " (" + (char) ALL_CLOSE_MNEMONIC + ")";
        }
        
        
    }
}
