package com.example.task.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.task.model.result.ResultCode;
import com.example.task.model.result.ResultValue;

import jakarta.validation.UnexpectedTypeException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResultValue> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> 
            errors.put(error.getField(), error.getDefaultMessage()));

        return new ResponseEntity<>(new ResultValue(ResultCode.INVALID_INPUT, errors), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnexpectedTypeException.class)
    public ResponseEntity<ResultValue> handleUnexpectedTypeException(UnexpectedTypeException ex) {
        // UnexpectedTypeException 발생 시 사용자에게 반환할 메시지 생성
        Map<String, String> error = new HashMap<>();
        error.put("error", "유효하지 않은 타입입니다. 요청 값의 타입을 확인해 주세요.");

        return new ResponseEntity<>(new ResultValue(ResultCode.INVALID_INPUT, error), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ResultValue> handleBaseException(BaseException ex) {
        return new ResponseEntity<>(new ResultValue(ex.getResultCode(), ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResultValue> handleGenericException(Exception ex) {
        return new ResponseEntity<>(new ResultValue(ResultCode.INTERNAL_SERVER_ERROR, "서버 내부 오류 발생"), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}