package com.school.student.module;

import com.banti.framework.platform.Activator;
import com.banti.framework.platform.Command;
import com.banti.framework.platform.PluginModule;
import com.banti.framework.platform.module.DefaultActivator;
import com.school.student.StudentDetailsCommand;
import com.school.student.StudentRegistrationCommand;

public class StudenPluginCommand extends PluginModule {
	private Activator activator;
	private StudentModuleSchema moduleSchema;

	public StudenPluginCommand(StudentModuleSchema moduleSchema) {
		super(moduleSchema.STUDENT_MENU_NAME);
		activator = new DefaultActivator();
		this.moduleSchema = moduleSchema;
	}

	@Override
	public Activator getActivator() {
		return activator;
	}

	@Override
	public int getOrder() {
		return 1;
	}

	@Override
	public Command[] getCommands() {
		Command c[] = new Command[3];
		c[0] = new StudentDetailsCommand(moduleSchema);
		//c[1]=new AdmissionModule();
		c[1] = new StudentRegistrationCommand(moduleSchema);
		return c;
	}
}
