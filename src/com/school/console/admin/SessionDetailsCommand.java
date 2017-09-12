package com.school.console.admin;

import java.awt.event.ActionEvent;

import com.banti.framework.platform.Command;

public class SessionDetailsCommand extends Command {

	public SessionDetailsCommand(String name) {
		super(name);
	}

	@Override
	public void entryPoint(ActionEvent ae) {
		new SessionDetailsAction().setVisible(true);
	}

}
