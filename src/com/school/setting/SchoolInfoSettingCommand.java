package com.school.setting;

import java.awt.event.ActionEvent;

import com.banti.framework.platform.Command;

public class SchoolInfoSettingCommand extends Command {
//	private static final String NAME = ResourcesUtils
//			.getString("School Info Setting");

	public SchoolInfoSettingCommand(String name) {
		super(name);
	}

	@Override
	public void entryPoint(ActionEvent ae) {
		new SchoolInfoSetting();
	}

}
