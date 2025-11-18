package com.example.EdufyVideo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//ED-243-AA
@ResponseStatus(HttpStatus.CONFLICT)
public class UniqueConflictException extends RuntimeException {

    public UniqueConflictException(String object, Object value) {
        super(object + ": {" + value + "} already exists, duplicates is not allowed.");
    }
}
