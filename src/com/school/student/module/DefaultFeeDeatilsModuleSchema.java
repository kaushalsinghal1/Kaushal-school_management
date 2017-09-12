package com.school.student.module;

import java.awt.event.KeyEvent;

import com.banti.framework.theme.ToolKit;
import com.school.resource.ResourcesUtils;

public class DefaultFeeDeatilsModuleSchema extends FeeDeatilsModuleSchema {
	private static final String SERACH_ICON = "/com/school/resource/images/search.jpg";

	public DefaultFeeDeatilsModuleSchema() {
		FEE_DETAILS_MENU_NAME = ResourcesUtils.getString("FEE_DETAILS");
		FEE_DETAILS_DESCRIPTION = ResourcesUtils.getString("FEE_DETAILS");
		FEE_DETAILS_VISIBLE = true;
		FEE_DETAILS_ENABLE = true;
		FEE_DETAILS_MNEMONIC = KeyEvent.VK_S;

		FEE_COLLECTION_DETAILS_NAME = ResourcesUtils
				.getString("FEE_COLLECTION_DETAILS_LIST");
		FEE_COLLECTION_DETAILS_DESCRIPTION = ResourcesUtils
				.getString("FEE_COLLECTION_DETAILS");
		FEE_COLLECTION_DETAILS_VISIBLE = true;
		FEE_COLLECTION_DETAILS_ENABLE = true;
		FEE_COLLECTION_DETAILSMONIC = KeyEvent.VK_S;
		FEE_COLLECTION_DETAILS_ICON = null;
		//

		STUDENT_DUE_FEE__DETAILS_NAME = ResourcesUtils
				.getString("STUDENT_DUE_FEE__DETAILS_LIST");
		STUDENT_DUE_FEE__DETAILS_DESCRIPTION = ResourcesUtils
				.getString("STUDENT_DUE_FEE__DETAILS");
		STUDENT_DUE_FEE__DETAILS_VISIBLE = true;
		STUDENT_DUE_FEE__DETAILS_ENABLE = true;
		STUDENT_DUE_FEE__DETAILSMONIC = KeyEvent.VK_F;
		STUDENT_DUE_FEE__DETAILS_ICON = ToolKit.getInstance().createImageIcon(
				SERACH_ICON);
	}
}
