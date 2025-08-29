package com.hanjum.newshanjumapi.global.exception;

import org.chungnamthon.zeroroad.global.exception.dto.ErrorStatus;

public class NotFoundException extends RootException {

    public NotFoundException(ErrorStatus message) {
        super(message);
    }

}