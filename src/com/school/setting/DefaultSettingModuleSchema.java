package com.school.setting;

import java.awt.event.KeyEvent;

import com.school.resource.ResourcesUtils;

public class DefaultSettingModuleSchema extends SettingModuleSchema {
	public DefaultSettingModuleSchema() {
		SETTING_MENU_NAME = ResourcesUtils.getString("SETTING");
		SETTING_DESCRIPTION = ResourcesUtils.getString("SETTING");
		SETTING_VISIBLE = true;
		SETTING_ENABLE = true;
		SETTING_MNEMONIC = KeyEvent.VK_S;
		//
		SCHOOL_INFO_NAME = ResourcesUtils.getString("SCHOOL_DETAILS");
		SCHOOL_INFO_DESCRIPTION = ResourcesUtils.getString("SCHOOL_DETAILS");
		SCHOOL_INFO_VISIBLE = true;
		SCHOOL_INFO_ENABLE = true;
		SCHOOL_INFO_MNEMONIC = KeyEvent.VK_C;
		// public Icon CLASS_DETAILS_ICON;
		//
		// FEE_CONFIGMONIC=
		// public Icon FEE_CONFIG_ICON;
	}
}
