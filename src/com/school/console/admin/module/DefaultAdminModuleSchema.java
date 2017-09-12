package com.school.console.admin.module;

import java.awt.event.KeyEvent;

import com.school.resource.ResourcesUtils;

public class DefaultAdminModuleSchema extends AdminModuleSchema {
	public DefaultAdminModuleSchema() {
		ADMIN_MENU_NAME = ResourcesUtils.getString("ADMINISTRATOR");
		ADMIN_DESCRIPTION = ResourcesUtils.getString("ADMINISTRATOR");
		ADMIN_VISIBLE = true;
		ADMIN_ENABLE = true;
		ADMIN_MNEMONIC = KeyEvent.VK_A;
		//
		 CLASS_DETAILS_NAME= ResourcesUtils.getString("CLASS_DETAILS");;
		 CLASS_DETAILS_DESCRIPTION= ResourcesUtils.getString("CLASS_DETAILS");;
		 CLASS_DETAILS_VISIBLE=true;
		 CLASS_DETAILS_ENABLE=true;
		 CLASS_DETAILSMONIC=KeyEvent.VK_C;
		// public Icon CLASS_DETAILS_ICON;
		//
		 SESSION_DETAILS_NAME= ResourcesUtils.getString("SESSION_DETAILS");
		 SESSION_DETAILS_DESCRIPTION= ResourcesUtils.getString("SESSION_DETAILS");
		 SESSION_DETAILS_VISIBLE=true;
		 SESSION_DETAILS_ENABLE=true;
		 SESSION_DETAILSMONIC=KeyEvent.VK_S;
		// public Icon SESSION_DETAILS_ICON;
		//
		 FEE_CONFIG_NAME= ResourcesUtils.getString("FEE_CLASS_CONFIG");
		 FEE_CONFIG_DESCRIPTION= ResourcesUtils.getString("FEE_CLASS_CONFIG");
		 FEE_CONFIG_VISIBLE=true;
		 FEE_CONFIG_ENABLE=true;
		 //FEE_CONFIGMONIC=
		// public Icon FEE_CONFIG_ICON;
	}
}
