package com.school.utils;

import com.banti.framework.logging.LoggerFactory;

public class Logger {
	public static java.util.logging.Logger DEBUG = LoggerFactory.getInstance()
			.getLogger("DEBUG");
	public static java.util.logging.Logger EXCEPTION = LoggerFactory
			.getInstance().getExceptionLogger();
	public static java.util.logging.Logger EVENT = LoggerFactory.getInstance()
			.getEventLogger();
}
