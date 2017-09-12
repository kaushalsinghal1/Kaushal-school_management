package com.banti.framework.cwt;

public interface ProgressCommunicator extends Runnable {
    public void doPostProcess();
    public void doCancel();

}
