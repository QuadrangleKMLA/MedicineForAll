package com.quadrangle.capstone.pill_navigator.exceptions.exceptionHandler;

import com.quadrangle.capstone.pill_navigator.exceptions.ErrorMessage;
import com.quadrangle.capstone.pill_navigator.exceptions.exceptionClass.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@RestControllerAdvice
public class ResourceNotFoundExceptionHandler {
    @ExceptionHandler(value = ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage resourceNotFoundExceptionHandler(ResourceNotFoundException e, WebRequest request) {
        return new ErrorMessage(HttpStatus.NOT_FOUND.value(), new Date(), e.getMessage(), request.getDescription(false));
    }
}
