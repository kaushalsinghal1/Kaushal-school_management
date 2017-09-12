package com.school.student.module;

import com.banti.framework.platform.Activator;
import com.banti.framework.platform.Command;
import com.banti.framework.platform.PluginModule;
import com.banti.framework.platform.module.DefaultActivator;
import com.school.fees.search.DepositeFeeReportCommand;
import com.school.fees.search.FeeDetailsSearchCommand;

public class FeeDetailsPludinModule extends PluginModule {
	private Activator activator;
	private FeeDeatilsModuleSchema moduleSchema;

	public FeeDetailsPludinModule(FeeDeatilsModuleSchema moduleSchema) {
		super(moduleSchema.FEE_DETAILS_MENU_NAME);
		activator = new DefaultActivator();
		this.moduleSchema = moduleSchema;
	}

	@Override
	public Activator getActivator() {
		return activator;
	}

	@Override
	public int getOrder() {
		return 5;
	}

	@Override
	public Command[] getCommands() {
		Command c[] = new Command[3];
		c[0] = new DepositeFeeReportCommand(moduleSchema.FEE_COLLECTION_DETAILS_NAME);
		c[1]=new FeeDetailsSearchCommand(moduleSchema);
		return c;
	}
}
