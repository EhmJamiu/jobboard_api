package com.ehmjamiu.jobboard.exceptions;

public class UserDoesNotExist extends RuntimeException{
    public UserDoesNotExist(String message) {
        super(message);
    }
}