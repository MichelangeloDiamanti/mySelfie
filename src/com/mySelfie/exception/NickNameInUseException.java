package com.mySelfie.exception;

public class NickNameInUseException extends Exception {
    //Parameterless Constructor
    public NickNameInUseException() {}

    //Constructor that accepts a message
    public NickNameInUseException(String message)
    {
       super(message);
    }
}
