package com.shakun.ws.exception;

public class EmailVerificationException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3357637979828939901L;
	public EmailVerificationException(String message) {
		super(message);
	}
}
