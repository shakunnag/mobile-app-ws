package com.shakun.ws.exception;

public class CouldNotDeleteRecordException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7838832619200686415L;

	public CouldNotDeleteRecordException(String message) {
		super(message);
	}
}
