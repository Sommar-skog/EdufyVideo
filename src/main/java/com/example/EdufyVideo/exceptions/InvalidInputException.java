package com.example.EdufyVideo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//ED-243-AA
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidInputException extends RuntimeException {

    private  String object;
    private  String field;
    private  Object value;
    private String message;

    public InvalidInputException(String object, String field, Object value) {
        super(String.format("Invalid inputs: %S [%s] cannot be %s.", object, field, value));
        this.object = object;
        this.field = field;
        this.value = value;
    }

    public InvalidInputException(String message) {
        super(message);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getObject() {
        return object;
    }

    public String getField() {
        return field;
    }

    public Object getValue() {
        return value;
    }

}

