package com.banti.framework.logging;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.ErrorManager;
import java.util.logging.FileHandler;
import java.util.logging.Filter;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class DailyFileHandler extends Handler {
    private FileHandler fh;
    private String baseName;
    private String fileName;
    private int limit;
    private int count;

    public DailyFileHandler(String base) throws SecurityException, IOException {
        this(base, 0, 10);
    }

    public DailyFileHandler(String base, int limit, int count) throws SecurityException,
        IOException {

        baseName = base;
        fileName = getFileName();
        this.count = count;
        this.limit = limit;
        fh = new FileHandler(fileName, limit, count, false);
    }

    private String getFileName() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        int ind = baseName.lastIndexOf('.');
        if (ind >= 0) {
            return baseName.substring(0, ind)
                + "-"
                + sdf.format(new Date())
                + "-%u.%g"
                + baseName.substring(ind);
        } else {
            return baseName + "-" + sdf.format(new Date()) + "-%u.%g";
        }
    }

    public void setEncoding(String encoding) throws SecurityException, UnsupportedEncodingException {
        fh.setEncoding(encoding);
    }

    public void setFormatter(Formatter newFormatter) {
        fh.setFormatter(newFormatter);
    }

    public void setFilter(Filter newFilter) {
        fh.setFilter(newFilter);
    }

    public void setLevel(Level newLevel) {
        fh.setLevel(newLevel);
    }

    public void setErrorManager(ErrorManager em) {
        fh.setErrorManager(em);
    }

    public boolean isLoggable(LogRecord record) {
        return fh.isLoggable(record);
    }

    public Filter getFilter() {
        return fh.getFilter();
    }

    public Formatter getFormatter() {
        return fh.getFormatter();
    }

    public String getEncoding() {
        return fh.getEncoding();
    }

    public ErrorManager getErrorManager() {
        return fh.getErrorManager();
    }

    @Override
    public void close() throws SecurityException {
        fh.close();
    }

    @Override
    public void flush() {
        fh.flush();
    }

    @Override
    public synchronized void publish(LogRecord record) {
        String newName = getFileName();
        if (!newName.equals(fileName)) {
            FileHandler oldFh = fh;
            try {
                fh = new FileHandler(newName, limit, count, false);
                fh.setEncoding(oldFh.getEncoding());
                fh.setErrorManager(oldFh.getErrorManager());
                fh.setFilter(oldFh.getFilter());
                fh.setFormatter(oldFh.getFormatter());
                fh.setLevel(oldFh.getLevel());
                fileName = newName;

            } catch (SecurityException e) {
                System.err.println("Could not open log file: "
                    + newName
                    + ", "
                    + e.getLocalizedMessage());
            } catch (IOException e) {
                System.err.println("Could not open log file: "
                    + newName
                    + ", "
                    + e.getLocalizedMessage());
            } finally {
                oldFh.close();
            }
        }
        fh.publish(record);
    }
}
