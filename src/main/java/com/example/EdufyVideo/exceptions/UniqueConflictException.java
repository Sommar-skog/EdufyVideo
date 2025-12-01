package com.example.EdufyVideo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//ED-243-AA //ED-378-AA updated
@ResponseStatus(HttpStatus.CONFLICT)
public class UniqueConflictException extends RuntimeException {

    private final String object;
    private final Object value;

    public UniqueConflictException(String object, Object value) {
        super(object + ": {" + value + "} already exists, duplicates is not allowed.");
        this.object = object;
        this.value = value;
    }

    public String getObject() {
        return object;
    }

    public Object getValue() {
        return value;
    }
}
