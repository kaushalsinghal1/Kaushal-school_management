package com.banti.framework.logging;

/**
 * Constants used by the Logger are defined here As of now this interface is
 * implemented by Logger.java
 * In case if the logger can not find any value set or loaded from the conf
 * then it takes the default values from this interface.
 */
public interface LoggerConstants {

    String CONF_FILE = "config/logging.properties";
    //default configuration file
    String COUNT_TAG = "COUNT";
    String APPEND_TAG = "APPEND";
    String LIMIT_TAG = "LIMIT";
    String DEBUGMOD_TAG = "DEBUG";

    String DEBUG_LEVEL_TAG = "DEBUG_LEVEL";
    String EXCEPTION_LEVEL_TAG = "EXCEPTION_LEVEL";
    String EVENT_LEVEL_TAG = "EVENT_LEVEL";

    String LOG_COUNT_TAG = "LOG_COUNT";
    String LOG_LIMIT_TAG = "LOG_LIMIT";
    String LOG_LEVEL_TAG = "LOG_LEVEL";
    int DEFAULT_COUNT = 100;
    int DEFAULT_LIMIT = 0;

    String DEBUGLOGFILE_TAG = "DEBUG_LOG";
    String EVENTLOGFILE_TAG = "EVENT_LOG";
    String EXCEPTIONLOGFILE_TAG = "EXCEPTION_LOG";

    String DEFAULT_DEBUGLOGFILE = "log/debug.log";
    String DEFAULT_EVENTLOGFILE = "log/event.log";
    String DEFAULT_EXCEPTIONLOGFILE = "log/exception.log";

    String DEFAULT_DEBUG_LOGGER = "DEBUG";
    String DEFAULT_EVENT_LOGGER = "EVENT";
    String DEFAULT_EXCEPTION_LOGGER = "EXCEPTION";

    String STARTUP_LOGGER_ENABLE = "STARTUP_LOGGER_ENABLE";
    String STARTUP_LOGGER_PERIOD = "STARTUP_LOGGER_PERIOD";
}
