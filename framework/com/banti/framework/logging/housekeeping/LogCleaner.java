package com.banti.framework.logging.housekeeping;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import com.banti.framework.logging.config.FLLogger;

public class LogCleaner {

    protected String getLogDir() {
        return HouseKeepingConstants.LOG_DIR;
    }

    protected String getBackupDir() {
        return HouseKeepingConstants.BACKUP_DIR;
    }

    protected LogFileFilter createLogFileFilter() {
        return new DefaultDeleteFileFilter();
    }

    protected LogFileFilter createArchiveFilter() {
        return new DefaultArchiveFilter();
    }

    protected SimpleDateFormat createSDF() {
        return new SimpleDateFormat("yyyyMMdd");
    }

    public void execute(HouseKeepingConfiguration hc) {
        File logDir = new File(getLogDir());
        deleteFiles(logDir, createLogFileFilter(), hc);

        File backupDir = new File(getBackupDir());
        deleteFiles(backupDir, createArchiveFilter(), hc);
        deleteFiles(backupDir, createLogFileFilter(), hc);
    }

    private void deleteFiles(File dir, LogFileFilter lfFilter, HouseKeepingConfiguration hc) {
        dir.list(lfFilter);

        SimpleDateFormat sdf = createSDF();

        Iterator<String> baseIter = lfFilter.baseSet().iterator();
        while (baseIter.hasNext()) {
            String base = baseIter.next();
            if (!hc.isDeleteBackup(base)) {
                FLLogger.debug.finest("Log backup deletion for " + base + " is disabled");
                continue;
            }
            Date date = hc.getDeleteDate(base);
            String strDate = sdf.format(date);

            Iterator<LogFile> fileIter = lfFilter.files(base).iterator();
            while (fileIter.hasNext()) {
                LogFile lf = fileIter.next();

                File file = new File(lf.getFullPath());
                if (strDate.compareTo(lf.getDate()) >= 0) {
                    if (file.exists()) {
                        if (file.delete()) {
                            FLLogger.debug.fine("Deleted file: " + file);
                        } else {
                            FLLogger.debug.warning("Unable to delete file: " + file);
                        }
                    }
                }
            }
        }
    }
}
