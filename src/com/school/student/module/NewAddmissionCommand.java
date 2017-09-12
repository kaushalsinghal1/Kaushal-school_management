package com.school.student.module;

import java.awt.event.ActionEvent;

import com.banti.framework.platform.Command;
import com.school.student.StudentRegistrationAction;

public class NewAddmissionCommand extends Command {
	private static final String NAME = "New Student Admission";

	public NewAddmissionCommand() {
		super(NAME);
		setVisible(true);
		setDescription("New Student Admission");
		setLoginNeeds(true);
	}

	@Override
	public void entryPoint(ActionEvent ae) {
		new StudentRegistrationAction("New Student Admission");

	}

}
