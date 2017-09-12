package com.school.setting;

import java.util.ArrayList;

import com.banti.framework.platform.Activator;
import com.banti.framework.platform.Command;
import com.banti.framework.platform.PluginModule;
import com.banti.framework.platform.module.DefaultActivator;

public class SettingModule extends PluginModule {
	private Activator activator;

	private ArrayList<Command> cmdList;

	public SettingModule(SettingModuleSchema schema) {
		super(schema.SETTING_MENU_NAME);
		super.setDescription(schema.SETTING_DESCRIPTION);
		super.setMnemonic(schema.SETTING_MNEMONIC);
		super.setEnabled(schema.SETTING_ENABLE);
		super.setVisible(schema.SETTING_VISIBLE);
		activator = new DefaultActivator();

		cmdList = new ArrayList<Command>();
		Command command = new SchoolInfoSettingCommand(
				schema.SCHOOL_INFO_NAME);
		command.setDescription(schema.SCHOOL_INFO_DESCRIPTION);
	//	command.setMnemonic(schema.SCHOOL_INFO_MNEMONIC);
		command.setLoginNeeds(true);
		command.setIcon(schema.SCHOOL_INFO_ICON);
		command.setVisible(schema.SCHOOL_INFO_VISIBLE);
		command.setEnabled(schema.SCHOOL_INFO_VISIBLE);
		cmdList.add(command);
	}

	@Override
	public Activator getActivator() {
		return activator;
	}

	@Override
	public int getOrder() {
		return 0;
	}

	@Override
	public Command[] getCommands() {
		Command[] cmds = new Command[cmdList.size()];
		for (int i = 0; i < cmdList.size(); i++) {
			cmds[i] = (Command) cmdList.get(i);
		}

		return cmds;
	}

}
