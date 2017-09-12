package com.school.console.admin;

import java.awt.event.ActionEvent;

import com.banti.framework.platform.Command;
import com.school.fees.SessionFeeDetailsAction;

public class FeeConfigureCommand extends Command {

	public FeeConfigureCommand(String name) {
		super(name);
	}

	@Override
	public void entryPoint(ActionEvent ae) {
		new SessionFeeDetailsAction().setVisible(true);
	}

}
