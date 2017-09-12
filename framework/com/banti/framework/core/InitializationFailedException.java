package com.banti.framework.core;

/**
 * This exception means an initializatoin failure.
 */
public class InitializationFailedException extends Exception {

    /**
     * A default constructor. 
     */
    public InitializationFailedException() {
        super();
    }

    /**
     * A constructor with a message.
     * 
     * @param message - an exceptional message. 
     */
    public InitializationFailedException(String message) {
        super(message);
    }

    /**
     * A constructor with its cause.
     * 
     * @param cause - Throwable instance of the cause of initialization failure
     */
    public InitializationFailedException(Throwable cause) {
        super(cause);
    }

    /**
     * A constructor includes its message and its cause of failure.
     * 
     * @param message - an exceptional message.
     * @param cause - Throwable instance of the cause of initialization failure.
     */
    public InitializationFailedException(String message, Throwable cause) {
        super(message, cause);
    }

}
