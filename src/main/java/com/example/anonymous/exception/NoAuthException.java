package com.example.anonymous.exception;

public class NoAuthException extends InvalidInputException{
    public NoAuthException()
    {
        super();
    }
    public NoAuthException(String msg)
    {
        super(msg);
    }
}
