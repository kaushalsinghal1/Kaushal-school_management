package com.school.console.admin.module;

import java.util.ArrayList;

import com.banti.framework.platform.Activator;
import com.banti.framework.platform.Command;
import com.banti.framework.platform.PluginModule;
import com.banti.framework.platform.module.DefaultActivator;
import com.school.console.admin.ClassDetailsCommand;
import com.school.console.admin.FeeConfigureCommand;
import com.school.console.admin.SessionDetailsCommand;

public class AdminModule extends PluginModule {
	private Activator activator;

	private ArrayList<Command> cmdList;

	public AdminModule(AdminModuleSchema schema) {
		super(schema.ADMIN_MENU_NAME);
		super.setDescription(schema.ADMIN_DESCRIPTION);
		super.setMnemonic(schema.ADMIN_MNEMONIC);
		super.setEnabled(schema.ADMIN_ENABLE);
		super.setVisible(schema.ADMIN_VISIBLE);
		activator = new DefaultActivator();

		cmdList = new ArrayList<Command>();
		Command command = new ClassDetailsCommand(
				schema.CLASS_DETAILS_NAME);
		command.setDescription(schema.CLASS_DETAILS_DESCRIPTION);
		command.setMnemonic(schema.CLASS_DETAILSMONIC);
		command.setLoginNeeds(true);
		command.setIcon(schema.CLASS_DETAILS_ICON);
		command.setVisible(schema.CLASS_DETAILS_VISIBLE);
		command.setEnabled(schema.CLASS_DETAILS_ENABLE);
		cmdList.add(command);
		command=new SessionDetailsCommand(schema.SESSION_DETAILS_NAME);
		command.setDescription(schema.SESSION_DETAILS_DESCRIPTION);
		command.setMnemonic(schema.SESSION_DETAILSMONIC);
		command.setLoginNeeds(true);
		command.setIcon(schema.SESSION_DETAILS_ICON);
		command.setVisible(schema.SESSION_DETAILS_VISIBLE);
		command.setEnabled(schema.SESSION_DETAILS_ENABLE);
		cmdList.add(command);
		command=new FeeConfigureCommand(schema.FEE_CONFIG_NAME);
		command.setDescription(schema.FEE_CONFIG_DESCRIPTION);
		command.setMnemonic(schema.FEE_CONFIGMONIC);
		command.setLoginNeeds(true);
		command.setIcon(schema.FEE_CONFIG_ICON);
		command.setVisible(schema.FEE_CONFIG_VISIBLE);
		command.setEnabled(schema.FEE_CONFIG_ENABLE);
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
