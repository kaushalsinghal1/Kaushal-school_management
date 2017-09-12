package com.banti.framework.platform;

import java.awt.Frame;

import javax.swing.JDialog;

/**
 * This is an interface which provides a printing method.
 * Printable InternalWindow must implement this interface. <i>Application</i>
 * manages and controls the printing operation for the focused InternalWindow
 * through this interface. 
 */
public interface PrintControllable {


    /**
     * This method fulfills a printing operation. The concrete method must do printing
     * in this method. 
     *
     */
    public void doPrint();
            
    /**
     * Returns JDialog, which should show the preview of the contents to be printed.
     * If Not-Null instance is returned from this method, <i>Application</i> makes 
     * "Preview" menu item available and shows the JDialog. 
     * 
     * @param parent parent frame of the JDialog.
     * @return JDIalog that contains Preview contents.
     */
    public JDialog createPreviewDialog(Frame parent);
    
}
