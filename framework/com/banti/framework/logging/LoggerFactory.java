package com.banti.framework.logging;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A class which has the Different FileHandlers and formatters for writing
 * logs. Loads the properties from a config file, if the configuration is not
 * found then assigns the default values. The default values are stored in
 * LoggerConstants. Using register(String) or getLogger(String) method a new
 * instance of JDK logger with the default debug/event/exception handlers and
 * format can be created.
 */
public class LoggerFactory implements LoggerConstants {

    private Properties properties;
    private Exception exp;

    // All the properties will be having there default values initially.
    private String debugLogFile = DEFAULT_DEBUGLOGFILE;
    private String eventLogFile = DEFAULT_EVENTLOGFILE;
    private String exceptionLogFile = DEFAULT_EXCEPTIONLOGFILE;
    private int count = DEFAULT_COUNT;
    private int limit = DEFAULT_LIMIT;
    private boolean appendFile;
    private boolean debugMod = true;

    //Default level.
    private Level debugLevel = Level.INFO;
    private Level exceptionLevel = Level.INFO;
    private Level eventLevel = Level.FINE;

    //Default debug/event/exception handlers are part of logger package.
    private Handler debugHandler;
    private Handler eventHandler;
    private Handler exceptionHandler;

    //By default three formatter are defined in logger package, but user can
    // define his own formatter and set to the this instance.
    private LogFormatter debugFormatter;
    private LogFormatter eventFormatter;
    private LogFormatter exceptionFormatter;

    private String HOME_DIR;

    /**
     * Event log is set initially this can be accessedd by Logger.EVENT.
     */
    private java.util.logging.Logger EVENT;
    /**
     * Exception log is set initially this can be accessed by Logger.EXCEPTION.
     */
    private java.util.logging.Logger EXCEPTION;

    /**
     * This class is a singleton class and LOGGER object is retained as one
     * single instance of the class.
     */
    private static LoggerFactory factory = null;
    /**
     * This map holds all the key->Object pare of Logger name and Logger object.
     */
    private Map<String, java.util.logging.Logger> debugLoggerMap = Collections.synchronizedMap(new HashMap<String, java.util.logging.Logger>());

    /**
     * This specifies whether startup log handler is enabled or not.
     */
    private boolean startupLoggerEnable = true;

    /**
     * This specifies when startup log handler is removed by startupTimer.
     */
    private long startupLoggerPeriod = 600000;

    /**
     * The startup handler for debug log
     */
    private StartupHandler startupDebug = null;

    /**
     * The startup handler for event log
     */
    private StartupHandler startupEvent = null;

    /**
     * The startup handler for exception log
     */
    private StartupHandler startupExcep = null;

    /**
     * A static method gets the instance of the Logger checks if the logger is
     * initialized. If LOGGER is null then creates the instance else return the
     * created LOGGER object.
     * 
     * @return
     */
    public synchronized static LoggerFactory getInstance() {
        if (factory == null) {
            factory = new LoggerFactory();
        }
        return factory;
    }

