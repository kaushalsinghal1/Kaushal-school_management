package com.school.resource;

import java.util.ListResourceBundle;

public class Resources extends ListResourceBundle {
	public Object[][] getContents() {
		return contents;
	}

	static final Object[][] contents = {
			{ "AQUA", "Aqua" },
			{ "ALL_CLASSES", "All Classs" },
			{ "ADMINISTRATOR", "Administrator" },
			{ "CLASS_DETAILS", "Class Details" },
			{ "SESSION_DETAILS", "Session Details" },
			{ "FEE_CLASS_CONFIG", "Session Class Fees Details" },
			{ "LOGGEDIN_ACCOUNT_CANNOT_BE_DELETED",
					"Logged in account cannot be deleted" },
			{ "STUDENT_DETAILS_LIST", "Student Details List" },
			{ "STUDENT", "Student" },
			{ "STUDENT_REGISTRATION", "Student Registration" },
			{ "REGISTER", "Register" },
			{ "UPDATE", "Update" },
			{ "DELETE", "Delete" },
			{ "SAVE", "Save" },
			{ "SKIP", "Skip" },
			{ "CLOSE", "Close" },
			{ "SUBMIT", "Submit" },
			{ "CANCEL", "Cancel" },
			{ "SERVICE_BUTTON", "Services >>" },
			{ "DEPOSITE_FEE", "Deposite Fee" },
			{ "OPEN_DEPOSITED_FEE_DETAILS", "Open Deposited Fee Details List" },
			{ "CLOSE_DEPOSITED_FEE_DETAILS", "Close Deposited Fee Details List" },
			{ "OLD_SCHOOL_INFO", "Old School Information" },
			{ "OLD_SCHOOL_NAME", "Old School Name" },
			{ "CHECK_TIMESPECIFY", "Select Search Period" },
			{ "PASSOUT_CLASS", "Class" }, { "PASSOUT_YEAR", "Year" },
			{ "ALL_SESSION", "All Session" }, { "FEE_DETAILS", "Fee Details" },
			{ "FEE_COLLECTION_DETAILS_LIST", "Fee Collection Details List" },
			{ "FEE_COLLECTION_DETAILS", "Fee Collection Details" },
			{"STUDENT_DUE_FEE__DETAILS_LIST","Due Fee Details"},
			{"STUDENT_DUE_FEE__DETAILS","Student Due Fee Details"},
			{ "PRINT", "Print" }, { "SAVE_AS_IMAGE", "Save as image" },
			{"SETTING","Setting"},
			{"SCHOOL_DETAILS","School Details Setting"},
			{"STUDENT_SEARCH","Student Search"},
			{"SEARCH","Search"},
			{"CSV_OUTPUT","CSV Output"},
			{"ID_CARD","IDentity Card"}

	};

}