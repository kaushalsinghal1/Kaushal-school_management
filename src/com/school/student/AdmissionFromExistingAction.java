package com.school.student;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.AbstractAction;

import com.school.hiebernate.dbo.ClassDetails;
import com.school.hiebernate.dbo.SessionDetails;
import com.school.hiebernate.dbo.StudentDetails;

public class AdmissionFromExistingAction extends AbstractAction {
	private List<StudentDetails> studentDetailsList;

	public AdmissionFromExistingAction() {
		super("Existing Student Admission");
		studentDetailsList = new ArrayList<StudentDetails>();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		new ExistingStudentAdmission(studentDetailsList).setVisible(true);

	}

	public void setStudents(List<StudentDetails> list) {
		studentDetailsList.clear();
		if(list==null||list.size()==0){
			setEnabled(false);
			return;
		}
		ClassDetails classDetails = list.get(0).getClassDetails();
		SessionDetails sessionDetails = list.get(0).getSessionDetails();
		for (StudentDetails details : list) {
			if (classDetails.getClassId() != details.getClassDetails()
					.getClassId()
					|| sessionDetails.getSessionId() != details
							.getSessionDetails().getSessionId()) {
				setEnabled(false);
				return;
			}
		}
		studentDetailsList.addAll(list);
		setEnabled(true);
	}

	public void setStudents(StudentDetails[] studentDetails) {
		studentDetailsList.clear();
		studentDetailsList.addAll(Arrays.asList(studentDetails));
		setEnabled(true);
	}

	private StudentDetails createStudentDetails(StudentDetails studentDetails) {
		StudentDetails details = null;
		try {
			details = (StudentDetails) studentDetails.clone();
		} catch (CloneNotSupportedException e) {
		}
		return details;
	}

}
