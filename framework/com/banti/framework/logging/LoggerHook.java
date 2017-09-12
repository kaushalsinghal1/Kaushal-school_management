package com.banti.framework.logging;

/**
 * Interface which can be impelmented and can be added to the logger for geting
 * the copy of eventlog's copy.
 */
public interface LoggerHook {

  int buffer = 100;

  /**
	 * This method is called when the event log message is generated with the
	 * message as the param A simple example for the implementation:
	 * <p>
	 * <code><pre>
	     public void append(String message) {
        if (data.size() > buffer) {
          data.remove(0); 
        }
        data.add(message);
       } 
   * </pre></code>
	 */
  void append(String message);

}
