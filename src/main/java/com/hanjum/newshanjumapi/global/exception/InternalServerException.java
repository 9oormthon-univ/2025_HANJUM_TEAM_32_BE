package com.hanjum.newshanjumapi.global.exception;

import org.chungnamthon.zeroroad.global.exception.dto.ErrorStatus;

public class InternalServerException extends RootException {

    public InternalServerException(ErrorStatus message) {
        super(message);
    }

}