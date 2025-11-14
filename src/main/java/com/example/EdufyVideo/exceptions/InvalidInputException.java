package com.example.EdufyVideo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//ED-243-AA
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidInputException extends RuntimeException {

    private final String object;
    private final String field;
    private final Object value;

    public InvalidInputException(String object, String field, Object value) {
        super(String.format("Invalid input: %S [%s] cannot be %s.", object, field, value));
        this.object = object;
        this.field = field;
        this.value = value;
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

