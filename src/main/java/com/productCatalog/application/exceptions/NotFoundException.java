package com.productCatalog.application.exceptions;

public class NotFoundException extends RuntimeException {

	private static final long serialVersionUID = 2050073891958040611L;

	public NotFoundException(String message) {
        super(message);
    }
}
