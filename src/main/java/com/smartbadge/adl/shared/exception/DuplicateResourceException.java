package com.smartbadge.adl.shared.exception;

public class DuplicateResourceException extends RuntimeException {

	private static final long serialVersionUID = 1287444153042394437L;

	public DuplicateResourceException(String resourceName, String identifier) {
		super(String.format("%s already exists with identifier: %s", resourceName, identifier));
	}
}
