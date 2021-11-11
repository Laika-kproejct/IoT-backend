package com.laika.IoT.exception.errors;

import com.laika.IoT.exception.ErrorCode;

public class NotFoundSensorException extends RuntimeException {

    public NotFoundSensorException(){
        super(ErrorCode.NOT_FOUND_SENSOR.getMessage());
    }

    public NotFoundSensorException(Exception ex){
        super(ex);
    }
}
