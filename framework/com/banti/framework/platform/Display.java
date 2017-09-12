package com.banti.framework.platform;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.beans.PropertyVetoException;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.DefaultDesktopManager;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JViewport;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.banti.framework.theme.ToolKit;

/**
 * TODO 
 */
final class Display implements ViewColleague, ChangeListener {

    private int OPEN_FRAME_COUNT = 0;
    private int X_OFFSET = 50;
    private int Y_OFFSET = 50;

    private JComponent screen;
    private ScrollableDesktopPane currentDeskTop;
    private HashMap<String, JComponent> desktopList;
    private JTabbedPane tab;

    private ViewMediator view;

    Display(ViewMediator mediator) {
        view = mediator;
        currentDeskTop = new ScrollableDesktopPane();
        screen = currentDeskTop;
        desktopList = new HashMap<String, JComponent>();
        desktopList.put(ViewMediator.TopScreenTitle, screen);
        tab = null;
    }

    JComponent Screen() {
        return screen;
    }

    ScrollableDesktopPane getCurrentDeskTopPane() {
        return currentDeskTop;
    }

    int getCurrentDeskTopIndex() {
        if (tab == null)
            return 0;

        return tab.getSelectedIndex();
    }

    String getCurrentDeskTopTitle() {
        if (tab == null || tab.getSelectedIndex() == 0)
            return ViewMediator.TopScreenTitle;

        return tab.getTitleAt(tab.getSelectedIndex());
    }

    void repaint() {
        screen.repaint();
    }

    void setOffset(int x, int y) {
        if (x >= 0)
            X_OFFSET = x;
        if (y >= 0)
            Y_OFFSET = y;
    }

