package com.banti.framework.platform;

import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import com.banti.framework.platform.Display.ScrollableDesktopPane;
import com.banti.framework.platform.ViewMediator.DefaultCommand.Print;
import com.banti.framework.platform.ViewMediator.DefaultCommand.PrintPreview;


/**
 * 
 */
final class PrintController {

    private Command print;
    private Command preview;
    
    private ViewMediator view;
    
    /**
     * 
     */
    PrintController(ViewMediator mediator) {
        super();
        view = mediator;
    }
    
    void set(Print print) {
        this.print = print;
    }
    
    void set (PrintPreview preview) {
        this.preview = preview;
    }    
    
    final class PrintMenuArranger implements MenuListener {

        PrintMenuArranger() {
            super();
        }
        
        /* (non-Javadoc)
         * @see javax.swing.event.MenuListener#menuSelected(javax.swing.event.MenuEvent)
         */
        public void menuSelected(MenuEvent e) {
            ScrollableDesktopPane deskTop = view.getCurrentDeskTopPane();
            InternalWindow selectedWindow = deskTop.getSelectedWindow();

            if (selectedWindow == null || !selectedWindow.isSelected()) {
                if (print != null) print.setEnabled(false);
                if (preview != null) preview.setEnabled(false);

                return;
            }
            
            if (selectedWindow instanceof PrintControllable) {
                PrintControllable printable = (PrintControllable) selectedWindow;
                if (print != null) {
                    print.setEnabled(true);
                }
                
                if (preview != null) {
                    if (printable.createPreviewDialog(null) == null) {
                        preview.setEnabled(false);
                    } else {
                        preview.setEnabled(true);
                    }
                }
                
            } else {
                if (print != null) print.setEnabled(false);
                if (preview != null) preview.setEnabled(false);
            }
        }

        /* (non-Javadoc)
         * @see javax.swing.event.MenuListener#menuCanceled(javax.swing.event.MenuEvent)
         */
        public void menuCanceled(MenuEvent e) {
            //  do nothing.            
        }

        /* (non-Javadoc)
         * @see javax.swing.event.MenuListener#menuDeselected(javax.swing.event.MenuEvent)
         */
        public void menuDeselected(MenuEvent e) {
            // do nothing. 
        }

        
    }
}
