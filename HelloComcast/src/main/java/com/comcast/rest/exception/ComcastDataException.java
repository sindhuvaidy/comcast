package com.comcast.rest.exception;

public class ComcastDataException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	public ComcastDataException(String errorMessage) {
		super(errorMessage);
	}

	@Override
	public String toString() {
		return "ComcastDataException [errorMessage=" + getMessage() + "]";
	}
	
}
