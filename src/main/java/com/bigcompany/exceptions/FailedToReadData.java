package com.bigcompany.exceptions;

public class FailedToReadData extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public FailedToReadData(String message, Throwable cause) {
		super(message, cause);
	}

}
