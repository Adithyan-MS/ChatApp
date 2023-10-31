package com.thinkpalm.ChatApplication.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.ResponseEntity.status;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String,String> handleInvalidArguement(MethodArgumentNotValidException ex){
        Map<String,String> errorMap = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(fieldError -> {
            errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
        });
        return errorMap;
    }
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DuplicateEntryException.class)
    public ErrorResponse handleDuplicateEntryException(DuplicateEntryException ex){
        String error = "Duplicate Entry Exception";
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(HttpStatus.CONFLICT);
        errorResponse.setCode(HttpStatus.CONFLICT.value());
        errorResponse.setError(error);
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));
        return errorResponse;
    }
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    public ErrorResponse handleUsernameNotFoundException(UserNotFoundException ex){
        String error = "User Not Found Exception";
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(HttpStatus.NOT_FOUND);
        errorResponse.setCode(HttpStatus.NOT_FOUND.value());
        errorResponse.setError(error);
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));
        return errorResponse;
    }
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(RoomNotFoundException.class)
    public ErrorResponse handleRoomNotFoundException(RoomNotFoundException ex){
        String error = "Room Not Found";
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(HttpStatus.NOT_FOUND);
        errorResponse.setCode(HttpStatus.NOT_FOUND.value());
        errorResponse.setError(error);
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));
        return errorResponse;
    }
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ExceptionHandler(InvalidDataException.class)
    public ErrorResponse handleInvalidDataException(InvalidDataException ex){
        String error = "Invalid Data";
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(HttpStatus.NOT_ACCEPTABLE);
        errorResponse.setCode(HttpStatus.NOT_ACCEPTABLE.value());
        errorResponse.setError(error);
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));
        return errorResponse;
    }
}
