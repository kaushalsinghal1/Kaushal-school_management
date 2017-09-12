
package com.banti.framework.logging;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import com.banti.framework.utils.CSVParser;


public abstract class AbstractLogWriter {

    private static final DateFormat logDateformat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    private DateFormat fileDateformat = new SimpleDateFormat("yyMMdd");

    protected final Logger DEBUG = LoggerFactory.getInstance().getLogger(null);
    protected final Logger EVENT = LoggerFactory.getInstance().getEventLogger();
    protected final Logger EXCEPTION = LoggerFactory.getInstance().getExceptionLogger();

    private Calendar s_cal;
    private Calendar c_cal;
    private SimpleLogFormatter logFormatter;
    public static final int PARAM1_INDEX = 3;
    public static final int PARAM2_INDEX = 4;
    public static final int POLICY_NAME_INDEX = 8;
    public static final int RULE_NAME_INDEX = 9;
    
    protected java.util.logging.Logger msgLog;

    /**
     * "postLoggingListeners" contains the PostLoggingListener.
     * These listener's "postLoggingProcess" is called after "log(String str)" method.
     */
    private Map<String, PostLoggingListener> postLoggingListeners = Collections.synchronizedMap(new HashMap<String, PostLoggingListener>());

    public AbstractLogWriter() {
        s_cal = Calendar.getInstance();
        c_cal = Calendar.getInstance();
    }

    public AbstractLogWriter(DateFormat fileNameFormat) { 
        this();
        fileDateformat = fileNameFormat;
    }
    
    private synchronized void validateCurrentFile() {
        if (logFormatter == null) {
            logFormatter = new SimpleLogFormatter();
            c_cal.setTimeInMillis(System.currentTimeMillis());
            createNewHandler();
            return;
        }
        c_cal.setTimeInMillis(System.currentTimeMillis());
        if (c_cal.get(Calendar.YEAR) != s_cal.get(Calendar.YEAR)
            || c_cal.get(Calendar.MONTH) != s_cal.get(Calendar.MONTH)
            || c_cal.get(Calendar.DAY_OF_MONTH) != s_cal.get(Calendar.DAY_OF_MONTH)
            || c_cal.get(Calendar.HOUR_OF_DAY) != s_cal.get(Calendar.HOUR_OF_DAY)) {
            createNewHandler();
        }
    }

    public void log(String str) {
        log(new Date(), str);
    }

