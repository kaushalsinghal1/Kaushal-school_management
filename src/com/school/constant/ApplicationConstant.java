package com.school.constant;

import java.text.SimpleDateFormat;

public interface ApplicationConstant {
	public static String ADMIN = "admin";
	public static String USER = "user";
	public static String SUPER_USER = "super_user";
	public static final SimpleDateFormat sdf_fulldate = new SimpleDateFormat(
			"dd-MM-yyyy hh:mm:ss");
	public static final SimpleDateFormat sdf_date = new SimpleDateFormat(
			"dd-MM-yyyy");

	public static String OPERATION_CONFIG_FILE = "./config/Operation.csv";
	public static String STATES_CONFIG_FILE = "./config/state.csv";
	public final String TEMP_IMAGE_FILE = "data/temp.png";
	public final String LICENSE_CONF_FILE = "config/license.conf";
	public final String TRIAL_PERIOD_PROPERTY = "TRIAL_PERIOD";
	public final String SCHOOL_CONF_FILE = "config/application.conf";

	public final String SCHOOL_NAME = "school.name";
	public final String SCHOOL_SUB_TITLE = "school.subtitle";
	public final String SCHOOL_LOG_PATH = "school.logopath";
	public final String SCHOOL_ADDRESS_LINE1 = "school.address.lin1";
	public final String SCHOOL_ADDRESS_LINE2 = "school.address.line2";
	public final String SCHOOL_IDCARD_TITLE = "school.idcard.title";

	public final String DEFAULT_SCHOOL_NAME = "School name";
	public final String DEFAULT_SCHOOL_SUB_TITLE = "Hindi and English Medium ";
	public final String DEFAULT_SCHOOL_LOG_PATH = "config/report/logo.png";
	public final String DEFAULT_SCHOOL_ADDRESS_LINE1 = "Strreet";
	public final String DEFAULT_SCHOOL_ADDRESS_LINE2 = " City and phone";
	public final String DEFAULT_SCHOOL_IDCARD_TITLE = "Student Identity Card";

	// private static long lastReadConf;

	public static final String OCCUPATION[] = { "Business", "Employee",
			"Self Employee" };
}
