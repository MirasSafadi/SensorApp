package com.example.designpatternsproject.boundary.Control;

public class SensorNotAvailableException extends Exception {
    public SensorNotAvailableException(String errMsg, Throwable throwable){
        super(errMsg,throwable);
    }
}
