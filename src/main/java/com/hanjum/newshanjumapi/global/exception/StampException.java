package com.hanjum.newshanjumapi.global.exception;

import org.chungnamthon.zeroroad.global.exception.dto.ErrorStatus;

public class StampException extends RootException{

    public StampException(ErrorStatus message) {
        super(message);
    }
}
