package com.banti.framework.cwt.progressdialog;

public interface MonitoredProcess {

    /**
     * Defines pre process.
     * @return
     */
    boolean doPreProcess();

    /**
     * Defines main process.
     * @return
     */
    Object doMainProcess();

    void doPostProcess(Object result);

    /**
     * This method is called after doMainProcess.
     */
    void doFinally();

    /**
     * Starts monitor process.
     */
    void start();
}
