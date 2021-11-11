package com.laika.IoT.exception.errors;

import com.laika.IoT.exception.ErrorCode;

public class NotFoundHomeException extends RuntimeException {

    public NotFoundHomeException(){
        super(ErrorCode.NOT_FOUND_HOME.getMessage());
    }
    public NotFoundHomeException(Exception ex){
        super(ex);
    }
}