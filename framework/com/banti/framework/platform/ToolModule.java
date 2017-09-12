package com.banti.framework.platform;

import java.util.ArrayList;

import com.banti.framework.platform.module.ToolModuleScheme;

/**
 * 
 */
final class ToolModule extends Module {
    
    private ArrayList cmdList;
    
    ToolModule(ToolModuleScheme toolScheme) {
        super(toolScheme.TOOL_MENU_NAME);
        super.setDescription(toolScheme.TOOL_DESCRIPTION);
        super.setVisible(toolScheme.TOOL_VISIBLE);
        super.setEnabled(toolScheme.TOOL_ENABLE);
        super.setMnemonic(toolScheme.TOOL_MNEMONIC);
        
        cmdList = new ArrayList();
    }

    /* (non-Javadoc)
     * @see com.cysols.framework.platform.Module#getCommands()
     */
    public Command[] getCommands() {
        Command[] cmds = new Command[cmdList.size()];
        for (int i = 0; i < cmdList.size(); i++) {
            cmds[i] = (Command) cmdList.get(i);
        }
        
        return cmds;
    }
    
    void addCommand(Command command) {
        if (command == null) return;
        
        cmdList.add(command);
    }
    
    void removeCommand(Command command) {
        if (command == null) return;
        synchronized (cmdList) {
            cmdList.remove(command);
        }
    }
}
