package com.banti.framework.logging;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * The format of the Debug messages writen to the logger is defined in this
 * class, which is used by the Logger
 */
public class DebugFormatter extends LogFormatter {

    private final static String format = "{0,date,short} {0,time}";

    // format of the date

    /**
     * Dafault constractor.
     */
    public DebugFormatter(boolean mode) {
        this.mode = mode;
    }

    /**
     * Format the given LogRecord.
     * 
     * @param record
     *          the log record to be formatted.
     * @return a formatted log record
     */
    public synchronized String format(LogRecord record) {
        if (!mode) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        // Minimize memory allocations here.
        dat.setTime(record.getMillis());
        args[0] = dat;
        if (formatter == null) {
            formatter = new MessageFormat(format);
        }
        formatter.format(args, sb, null);
        sb.append(" ");

        String cname = record.getSourceClassName();
        if (cname != null) {
            int ind = cname.lastIndexOf('.');
            if (ind >= 0 && ind < cname.length()) {
                cname = cname.substring(ind + 1);
            }
            sb.append(cname);
        }
        if (record.getLoggerName() != null) {
            sb.append(" ");
            sb.append(record.getLoggerName());
        }
        sb.append(" ");
        sb.append(record.getLevel().getName());
        sb.append("[" + Thread.currentThread().getId() + "]: ");
        sb.append(formatMessage(record));
        sb.append(lineSeparator);
        if (record.getThrown() != null) {
            try {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                record.getThrown().printStackTrace(pw);
                pw.close();
                sb.append(sw.toString());
            } catch (Exception ex) {
                LoggerFactory.getInstance().getExceptionLogger().log(
                    Level.WARNING,
                    "Exception Occurred",
                    ex);
            }
        }
        String tmp = sb.toString();
        if (uiHook != null) {
            uiHook.append(tmp);
        }
        return tmp;
    }

}