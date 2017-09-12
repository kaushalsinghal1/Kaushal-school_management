package com.school.student.module;

import com.banti.framework.platform.Activator;
import com.banti.framework.platform.Command;
import com.banti.framework.platform.Module;
import com.banti.framework.platform.PluginModule;
import com.banti.framework.platform.module.DefaultActivator;

public class AdmissionPluginModule extends PluginModule {
	private final static String NAME = "Admission";
	private Command[] commands;

	public AdmissionPluginModule() {
		super(NAME);
		commands = new Command[2];
		commands[0]=new NewAddmissionCommand();
		commands[1]=new ExistingAdmissionCommand();
		setVisible(true);

		setEnabled(true);
	}

	@Override
	public Command[] getCommands() {
		return commands;
	}

	@Override
	public Activator getActivator() {
		return new DefaultActivator();
	}

	@Override
	public int getOrder() {
		return 2;
	}

}
