package com.example.task.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.task.model.result.ResultCode;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BaseException extends RuntimeException 
{
    Logger logger = LoggerFactory.getLogger(BaseException.class);
    private static final long serialVersionUID = 1L;

    private ResultCode resultCode = ResultCode.FAILED;

    public BaseException(ResultCode resultCode, String msg) {
        super(msg);        
        this.resultCode = resultCode;
    }

    public ResultCode getResultCode() {
        return resultCode;
    }
}