package com.school.fees.search;

import java.awt.event.ActionEvent;

import com.banti.framework.platform.Command;
import com.school.student.module.FeeDeatilsModuleSchema;

public class FeeDetailsSearchCommand extends Command {

	public FeeDetailsSearchCommand(FeeDeatilsModuleSchema schema) {
		super(schema.STUDENT_DUE_FEE__DETAILS_NAME);
	//	setIcon(schema.STUDENT_DUE_FEE__DETAILS_ICON);
		setDescription(schema.STUDENT_DUE_FEE__DETAILS_DESCRIPTION);
		setSeparator(true);
		setLoginNeeds(true);
	}

	@Override
	public void entryPoint(ActionEvent ae) {
		new FeeDetailsSearchDialog();

	}
}
