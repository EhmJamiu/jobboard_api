package com.ehmjamiu.jobboard.exceptions;

import com.ehmjamiu.jobboard.models.enums.UserRole;

import java.util.Set;

public class MultiRoleException extends RuntimeException{
    private final Set<UserRole> requiredRoles;
    private final boolean requireAll;

    public MultiRoleException(String message,Set<UserRole> requiredRoles, boolean requireAll) {
        super(message);
        this.requiredRoles = requiredRoles;
        this.requireAll = requireAll;
    }

}