    public void log(String[] csvField) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < csvField.length; i++) {
            sb.append(CSVParser.convert(csvField[i]));
            if (i + 1 < csvField.length) {
                sb.append(",");
            }
        }
        log(new Date(), sb.toString());
    }

    public synchronized void log(Date specificDate, String str) {
        validateCurrentFile();
        logFormatter.setSpecificDate(specificDate);
        msgLog.info(str);
        postLoggingProcess(str);
    }

    public void log(Date specificDate, String[] csvField) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < csvField.length; i++) {
            sb.append(CSVParser.convert(csvField[i]));
            if (i + 1 < csvField.length) {
                sb.append(",");
            }
        }
        log(specificDate, sb.toString());
    }

    protected void createNewHandler() {
        s_cal.setTimeInMillis(System.currentTimeMillis());
        Handler[] h = msgLog.getHandlers();
        if (h != null) {
            for (int i = 0; i < h.length; i++) {
                h[i].close();
                msgLog.removeHandler(h[i]);
            }
        }
        Handler newHandler = null;
        try {
            //filename, limet,count, appendmode
            String file = getFileName()
                + "-"
                + (fileDateformat.format(c_cal.getTime()) + "_%g.csv");
            if (checkAndCreateDir(file) == null) {
                DEBUG.warning("Unable to create a file so ConsoleHandler is used. FILE=" + file);
                EVENT.info("Unable to create a file. FILE=" + file);
            } else {
                // limet 5600000
                newHandler = new FileHandler(file, 5600000, 100, true);
                DEBUG.info("Creates the new handler for logging. FILE=" + file);
                EVENT.info("Creates the new handler for logging. FILE=" + file);
            }
        } catch (SecurityException e) {
            DEBUG.warning("Error while creating file handler ERROR=" + e.getMessage());
            EXCEPTION.log(Level.WARNING, "Error while creating file handler", e);
        } catch (IOException e) {
            DEBUG.warning("Error while creating file handler ERROR=" + e.getMessage());
            EXCEPTION.log(Level.WARNING, "Error while creating file handler", e);
        } finally {
            if (newHandler == null) {
                newHandler = new ConsoleHandler();
            }
        }
        newHandler.setFormatter(logFormatter);
        newHandler.setLevel(Level.ALL);
        msgLog.addHandler(newHandler);
        msgLog.setUseParentHandlers(false);
        notifyReplacePoint();
    }

    protected abstract String getFileName();

    protected abstract void notifyReplacePoint();

    private class SimpleLogFormatter extends LogFormatter {
        private Date specifiedDate;

        public void setSpecificDate(Date specificDate) {
            specifiedDate = specificDate;
        }

        public String format(LogRecord rec) {
            StringBuffer buf = new StringBuffer();
            if (specifiedDate != null) {
                buf.append(logDateformat.format(specifiedDate) + ",");
            } else {
                specifiedDate = new Date();
                buf.append(logDateformat.format(specifiedDate) + ",");
            }
            specifiedDate = null;
            buf.append(formatMessage(rec) + super.lineSeparator);
            return buf.toString();
        }
    }

    private String checkAndCreateDir(String filePath) {
        if (filePath == null || filePath.trim().length() < 1) {
            return null;
        }
        filePath = filePath.trim();
        File file = new File(filePath);
        if (file.getParentFile() == null) {
            return filePath;
        }
        if (!(file.getParentFile()).exists()) {
            boolean canCreate = (file.getParentFile()).mkdirs();
            if (!canCreate) {
                DEBUG.warning("Unable to create a directory. DIRNAME=" + file.getParentFile());
                return null;
            }
        }
        return filePath;
    }

    /**
     * Returns the disk usage size, recursively for directories.
     * @return disk usage size (byte)
     */
    public long getDirUsageSize() {
        String dirName = new File(getFileName()).getParent();
        if (dirName == null) {
            dirName = ".";
        }
        File dir = new File(dirName);
        return calculateDirSize(dir);
    }

    private long calculateDirSize(File dir) {
        long size = 0;
        String[] files = dir.list();
        if (files == null || files.length < 1) {
            return 0;
        }
        for (int i = 0; i < files.length; i++) {
            File tmp = new File(dir.getAbsolutePath() + "/" + files[i]);
            if (tmp.isDirectory()) {
                size += calculateDirSize(tmp);
            } else {
                size += tmp.length();
            }
        }
        return size;
    }

    /**
     * This method is called at the last of "log(String str)" method.
     * "postLoggingProcess(String str)" of PostLoggingListener in "postLoggingListeners" map is called.
     * @param str
     */
    private void postLoggingProcess(String str) {
        if (postLoggingListeners.size() > 0) {
            Iterator<String> ite = postLoggingListeners.keySet().iterator();
            while (ite.hasNext()) {
                PostLoggingListener lis = postLoggingListeners.get(ite.next());
                lis.postLoggingProcess(str);
            }
        }
    }

    /**
     * Adds new PostLoggingListener.
     * @param lis - PostLoggingListener
     */
    public void addPostLoggingListener(PostLoggingListener lis) {
        postLoggingListeners.put(lis.getListenerName(), lis);
        DEBUG.info("New PostLoggingListener is added. Added listener's name = '"
            + lis.getListenerName()
            + "'");
    }

    /**
     * Remove PostLoggingListener with name.
     * @param name
     */
    public void removePostLoggingListener(String name) {
        postLoggingListeners.remove(name);
        DEBUG.info("PostLoggingListener is removed. Removed listener's name = '" + name + "'");
    }

    /**
     * Remove PostLoggingListener with PostLoggingListener.
     * @param lis
     */
    public void removePostLoggingListener(PostLoggingListener lis) {
        removePostLoggingListener(lis.getListenerName());
    }

}
