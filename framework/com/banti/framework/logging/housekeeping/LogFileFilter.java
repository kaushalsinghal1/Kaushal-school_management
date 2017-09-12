package com.banti.framework.logging.housekeeping;

import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public abstract class LogFileFilter implements FilenameFilter {
    protected HashMap<String, ArrayList<LogFile>> baseFileMap;

    public LogFileFilter() {
        baseFileMap = new HashMap<String, ArrayList<LogFile>>();
    }

    public Set<String> baseSet() {
        return baseFileMap.keySet();
    }

    public List<LogFile> files(String base) {
        List<LogFile> l = baseFileMap.get(base);
        if (l == null) {
            return new ArrayList<LogFile>();
        }
        return l;
    }
}
