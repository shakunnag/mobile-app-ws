package com.shakun.ws.exception;

public class MissingRequiredFieldException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8177674971451118862L;

	public MissingRequiredFieldException(String message) {
		super(message);
	}
}
