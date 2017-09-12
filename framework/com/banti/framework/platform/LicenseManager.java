/*
 * Copyright (c) Cyber Solutions Inc.
 * All Rights Reserved.
 * 
 * Created on 2004/08/20
 */
package com.banti.framework.platform;

import java.beans.PropertyChangeListener;

/**
 * This is an interface that manages LicenseCode through License Registration.
 */
public interface LicenseManager {
	public static final String trialCodePreFix="SCH0-";
	public static final String MAIN_LICENCE="Main Licence";
    /**
     * Registers a license code of the specified PluginCommand.
     * 
     * @param command PluginCommand
     * @param enteredCode a License Code of the specified Plugin.
     * 
     * @return true if registered successfully.
     */
    public boolean register(PluginCommand command, String enteredCode);
    
    /**
     * Deletes the license code of the specified PluginCommand.
     * 
     * @param command PluginCommand
     * 
     * @return true if its license code is deleted successfully.
     */
    public boolean delete(PluginCommand command);
    
    /**
     * Reads a license code of the specified PliuginCommand.
     * 
     * @param command the specified Plug-in
     * @return the registered License Code of the Plug-in.
     */
    public String read(PluginCommand command);
    
    /**
     * Adds PropertyChangeListener to LicenseManager. It is possible to notify
     * property change to added listeners.
     * 
     * @param pcListener PropertyChangeListener concrete instance.
     */
    void addPropertyChangeListener(PropertyChangeListener pcListener);
    
    /**
     * This method will be called after LicenseManager.register or delete method is called.
     * If License Code is registered/deleted successfully, it is necessary to implement PluginCommand
     * enable/disable process in this method.
     * 
     * @param command
     */
    public void visit(PluginCommand command);

    /**
     * This method will be called after LicenseManager.register or delete method is called.
     * If License Code is registered/deleted successfully, it is necessary to implement PluginModule
     * enable/disable process in this method. Then "visit(PluginCommand) should be called if
     * the PluginModule contains PluginCommand in itself. 
     * 
     * @param module
     */
    public void visit(PluginModule module);
    
    /**
     * This method will be called if the manager is down and null response is returned in activator. 
     * @return managerDownFlag 
     */
    public boolean isResponseNull();
}
