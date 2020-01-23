package com.shakun.ws.exception;

public class UserNotFoundException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7350205822750845993L;

	public UserNotFoundException(String message) {
		super(message);
	}

}