    /**
     * Constructor Creates a object of the properties and loads the default
     * values.
     */
    private LoggerFactory() {
        try {
            HOME_DIR = System.getProperty("cysol.home.dir");
            EVENT = Logger.getLogger(DEFAULT_EVENT_LOGGER);
            EXCEPTION = Logger.getLogger(DEFAULT_EXCEPTION_LOGGER);

            loadLogger();
            register(DEFAULT_DEBUG_LOGGER);

            if (startupLoggerEnable) {
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        // remove startup log handlers.
                        synchronized (debugLoggerMap) {
                            startupLoggerEnable = false;
                            Iterator<Logger> iter = debugLoggerMap.values().iterator();
                            while (iter.hasNext()) {
                                Logger logger = iter.next();
                                if (startupDebug != null) {
                                    logger.removeHandler(startupDebug);
                                }
                            }
                        }
                        if (startupEvent != null) {
                            EVENT.removeHandler(startupEvent);
                            startupEvent.close();
                            startupEvent = null;
                        }
                        if (startupExcep != null) {
                            EXCEPTION.removeHandler(startupExcep);
                            startupExcep.close();
                            startupExcep = null;
                        }
                        if (startupDebug != null) {
                            startupDebug.close();
                            startupDebug = null;
                        }

                    }
                }, startupLoggerPeriod);
            }

            EVENT.fine("Configuration of logger :");
            for (Iterator iter = properties.keySet().iterator(); iter.hasNext();) {
                String key = (String) iter.next();
                EVENT.fine(key + " = " + properties.getProperty(key));
            }

            EVENT.fine("Acutal values used for logger :");
            EVENT.fine("debugLogFile = " + debugLogFile);
            EVENT.fine("eventLogFile = " + eventLogFile);
            EVENT.fine("exceptionLogFile = " + exceptionLogFile);
            EVENT.fine("count = " + count);
            EVENT.fine("limit = " + limit);
            EVENT.fine("appendFile = " + appendFile);
            EVENT.fine("debugMod = " + debugMod);

        } catch (Exception e) {
            EVENT = Logger.getAnonymousLogger();
            EXCEPTION = Logger.getAnonymousLogger();

            debugMod = false;
            java.util.logging.Logger logger = Logger.getAnonymousLogger();
            setupLogger(logger);
            debugLoggerMap.put(DEFAULT_DEBUG_LOGGER, logger);
        }

    }

    /**
     * This method of the class loads the configuration file and setts the
     * properties of the logger, if loading fails the default values are set.
     * String CONF_FILE = "./config/logging.properties"; String COUNT_TAG =
     * "COUNT"; (tag) String APPEND_TAG = "APPEND";(tag) String LIMIT_TAG =
     * "LIMIT";(tag) String DEBUGMOD_TAG = "DEBUG";(tag) String DEBUGLOGFILE_TAG =
     * "DEBUG_LOG";(tag)
     * 
     * String DEFAULT_LOGFILE = "log.txt"; int DEFAULT_COUNT = 2; int
     * DEFAULT_LIMIT = 512000;
     */
    private void loadLogger() {
        properties = readLoggingConf(CONF_FILE);
        loadProperties(properties);
        eventFormatter = new EventFormatter();
        exceptionFormatter = new ExceptionFormatter();
        setHandler();
    }

    /**
     * Checks if the configuration file is preset and loads the same to a class
     * level properties object
     * 
     * @return Properties - returns null if it fails to read the file.
     */
    private Properties readLoggingConf(String confFile) {

        String otherFile = null;

        if (HOME_DIR != null) {
            otherFile = HOME_DIR + System.getProperty("file.separator") + confFile;
        }

        Properties prop = new Properties();
        try {
            if ((new File(confFile)).exists()) {
                FileInputStream fis = new FileInputStream(confFile);
                prop.load(fis);

            } else if (otherFile != null && new File(otherFile).exists()) {
                FileInputStream fis = new FileInputStream(otherFile);
                prop.load(fis);

            } else {
                URL url = LoggerFactory.class.getResource("/" + confFile);
                prop.load(url.openStream());
            }
        } catch (Exception exc) {
            exp = exc;
            // config file could not be read, so set the default values.
        }
        return prop;
    }

    /**
     * checks for all the tags in the properties object for its values, If the
     * object is null then the default values are assigner, else the values from
     * the properties object is set.
     * 
     * @param properties
     */
    private void loadProperties(Properties properties) {
        String tmp = checkAndCreateDir(properties.getProperty(EVENTLOGFILE_TAG));
        if (tmp != null) {
            eventLogFile = tmp;
        } else {
            checkAndCreateDir(eventLogFile);
        }

        tmp = checkAndCreateDir(properties.getProperty(EXCEPTIONLOGFILE_TAG));
        if (tmp != null) {
            exceptionLogFile = tmp;
        } else {
            checkAndCreateDir(exceptionLogFile);
        }

        tmp = checkAndCreateDir(properties.getProperty(DEBUGLOGFILE_TAG));
        if (tmp != null) {
            debugLogFile = tmp;
        } else {
            checkAndCreateDir(debugLogFile);
        }

        try {
            tmp = properties.getProperty(LOG_LIMIT_TAG);
            int tmpInt = Integer.parseInt(tmp);
            if (tmpInt > 0) {
                limit = tmpInt;
            }
        } catch (Exception exp) {
        }

        tmp = properties.getProperty(APPEND_TAG);
        if (tmp != null) {
            appendFile = Boolean.valueOf(tmp).booleanValue();
        }

        tmp = properties.getProperty(DEBUGMOD_TAG);
        if (tmp != null) {
            debugMod = Boolean.valueOf(tmp).booleanValue();
        }
        try {
            tmp = properties.getProperty(LOG_COUNT_TAG);
            int tmpInt = Integer.parseInt(tmp);
            if (tmpInt > 0) {
                count = tmpInt;
            }
        } catch (Exception exp) {
        }

        Level defLv = Level.INFO;
        tmp = properties.getProperty(LOG_LEVEL_TAG);
        if (tmp != null && tmp.trim().length() > 0) {
            try {
                defLv = Level.parse(tmp.trim());
                debugLevel = defLv;
                exceptionLevel = defLv;
                eventLevel = defLv;
            } catch (IllegalArgumentException e) {
                exp = e;
            }
        }

        tmp = properties.getProperty(DEBUG_LEVEL_TAG);
        if (tmp != null && tmp.trim().length() > 0) {
            try {
                debugLevel = Level.parse(tmp.trim());
            } catch (IllegalArgumentException e) {
                exp = e;
                // Uses default level.
            }
        } else {
            properties.setProperty(DEBUG_LEVEL_TAG, debugLevel.getName());
        }

        tmp = properties.getProperty(EXCEPTION_LEVEL_TAG);
        if (tmp != null && tmp.trim().length() > 0) {
            try {
                exceptionLevel = Level.parse(tmp.trim());
            } catch (IllegalArgumentException e) {
                exp = e;
                // Uses default level.
            }
        } else {
            properties.setProperty(EXCEPTION_LEVEL_TAG, exceptionLevel.getName());
        }

        tmp = properties.getProperty(EVENT_LEVEL_TAG);
        if (tmp != null && tmp.trim().length() > 0) {
            try {
                eventLevel = Level.parse(tmp.trim());
            } catch (IllegalArgumentException e) {
                exp = e;
                // Uses default level.
            }
        } else {
            properties.setProperty(EVENT_LEVEL_TAG, eventLevel.getName());
        }

        // startup log handler stuff.
        tmp = properties.getProperty(STARTUP_LOGGER_ENABLE);
        if ("false".equalsIgnoreCase(tmp)) {
            startupLoggerEnable = false;
        }
        tmp = properties.getProperty(STARTUP_LOGGER_PERIOD);
        if (tmp != null) {
            try {
                long tmpLong = Long.parseLong(tmp);
                if (tmpLong > 0) {
                    startupLoggerPeriod = tmpLong * 1000;
                }
            } catch (Exception exp) {
            }
        }
    }

    /**
     * This method creats the parent directorys and the parent dir if the
     * specified path or parent dir are not present.
     * 
     * @param filePath
     * @return
     */
    private String checkAndCreateDir(String filePath) {
        if (filePath == null || filePath.trim().length() < 1) {
            return null;
        }
        filePath = filePath.trim();

        if (HOME_DIR != null && !filePath.startsWith(HOME_DIR)) {
            filePath = HOME_DIR + System.getProperty("file.separator") + filePath;
        }

        File file = new File(filePath);
        if (file.getParentFile() == null) {
            return filePath;
        }
        try {
            if (!(file.getParentFile()).exists()) {
                boolean canCreate = (file.getParentFile()).mkdirs();
                if (!canCreate) {
                    EXCEPTION.info("Can not create a directory :" + file.getParentFile());
                    return null;
                }
            }
        } catch (Exception e) {
            exp = e;
            return null;
        }
        return filePath;
    }

    /**
     * Default formatter for debug log can be over writen by the setting the user
     * defained LogFormatter using this method.
     * 
     * @param formatter
     */
    public void setDebugFormatter(LogFormatter formatter) {
        debugFormatter = formatter;
        if (debugHandler != null) {
            debugHandler.setFormatter(debugFormatter);
        }
    }

    /**
     * Default formatter for event log can be over writen by the setting the user
     * defained LogFormatter using this method.
     * 
     * @param formatter
     */
    public void setEventFormatter(LogFormatter formatter) {
        eventFormatter = formatter;
        if (eventHandler != null) {
            eventHandler.setFormatter(eventFormatter);
        }
    }

    /**
     * Default formatter for exception log can be over writen by the setting the
     * user defained LogFormatter using this method.
     * 
     * @param formatter
     */
    public void setExceptionFormatter(LogFormatter formatter) {
        exceptionFormatter = formatter;
        if (exceptionHandler != null) {
            exceptionHandler.setFormatter(exceptionFormatter);
        }
    }

    /**
     * LoggerHook can be added to Event log, all the messages set to event log
     * will b notified to the hook's append method
     * 
     * @param hook
     */
    public void addEventLogHook(LoggerHook hook) {
        if (eventFormatter != null) {
            eventFormatter.addLogHook(hook);
        }
    }

    /**
     * LoggerHook can be added to Exception log, all the messages set to event
     * log will b notified to the hook's append method
     * 
     * @param hook
     */
    public void addExceptionLogHook(LoggerHook hook) {
        if (exceptionFormatter != null) {
            exceptionFormatter.addLogHook(hook);
        }
    }

    /**
     * LoggerHook can be added to Debug log, all the messages set to event log
     * will b notified to the hook's append method
     * 
     * @param hook
     */
    public void addDebugLogHook(LoggerHook hook) {
        if (debugFormatter != null) {
            debugFormatter.addLogHook(hook);
        }
    }

    /**
     * Sets all the file handlers(Debug handler,Event Handler and Exception
     * handler with the given properties. Debug formatter is set to all the
     * objects in the debugLoggerMap.
     */
    private void setHandler() {
        try {
            if (debugMod) {
                debugFormatter = new DebugFormatter(debugMod);
                debugHandler = new DailyFileHandler(debugLogFile, limit, count);
                debugHandler.setFormatter(debugFormatter);
                debugHandler.setLevel(debugLevel);

                if (startupLoggerEnable) {
                    startupDebug = new StartupHandler(debugLogFile);
                    startupDebug.setFormatter(debugFormatter);
                    startupDebug.setLevel(debugLevel);
                }
            }
            synchronized (debugLoggerMap) {
                for (Iterator<Logger> iter = debugLoggerMap.values().iterator(); iter.hasNext();) {
                    java.util.logging.Logger logger = iter.next();
                    setupLogger(logger);
                }
            }
        } catch (Exception exc) {
            exp = exc;
        }

        try {
            if (eventLevel.equals(Level.OFF)) {
                EVENT.setUseParentHandlers(true);
                EVENT.setLevel(eventLevel);
            } else {
                eventHandler = new DailyFileHandler(eventLogFile, limit, count);
                eventHandler.setFormatter(eventFormatter);
                EVENT.setUseParentHandlers(false);
                eventHandler.setLevel(eventLevel);
                EVENT.addHandler(eventHandler);
                EVENT.setLevel(eventLevel);

                if (startupLoggerEnable) {
                    startupEvent = new StartupHandler(eventLogFile);
                    startupEvent.setFormatter(eventFormatter);
                    startupEvent.setLevel(eventLevel);
                    EVENT.addHandler(startupEvent);
                }

            }
        } catch (Exception exc) {
            exp = exc;
        }

        try {
            if (exceptionLevel.equals(Level.OFF)) {
                EXCEPTION.setUseParentHandlers(true);
                EXCEPTION.setLevel(exceptionLevel);
            } else {
                exceptionHandler = new DailyFileHandler(exceptionLogFile, limit, count);
                exceptionHandler.setFormatter(exceptionFormatter);
                EXCEPTION.setUseParentHandlers(false);
                exceptionHandler.setLevel(exceptionLevel);
                EXCEPTION.addHandler(exceptionHandler);
                EXCEPTION.setLevel(exceptionLevel);

                if (startupLoggerEnable) {
                    startupExcep = new StartupHandler(exceptionLogFile);
                    startupExcep.setFormatter(exceptionFormatter);
                    startupExcep.setLevel(exceptionLevel);
                    EXCEPTION.addHandler(startupExcep);
                }
            }
        } catch (Exception exc) {
            exp = exc;
        }
    }

    /**
     * Removed the handlers from the logger objects which are in the
     * debugLoggerMap. And also removed the handlers of the event handler and
     * exception handler
     */
    public void removeHandlers() {
        if (debugHandler != null) {
            synchronized (debugLoggerMap) {
                for (Iterator<Logger> iter = debugLoggerMap.values().iterator(); iter.hasNext();) {
                    java.util.logging.Logger logger = iter.next();
                    logger.removeHandler(debugHandler);
                    logger.removeHandler(startupDebug);
                }
            }
            debugHandler.close();
        }
        if (eventHandler != null) {
            EVENT.removeHandler(eventHandler);
            EVENT.removeHandler(startupEvent);
            eventHandler.close();
        }
        if (exceptionHandler != null) {
            EXCEPTION.removeHandler(exceptionHandler);
            EXCEPTION.removeHandler(startupExcep);
            exceptionHandler.close();
        }
    }

    /**
     * Checks if the logName is the same as EXCEPTION or EVENT log name if so
     * then retrun EXCEPTOIN or EVENT log object. else cecks in the
     * debugLoggerMap for the key if matched then retruns the same object else
     * registers the logger and returns the object. If the logName is null or
     * blanck then returns the default debug logger
     * 
     * @param logName
     * @return
     */
    public java.util.logging.Logger getLogger(String logName) {
        if (logName == null || logName.trim().length() < 1) {
            return debugLoggerMap.get(DEFAULT_DEBUG_LOGGER);
        } else if (logName.equals(DEFAULT_EXCEPTION_LOGGER)) {
            return EXCEPTION;
        } else if (logName.equals(DEFAULT_EVENT_LOGGER)) {
            return EVENT;
        }
        return register(logName);
    }

    /**
     * Returns Exception logger.
     * @return
     */
    public java.util.logging.Logger getExceptionLogger() {
        return EXCEPTION;
    }

    /**
     * Returns Event Logger.
     * @return
     */
    public java.util.logging.Logger getEventLogger() {
        return EVENT;
    }

    /**
     * Reisters a new logger with the logName.
     * Cecks in the debugLoggerMap for the key if matched then retruns the same object
     * else creates a new Logger object and puts to he map with the key as
     * logName and returns the logger object.
     * 
     * @param logName
     * @return
     */
    private java.util.logging.Logger register(String logName) {
        if (!debugLoggerMap.containsKey(logName)) {
            java.util.logging.Logger logger = java.util.logging.Logger.getLogger(logName);
            setupLogger(logger);
            debugLoggerMap.put(logName, logger);
            return logger;
        }
        return debugLoggerMap.get(logName);
    }

    /**
     * Removes the logger from the debug map for removing the logger the key or
     * the logName should be specified. If the key or the logName does not match
     * then null is retruned, else return the removed object.
     * 
     * @param logName
     * @return
     */
    public void unRegister(String logName) {
        if (debugLoggerMap.containsKey(logName)) {
            java.util.logging.Logger logger = debugLoggerMap.remove(logName);
            try {
                logger.removeHandler(debugHandler);
                logger.removeHandler(startupDebug);
            } catch (SecurityException e) {
                exp = e;
                logger = null;
            }
        }
    }

    /**
     * Stes the level and handler for the debug modules.
     * 
     * @param logger
     */
    private void setupLogger(java.util.logging.Logger logger) {
        try {
            if (debugMod && debugHandler != null) {
                logger.addHandler(debugHandler);
                logger.setUseParentHandlers(false);
                logger.setLevel(debugLevel);

                if (startupLoggerEnable) {
                    logger.addHandler(startupDebug);
                }
            } else {
                logger.setUseParentHandlers(true);
                logger.setLevel(Level.OFF);
            }
        } catch (Exception e) {
            exp = e;
        }
    }

    /**
     * Resets all handlers by the specified properites.
     * @param prop
     */
    public void resetProperies(Properties prop) {
        removeHandlers();
        properties = prop;
        loadProperties(prop);
        setHandler();
    }

    /**
     * Resets all handlers by the specified properites.
     * @param prop
     */
    public void setProperies(Properties prop) {

        String key;
        String value;
        boolean isDifferent = false;
        if (properties.size() == prop.size()) {
            for (Iterator ite = properties.keySet().iterator(); ite.hasNext();) {
                key = (String) ite.next();
                value = properties.getProperty(key);
                if (!value.equals(prop.get(key))) {
                    isDifferent = true;
                }
            }
        }
        if (!isDifferent) {
            return; // No change
        }
        resetProperies(prop);
    }

    /**
     * Creates the extra debug Logger by the specified name and file name.
     * If the specified name allready existes or the specified value are invalid, 
     * Calls getLogger method.
     * Logging is carried out though debug mode is false. 
     * So you must specify the log level after create this logger.
     * @param name fo log
     * @param fileName
     * @return
     */
    public Logger createExtraLogger(String name, String fileName) {
        String logFile = checkAndCreateDir(fileName);

        if (fileName == null
            || name == null
            || name.trim().length() < 1
            || debugLoggerMap.containsKey(name)) {
            return getLogger(name);
        }
        try {
            Handler extraHandler = new DailyFileHandler(logFile, limit, count);
            LogFormatter eFormatter = new DebugFormatter(true);
            extraHandler.setFormatter(eFormatter);
            extraHandler.setLevel(Level.ALL);
            java.util.logging.Logger logger = java.util.logging.Logger.getLogger(name);
            logger.addHandler(extraHandler);
            logger.setUseParentHandlers(false);
            logger.setLevel(Level.ALL);
            return logger;
        } catch (Exception e) {
            exp = e;
        }
        return getLogger(name);
    }

    /**
     * Creates the extra Logger for event by the specified name and file name.
     * If the specified name allready existes or the specified value are invalid, 
     * Calls getLogger method.
     * @param name
     * @param fileName
     * @return
     */
    public Logger createExtraEventLogger(String name, String fileName) {
        String logFile = checkAndCreateDir(fileName);
        if (fileName == null
            || name == null
            || name.trim().length() < 1
            || debugLoggerMap.containsKey(name)) {
            return getLogger(name);
        } else if (name.equals(DEFAULT_EXCEPTION_LOGGER)) {
            return EXCEPTION;
        } else if (name.equals(DEFAULT_EVENT_LOGGER)) {
            return EVENT;
        }
        try {
            Handler extraHandler = new DailyFileHandler(logFile, limit, count);
            extraHandler.setFormatter(eventFormatter);
            extraHandler.setLevel(Level.ALL);
            java.util.logging.Logger logger = java.util.logging.Logger.getLogger(name);
            logger.addHandler(extraHandler);
            logger.setUseParentHandlers(false);
            logger.setLevel(Level.ALL);
            return logger;
        } catch (Exception e) {
            exp = e;
        }
        return EVENT;
    }

    /**
     * Creates the extra Logger for exception by the specified name and file name.
     * If the specified name allready existes or the specified value are invalid, 
     * Calls getLogger method.
     * @param name
     * @param fileName
     * @return
     */
    public Logger createExtraExceptionLogger(String name, String fileName) {
        String logFile = checkAndCreateDir(fileName);
        if (fileName == null
            || name == null
            || name.trim().length() < 1
            || debugLoggerMap.containsKey(name)) {
            return getLogger(name);
        } else if (name.equals(DEFAULT_EXCEPTION_LOGGER)) {
            return EXCEPTION;
        } else if (name.equals(DEFAULT_EVENT_LOGGER)) {
            return EVENT;
        }
        try {
            Handler extraHandler = new DailyFileHandler(logFile, limit, count);
            extraHandler.setFormatter(exceptionFormatter);
            extraHandler.setLevel(Level.ALL);
            java.util.logging.Logger logger = java.util.logging.Logger.getLogger(name);
            logger.addHandler(extraHandler);
            logger.setUseParentHandlers(false);
            logger.setLevel(Level.ALL);
            return logger;
        } catch (Exception e) {
            exp = e;
        }
        return EXCEPTION;
    }

    /**
     * Returns the properties as the current set value. 
     * @param pro
     */
    public Properties getProperties() {

        if (HOME_DIR != null) {
            String tmp = eventLogFile;
            if (eventLogFile.startsWith(HOME_DIR + System.getProperty("file.separator"))) {
                tmp = eventLogFile.substring(HOME_DIR.length() + 1);
            }
            properties.put(EVENTLOGFILE_TAG, tmp);

            tmp = exceptionLogFile;
            if (exceptionLogFile.startsWith(HOME_DIR + System.getProperty("file.separator"))) {
                tmp = exceptionLogFile.substring(HOME_DIR.length() + 1);
            }
            properties.put(EXCEPTIONLOGFILE_TAG, tmp);

            tmp = debugLogFile;
            if (debugLogFile.startsWith(HOME_DIR + System.getProperty("file.separator"))) {
                tmp = debugLogFile.substring(HOME_DIR.length() + 1);
            }
            properties.put(DEBUGLOGFILE_TAG, tmp);

        } else {
            properties.put(EVENTLOGFILE_TAG, eventLogFile);
            properties.put(EXCEPTIONLOGFILE_TAG, exceptionLogFile);
            properties.put(DEBUGLOGFILE_TAG, debugLogFile);
        }

        properties.put(LIMIT_TAG, "" + limit);
        properties.put(APPEND_TAG, "" + appendFile);
        properties.put(DEBUGMOD_TAG, "" + debugMod);
        properties.put(COUNT_TAG, "" + count);
        properties.put(DEBUG_LEVEL_TAG, debugLevel.toString());
        properties.put(EVENT_LEVEL_TAG, eventLevel.toString());
        properties.put(EXCEPTION_LEVEL_TAG, exceptionLevel.toString());
        return properties;
    }

    /**
     * Gets the status of the Debug mode on or off return true if debug mode is
     * on else return false.
     * 
     * @return
     */
    public boolean isDebugOn() {
        return debugMod;
    }

    /**
     * Gets the status of the Append mode on or off return true if Append mode is
     * on else return false.
     * 
     * @return
     */
    public boolean isAppendOn() {
        return appendFile;
    }

    /**
     * Returns the latest exception.
     * @return
     */
    public Exception getError() {
        return exp;
    }

    public String getConfigFilePath() {
        String path = LoggerConstants.CONF_FILE;
        if (HOME_DIR != null) {
            path = HOME_DIR + System.getProperty("file.separator") + path;
        }
        return path;
    }

    public static void main(String[] args) {
        String home = System.getProperty("user.home");
        System.out.println(home);

        String fileName = home + System.getProperty("file.separator") + "sample";
        System.out.println(fileName);
        // System.setProperty("cysol.home.dir", fileName);

        LoggerFactory factory = LoggerFactory.getInstance();
        factory.getLogger("Sample").info("Debug");
        factory.getExceptionLogger().info("Exception");
        factory.getEventLogger().info("Event");

        System.out.println(factory.getConfigFilePath());

        factory.checkAndCreateDir(fileName + System.getProperty("file.separator") + 4444);
    }
}