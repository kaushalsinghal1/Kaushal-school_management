/* 
 * File :   CLogger.java 
 * Created: 2004/06/25
 * Project: cysol_framework
 * 
 */
package com.banti.framework.logging.config;

import com.banti.framework.logging.LoggerFactory;

/**
 * The responsibility of this class is to provide Loggers used by 
 * logging package.
 * @author cysol.
 *
 */
public class FLLogger {

    public static java.util.logging.Logger debug, event, exception;
    static {
        debug = LoggerFactory.getInstance().getLogger("LOGGING");
        event = LoggerFactory.getInstance().getEventLogger();
        exception = LoggerFactory.getInstance().getExceptionLogger();
    }
}