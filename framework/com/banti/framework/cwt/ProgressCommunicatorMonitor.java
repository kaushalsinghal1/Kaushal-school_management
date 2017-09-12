package com.banti.framework.cwt;

import java.awt.Dialog;
import java.awt.Frame;

import com.banti.framework.platform.Application;

public class ProgressCommunicatorMonitor extends ProgressStatusBar {
    private ProgressCommunicator communicator;

    public ProgressCommunicatorMonitor(
        String message,
        boolean modal,
        ProgressCommunicator communicator) {
        this(Application.Frame, message, modal, communicator);
    }

    public ProgressCommunicatorMonitor(
        Frame frame,
        String message,
        boolean modal,
        ProgressCommunicator communicator) {
        super(frame, message, modal);
        setCancelEnabled(true);
        this.communicator = communicator;
    }
    
    public ProgressCommunicatorMonitor(
            Dialog dialog,
            String message,
            boolean modal,
            ProgressCommunicator communicator) {
            super(dialog, message, modal);
            setCancelEnabled(true);
            this.communicator = communicator;
        }

    @Override
    protected void doPreProcess() {
        dialog.setLocation(getPreferredLocation());

    }

    @Override
    protected void doPostProcess() {
        communicator.doPostProcess();

    }
    @Override
    protected void doCancel() {
    	communicator.doCancel();
    }

    public void start() {
        super.start(communicator);
        // start(commObject, 90, 2);
    }

    @Override
    protected void doFinally() {

    }
}
