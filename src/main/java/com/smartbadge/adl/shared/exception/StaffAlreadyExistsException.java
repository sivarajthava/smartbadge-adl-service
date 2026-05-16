package com.smartbadge.adl.shared.exception;

public class StaffAlreadyExistsException extends RuntimeException {

    private static final long serialVersionUID = 1479145334435314828L;

    public StaffAlreadyExistsException() {
        super("Staff already exists in the system");
    }
}
