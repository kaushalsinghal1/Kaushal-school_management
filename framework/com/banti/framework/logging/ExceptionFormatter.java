package com.banti.framework.logging;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * This class will prepare the message format of the exception log, this will
 * have the exception's print stack
 */
public class ExceptionFormatter extends LogFormatter {

    private final static String format = "{0,date,short} {0,time}";

    /**
     * Format the given LogRecord.
     * @param record as the log record to be formatted.
     * @return a formatted log record
     */
    public synchronized String format(LogRecord record) {
        StringBuffer sb = new StringBuffer();
        // Minimize memory allocations here.
        dat.setTime(record.getMillis());
        args[0] = dat;
        if (formatter == null) {
            formatter = new MessageFormat(format);
        }
        formatter.format(args, sb, null);
        sb.append(" ");
        if (record.getLoggerName() != null) {
            sb.append(" ");
            sb.append(record.getLoggerName());
        }
        sb.append(" ");
        String message = formatMessage(record);
        sb.append(record.getLevel().getName());
        sb.append("[" + Thread.currentThread().getId() + "]: ");
        sb.append(message);
        sb.append(lineSeparator);
        if (record.getThrown() != null) {
            try {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                record.getThrown().printStackTrace(pw);
                pw.close();
                sb.append(sw.toString());
            } catch (Exception ex) {
                //ex.printStackTrace();
                LoggerFactory.getInstance().getExceptionLogger().log(
                    Level.WARNING,
                    "Exception Occurred",
                    ex);
            }
        }
        // the abowe String buffer is prepared in the required format and returned
        // as String.
        String tmp = sb.toString();
        if (uiHook != null) {
            uiHook.append(tmp);
        }
        return tmp;
    }

}
