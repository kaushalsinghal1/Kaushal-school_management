package com.banti.framework.logging.housekeeping;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DefaultDeleteFileFilter extends LogFileFilter {
    private Matcher matcher;
    private String base;

    public DefaultDeleteFileFilter() {
        this("([^-]+)-(\\d+)-\\d+\\.\\d+\\.log(\\.lck)?");
    }

    public DefaultDeleteFileFilter(String patStr) {
        Pattern pat = Pattern.compile(patStr);
        matcher = pat.matcher("");
    }

    public void setBase(String base) {
        this.base = base;
    }

    public boolean accept(File dir, String name) {
        matcher.reset(name);
        if (matcher.matches()) {
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
