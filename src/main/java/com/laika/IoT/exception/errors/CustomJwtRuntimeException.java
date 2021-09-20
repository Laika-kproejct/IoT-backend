package com.laika.IoT.exception.errors;

import com.laika.IoT.exception.ErrorCode;

public class CustomJwtRuntimeException extends RuntimeException {

    public CustomJwtRuntimeException(){
        super(ErrorCode.AUTHENTICATION_FAILED.getMessage());
    }

    public CustomJwtRuntimeException(Exception ex){
        super(ex);
    }
}