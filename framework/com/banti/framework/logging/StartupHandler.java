package com.banti.framework.logging;

import java.io.IOException;
import java.util.logging.FileHandler;

public class StartupHandler extends FileHandler {
    public StartupHandler(String base) throws IOException, SecurityException {
        super(getFileName(base), 0, 3, false);
    }

    private static String getFileName(String base) {
        int ind = base.lastIndexOf('/');
        if (ind >= 0 && ind < base.length() - 1) {
            base = base.substring(0, ind + 1) + "startup_" + base.substring(ind + 1);
        } else {
            base = "startup_" + base;
        }
        ind = base.lastIndexOf('.');
        if (ind >= 0) {
            return base.substring(0, ind)
                + "-%u.%g"
                + base.substring(ind);
        } else {
            return base + "-%u.%g";
        }
    }
}
