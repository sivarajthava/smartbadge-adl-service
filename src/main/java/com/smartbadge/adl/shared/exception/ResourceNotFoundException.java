package com.smartbadge.adl.shared.exception;

public class ResourceNotFoundException extends RuntimeException {

    private static final long serialVersionUID =  8177718421717752374L;

	public ResourceNotFoundException(String resourceName, String identifier) {
        super(String.format("%s not found with identifier: %s", resourceName, identifier));
    }
}
