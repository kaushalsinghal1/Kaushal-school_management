package com.banti.framework.platform;

import java.awt.event.ActionEvent;

import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

/**
 * This is an extended Command, abstract class. Two methods with regard to "Selected" attribute
 * are added. If a command is implemented by extending ToggledCommand, then <i>Application</i>
 * shows it as a CheckBoxMenuItem on the menu bar and as a ToggledButton on the tool bar.  
 */
public abstract class ToggledCommand extends Command {

    /* (non-Javadoc)
     * @see com.cysols.framework.platform.Command#entryPoint(java.awt.event.ActionEvent)
     */
    public abstract void entryPoint(ActionEvent e);
    
    /**
     * A constructor, a name of the command is given only by this constructor.
     * 
     * @param name the name of the command, which is displayed as the menu item.
     */
    public ToggledCommand(String name) {
        super(name);
    }

    /**
     * Returns true if thestatus of the command is "Selected".
     * 
     * @return selected or not.
     */
    public final boolean isSelected() {
        return super.isCmdSelected();
    }
    
    /**
     * Sets the status of "Selected".
     * 
     * @param aFlag true means the command has "Selected" status.
     */
    public final void setSelected(boolean aFlag) {
        super.setCmdSelected(aFlag);
    }

    /**
     * When InternalWindow is closed which is opened by "entryPoint" of this command,
     * it gets possible to change the "selected" status of this command into false by this method.
     * 
     * @param window InternalWindow object, which is probably created by "entryPoint" of this class.
     */
    protected void join(InternalWindow window) {
        if (window == null) return;        
        window.addInternalFrameListener(new InnerInternalFrameListener());
    }
    
    // ---------- non-public method -----------------------------------------------
    /**
     * This is a method for Visitor pattern.
     */     
    void accept(MenuBar bar) {
        bar.visit(this);
    }
    
    /*
     * Inner InternalFrameListener
     * This work is to change selected status of ToggledCommand
     * when the window is closed. 
     */
    private class InnerInternalFrameListener implements InternalFrameListener {
        
        public void internalFrameClosed(InternalFrameEvent e) {                
            setSelected(false);
        }

        public void internalFrameActivated(InternalFrameEvent e) {
            // do nothing.
        }
        public void internalFrameClosing(InternalFrameEvent e) {
            // do nothing.
        }
        public void internalFrameDeactivated(InternalFrameEvent e) {
            // do nothing.                
        }
        public void internalFrameDeiconified(InternalFrameEvent e) {
            // do nothing.                
        }

        public void internalFrameIconified(InternalFrameEvent e) {
            // do nothing.                
        }
        public void internalFrameOpened(InternalFrameEvent e) {
            // do nothing                
        }            
    }
}