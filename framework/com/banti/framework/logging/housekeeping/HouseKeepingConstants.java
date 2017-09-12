package com.banti.framework.logging.housekeeping;

public interface HouseKeepingConstants {
    String LOG_DIR = "log";
    String BACKUP_DIR = "backup";

    String BACKUP_CONFIG_FILE = "config/backup.properties";

    String TAG_ENABLE_BACKUP = "ENABLE_BACKUP";
    String TAG_EXECUTE_TIME = "EXECUTE_TIME";
    String TAG_IS_DAYS_BACKUP_BEFORE = "IS_DAYS_BACKUP_BEFORE";
    String TAG_BACKUP_BEFORE = "BACKUP_BEFORE";

    String TAG_DELETE_BACKUP = "DELETE_BACKUP";
    String TAG_IS_DAYS_DELETE_BEFORE = "IS_DAYS_DELETE_BEFORE";
    String TAG_DELETE_BEFORE = "DELETE_BEFORE";

    boolean DEFAULT_ENABLE_BACKUP = true;
    String DEFAULT_EXECUTE_TIME = "1-0";
    boolean DEFAULT_IS_DAYS_BACKUP_BEFORE = true;
    int DEFAULT_BACKUP_BEFORE = 7;
    boolean DEFAULT_DELETE_BACKUP = true;
    boolean DEFAULT_IS_DAYS_DELETE_BEFORE = false;
    int DEFAULT_DELETE_BEFORE = 1;
}
