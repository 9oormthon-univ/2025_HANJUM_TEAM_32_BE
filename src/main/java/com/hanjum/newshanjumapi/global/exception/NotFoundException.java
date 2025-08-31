package com.hanjum.newshanjumapi.global.exception;


import com.hanjum.newshanjumapi.global.exception.dto.ErrorStatus;

public class NotFoundException extends RootException {

    public NotFoundException(ErrorStatus message) {
        super(message);
    }

}