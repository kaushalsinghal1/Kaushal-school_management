package com.banti.framework.logging;

import java.text.MessageFormat;
import java.util.Date;
import java.util.logging.Formatter;

/**
 * This class is abstract. some variable and one method are defined.
 */
public abstract class LogFormatter extends Formatter {

    protected MessageFormat formatter;
    protected boolean mode = false;
    protected LoggerHook uiHook;
    protected Date dat = new Date();
    protected Object args[] = new Object[1];

    /**
     * Line separator string. This is the value of the line.separator
     * property at the moment that the SimpleFormatter was created.
     */
    protected String lineSeparator = System.getProperty("line.separator");

    /**
     * This method will add a LoggerHook to the event loger The class which is
     * implementing this interface(LoggerHook) will get a copy of all the
     * messages sent to the event log.
     * 
     * @param hook
     */
    public void addLogHook(LoggerHook hook) {
        uiHook = hook;
    }
}
