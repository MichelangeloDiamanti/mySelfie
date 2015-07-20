package com.mySelfie.exception;

public class UsernameInUseException extends Exception {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//Parameterless Constructor
    public UsernameInUseException() {}

    //Constructor that accepts a message
    public UsernameInUseException(String message)
    {
       super(message);
    }
}
