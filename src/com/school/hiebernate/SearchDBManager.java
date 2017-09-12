package com.school.hiebernate;

import java.util.List;

import com.school.hiebernate.dbo.ClassDetails;
import com.school.hiebernate.dbo.SessionDetails;
import com.school.hiebernate.dbo.StudentDetails;
import com.school.hiebernate.dbo.StudentFeeDetails;

public abstract class SearchDBManager {

	public abstract List<StudentDetails> getStudentDetails(String firstName,
			String lastName, String fatherName, ClassDetails classDetails,
			SessionDetails sessionDetails) throws RuntimeException;

	public abstract List<StudentFeeDetails> getStudentFeeDetails(
			ClassDetails classDetails, SessionDetails sessionDetails)
			throws RuntimeException;
}
