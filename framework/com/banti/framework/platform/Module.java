package com.banti.framework.platform;

import java.awt.event.ActionEvent;

/**
 * An abstract <i>Module</i> realizes a Composite Pattern for command hierarchy.
 * A concrete Module can has some <i>Command</i> instances or another Module itself.
 * Module is used as the top menu of the menu bar. This platform has FileModule, EditModule,
 * etc as concrete Module implementations. It is possible to extend Module and implement
 * your own module. Application can handle your module properly and show it as Menu.
 */
public abstract class Module extends Command {

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
     * @param name the name of the module, which is displayed as the menu.
     */
    public Module(String name) {
        super(name);
    }
    
    /**
     * A concrete implementation of abstract Command class. Module itself extends
     * Command. This method always does nothing since Module never executes its function.
     */
    public final void entryPoint(ActionEvent ae) {
        return;
    }

    
    void accept(MenuBar bar) {
        bar.visit(this);
    }
    
}