package com.banti.framework.logging.housekeeping;

public class LogFile {
    private String base;
    private String date;
    private String fullName;
    private String dir;

    public LogFile(String base, String date, String fullName, String dir) {
        this.base = base;
        this.date = date;
        this.fullName = fullName;
        this.dir = dir;
    }

    public String getDateBase() {
        return base + "-" + date;
    }

    public String getFullPath() {
        return dir + "/" + fullName;
    }

    public String getBase() {
        return base;
    }

    public String getDate() {
        return date;
    }

    public String getDir() {
        return dir;
    }

    public String getFullName() {
        return fullName;
    }
}
