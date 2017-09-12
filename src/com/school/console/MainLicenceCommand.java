package com.school.console;

import java.awt.event.ActionEvent;

import com.banti.framework.platform.Activator;
import com.banti.framework.platform.PluginCommand;

public class MainLicenceCommand extends PluginCommand {

	private Activator activator;

	public MainLicenceCommand() {
		super(null);
		setLoginNeeds(true);
		setVisible(false);
		setEnabled(false);
		activator = new MainActivator();
	}

	@Override
	public void entryPoint(ActionEvent ae) {
		return;
	}

	@Override
	public Activator getActivator() {
		return activator;
	}

	@Override
	public int getOrder() {
		return 10;
	}

}
