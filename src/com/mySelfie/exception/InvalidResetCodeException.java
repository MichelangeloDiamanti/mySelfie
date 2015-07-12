package com.mySelfie.exception;

public class InvalidResetCodeException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//Parameterless Constructor
    public InvalidResetCodeException() {}

    //Constructor that accepts a message
    public InvalidResetCodeException(String message)
    {
       super(message);
    }
}
