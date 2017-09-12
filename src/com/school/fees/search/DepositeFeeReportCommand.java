package com.school.fees.search;

import java.awt.event.ActionEvent;

import com.banti.framework.platform.Command;

public class DepositeFeeReportCommand extends Command {

	public DepositeFeeReportCommand(String name) {
		super(name);
		setLoginNeeds(true);
	}

	@Override
	public void entryPoint(ActionEvent ae) {
		new DepositeFeeSearch();
		
	}

}
