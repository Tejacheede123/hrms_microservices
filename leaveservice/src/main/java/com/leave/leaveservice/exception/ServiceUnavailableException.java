package com.leave.leaveservice.exception;

public class ServiceUnavailableException  extends RuntimeException{
    public ServiceUnavailableException(String message) {
        super(message);
    }

}
