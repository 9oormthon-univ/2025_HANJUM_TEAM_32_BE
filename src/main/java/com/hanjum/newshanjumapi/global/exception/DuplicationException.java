package com.hanjum.newshanjumapi.global.exception;


import com.hanjum.newshanjumapi.global.exception.dto.ErrorStatus;

public class DuplicationException extends RootException {

    public DuplicationException(ErrorStatus message) {
        super(message);
    }

}