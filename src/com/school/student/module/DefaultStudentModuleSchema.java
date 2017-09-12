package com.school.student.module;

import java.awt.event.KeyEvent;

import com.school.resource.ResourcesUtils;

public class DefaultStudentModuleSchema extends StudentModuleSchema {
	
	public DefaultStudentModuleSchema() {
		STUDENT_MENU_NAME = ResourcesUtils.getString("STUDENT");
		STUDENT_DESCRIPTION = ResourcesUtils.getString("STUDENT");
		STUDENT_VISIBLE = true;
		STUDENT_ENABLE = true;
		STUDENT_MNEMONIC = KeyEvent.VK_S;

		STUDENT_DETAILS_NAME = ResourcesUtils.getString("STUDENT_DETAILS_LIST");
		STUDENT_DETAILS_DESCRIPTION = ResourcesUtils
				.getString("STUDENT_DETAILS");
		STUDENT_DETAILS_VISIBLE = true;
		STUDENT_DETAILS_ENABLE = true;
		STUDENT_DETAILSMONIC = KeyEvent.VK_S;
		 STUDENT_DETAILS_ICON = null;
		//
		STUDENT_REGISTRATION_NAME = ResourcesUtils
				.getString("STUDENT_REGISTRATION");
		STUDENT_REGISTRATION_DESCRIPTION = ResourcesUtils
				.getString("STUDENT_REGISTRATION");
		STUDENT_REGISTRATION_VISIBLE = true;
		STUDENT_REGISTRATION_ENABLE = true;
		STUDENT_REGISTRATIONMONIC = KeyEvent.VK_R;
		STUDENT_REGISTRATION_ICON=null;
	}
}
