package com.example.SensorApp.boundary.Control;
/*
this class is a custom exception
 */
public class SensorNotAvailableException extends Exception {
    public SensorNotAvailableException(String errMsg, Throwable throwable){
        super(errMsg,throwable);
    }
}
