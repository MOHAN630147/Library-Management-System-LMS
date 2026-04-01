package com.hexaware.lms.exception;

import java.util.HashMap;
import java.util.Map;

import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<Map<String,String>> handleResourceNotFound(ResourceNotFoundException ex){

	    log.error("Resource not found: {}", ex.getMessage());

	    Map<String,String> error = new HashMap<>();
	    error.put("message", ex.getMessage());

	    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String,String> handleBadRequest(BadRequestException ex){

        Map<String,String> error = new HashMap<>();
        error.put("error Bad request", ex.getMessage());

        return error;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String,String> handleGeneralException(Exception ex){
        log.error("An unexpected error occurred: ", ex);
        Map<String,String> error = new HashMap<>();
        error.put("error","Something went wrong: " + ex.getMessage());
        return error;
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String,String> handleThrowable(Throwable t){
        log.error("A critical error occurred: ", t);
        Map<String,String> error = new HashMap<>();
        error.put("error","A system error occurred. Please check logs.");
        return error;
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String,String> handleValidationException(MethodArgumentNotValidException ex){

        Map<String,String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });

        return errors;
    }
}