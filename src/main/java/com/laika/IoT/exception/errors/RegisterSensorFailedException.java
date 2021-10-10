package com.laika.IoT.exception.errors;


import com.laika.IoT.exception.ErrorCode;

public class RegisterSensorFailedException extends RuntimeException {

    public RegisterSensorFailedException(){
        super(ErrorCode.REGISTER_SENSOR_FAILED.getMessage());
    }

    public RegisterSensorFailedException(Exception ex){
        super(ex);
    }
}