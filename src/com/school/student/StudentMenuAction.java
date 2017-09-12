package com.school.student;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.school.resource.CommandConstant;

public class StudentMenuAction implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		if (CommandConstant.STRUDENT_REGISTRATION.equals(e.getActionCommand())) {
			new StudentRegistrationAction();
		} else if (CommandConstant.STRUDENT_FEE_DETAILS.equals(e
				.getActionCommand())) {
			//StudentDetailsList detailsList = new StudentDetailsList();
		//	SchoolMainFrame.addStudentDetailsFrame();
//			detailsList.setFramesize();
//			detailsList.setVisible(true);

		}
	}

}
