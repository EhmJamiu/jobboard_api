package com.ehmjamiu.jobboard.exceptions;

import com.ehmjamiu.jobboard.models.enums.UserRole;

public class SingleRoleBasedException extends RuntimeException{

    UserRole role;
    public SingleRoleBasedException(String message, UserRole role) {
        super(message);
        this.role = role;
    }
}