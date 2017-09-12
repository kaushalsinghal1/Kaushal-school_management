package com.school.student.module;

import java.awt.event.ActionEvent;

import com.banti.framework.platform.Command;
import com.school.student.StudentDetailsCommand;

public class ExistingAdmissionCommand extends Command{
	private static final String NAME = "Existing Student Admission";

	public ExistingAdmissionCommand() {
		super(NAME);
		setDescription(NAME);
		setLoginNeeds(true);
	}

	@Override
	public void entryPoint(ActionEvent ae) {
		StudentDetailsCommand.showStudentSearchDetails();		
	}

}
