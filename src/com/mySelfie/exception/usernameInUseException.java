package com.mySelfie.exception;

public class usernameInUseException extends Exception {
    //Parameterless Constructor
    public usernameInUseException() {}

    //Constructor that accepts a message
    public usernameInUseException(String message)
    {
       super(message);
    }
}
