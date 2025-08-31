package com.hanjum.newshanjumapi.global.exception;


import com.hanjum.newshanjumapi.global.exception.dto.ErrorStatus;

public class InternalServerException extends RootException {

    public InternalServerException(ErrorStatus message) {
        super(message);
    }

}