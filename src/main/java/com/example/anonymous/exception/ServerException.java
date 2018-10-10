package com.example.anonymous.exception;

public class ServerException extends RuntimeException{
    public ServerException()
    {
        super();
    }
    public ServerException(String msg)
    {
        super(msg);
    }
}
