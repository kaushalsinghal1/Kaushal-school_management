package com.banti.framework.platform;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JToolBar;

import com.banti.framework.platform.ViewMediator.DefaultCommand.Switch;
import com.banti.framework.platform.ViewMediator.DefaultCommand.WindowEntry;
import com.banti.framework.theme.ToolKit;

/**
 * 
 */
final class WindowBar implements ViewColleague {
    
    private JToolBar windowBar;

    private ViewMediator view;
    private HashMap windowLists;
    
    private PropertyChangeListener listener;
    
    WindowBar(ViewMediator mediator) {
        view = mediator;

        windowBar = new JToolBar();
        ToolKit tool = ToolKit.getInstance();
        windowBar.setToolTipText(tool.getString("WINDOW_BAR_TOOL_TIP"));
        windowBar.setVisible(false);
        windowBar.setFloatable(false);
        windowBar.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

        windowLists = new HashMap();
        windowLists.put(ViewMediator.TopScreenTitle, new InnerWindowButtonList());
    
        listener = new InnerPropertyChangeListener();
    }

    public void windowClosed(InternalWindow window) {
        String title = view.getCurrentDeskTopTitle();

        if (windowLists.get(title) == null) return;
    
        InnerWindowButtonList list = (InnerWindowButtonList) windowLists.get(title);
    
        if (!list.contains(window))
            return;
    
        InnerToggleButton button = list.getButton(window);
        windowBar.remove(button);
        list.remove(window);
    
        list.changeSelections(window);
    
    }

    public void windowOpened(final InternalWindow window) {
        // For title changed
        window.addPropertyChangeListener(listener);
        
        // Adding a button on window bar
        String title = view.getCurrentDeskTopTitle();
    
        if (windowLists.get(title) == null)
            windowLists.put(title, new InnerWindowButtonList());
    
        InnerWindowButtonList list = (InnerWindowButtonList) windowLists.get(title);
    
        WindowEntry entry = view.CommandBuilder.createWindowEntry(window);
        InnerToggleButton button = new InnerToggleButton(entry.getRuntimeAction());
        button.setText(null);
        button.setToolTipText(window.getTitle());        
        button.setIcon(window.getFrameIcon());
        button.setMargin(new Insets(0, 0, 0, 0));
    
        if (!list.contains(window)) {
            windowBar.add(button);
            list.add(window, button);
        }
    
        list.changeSelections(window);
        window.validate();
        windowBar.repaint();
    }
    
    public void windowSelected(InternalWindow window) {
        String title = view.getCurrentDeskTopTitle();
        this.activateSelections(title, window);                
    }
    
    public void tabCreated(String title) {
        windowLists.put(title, new InnerWindowButtonList());
    }
        
    public void tabClosed(String title) {
        windowLists.remove(title);        
    }
    
    public void tabChanged(String title) {
        this.changeWindowButtons(title);
        this.activateSelections(title, null);
    }

    public void show() {
        Switch barSwitch = view.CommandBuilder.getWindowBarSwitch();
        if (barSwitch != null) {
            windowBar.setVisible(barSwitch.isSelected());
        } else {
            windowBar.setVisible(true);
        }
    }

    void repaint() {
        windowBar.repaint();
    }
    
    JToolBar Bar() {
        return windowBar;
    }
    
    private void activateSelections(String title, InternalWindow window) {

        if (windowLists.get(title) == null) return;

        InnerWindowButtonList list = (InnerWindowButtonList) windowLists.get(title);
        list.changeSelections(window);        
    }
    
    private void changeWindowButtons(String title) {
        windowBar.removeAll();

        if (windowLists.get(title) == null) return;


        // If there are some window in the new selected tab,
        // InnerWindowButtonList of the
        // selected index are obtained and those window menu items are
        // re-registered in
        // the window menu.
        InnerWindowButtonList list = (InnerWindowButtonList) windowLists.get(title);
        InnerToggleButton[] buttons = list.getButtons();

        if (buttons.length == 0)
            return;

        for (int i = 0; i < buttons.length; i++) {
            windowBar.add(buttons[i]);
        }
    }

    
    
    //-----------------------------Inner Classes ------------------------------------------------------------//

    //-----------------------------InnerWindowButtonList-------------------------------------------------//
    /**
     * This is a private inner class, is a ValueObject and keeps the pairs of
     * InternalWindow instances and its window button in the status bar.
     */
    private class InnerWindowButtonList {
        private ArrayList winList;

        private HashMap winButtons;

        InnerWindowButtonList() {
            winList = new ArrayList();
            winButtons = new HashMap();
        }

        void add(InternalWindow window, InnerToggleButton button) {
            winList.add(window);
            winButtons.put(window, button);
        }

        boolean contains(InternalWindow window) {
            return winList.contains(window);
        }

        InnerToggleButton getButton(InternalWindow window) {
            return (InnerToggleButton) winButtons.get(window);
        }

        void remove(InternalWindow window) {
            winButtons.remove(window);
            winList.remove(window);
        }

        InnerToggleButton[] getButtons() {
            InnerToggleButton[] buttons = new InnerToggleButton[winList.size()];
            for (int i = 0; i < winList.size(); i++) {
                buttons[i] = (InnerToggleButton) winButtons.get(winList.get(i));
            }

            return buttons;
        }

        void changeSelections(InternalWindow window) {
            for (int i = 0; i < winList.size(); i++) {
                InternalWindow win = (InternalWindow) winList.get(i);
                InnerToggleButton button = (InnerToggleButton) winButtons.get(win);
                if (win == window) {
                    button.setSelected(true);
                } else {
                    button.setSelected(false);
                }
            }
        }
    }


    //-----------------------------InnerToggledButton-----------------------------------------------------//
    private class InnerToggleButton extends javax.swing.JToggleButton  
    		implements PropertyChangeListener, ActionListener {
        
        private Command.RuntimeAction action;        
        InnerToggleButton(Command.RuntimeAction action) {
            super(action);
            this.action = action;
            super.setVisible(action.isVisible());
            super.addActionListener(this);
            action.addPropertyChangeListener(this);
        }
        
        public void setSelected(boolean aFlag) {
            super.setSelected(aFlag);
            if (action != null) action.setSelected(aFlag);
        }
        
        public void actionPerformed(ActionEvent ae) {
            if (!super.isSelected()) {
                super.setSelected(true);
            }
        }
    
        public void propertyChange(PropertyChangeEvent e) {
            super.setEnabled(action.isEnabled());
            super.setVisible(action.isVisible());
            super.setSelected(action.isSelected());
        }
    }

    //  -----------------------------InnerPropertyChangeListener-------------------------------------------//
	private class InnerPropertyChangeListener implements PropertyChangeListener {
        /* (non-Javadoc)
         * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
         */
        public void propertyChange(PropertyChangeEvent pce) {
            if (pce == null) return;
            
            if (pce.getPropertyName().equals(InternalWindow.TITLE_PROPERTY)) {
                try {
                    InternalWindow window = (InternalWindow) pce.getSource();
                    String newTitle = (String) pce.getNewValue();
                    
                    String title = view.getCurrentDeskTopTitle();
                    if (windowLists.get(title) == null) return;
                
                    InnerWindowButtonList list = (InnerWindowButtonList) windowLists.get(title);
                
                    if (!list.contains(window))
                        return;
                
                    InnerToggleButton button = list.getButton(window);
                    button.setToolTipText(newTitle);
                    
                } catch (ClassCastException e) {
                    return;
                }
            }
        }
	}
}
