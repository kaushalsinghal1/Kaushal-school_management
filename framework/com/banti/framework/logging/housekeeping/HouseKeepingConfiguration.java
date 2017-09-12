package com.banti.framework.logging.housekeeping;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;

import com.banti.framework.logging.config.FLLogger;

public class HouseKeepingConfiguration {
    private File config;
    private Properties props;

    public HouseKeepingConfiguration() {
        this(new File(HouseKeepingConstants.BACKUP_CONFIG_FILE));
    }

    public HouseKeepingConfiguration(File config) {
        this.config = config;
        props = new Properties();
    }

    public void loadConfig(String file) throws IOException {
        this.config = new File(file);
        loadConfig();
    }

    public void loadConfig() throws IOException {

        if (!config.exists()) {
            return;
        }
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(config);
            props.load(fis);
            FLLogger.event.config("Following records are read from backup configuration file.");
            for (Iterator iter = props.keySet().iterator(); iter.hasNext();) {
                String key = (String) iter.next();
                String val = (String) props.get(key);
                FLLogger.event.config("\t" + key + "=" + val);
            }
        } finally {
            if (fis != null) {
                fis.close();
            }
        }
    }

    void saveConfig() throws IOException {
        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(config);
            props.store(fos, "");
            FLLogger.event.config("Following records are stored to backup configuration file.");
            for (Iterator iter = props.keySet().iterator(); iter.hasNext();) {
                String key = (String) iter.next();
                String val = (String) props.get(key);
                FLLogger.event.config("\t" + key + "=" + val);
            }
        } finally {
            if (fos != null) {
                fos.close();
            }
        }
    }

    public Date getExecuteDate() {
        String time = getExecuteTime();
        Calendar cal = Calendar.getInstance();

        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int min = cal.get(Calendar.MINUTE);

        String[] hm = time.split("-");
        int execHour = 1;
        int execMin = 0;
        if (hm.length == 2) {
            try {
                execHour = Integer.parseInt(hm[0]);
                execMin = Integer.parseInt(hm[1]);
            } catch (NumberFormatException e) {
                FLLogger.debug.warning("Invalid execute time: " + time + ", using default 1-0");
            }
        } else {
            FLLogger.debug.warning("Invalid execute time: " + time + ", using default 1-0");
        }
        if (execHour < hour || (execHour == hour && execMin < min)) {
            cal.add(Calendar.DATE, 1);
        }
        cal.set(Calendar.HOUR_OF_DAY, execHour);
        cal.set(Calendar.MINUTE, execMin);
        cal.set(Calendar.SECOND, 0);
        return cal.getTime();
    }

    public String getExecuteTime() {
        return props.getProperty(
            HouseKeepingConstants.TAG_EXECUTE_TIME,
            HouseKeepingConstants.DEFAULT_EXECUTE_TIME);
    }

    public void setExecuteTime(String time) {
        props.setProperty(HouseKeepingConstants.TAG_EXECUTE_TIME, time);
    }

    public boolean isDeleteBackup(String keyword) {
        return getBoolean(
            HouseKeepingConstants.TAG_DELETE_BACKUP,
            keyword,
            HouseKeepingConstants.DEFAULT_DELETE_BACKUP);
    }

    public void setDeleteBackup(String keyword, boolean deleteBackup) {
        String key = HouseKeepingConstants.TAG_DELETE_BACKUP;
        if (keyword != null) {
            key += "." + keyword;
        }
        props.setProperty(key, "" + deleteBackup);
    }

    public boolean isEnableBackup(String keyword) {
        return getBoolean(
            HouseKeepingConstants.TAG_ENABLE_BACKUP,
            keyword,
            HouseKeepingConstants.DEFAULT_ENABLE_BACKUP);
    }

    public void setEnableBackup(String keyword, boolean enableBackup) {
        String key = HouseKeepingConstants.TAG_ENABLE_BACKUP;
        if (keyword != null) {
            key += "." + keyword;
        }
        props.setProperty(key, "" + enableBackup);
    }

    public Date getBackupDate(String keyword) {
        Calendar cal = Calendar.getInstance();
        int field = Calendar.DAY_OF_MONTH;
        if (!isDaysBackupBefore(keyword)) {
            field = Calendar.MONTH;
        }
        cal.add(field, -getBackupBefore(keyword));
        return cal.getTime();
    }

    public int getBackupBefore(String keyword) {
        return getInt(
            HouseKeepingConstants.TAG_BACKUP_BEFORE,
            keyword,
            HouseKeepingConstants.DEFAULT_BACKUP_BEFORE);
    }

    public void setBackupBefore(String keyword, int backupBefore) {
        String key = HouseKeepingConstants.TAG_BACKUP_BEFORE;
        if (keyword != null) {
            key += "." + keyword;
        }
        props.setProperty(key, "" + backupBefore);
    }

    public Date getDeleteDate(String keyword) {
        Calendar cal = Calendar.getInstance();
        int field = Calendar.DAY_OF_MONTH;
        if (!isDaysDeleteBefore(keyword)) {
            field = Calendar.MONTH;
        }
        cal.add(field, -getDeleteBefore(keyword));
        return cal.getTime();
    }

    public int getDeleteBefore(String keyword) {
        return getInt(
            HouseKeepingConstants.TAG_DELETE_BEFORE,
            keyword,
            HouseKeepingConstants.DEFAULT_DELETE_BEFORE);
    }

    public void setDeleteBefore(String keyword, int deleteBefore) {
        String key = HouseKeepingConstants.TAG_DELETE_BEFORE;
        if (keyword != null) {
            key += "." + keyword;
        }
        props.setProperty(key, "" + deleteBefore);
    }

    public boolean isDaysBackupBefore(String keyword) {
        return getBoolean(
            HouseKeepingConstants.TAG_IS_DAYS_BACKUP_BEFORE,
            keyword,
            HouseKeepingConstants.DEFAULT_IS_DAYS_BACKUP_BEFORE);
    }

    public void setDaysBackupBefore(String keyword, boolean isDaysBackupBefore) {
        String key = HouseKeepingConstants.TAG_IS_DAYS_BACKUP_BEFORE;
        if (keyword != null) {
            key += "." + keyword;
        }
        props.setProperty(key, "" + isDaysBackupBefore);
    }

    public boolean isDaysDeleteBefore(String keyword) {
        return getBoolean(
            HouseKeepingConstants.TAG_IS_DAYS_DELETE_BEFORE,
            keyword,
            HouseKeepingConstants.DEFAULT_IS_DAYS_DELETE_BEFORE);
    }

    public void setDaysDeleteBefore(String keyword, boolean isDaysDeleteBefore) {
        String key = HouseKeepingConstants.TAG_IS_DAYS_DELETE_BEFORE;
        if (keyword != null) {
            key += "." + keyword;
        }
        props.setProperty(key, "" + isDaysDeleteBefore);
    }

    private int getInt(String key, String keyword, int defval) {
        String tmp = null;
        if (keyword == null) {
            tmp = props.getProperty(key);
        } else {
            tmp = props.getProperty(key + "." + keyword);
            if (tmp == null) {
                tmp = props.getProperty(key);
            }
        }
        if (tmp == null) {
            return defval;
        } else {
            try {
                return Integer.parseInt(tmp);
            } catch (NumberFormatException e) {
                return defval;
            }
        }
    }

    private boolean getBoolean(String key, String keyword, boolean defval) {
        String tmp = null;
        if (keyword == null) {
            tmp = props.getProperty(key);
        } else {
            tmp = props.getProperty(key + "." + keyword);
            if (tmp == null) {
                tmp = props.getProperty(key);
            }
        }
        if (tmp == null) {
            return defval;
        } else {
            try {
                if (defval) {
                    return !"false".equalsIgnoreCase(tmp);
                } else {
                    return "true".equalsIgnoreCase(tmp);
                }
            } catch (NumberFormatException e) {
                return defval;
            }
        }
    }
}
