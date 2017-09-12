/*
 * Copyright (c) Cyber Solutions Inc.
 * All Rights Reserved.
 * 
 * Created on 2004/08/16
 */
package com.banti.framework.platform.module;

import java.awt.event.KeyEvent;

import com.banti.framework.platform.licence.PrefsLicenseManager;
import com.banti.framework.theme.ToolKit;

/**
 * TODO
 */
public class DefaultHelpModuleScheme extends HelpModuleScheme {

	public DefaultHelpModuleScheme() {
		ToolKit tool = ToolKit.getInstance();

		HELP_MENU_NAME = tool.getString("HELP");
		HELP_DESCRIPTION = null;
		HELP_VISIBLE = true;
		HELP_ENABLE = true;
		HELP_MNEMONIC = KeyEvent.VK_H;
		if (HELP_MNEMONIC != -1 && HELP_MENU_NAME.indexOf(HELP_MNEMONIC) == -1) {
			HELP_MENU_NAME = HELP_MENU_NAME + " (" + (char) HELP_MNEMONIC + ")";
		}

		HELP_CONTENTS_MENU_NAME = tool.getString("HELP_CONTENTS");
		HELP_CONTENTS_DESCRIPTION = tool.getString("HELP_CONTENTS_TOOL_TIP");
		HELP_CONTENTS_ICON = tool
				.createImageIcon("/com/banti/framework/platform/images/help.gif");
		HELP_CONTENTS_VISIBLE = true;
		HELP_CONTENTS_ENABLE = true;
		HELP_CONTENTS_BUTTON_ENABLE = false;
		HELP_CONTENTS_FILE=true;
		HELP_CONTENTS_MNEMONIC = KeyEvent.VK_H;
		HELP_CONTENTS_ACCELERATOR = null;
	//	HELP_CONTENTS_URL = "help/index.html";
		HELP_CONTENTS_URL ="manual/UserManual_SMS.pdf";
		if (HELP_CONTENTS_MNEMONIC != -1
				&& HELP_CONTENTS_MENU_NAME.indexOf(HELP_CONTENTS_MNEMONIC) == -1) {
			HELP_CONTENTS_MENU_NAME = HELP_CONTENTS_MENU_NAME + " ("
					+ (char) HELP_CONTENTS_MNEMONIC + ")";
		}

		LICENSE_MENU_NAME = tool.getString("LICENSE_ENTRY");
		LICENSE_DESCRIPTION = tool.getString("LICENSE_TOOL_TIP");
		LICENSE_ICON = null;
		LICENSE_VISIBLE = true;
		LICENSE_ENABLE = true;
		LICENSE_BUTTON_ENABLE = false;
		LICENSE_MNEMONIC = -1;
		LICENSE_ACCELERATOR = null;
		LICENSE_REGISTRY_INSTANCE = new PrefsLicenseManager();

		if (LICENSE_MNEMONIC != -1
				&& LICENSE_MENU_NAME.indexOf(LICENSE_MNEMONIC) == -1) {
			LICENSE_MENU_NAME = LICENSE_MENU_NAME + " ("
					+ (char) LICENSE_MNEMONIC + ")";
		}

		ABOUT_MENU_NAME = tool.getString("ABOUT");
		ABOUT_DESCRIPTION = null;
		ABOUT_ICON = null;
		ABOUT_VISIBLE = true;
		ABOUT_ENABLE = true;
		ABOUT_BUTTON_ENABLE = false;
		ABOUT_MNEMONIC = -1;
		ABOUT_ACCELERATOR = null;
		ABOUT_IMAGE_ICON = tool
				.createImageIcon("/com/banti/framework/platform/images/sms.jpg");
		ABOUT_OPTIONAL_PANEL = null;
		ABOUT_VERSION_MESSAGE = tool.getString("VERSION")
				+ " : "
				+ System.getProperty("VERSION_VALUE",
						tool.getString("VERSION_VALUE"));
		ABOUT_BUILD_MESSAGE = tool.getString("Contact") + " : "
				+ System.getProperty("CONTACT_VALUE", "Kaushal- +918792703578");
		if (ABOUT_MNEMONIC != -1
				&& ABOUT_MENU_NAME.indexOf(ABOUT_MNEMONIC) == -1) {
			ABOUT_MENU_NAME = ABOUT_MENU_NAME + " (" + (char) ABOUT_MNEMONIC
					+ ")";
		}

		HELP_COMMANDS = null;
	}
}
