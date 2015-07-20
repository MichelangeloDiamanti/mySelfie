package com.mySelfie.exception;

public class EmailInUseException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//Parameterless Constructor
    public EmailInUseException() {}

    //Constructor that accepts a message
    public EmailInUseException(String message)
    {
       super(message);
    }

}
