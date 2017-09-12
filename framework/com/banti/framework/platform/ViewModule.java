package com.banti.framework.platform;

import java.util.ArrayList;

import com.banti.framework.platform.module.ViewModuleScheme;

/**
 * 
 */

final class ViewModule extends Module {

    private ArrayList cmdList;
    
    ViewModule(ViewModuleScheme viewScheme, ViewMediator view) {
        super(viewScheme.VIEW_MENU_NAME);        
        super.setDescription(viewScheme.VIEW_DESCRIPTION);
        super.setVisible(viewScheme.VIEW_VISIBLE);
        super.setEnabled(viewScheme.VIEW_ENABLE);
        super.setMnemonic(viewScheme.VIEW_MNEMONIC);
        
        ToggledCommand iconBarSwitch = view.CommandBuilder.createToolBarSwitch(viewScheme.TOOL_BAR_MENU_NAME);        
        iconBarSwitch.setDescription(viewScheme.TOOL_BAR_DESCRIPTION);
        iconBarSwitch.setIcon(viewScheme.TOOL_BAR_ICON);
        iconBarSwitch.setVisible(viewScheme.TOOL_BAR_VISIBLE);
        iconBarSwitch.setEnabled(viewScheme.TOOL_BAR_ENABLE);
        iconBarSwitch.setButtonEnabled(false);
        iconBarSwitch.setMnemonic(viewScheme.TOOL_BAR_MNEMONIC);
        iconBarSwitch.setSelected(viewScheme.TOOL_BAR_SELECTED);

        ToggledCommand statusBarSwitch = view.CommandBuilder.createStatusBarSwitch(viewScheme.STATUS_BAR_MENU_NAME);
        statusBarSwitch.setDescription(viewScheme.STATUS_BAR_DESCRIPTION);
        statusBarSwitch.setIcon(viewScheme.STATUS_BAR_ICON);
        statusBarSwitch.setVisible(viewScheme.STATUS_BAR_VISIBLE);
        statusBarSwitch.setEnabled(viewScheme.STATUS_BAR_ENABLE);
        statusBarSwitch.setButtonEnabled(false);
        statusBarSwitch.setMnemonic(viewScheme.STATUS_BAR_MNEMONIC);
        statusBarSwitch.setSelected(viewScheme.STATUS_BAR_SELECTED);

        ToggledCommand windowBarSwitch = view.CommandBuilder.createWindowBarSwitch(viewScheme.WINDOW_BAR_MENU_NAME);
        windowBarSwitch.setDescription(viewScheme.WINDOW_BAR_DESCRIPTION);
        windowBarSwitch.setIcon(viewScheme.WINDOW_BAR_ICON);
        windowBarSwitch.setVisible(viewScheme.WINDOW_BAR_VISIBLE);
        windowBarSwitch.setEnabled(viewScheme.WINDOW_BAR_ENABLE);
        windowBarSwitch.setButtonEnabled(false);
        windowBarSwitch.setMnemonic(viewScheme.WINDOW_BAR_MNEMONIC);
        windowBarSwitch.setSelected(viewScheme.WINDOW_BAR_SELECTED);

        cmdList = new ArrayList(4);        
        cmdList.add(iconBarSwitch);
        cmdList.add(statusBarSwitch);
        cmdList.add(windowBarSwitch);
        
        if (viewScheme.VIEW_COMMANDS != null) {
            for (int i = 0; i < viewScheme.VIEW_COMMANDS.length; i++) {
                Command cmd = viewScheme.VIEW_COMMANDS[i];
                cmdList.add(cmd);
            }
        }        
        
        if (viewScheme.VIEW_ENABLE) {
            Command pref = view.CommandBuilder.new Preference(viewScheme.PREFERENCE_MENU_NAME);
            pref.setDescription(viewScheme.PREFERENCE_DESCRIPTION);
            pref.setIcon(viewScheme.PREFERENCE_ICON);
            pref.setVisible(viewScheme.PREFERENCE_VISIBLE);
            pref.setEnabled(viewScheme.PREFERENCE_ENABLE);
            pref.setButtonEnabled(viewScheme.PREFERENCE_BUTTON_ENABLE);
            pref.setMnemonic(viewScheme.PREFERENCE_MNEMONIC);
            pref.setAccelerator(viewScheme.PREFERENCE_ACCELERATOR);        
            pref.setSeparator(true);
            
            cmdList.add(pref);
        }
    }
    
    /* (non-Javadoc)
     * @see com.banti.framework.platform.Module#getCommands()
     */
    public Command[] getCommands() {
        Command[] cmds = new Command[cmdList.size()];
        for (int i = 0; i < cmdList.size(); i++) {
            cmds[i] = (Command) cmdList.get(i);
        }
        
        return cmds;
    }
}