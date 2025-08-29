package com.hanjum.newshanjumapi.global.exception;

import lombok.Getter;
import org.chungnamthon.zeroroad.global.exception.dto.ErrorStatus;

@Getter
public abstract class RootException extends RuntimeException {

    private final ErrorStatus errorStatus;

    protected RootException(ErrorStatus message) {
        super(message.getMessage());
        this.errorStatus = message;
    }

}