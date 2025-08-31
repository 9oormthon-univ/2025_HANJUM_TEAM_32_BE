package com.hanjum.newshanjumapi.global.exception;

import com.hanjum.newshanjumapi.global.exception.dto.ErrorStatus;
import lombok.Getter;

@Getter
public abstract class RootException extends RuntimeException {

    private final ErrorStatus errorStatus;

    protected RootException(ErrorStatus message) {
        super(message.getMessage());
        this.errorStatus = message;
    }

}