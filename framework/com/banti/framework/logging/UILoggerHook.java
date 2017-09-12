package com.banti.framework.logging;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JList;
import javax.swing.ListModel;
import javax.swing.event.ListDataListener;

/**
 * This class is the implementation for the LoggerHok, which is having a JList.
 * The JList in this class will have the latest <buffer>message which where
 * sent to event log. using getLog() method the JList component can be
 * received.
 */

//########################################################
//  Please take care you no log messages are added here //
//  in this class, It may end up in Recursive calls :) //
//########################################################
public class UILoggerHook implements LoggerHook, ListModel {

    private JList log = new JList();
    private List data = new ArrayList(buffer + 1);

    /**
     * Implementation of LoggerHokk interface method. Checks if the messages in
     * the list excedes the buffer size, if so removes the first(old) message
     * from the list. And appends the list with the new message.
     */
    public void append(String message) {
        if (data.size() > buffer) {
            data.remove(0);
        }
        data.add(message);
    }

    /** 
     * Implementaion of ListModel method.
     */
    public Object getElementAt(int count) {
        return data.get(count);
    }

    /** 
     * Implementaion of ListModel method. 
     */
    public void addListDataListener(ListDataListener arg0) {
    }

    /** 
     * Implementaion of ListModel method. 
     */
    public void removeListDataListener(ListDataListener arg0) {
    }

    /**
     * Implementaion of ListModel method. 
     */
    public int getSize() {
        return data.size();
    }

    /**
     * Gest the instance of the JList which will display the messages
     * 
     * @return
     */
    public JList getLog() {
        return log;
    }
}
