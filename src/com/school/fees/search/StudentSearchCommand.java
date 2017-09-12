package com.school.fees.search;

import java.awt.event.ActionEvent;

import com.banti.framework.platform.Activator;
import com.banti.framework.platform.PluginCommand;
import com.banti.framework.platform.module.DefaultActivator;
import com.school.student.StudentDetailsCommand;

public class StudentSearchCommand extends PluginCommand {
	private Activator activator;

	public StudentSearchCommand(DefaultSearchCommandSchema commandSchema) {
		super(commandSchema.getDISPLAY_NAME());
		super.setButtonEnabled(true);
		super.setIcon(commandSchema.getIcon());
		super.setVisible(true);
		super.setCommandKey(commandSchema.getCOMMANDSTRING());
		setLoginNeeds(true);
		setDescription(commandSchema.getDISPLAY_NAME());
		activator = new DefaultActivator();
	}

	@Override
	public void entryPoint(ActionEvent ae) {
		StudentDetailsCommand.showStudentSearchDetails();
		//StudentDetailsList detailsList = new StudentDetailsList(true);
//		detailsList.setFramesize(SchoolMain.Frame);
//		detailsList.setVisible(true);

	}

	@Override
	public Activator getActivator() {
		return activator;
	}

	@Override
	public int getOrder() {
		// TODO Auto-generated method stub
		return 100;
	}

}
