package com.naamad.springmongodb.exception;

public class PersonRequestException extends RuntimeException{

    public PersonRequestException(String message) {
        super(message);
    }
}
