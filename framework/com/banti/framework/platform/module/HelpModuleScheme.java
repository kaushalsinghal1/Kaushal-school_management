package com.banti.framework.platform.module;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import com.banti.framework.platform.Command;
import com.banti.framework.platform.LicenseManager;

/**
 * TODO
 */
public class HelpModuleScheme {

    public String HELP_MENU_NAME;
    public String HELP_DESCRIPTION;
    public boolean HELP_VISIBLE;
    public boolean HELP_ENABLE;
    public int HELP_MNEMONIC;
    
    public String HELP_CONTENTS_MENU_NAME;
    public String HELP_CONTENTS_DESCRIPTION;
    public Icon HELP_CONTENTS_ICON;
    public boolean HELP_CONTENTS_VISIBLE;
    public boolean HELP_CONTENTS_ENABLE;
    public boolean HELP_CONTENTS_BUTTON_ENABLE;
    public int HELP_CONTENTS_MNEMONIC;
    public KeyStroke HELP_CONTENTS_ACCELERATOR; 
    public String HELP_CONTENTS_URL;
    public boolean HELP_CONTENTS_FILE;
    public Command HELP_COMMAND;

    public String LICENSE_MENU_NAME;
    public String LICENSE_DESCRIPTION;
    public Icon LICENSE_ICON;
    public boolean LICENSE_VISIBLE;
    public boolean LICENSE_ENABLE;
    public boolean LICENSE_BUTTON_ENABLE;
    public int LICENSE_MNEMONIC;
    public KeyStroke LICENSE_ACCELERATOR;
    public LicenseManager LICENSE_REGISTRY_INSTANCE;

    public String ABOUT_MENU_NAME;
    public String ABOUT_DESCRIPTION;
    public Icon ABOUT_ICON;
    public boolean ABOUT_VISIBLE;
    public boolean ABOUT_ENABLE;
    public boolean ABOUT_BUTTON_ENABLE;
  
    public int ABOUT_MNEMONIC;
    public KeyStroke ABOUT_ACCELERATOR;
    public ImageIcon ABOUT_IMAGE_ICON;
    public JPanel ABOUT_OPTIONAL_PANEL;
    public String ABOUT_VERSION_MESSAGE;
    public String ABOUT_BUILD_MESSAGE;
    
    public Command[] HELP_COMMANDS;
}
