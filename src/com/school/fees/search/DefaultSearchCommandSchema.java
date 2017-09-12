package com.school.fees.search;

import javax.swing.ImageIcon;

import com.banti.framework.theme.ToolKit;

public class DefaultSearchCommandSchema {
	private ImageIcon icon;
	private String DISPLAY_NAME = "Student Search";
	private String COMMANDSTRING = "SEARCH";

	public DefaultSearchCommandSchema() {
		icon = ToolKit.getInstance().createImageIcon(imagePath);
	}

	public DefaultSearchCommandSchema(String displayString, String CMD_STRING) {
		this.DISPLAY_NAME = displayString;
		this.COMMANDSTRING = CMD_STRING;
		icon = ToolKit.getInstance().createImageIcon(imagePath);
	}

	public ImageIcon getIcon() {
		return icon;
	}

	public void setIcon(ImageIcon icon) {
		this.icon = icon;
	}

	public String getDISPLAY_NAME() {
		return DISPLAY_NAME;
	}

	public void setDISPLAY_NAME(String dISPLAY_NAME) {
		DISPLAY_NAME = dISPLAY_NAME;
	}

	public String getCOMMANDSTRING() {
		return COMMANDSTRING;
	}

	public void setCOMMANDSTRING(String cOMMANDSTRING) {
		COMMANDSTRING = cOMMANDSTRING;
	}

	private static final String imagePath = "/com/school/resource/images/search.png";

}