    public void windowOpened(InternalWindow window) {
        javax.swing.SwingUtilities.updateComponentTreeUI(window);
        currentDeskTop.add(window);

        Point p = window.getLocation();
        if (p.x <= 0 && p.y <= 0) {
            window.setLocation(X_OFFSET * OPEN_FRAME_COUNT, Y_OFFSET * OPEN_FRAME_COUNT);

            OPEN_FRAME_COUNT++;
            if (OPEN_FRAME_COUNT > 2)
                OPEN_FRAME_COUNT = 0;
        }

        try {
            window.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {
            return;
        }
    }

    public void windowClosed(InternalWindow window) {
        if (window == null)
            return;

        currentDeskTop.remove(window);
    }

    public void windowSelected(InternalWindow window) {
        // do nothing
        return;
    }

    public void tabCreated(String title) {
        if (tab == null) {
            tab = new JTabbedPane(SwingConstants.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);

            tab.add(ToolKit.getString("MAIN"), currentDeskTop);
            screen = tab;
        }

        if (desktopList.containsKey(title)) {
            this.tabChanged(title);
            return;
        }
        tab.removeChangeListener(this);
        ScrollableDesktopPane newDesktop = new ScrollableDesktopPane();
        newDesktop.setDragMode(currentDeskTop.getDragMode());
        desktopList.put(title, newDesktop);
        tab.add(title, newDesktop);
        currentDeskTop = newDesktop;

        tab.setSelectedComponent(currentDeskTop);
        tab.addChangeListener(this);

        tab.setVisible(true);
    }

    /* (non-Javadoc)
     * @see com.cysols.framework.platform.ViewColleague#tabClosed(int)
     */
    public void tabClosed(String title) {
        InternalWindow[] windows = currentDeskTop.getAllInternalWindows();
        for (int i = 0; i < windows.length; i++) {
            if (windows[i] != null) {
                windows[i].dispose();
            }
        }

        if (tab == null)
            return;
        if (title == null)
            return;

        int selectedIndex = tab.getSelectedIndex();
        if (selectedIndex != 0 && !title.equals(tab.getTitleAt(selectedIndex)))
            return;

        tab.removeChangeListener(this);

        desktopList.remove(title);
        tab.remove(selectedIndex);

        selectedIndex = tab.getSelectedIndex();
        currentDeskTop = (ScrollableDesktopPane) desktopList.get(tab.getTitleAt(selectedIndex));
        if (currentDeskTop == null)
            currentDeskTop = (ScrollableDesktopPane) desktopList.get(ViewMediator.TopScreenTitle);

        if (desktopList.size() == 1) {
            tab.removeAll();
            tab = null;
            screen = currentDeskTop;

            desktopList.clear();
            desktopList.put(ViewMediator.TopScreenTitle, currentDeskTop);
        } else {
            tab.addChangeListener(this);
        }
    }

    public void tabChanged(String title) {
        if (!desktopList.containsKey(title))
            return;

        currentDeskTop = (ScrollableDesktopPane) desktopList.get(title);
        if (tab != null && currentDeskTop != tab.getSelectedComponent()) {
            tab.setSelectedComponent(currentDeskTop);
        }

        try {
            if (currentDeskTop.getSelectedWindow() != null)
                currentDeskTop.getSelectedWindow().setSelected(false);
        } catch (PropertyVetoException pe) {
            // ignore
        }
        OPEN_FRAME_COUNT = 0;
    }

    public void show() {
        currentDeskTop.updateUI();
        currentDeskTop.setVisible(true);
    }

    public void stateChanged(ChangeEvent e) {
        if (e.getSource() instanceof JTabbedPane) {
            if (tab == null)
                return;

            String title = this.getCurrentDeskTopTitle();

            view.changeTab(title);
        }
    }

    int getDesktopCount() {
        return desktopList.size();
    }

    void setDragMode(boolean mode) {
        if (tab == null) {
            if (mode) {
                currentDeskTop.setDragMode(javax.swing.JDesktopPane.LIVE_DRAG_MODE);
            } else {
                currentDeskTop.setDragMode(javax.swing.JDesktopPane.OUTLINE_DRAG_MODE);
            }
        } else {
            ScrollableDesktopPane pane = null;
            Iterator itr = desktopList.values().iterator();
            while (itr.hasNext()) {
                pane = (ScrollableDesktopPane) itr.next();
                if (mode) {
                    pane.setDragMode(javax.swing.JDesktopPane.LIVE_DRAG_MODE);
                } else {
                    pane.setDragMode(javax.swing.JDesktopPane.OUTLINE_DRAG_MODE);
                }
            }
        }
    }

    /**
     * This is an inner class which can show scroll bars according to Internal Window
     * movement. Scrollable DeskTop Pane controls its scroll bars when Internal Window 
     * is moved. And it informs ViewMediator of Internal Frame activation in the desktop.
     */
    class ScrollableDesktopPane extends JPanel {
        private JScrollPane scrollPane;
        private JDesktopPane desktopPane;
        private InnerDesktopManager desktopManager;
        /**
         * A constructor.
         */
        ScrollableDesktopPane() {
            super(new BorderLayout());

            desktopPane = new JDesktopPane();
            scrollPane = new JScrollPane(desktopPane);
            super.add(scrollPane, BorderLayout.CENTER);

            super.setVisible(false);
            
            desktopManager = new InnerDesktopManager();
            desktopPane.setDesktopManager(desktopManager);
        }

        InternalWindow add(InternalWindow window) {
            InternalWindow win = (InternalWindow) desktopPane.add(window);
            win.addComponentListener(desktopManager);
            return win;
        }

        void remove(InternalWindow window) {
            window.removeComponentListener(desktopManager);
            desktopPane.remove(window);
        }

        void setDragMode(int dragMode) {
            desktopPane.setDragMode(dragMode);
        }

        int getDragMode() {
            return desktopPane.getDragMode();
        }

        InternalWindow[] getAllInternalWindows() {
            javax.swing.JInternalFrame[] frames = desktopPane.getAllFrames();
            InternalWindow[] windows = new InternalWindow[frames.length];
            for (int i = 0; i < frames.length; i++) {
                try {
                    windows[i] = (InternalWindow) frames[i];
                } catch (ClassCastException e) {
                    continue;
                }
            }

            return windows;
        }

        int getWindowCount() {
            InternalWindow[] wins = this.getAllInternalWindows();
            return wins.length;
        }

        InternalWindow getSelectedWindow() {
            if (desktopPane.getSelectedFrame() instanceof InternalWindow) {
                return (InternalWindow) desktopPane.getSelectedFrame();
            }

            return null;
        }

        /**
         * This is a private inner class. This works for managing Internal Window
         * in the ScrollableDesktopPane. This class fires to resize or to notify 
         * window focus to ViewMediator through watching the action of Internal
         * Window.
         */
        private class InnerDesktopManager extends DefaultDesktopManager implements ComponentListener {

            InnerDesktopManager() {
                Application.Frame.addComponentListener(this);
            }

            public void openFrame(JInternalFrame f) {
                super.openFrame(f);
                this.resize();
            }

            public void activateFrame(javax.swing.JInternalFrame f) {
                super.activateFrame(f);
                try {
                    view.selectWindow((InternalWindow) f);
                } catch (ClassCastException e) {
                    // ignore
                }
                this.resize();
            }

            /* (non-Javadoc)
             * @see javax.swing.DesktopManager#closeFrame(javax.swing.JInternalFrame)
             */
            public void closeFrame(JInternalFrame f) {
                super.closeFrame(f);
                this.resize();
            }

            public void endDraggingFrame(JComponent f) {
                super.endDraggingFrame(f);
                this.resize();
            }

            public void deactivateFrame(JInternalFrame f) {
                super.deactivateFrame(f);
                this.resize();
            }

            public void deiconifyFrame(JInternalFrame f) {
                super.deiconifyFrame(f);
                this.resize();
            }

            public void iconifyFrame(JInternalFrame f) {
                super.iconifyFrame(f);
                this.resize();
            }

            public void maximizeFrame(JInternalFrame f) {
                super.maximizeFrame(f);
                this.resize();
            }

            public void minimizeFrame(JInternalFrame f) {
                super.minimizeFrame(f);
                this.resize();
            }

            public void componentResized(ComponentEvent e) {
                this.resize();
            }

            public void componentMoved(ComponentEvent e) {
            }

            public void componentShown(ComponentEvent e) {
            }

            public void componentHidden(ComponentEvent e) {
                this.resize();
            }

            /*
             * This method is a core resizing logic. ScrollableDesktopPane can
             * repaint a new scroll bars through a calculation of this method.
             */
            private void resize() {
                javax.swing.SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        Rectangle rec = scrollPane.getViewport().getViewRect();
                        int maxXP = rec.width + rec.x;
                        int minXP = rec.x;
                        int maxYP = rec.height + rec.y;
                        int minYP = rec.y;

                        javax.swing.JInternalFrame[] frames = desktopPane.getAllFrames();
                        for (int i = 0; i < frames.length; i++) {
                            javax.swing.JInternalFrame frame = frames[i];
                            JComponent com = frame;
                            if (frame.isIcon()) {
                                com = frame.getDesktopIcon();
                            } else if (frame.isMaximum()) {
                                continue;
                            }
                            minXP = com.getX() < minXP ? com.getX() : minXP;
                            maxXP = (com.getX() + com.getWidth()) > maxXP ? com.getX()
                                + com.getWidth() : maxXP;

                            minYP = com.getY() < minYP ? com.getY() : minYP;
                            maxYP = (com.getY() + com.getHeight()) > maxYP ? com.getY()
                                + com.getHeight() : maxYP;
                        }

                        desktopPane.setVisible(false);

                        if (minXP != 0 || minYP != 0) {
                            for (int i = 0; i < frames.length; i++) {
                                javax.swing.JInternalFrame frame = frames[i];
                                if (frame.isIcon()) {
                                } else {
                                    frame.setLocation(frame.getX() - minXP, frame.getY() - minYP);
                                }
                            }

                            JViewport viewPort = scrollPane.getViewport();
                            viewPort.setViewSize(new Dimension(maxXP - minXP, maxYP - minYP));
                            viewPort.setViewPosition(new Point(rec.x - minXP, rec.y - minYP));
                            scrollPane.setViewport(viewPort);
                        }
                        Dimension dim = new Dimension(maxXP - minXP, maxYP - minYP);
                        for (int i = 0; i < frames.length; i++) {
                            javax.swing.JInternalFrame frame = frames[i];
                            if (frame.isMaximum()) {
                                frame.setPreferredSize(dim);
                            }
                        }

                        desktopPane.setPreferredSize(dim);
                        desktopPane.revalidate();
                        desktopPane.setVisible(true);
                    }
                });
            }

        }
    }

}