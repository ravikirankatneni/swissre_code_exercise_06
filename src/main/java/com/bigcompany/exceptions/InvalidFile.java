package com.bigcompany.exceptions;

public class InvalidFile extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InvalidFile(String message, Throwable cause) {
		super(message, cause);
	}
	
}
