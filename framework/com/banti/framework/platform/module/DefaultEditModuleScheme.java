package com.banti.framework.platform.module;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import com.banti.framework.theme.ToolKit;

public class DefaultEditModuleScheme extends EditModuleScheme {

    public DefaultEditModuleScheme() {
        ToolKit tool = ToolKit.getInstance();
        
        EDIT_MENU_NAME = tool.getString("EDIT");
        EDIT_DESCRIPTION = null;
        EDIT_VISIBLE = true;
        EDIT_ENABLE = true;
        EDIT_MNEMONIC = KeyEvent.VK_E;    
        if (EDIT_MNEMONIC != -1 && EDIT_MENU_NAME.indexOf(EDIT_MNEMONIC) == -1) {
            EDIT_MENU_NAME = EDIT_MENU_NAME +  " (" + (char) EDIT_MNEMONIC + ")";            
        }
    
        COPY_MENU_NAME = tool.getString("COPY");
        COPY_DESCRIPTION = null;
        COPY_ICON = tool.createImageIcon("/com/banti/framework/platform/images/copy.gif");
        COPY_VISIBLE = true;
        COPY_ENABLE = false;
        COPY_BUTTON_ENABLE = false;
        COPY_MNEMONIC = -1;
        COPY_ACCELERATOR =  KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK);
        if (COPY_MNEMONIC != -1 && COPY_MENU_NAME.indexOf(COPY_MNEMONIC) == -1) {
            COPY_MENU_NAME = COPY_MENU_NAME +  " (" + (char) COPY_MNEMONIC + ")";            
        }
        
        CUT_MENU_NAME = tool.getString("CUT");
        CUT_DESCRIPTION = null;
        CUT_ICON = tool.createImageIcon("/com/banti/framework/platform/images/cut.gif");
        CUT_VISIBLE = true;
        CUT_ENABLE = false;
        CUT_BUTTON_ENABLE = false;
        CUT_MNEMONIC = -1;
        CUT_ACCELERATOR =  KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK);
        if (CUT_MNEMONIC != -1 && CUT_MENU_NAME.indexOf(CUT_MNEMONIC) == -1) {
            CUT_MENU_NAME = CUT_MENU_NAME +  " (" + (char) CUT_MNEMONIC + ")";            
        }
        
        PASTE_MENU_NAME = tool.getString("PASTE");
        PASTE_DESCRIPTION = null;
        PASTE_ICON = tool.createImageIcon("/com/banti/framework/platform/images/paste.gif");
        PASTE_VISIBLE = true;
        PASTE_ENABLE = false;
        PASTE_BUTTON_ENABLE = false;
        PASTE_MNEMONIC = -1;
        PASTE_ACCELERATOR =  KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK);
        if (PASTE_MNEMONIC != -1 && PASTE_MENU_NAME.indexOf(PASTE_MNEMONIC) == -1) {
            PASTE_MENU_NAME = PASTE_MENU_NAME +  " (" + (char) PASTE_MNEMONIC + ")";            
        }

        SELECTALL_MENU_NAME = tool.getString("SELECT_ALL");
        SELECTALL_DESCRIPTION = null;
        SELECTALL_ICON = tool.createImageIcon("/com/banti/framework/platform/images/empty.gif");
        SELECTALL_VISIBLE = true;
        SELECTALL_ENABLE = false;
        SELECTALL_BUTTON_ENABLE = false;
        SELECTALL_MNEMONIC = -1;
        SELECTALL_ACCELERATOR =  KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK);
        if (SELECTALL_MNEMONIC != -1 && SELECTALL_MENU_NAME.indexOf(SELECTALL_MNEMONIC) == -1) {
            SELECTALL_MENU_NAME = SELECTALL_MENU_NAME +  " (" + (char) SELECTALL_MNEMONIC + ")";            
        }

        DELETEALL_MENU_NAME = tool.getString("CLEAR");
        DELETEALL_DESCRIPTION = null;
        DELETEALL_ICON = tool.createImageIcon("/com/banti/framework/platform/images/empty.gif");
        DELETEALL_VISIBLE = true;
        DELETEALL_ENABLE = false;
        DELETEALL_BUTTON_ENABLE = false;
        DELETEALL_MNEMONIC = -1;
        DELETEALL_ACCELERATOR =  KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0);
        if (DELETEALL_MNEMONIC != -1 && DELETEALL_MENU_NAME.indexOf(DELETEALL_MNEMONIC) == -1) {
            DELETEALL_MENU_NAME = DELETEALL_MENU_NAME +  " (" + (char) DELETEALL_MNEMONIC + ")";            
        }

        EDIT_COMMANDS = null;
     
    }
}
