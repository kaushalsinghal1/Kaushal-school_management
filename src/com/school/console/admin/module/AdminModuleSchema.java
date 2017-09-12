package com.school.console.admin.module;

import javax.swing.Icon;

public abstract class AdminModuleSchema {
	public String ADMIN_MENU_NAME;
	public String ADMIN_DESCRIPTION;
	public boolean ADMIN_VISIBLE;
	public boolean ADMIN_ENABLE;
	public int ADMIN_MNEMONIC;

	public String CLASS_DETAILS_NAME;
	public String CLASS_DETAILS_DESCRIPTION;
	public boolean CLASS_DETAILS_VISIBLE;
	public boolean CLASS_DETAILS_ENABLE;
	public int CLASS_DETAILSMONIC;
	public Icon CLASS_DETAILS_ICON;

	public String SESSION_DETAILS_NAME;
	public String SESSION_DETAILS_DESCRIPTION;
	public boolean SESSION_DETAILS_VISIBLE;
	public boolean SESSION_DETAILS_ENABLE;
	public int SESSION_DETAILSMONIC;
	public Icon SESSION_DETAILS_ICON;
	
	public String FEE_CONFIG_NAME;
	public String FEE_CONFIG_DESCRIPTION;
	public boolean FEE_CONFIG_VISIBLE;
	public boolean FEE_CONFIG_ENABLE;
	public int FEE_CONFIGMONIC;
	public Icon FEE_CONFIG_ICON;

}
