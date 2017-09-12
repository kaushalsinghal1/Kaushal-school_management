package com.banti.framework.platform.module;

import java.awt.event.KeyEvent;

import com.banti.framework.theme.ToolKit;

/**
 * TODO
 */
public class DefaultToolModuleScheme extends ToolModuleScheme {

    public DefaultToolModuleScheme() {        
        ToolKit tool = ToolKit.getInstance();
    
        TOOL_MENU_NAME = tool.getString("TOOL");
        TOOL_DESCRIPTION = null;
        TOOL_VISIBLE = true;
        TOOL_ENABLE = true;
        TOOL_MNEMONIC = KeyEvent.VK_T;
        
        if (TOOL_MNEMONIC != -1 && TOOL_MENU_NAME.indexOf(TOOL_MNEMONIC) == -1) {
        	TOOL_MENU_NAME = TOOL_MENU_NAME + " (" + (char) TOOL_MNEMONIC + ")";
        }
    }
}
