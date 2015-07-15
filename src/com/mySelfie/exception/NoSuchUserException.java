package com.mySelfie.exception;

public class NoSuchUserException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//Parameterless Constructor
    public NoSuchUserException() {}

    //Constructor that accepts a message
    public NoSuchUserException(String message)
    {
       super(message);
    }

}
