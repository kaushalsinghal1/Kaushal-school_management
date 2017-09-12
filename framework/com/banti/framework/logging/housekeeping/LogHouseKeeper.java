package com.banti.framework.logging.housekeeping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;

import com.banti.framework.logging.config.FLLogger;

public class LogHouseKeeper {
    private static final long INTERVAL = 24 * 60 * 60 * 1000; // 1 day
    private static LogHouseKeeper instance = new LogHouseKeeper();

    private Timer timer;
    private HouseKeepingConfiguration hc;

    private boolean executing;

    private ArrayList<LogCompressor> compressors;
    private ArrayList<LogCleaner> cleaners;

    private LogHouseKeeper() {
        hc = new HouseKeepingConfiguration();
    }

    public static LogHouseKeeper getInstance() {
        return instance;
    }

    public synchronized void load() throws IOException {
        load(null);
    }

    public synchronized void load(String file) throws IOException {
        if (file != null) {
            hc.loadConfig(file);
        } else {
            hc.loadConfig();
        }

        compressors = new ArrayList<LogCompressor>();
        compressors.add(new LogCompressor());

        cleaners = new ArrayList<LogCleaner>();
        cleaners.add(new LogCleaner());
    }

    public HouseKeepingConfiguration getConfig() {
        return hc;
    }

    public synchronized void save() throws IOException {
        hc.saveConfig();
    }

    public void executeOnce() {
        new Thread(new HouseKeepingTask()).start();
    }

    public void start() {
        timer = new Timer("Log House Keeper", true);
        TimerTask task = new HouseKeepingTask();
        timer.schedule(task, hc.getExecuteDate(), INTERVAL);
    }

    public void addCustomCompressor(LogCompressor compressor) {
        compressors.add(compressor);
    }

    public void removeCustomCompressor(LogCompressor compressor) {
        compressors.remove(compressor);
    }

    public void addCustomCleaner(LogCleaner cleaner) {
        cleaners.add(cleaner);
    }

    public void removeCustomCleaner(LogCleaner cleaner) {
        cleaners.remove(cleaner);
    }

    private class HouseKeepingTask extends TimerTask {
        public HouseKeepingTask() {
        }

        @Override
        public void run() {
            synchronized (LogHouseKeeper.this) {
                if (executing) {
                    return;
                }
                executing = true;
            }
            try {
                FLLogger.debug.info("Log Backup process is starting");
                for (LogCompressor compressor : compressors) {
                    if (compressor != null) {
                        compressor.execute(hc);
                    }
                }

                FLLogger.debug.info("Backup Deletion process is starting");
                for (LogCleaner cleaner : cleaners) {
                    if (cleaner != null) {
                        cleaner.execute(hc);
                    }
                }
            } catch (Throwable t) {
                FLLogger.debug.warning("Error while log house keeping, ERROR="+ t.getMessage());
                FLLogger.exception.log(Level.WARNING, "Error while log house keeping", t);

            } finally {
                executing = false;
            }
        }
    }
}
