package com.example.BapZip.apiPayload.exception.handler;


import com.example.BapZip.apiPayload.code.BaseErrorCode;
import com.example.BapZip.apiPayload.exception.GeneralException;

public class TempHandler extends GeneralException {

    public TempHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}