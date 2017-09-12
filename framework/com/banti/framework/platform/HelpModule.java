package com.banti.framework.platform;

import java.util.ArrayList;

import com.banti.framework.core.InitializationFailedException;
import com.banti.framework.platform.module.HelpModuleScheme;

/**
 * TODO
 */
final class HelpModule extends Module {
    
    private Command helpContents;
    private Command license;
    private Command about;
    
    private ArrayList cmdList;
    
    public HelpModule(HelpModuleScheme helpScheme, ViewMediator view) 
    	throws InitializationFailedException {
        
        super(helpScheme.HELP_MENU_NAME);

        // Checks Invalid scheme
        if (helpScheme.LICENSE_VISIBLE && helpScheme.LICENSE_REGISTRY_INSTANCE == null) {
            throw new InitializationFailedException("License Entry Menu is visible but LicenseRegistry instance is null.");
        }        
    
        super.setDescription(helpScheme.HELP_DESCRIPTION);
        super.setVisible(helpScheme.HELP_VISIBLE);
        super.setEnabled(helpScheme.HELP_ENABLE);
        super.setMnemonic(helpScheme.HELP_MNEMONIC);
        
        if (helpScheme.HELP_COMMAND == null) {
            helpContents = view.CommandBuilder.
        		new HelpContents(helpScheme.HELP_CONTENTS_MENU_NAME, 
        		        							helpScheme.HELP_CONTENTS_URL,helpScheme.HELP_CONTENTS_FILE);
        } else {
            helpContents = helpScheme.HELP_COMMAND;
        }
        
        helpContents.setDescription(helpScheme.HELP_CONTENTS_DESCRIPTION);
        helpContents.setIcon(helpScheme.HELP_CONTENTS_ICON);
        helpContents.setVisible(helpScheme.HELP_CONTENTS_VISIBLE);
        helpContents.setEnabled(helpScheme.HELP_CONTENTS_ENABLE);
        helpContents.setButtonEnabled(helpScheme.HELP_CONTENTS_BUTTON_ENABLE);
        helpContents.setMnemonic(helpScheme.HELP_CONTENTS_MNEMONIC);
        helpContents.setAccelerator(helpScheme.HELP_CONTENTS_ACCELERATOR);
        helpContents.setSeparator(helpContents.isVisiable());

        license = view.CommandBuilder.new LicenseEntry(helpScheme.LICENSE_MENU_NAME,
                																helpScheme.LICENSE_REGISTRY_INSTANCE);
        license.setDescription(helpScheme.LICENSE_DESCRIPTION);
        license.setIcon(helpScheme.LICENSE_ICON);
        license.setVisible(helpScheme.LICENSE_VISIBLE);
        license.setEnabled(helpScheme.LICENSE_ENABLE);
        license.setButtonEnabled(helpScheme.LICENSE_BUTTON_ENABLE);
        license.setMnemonic(helpScheme.LICENSE_MNEMONIC);
        license.setAccelerator(helpScheme.LICENSE_ACCELERATOR);
        license.setSeparator(license.isVisiable());
        
        if (helpScheme.ABOUT_OPTIONAL_PANEL == null) {
            about = view.CommandBuilder.new About(helpScheme.ABOUT_MENU_NAME,
        	        			 helpScheme.ABOUT_IMAGE_ICON,
        	        			 helpScheme.ABOUT_VERSION_MESSAGE,
        	        			 helpScheme.ABOUT_BUILD_MESSAGE);
        } else {
            about = view.CommandBuilder.new About(helpScheme.ABOUT_MENU_NAME,
   			 					helpScheme.ABOUT_IMAGE_ICON,
   			 					helpScheme.ABOUT_OPTIONAL_PANEL,
   			 					helpScheme.ABOUT_VERSION_MESSAGE,
   			 					helpScheme.ABOUT_BUILD_MESSAGE);            
      	}
        
        about.setDescription(helpScheme.ABOUT_DESCRIPTION);
        about.setIcon(helpScheme.ABOUT_ICON);
        about.setVisible(helpScheme.ABOUT_VISIBLE);
        about.setEnabled(helpScheme.ABOUT_ENABLE);
        about.setButtonEnabled(helpScheme.ABOUT_BUTTON_ENABLE);
        about.setMnemonic(helpScheme.ABOUT_MNEMONIC);
        about.setAccelerator(helpScheme.ABOUT_ACCELERATOR);
        if (!license.isVisiable()) about.setSeparator(about.isVisiable());
        
        cmdList = new ArrayList(3);
        cmdList.add(helpContents);
        
        if (helpScheme.HELP_COMMANDS != null) {
            for (int i = 0; i < helpScheme.HELP_COMMANDS.length; i++) {
                Command cmd = helpScheme.HELP_COMMANDS[i];
                cmdList.add(cmd);
            }
        }
        
        cmdList.add(license);
        cmdList.add(about);
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
   
}
