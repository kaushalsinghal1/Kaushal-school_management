package com.banti.framework.logging;

public interface PostLoggingListener {

    /**
     * This method is called after ActionLog / OperationLog is written.
     * @param str
     */
    public void postLoggingProcess(String str);

    /**
     * Returns unique name of PostLoggingListener.
     * This name is used as "Key" of HashMap for listener management.
     * @return String - Name of this listener
     */
    public String getListenerName();

}
