package com.productCatalog.application.dto;

import java.util.List;

public class ErrorResponse {
	private int statusCode;
	private String message;
	private List<String> errors;

	public ErrorResponse() {
	}

	public ErrorResponse(int statusCode, String message) {
		this.statusCode = statusCode;
		this.message = message;
	}

	public List<String> getErrors() {
		return errors;
	}

	public void setErrors(List<String> errors) {
		this.errors = errors;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public ErrorResponse(int statusCode, List<String> errors) {
		this.statusCode = statusCode;
		this.errors = errors;
	}

	public ErrorResponse(int statusCode, String message, List<String> errors) {
		this.statusCode = statusCode;
		this.message = message;
		this.errors = errors;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public String getMessage() {
		return message;
	}
}