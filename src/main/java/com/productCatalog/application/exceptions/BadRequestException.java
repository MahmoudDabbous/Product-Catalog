package com.productCatalog.application.exceptions;

import java.util.List;

public class BadRequestException extends RuntimeException {

	private static final long serialVersionUID = -6059810039325277753L;

	private List<String> errors;
	
	public BadRequestException(String message) {
        super(message);
    }

	public BadRequestException(List<String> errors) {
		this.setErrors(errors);
	}

	public List<String> getErrors() {
		return errors;
	}

	public void setErrors(List<String> errors) {
		this.errors = errors;
	}
}
