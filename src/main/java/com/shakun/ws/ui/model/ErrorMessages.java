package com.shakun.ws.ui.model;

public enum ErrorMessages {

	MISSING_REQUIRED_FIELD("Mising Required Field. Check docs for required fields"),
	RECORD_ALREADY_EXISTS("Record already exists"), INTERNAL_SERVER_ERROR("Internal Server Error"),
	USER_NOT_FOUND("User not found"), INVALID_CREDENTIALS("Invalid credentials"),
	COULD_NOT_UPDATE_RECORD("Could not update record"), COULD_NOT_DELETE_RECORD("Could not delete record"),
	EMAIL_ADDRESS_NOT_VERIFIED("Email address not verified");

	private String errorMessage;

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	ErrorMessages(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}
