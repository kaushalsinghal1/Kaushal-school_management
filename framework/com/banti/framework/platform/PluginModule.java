package com.banti.framework.platform;

import java.awt.event.ActionEvent;

/**
 * The responsibility of this class is the same as <a href="Module.html"><i>Module</i></a> 
 * abstract class. This is a pluggable module. The disposition is the same as 
 * <i>PluginCommand</i>.
 */
public abstract class PluginModule extends PluginCommand {
    
    /* (non-Javadoc)
     * @see com.cysols.framework.platform.PluginCommand#getActivator()
     */
    public abstract Activator getActivator();
    
    
    /* (non-Javadoc)
     * @see com.cysols.framework.platform.PluginCommand#getOrder()
     */
    public abstract int getOrder();
    
    /**
     * Returns an array of Command concrete instances, which means the child
     * components of the module.
     * 
     * @return Command[] array.
     */    
    public abstract Command[] getCommands();

    /**
     * A constructor, a name of the module is given only by this constructor.
     * 
     * @param name the name of the module, which is displayed as the menu item.
     */    
    public PluginModule(String name) {
        super(name);
    }    
    
    /**
     * A concrete implementation of abstract PluginCommand class. 
     * This method always does nothing since PluginModule never executes its function.
     */    
    public final void entryPoint(ActionEvent ae) {
        return;
    }

    public void accept(LicenseManager licenseManager) {
        licenseManager.visit(this);
    }
    
    // ---------- non-public method -----------------------------------------------
    /**
     * This is a method for Visitor pattern.
     */    
    void accept(MenuBar bar) {
        bar.visit(this);
    }
}
