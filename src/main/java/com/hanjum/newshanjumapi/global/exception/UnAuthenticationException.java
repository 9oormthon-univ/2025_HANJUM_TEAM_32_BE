package com.hanjum.newshanjumapi.global.exception;


import com.hanjum.newshanjumapi.global.exception.dto.ErrorStatus;

public class UnAuthenticationException extends RootException {

    public UnAuthenticationException(ErrorStatus message) {
        super(message);
    }

}