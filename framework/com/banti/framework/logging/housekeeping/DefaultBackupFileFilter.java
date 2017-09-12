package com.banti.framework.logging.housekeeping;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DefaultBackupFileFilter extends LogFileFilter {
    private String base;
    private Matcher matcher;
    private String today;

    public DefaultBackupFileFilter() {
        this("([^-]+)-(\\d+)-\\d+\\.\\d+\\.log", "yyyyMMdd");
    }

    public DefaultBackupFileFilter(String patStr, String datePat) {
        Pattern pat = Pattern.compile(patStr);
        matcher = pat.matcher("");

        SimpleDateFormat sdf = new SimpleDateFormat(datePat);
        today = sdf.format(new Date());
    }

    public void setBase(String base) {
        this.base = base;
    }

    public boolean accept(File dir, String name) {
        matcher.reset(name);
        if (matcher.matches()) {
            File lckFile = new File(dir.getAbsolutePath() + "/" + name + ".lck");
            if (lckFile.exists() && today.equals(matcher.group(2))) {
                return false;
            }
            String b = null;
            if (base == null) {
                b = matcher.group(1);
            } else {
                b = base;
            }
            ArrayList<LogFile> list = baseFileMap.get(b);
            if (list == null) {
                list = new ArrayList<LogFile>();
                baseFileMap.put(b, list);
            }
            list.add(new LogFile(b, matcher.group(2), name, dir.getAbsolutePath()));
            return true;
        }
        return false;
    }
}
