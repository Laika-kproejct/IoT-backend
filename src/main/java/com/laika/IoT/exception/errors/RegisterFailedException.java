package com.laika.IoT.exception.errors;

import com.laika.IoT.exception.ErrorCode;

public class RegisterFailedException extends RuntimeException {

    public RegisterFailedException(){
        super(ErrorCode.REGISTER_FAILED.getMessage());
    }

    public RegisterFailedException(Exception ex){
        super(ex);
    }
}