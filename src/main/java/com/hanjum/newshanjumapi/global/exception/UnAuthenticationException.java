package com.hanjum.newshanjumapi.global.exception;

import org.chungnamthon.zeroroad.global.exception.dto.ErrorStatus;

public class UnAuthenticationException extends RootException {

    public UnAuthenticationException(ErrorStatus message) {
        super(message);
    }

}