package com.shakun.ws.exception;

public class AuthenticationException extends RuntimeException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4760079696476650185L;

	public AuthenticationException(String message) {
		super(message);
	}
}
