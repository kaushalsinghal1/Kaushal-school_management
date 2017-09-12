package com.banti.framework.platform;

import java.beans.PropertyVetoException;

import javax.swing.JInternalFrame;

import com.banti.framework.theme.ToolKit;


/**
 * An Internal Window that can be displayed in the <a href="Application.html"><i>Application</i></a>.
 * If you want to display a window in the Diplay area of your application, you <b><big>MUST</big></b> 
 * use InternalWindow. How to use is the same as "javax.swing.JInternalFrame". InternalWindow class
 * is a sub class of JInternalFrame. It has some extended methods to display itself in your application.
 * InternalWindow is managed by Application, and it is available to arrange windows by Window Menu.
 */
public class InternalWindow extends JInternalFrame {

    private final String IMAGE_PATH = "/com/banti/framework/platform/images/window.gif";
    
    private boolean packed = false;
    
    protected String HELP_ID;
    protected String DEFAULT_HELP_ID;
    
    /**
     * Creates a non-resizable, non-closable, non-maximizable, 
     * non-iconifiable JInternalFrame with the specified title.
     * 
     * @param title the non-null String to display in the title bar.
     */
    public InternalWindow(String title) {
        super(title, true, true, true, true);        
        ToolKit tool = ToolKit.getInstance();
        super.setFrameIcon(tool.createImageIcon(IMAGE_PATH));
    }    
    
    /**
     * Creates a non-closable, non-maximizable, non-iconifiable InternalWindow 
     * with the specified title and resizability.
     * 
     * @param title the String to display in the title bar
     * @param resizable f true, the internal frame can be resized.
     */
    public InternalWindow(String title, boolean resizable) {
        super(title, resizable);
        ToolKit tool = ToolKit.getInstance();
        super.setFrameIcon(tool.createImageIcon(IMAGE_PATH));
    }
    
    /**
     * Creates a non-maximizable, non-iconifiable InternalWindow 
     * with the specified title, resizability, and closability.
     * 
     * @param title the String to display in the title bar.
     * @param resizable if true, the internal frame can be resized.
     * @param closable if true, the internal frame can be closed.
     */
    public InternalWindow(String title, boolean resizable, boolean closable) {
        super(title, resizable, closable);
        ToolKit tool = ToolKit.getInstance();
        super.setFrameIcon(tool.createImageIcon(IMAGE_PATH));
    }

    /**
     * Creates a non-iconifiable InternalWindow 
     * with the specified title, resizability, closability, and maximizability.
     * 
     * @param title the String to display in the title bar.
     * @param resizable if true, the internal frame can be resized.
     * @param closable if true, the internal frame can be closed.
     * @param maximizable if true, the internal frame can be maximized.
     */
    public InternalWindow(String title, boolean resizable, boolean closable,
            boolean maximizable) {

        super(title, resizable, closable, maximizable);
        ToolKit tool = ToolKit.getInstance();
        super.setFrameIcon(tool.createImageIcon(IMAGE_PATH));
    }
    
    /**
     * Creates a JInternalWindow with the specified title, resizability, closability, 
     * maximizability, and iconifiability.
     * 
     * @param title the String to display in the title bar.
     * @param resizable if true, the internal frame can be resized.
     * @param closable if true, the internal frame can be closed.
     * @param maximizable if true, the internal frame can be maximized.
     * @param iconifiable if true, the internal frame can be iconified.
     */
    public InternalWindow(String title, boolean resizable, boolean closable,
            boolean maximizable, boolean iconifiable) {

        super(title, resizable, closable, maximizable, iconifiable);
        ToolKit tool = ToolKit.getInstance();
        super.setFrameIcon(tool.createImageIcon(IMAGE_PATH));
    }
    
    /**
     * Creates InternalWindow based on JInternalFrame instance. The InternalWindow contains 
     * the conmponents of the JInternalFrame. The shifted components are;<br>
     *  resizability, closability, maxmizability, iconifiability, the components of RootPane and Frame Icon. 
     * 
     * @param frame JInternalFrame which has the components to be shifted to InternalWindow.
     */
    public InternalWindow(JInternalFrame frame) {
        super("", frame.isResizable(), frame.isClosable(), frame.isMaximizable(), frame.isIconifiable());
        if (frame != null) {
            if (frame.getTitle() != null) 
                	super.setTitle(frame.getTitle());
            
            super.setRootPane(frame.getRootPane());
            
            if (frame.getFrameIcon() != null) {
                super.setFrameIcon(frame.getFrameIcon());
            } else {
                ToolKit tool = ToolKit.getInstance();
                super.setFrameIcon(tool.createImageIcon(IMAGE_PATH));                
            }
        }        
    }
    
    /**
     * Sets visibility of this InternalWindow. If false is set, the window is removed from the Display area. 
     */
    public final void setVisible(boolean aFlag) {
        if (Application.View != null) {
            if (aFlag) {
                if (this.isVisible()) {
                    this.toFront();
                } else {
                    super.setVisible(aFlag);
                    Application.View.addWindow(this);
                }
                
            } else {
                super.setVisible(aFlag);
                Application.View.removeWindow(this);
            }
        } else {
            super.setVisible(aFlag);
        }
    }
    
    public void toFront() {
        super.toFront();
        try {
            super.setSelected(true);
        } catch (PropertyVetoException e) {
            // do nothing
        }
    }
    
    public final void setTitle(String title) {
        super.setTitle(title);        
        super.firePropertyChange(TITLE_PROPERTY, this, title);
    }
    
    public void pack() {
        super.pack();
        this.packed = true;
    }

    public void updateUI() {
        super.updateUI();
        if (packed) super.pack();
    }
}
