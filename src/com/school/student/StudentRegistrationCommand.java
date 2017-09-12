package com.school.student;

import java.awt.event.ActionEvent;

import com.banti.framework.platform.Command;
import com.school.student.module.StudentModuleSchema;

public class StudentRegistrationCommand extends Command {

	public StudentRegistrationCommand(StudentModuleSchema moduleSchema) {
		super(moduleSchema.STUDENT_REGISTRATION_NAME);
		setLoginNeeds(true);
	}

	@Override
	public void entryPoint(ActionEvent ae) {
		new StudentRegistrationAction();
		//new ReciptWindow();
	}

}
