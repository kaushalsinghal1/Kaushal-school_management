package com.banti.framework.logging.housekeeping;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.banti.framework.logging.config.FLLogger;

public class LogCompressor {
    protected static int BUFSIZE = 8192;

    protected String getLogDir() {
        return HouseKeepingConstants.LOG_DIR;
    }

    protected String getBackupDir() {
        return HouseKeepingConstants.BACKUP_DIR;
    }

    protected LogFileFilter createLogFileFilter() {
        return new DefaultBackupFileFilter();
    }

    protected SimpleDateFormat createSDF() {
        return new SimpleDateFormat("yyyyMMdd");
    }

    public void execute(HouseKeepingConfiguration hc) {
        File backupDir = new File(getBackupDir());
        if (!backupDir.exists()) {
            backupDir.mkdirs();
        }
        File logDir = new File(getLogDir());
        LogFileFilter lfFilter = createLogFileFilter();
        logDir.list(lfFilter);

        SimpleDateFormat sdf = createSDF();

        Iterator<String> baseIter = lfFilter.baseSet().iterator();
        while (baseIter.hasNext()) {
            String base = baseIter.next();
            if (!hc.isEnableBackup(base)) {
                FLLogger.debug.finest("Log backup for " + base + " is disabled");
                continue;
            }
            Date date = hc.getBackupDate(base);
            String strDate = sdf.format(date);
            String prevDateBase = null;
            ArrayList<LogFile> compressList = new ArrayList<LogFile>();
            List<LogFile> lfList = lfFilter.files(base);

            Collections.sort(lfList, new Comparator<LogFile>() {
                public int compare(LogFile o1, LogFile o2) {
                    return o1.getDateBase().compareTo(o2.getDateBase());
                }
            });

            Iterator<LogFile> fileIter = lfList.iterator();

            while (fileIter.hasNext()) {
                LogFile lf = fileIter.next();

                //                File file = new File(lf.getFullPath());

                if (strDate.compareTo(lf.getDate()) >= 0) {
                    if (prevDateBase == null) {
                        prevDateBase = lf.getDateBase();
                    }
                    if (!lf.getDateBase().equals(prevDateBase) && compressList.size() > 0) {
                        compress(prevDateBase, compressList);
                        compressList.clear();
                        prevDateBase = lf.getDateBase();
                    }
                    compressList.add(lf);

                    //                } else {
                    //                    try {
                    //                        if (copy(lf)) {
                    //                            FLLogger.debug.fine("Copied file: " + file);
                    //                        }
                    //                    } catch (IOException e) {
                    //                        FLLogger.debug.warning("Unable to copy file: " + file);
                    //                        FLLogger.exception.log(Level.WARNING, "Unable to copy file: " + file, e);
                    //                    }
                }
            }
            if (compressList.size() > 0) {
                compress(prevDateBase, compressList);
            }
        }
    }

    protected void compress(String dateBase, List<LogFile> lfList) {
        try {
            zip(dateBase, lfList);
        } catch (IOException e) {
            FLLogger.debug.warning("Unable to compress file: " + dateBase + ".zip");
            FLLogger.exception.log(
                Level.WARNING,
                "Unable to compress file: " + dateBase + ".zip",
                e);
        }
        StringBuilder sb = new StringBuilder();
        Iterator<LogFile> iter = lfList.iterator();
        while (iter.hasNext()) {
            LogFile lf = iter.next();
            File file = new File(lf.getFullPath());
            if (!file.delete()) {
                FLLogger.debug.warning("Unable to delete file: " + file);
            }
            sb.append(lf.getFullName());
            sb.append(" ");
        }
        FLLogger.debug.fine("Compress " + sb.toString() + "into " + dateBase + ".zip");
    }

    protected void zip(String dateBase, List<LogFile> lfList) throws IOException {
        FileOutputStream fos = null;
        ZipOutputStream zos = null;
        FileInputStream fis = null;

        try {
            fos = new FileOutputStream(getBackupDir() + "/" + dateBase + ".zip");
            zos = new ZipOutputStream(fos);

            Iterator<LogFile> iter = lfList.iterator();
            while (iter.hasNext()) {
                LogFile lf = iter.next();
                ZipEntry ze = new ZipEntry(lf.getFullName());
                zos.putNextEntry(ze);
                fis = new FileInputStream(lf.getFullPath());

                int nread = 0;
                byte[] buf = new byte[BUFSIZE];

                while ((nread = fis.read(buf)) > 0) {
                    zos.write(buf, 0, nread);
                }
                fis.close();
                fis = null;
                zos.closeEntry();
            }

        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } finally {
                if (zos != null) {
                    zos.close();
                }
            }
        }
    }

    public boolean copy(LogFile file) throws IOException {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        File outFile = new File(getBackupDir() + "/" + file.getFullName());
        if (outFile.exists()) {
            return false;
        }
        try {
            fos = new FileOutputStream(outFile);

            fis = new FileInputStream(file.getFullPath());

            int nread = 0;
            byte[] buf = new byte[BUFSIZE];

            while ((nread = fis.read(buf)) > 0) {
                fos.write(buf, 0, nread);
            }
            return true;

        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } finally {
                    if (fos != null) {
                        fos.close();
                    }
                }
            }
        }
    }
}
