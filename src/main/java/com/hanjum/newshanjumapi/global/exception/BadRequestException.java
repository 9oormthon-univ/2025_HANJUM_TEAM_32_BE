package com.hanjum.newshanjumapi.global.exception;

import com.hanjum.newshanjumapi.global.exception.dto.ErrorStatus;

public class BadRequestException extends RootException {

    public BadRequestException(ErrorStatus message) {
        super(message);
    }

}