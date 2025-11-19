package com.example.EdufyVideo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//ED-78-AA
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    private String resource;
    private String field;
    private Object fieldValue;
    private String message;

    public ResourceNotFoundException(String resource, String field, Object fieldValue) {
        super(String.format("%s not found with %s: %s", resource, field, fieldValue));
        this.resource = resource;
        this.field = field;
        this.fieldValue = fieldValue;
    }

    //ED-255-AA
    public ResourceNotFoundException(String message){
        super(message);
        this.message = message;
    }

    public String getResource() {
        return resource;
    }

    public String getField() {
        return field;
    }

    public Object getFieldValue() {
        return fieldValue;
    }

    public String getMessage() {
        return message;
    }
}
