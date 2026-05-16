package com.smartbadge.adl.shared.exception;

public class UserNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 7224846421654038876L;

    public UserNotFoundException(String identifier) {
        super(String.format("User not found with identifier: %s", identifier));
    }
}
