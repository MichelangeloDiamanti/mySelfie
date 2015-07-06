package com.mySelfie.exception;

public class usernameInUseException extends Exception {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//Parameterless Constructor
    public usernameInUseException() {}

    //Constructor that accepts a message
    public usernameInUseException(String message)
    {
       super(message);
    }
}
