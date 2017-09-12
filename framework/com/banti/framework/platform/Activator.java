package com.banti.framework.platform;

/**
 * The responsibility of <i>Activator</i> is to activate Plug-in. How to activate is not defined 
 * since <i>Activator</i> is an interface. You can implement your own activation logic freely.
 * Only when "isActivated()" method returns true, the Plug-in gets enabled and can be called 
 * from the menu bar or the tool bar. The Platform gives you License Code based activation mechanism. 
 * An abstract <a href="module/AbstractPrefsActivator.html"><i>AbstractPrefsActivator</i></a> works
 * for such a license code activation.
 * It is also possible to use <a href="module/DefaultActivator.html"><i>DefaultActivator</i></a> class. 
 * It is a concrete implementation of <i>Activator</i> interface.
 * It always returns true from its "isActivated()" method. <i>DefaultActivator</i> will be used 
 * when a command is desirable to be plugged dynamically but when no license is needed.
 * If license is needless, you just use <i>DefaultActivator</i> for your own plugin command. 
 * Then, "isNoLicense()" method always returns true. This means that your own 
 * <i>Activator</i> need to return false if you implement a special <i>Activator</i>.
 */
public interface Activator {

    public static String ACTIVATED = "activated";
    
    
    /**
     * Returns a module name, which is listed in License Entry List.
     * 
     * @return a name of the module.
     */
    public String getModuleName();
    
    /**
     * Returns true if a corresponding Plug-in is enabled.  
     * 
     * @return true or false, which means Plug-in is available or not.
     */
    public boolean isActivated();
    
    /**
     * Returns whether license code is needed or not. "isActivated()" method
     * must always true if this method is true;
     * 
     * @return - true if it is unnecessary to register the license code, false otherwise. 
     */
    public boolean isNoLicense();
    
}
