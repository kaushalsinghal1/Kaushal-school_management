package com.banti.framework.platform.module;

import java.awt.event.KeyEvent;

import com.banti.framework.theme.ToolKit;

/**
 * 
 */
public class DefaultViewModuleScheme extends ViewModuleScheme {

    public DefaultViewModuleScheme() {
        ToolKit tool = ToolKit.getInstance();
        
        VIEW_MENU_NAME = tool.getString("VIEW");
        VIEW_DESCRIPTION = null;
        VIEW_VISIBLE = true;        
        VIEW_ENABLE = true;
        VIEW_MNEMONIC = KeyEvent.VK_V;
        if (VIEW_MNEMONIC != -1 && VIEW_MENU_NAME.indexOf(VIEW_MNEMONIC) == -1) {
            VIEW_MENU_NAME = VIEW_MENU_NAME +  " (" + (char) VIEW_MNEMONIC + ")";            
        }
    
        TOOL_BAR_MENU_NAME = tool.getString("TOOL_BAR_TOOL_TIP");
        TOOL_BAR_DESCRIPTION = tool.getString("TOOL_BAR_SWITCH");
        TOOL_BAR_ICON = null;
        TOOL_BAR_VISIBLE = true;
        TOOL_BAR_ENABLE = true;
        TOOL_BAR_MNEMONIC =-1;
        TOOL_BAR_SELECTED = true;

        STATUS_BAR_MENU_NAME = tool.getString("STATUS_BAR_TOOL_TIP");
        STATUS_BAR_DESCRIPTION = tool.getString("STATUS_BAR_SWITCH");
        STATUS_BAR_ICON = null;
        STATUS_BAR_VISIBLE = true;
        STATUS_BAR_ENABLE = true;
        STATUS_BAR_MNEMONIC = -1;
        STATUS_BAR_SELECTED = true;
    
        WINDOW_BAR_MENU_NAME = tool.getString("WINDOW_BAR_TOOL_TIP");
        WINDOW_BAR_DESCRIPTION = tool.getString("WINDOW_BAR_SWITCH");
        WINDOW_BAR_ICON = null;
        WINDOW_BAR_VISIBLE = true;
        WINDOW_BAR_ENABLE = true;
        WINDOW_BAR_MNEMONIC = -1;
        WINDOW_BAR_SELECTED = false;
        
        PREFERENCE_MENU_NAME = tool.getString("PREFERENCE");
        PREFERENCE_DESCRIPTION = tool.getString("PREFERENCE_TOOL_TIP");
        PREFERENCE_ICON = null;
        PREFERENCE_VISIBLE = true;
        PREFERENCE_ENABLE = true;
        PREFERENCE_BUTTON_ENABLE = false;
        PREFERENCE_MNEMONIC = -1;
        PREFERENCE_ACCELERATOR = null; 
        if (PREFERENCE_MNEMONIC != -1 && PREFERENCE_MENU_NAME.indexOf(PREFERENCE_MNEMONIC) == -1) {
            PREFERENCE_MENU_NAME = PREFERENCE_MENU_NAME +  " (" + (char) PREFERENCE_MNEMONIC + ")";            
        }
        
         VIEW_COMMANDS = null;
    }
}
