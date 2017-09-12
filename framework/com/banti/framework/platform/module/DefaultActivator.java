package com.banti.framework.platform.module;

import com.banti.framework.platform.Activator;

/**
 * The default implementation of Activator interface. 
 * This class always returns true when "isActivated()" method is called.
 * <p>
 * This class will be used when it is required to load Plug-in command/module but
 * not to switch enabled/disabled by activation mechanism.
 */
public class DefaultActivator implements Activator {
    
    /**
     * Public Constructor.
     *
     */
    public DefaultActivator() {
        super();
    }

    /**
     * Returns true always.
     */
    public boolean isActivated() {
        return true;
    }

    /**
     * Returns true always.
     */
    public boolean isNoLicense() {
        return true;
    }
    
    public String getModuleName() {
        return null;
    }
}
