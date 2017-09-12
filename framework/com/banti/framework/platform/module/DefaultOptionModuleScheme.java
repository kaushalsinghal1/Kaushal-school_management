package com.banti.framework.platform.module;

import java.awt.event.KeyEvent;

import javax.swing.Icon;

import com.banti.framework.theme.ToolKit;

/**
 * TODO
 */
public class DefaultOptionModuleScheme extends OptionModuleScheme {

	public DefaultOptionModuleScheme() {
		ToolKit tool = ToolKit.getInstance();

		OPTION_MENU_NAME = tool.getString("OPTION");
		OPTION_DESCRIPTION = null;
		OPTION_VISIBLE = true;
		OPTION_ENABLE = true;
		OPTION_MNEMONIC = KeyEvent.VK_O;
		OPTION_COMMANDS = null;

		ACCOUNT_MGNT_NAME = tool.getString("ACCOUNT_MGMT");
		ACCOUNT_MGNT_DESCRIPTION = tool.getString("ACCOUNT_MGMT");
		ACCOUNT_MGNT_VISIBLE = true;
		ACCOUNT_MGNT_ENABLE = true;
		ACCOUNT_MGNT_USER_ENABLE = true;
		ACCOUNT_MGNT_MNEMONIC = KeyEvent.VK_M;
		// Icon ACCOUNT_MGNT_ICON;

		if (OPTION_MNEMONIC != -1
				&& OPTION_MENU_NAME.indexOf(OPTION_MNEMONIC) == -1) {
			OPTION_MENU_NAME = OPTION_MENU_NAME + " (" + (char) OPTION_MNEMONIC
					+ ")";
		}
	}
}
