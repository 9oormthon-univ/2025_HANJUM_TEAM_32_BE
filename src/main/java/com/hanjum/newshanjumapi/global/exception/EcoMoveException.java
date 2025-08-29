package com.hanjum.newshanjumapi.global.exception;

import org.chungnamthon.zeroroad.global.exception.dto.ErrorStatus;

public class EcoMoveException extends RootException {

    public EcoMoveException(ErrorStatus message) {super(message);}
}