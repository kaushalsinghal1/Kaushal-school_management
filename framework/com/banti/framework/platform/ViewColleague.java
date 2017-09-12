package com.banti.framework.platform;


/**
 * This is an interface to be controlled by ViewMediator. ViewMediator can manage
 * and controll view components through this interface.
 */
public interface ViewColleague {
    
    public String TopScreenTitle = "MAIN";
    
    /**
     * This method is called by ViewMediator when a new window is opened.
     * 
     * @param window - a new opened InternalWindow.
     */
    public void windowOpened(InternalWindow window);
    
    /**
     * This method is called by ViewMediator when a window is closed.
     * 
     * @param window - an InternalWindow to be closed.
     */
    public void windowClosed(InternalWindow window);
    
    /**
     * This method is called by ViewMediator when a window is selected.
     * 
     * @param window - a selected InternalWindow  
     */
    public void windowSelected(InternalWindow window);
    
    /**
     * This method is called by ViewMediator when a new tab is created.
     * 
     * @param title  a title of the created tab pane.
     */
    public void tabCreated(String title);
    
    /**
     * This method is called by ViewMediator when a tab is closed.
     * 
     * @param title a title of the closed tab pane.
     */
    public void tabClosed(String title);
    
    /**
     * This method is called by ViewMediator when another tab is selected. <br>
     * NOTE: This method is also called when a new tab is created and when a tab is closed. 
     * Then, the selected index is an index of the new selected tab or of the biggest index among
     * the tabs.<br> 
     * In addition, zero is used when there are two tabs and one of them is closed. 
     * 
     * @param title a title of the selected tab pane.
     */
    public void tabChanged(String title);
    
    /**
     * This method is called by ViewMediator when it shows all componets.
     *
     */
    public void show();

}
