package com.shakun.ws.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.shakun.ws.ui.model.ErrorMessage;
import com.shakun.ws.ui.model.ErrorMessages;
@Provider
public class EmailVerificationExceptionMapper implements ExceptionMapper<EmailVerificationException> {

	@Override
	public Response toResponse(EmailVerificationException exception) {
		ErrorMessage message = new ErrorMessage(exception.getMessage(), ErrorMessages.EMAIL_ADDRESS_NOT_VERIFIED.name(),
				"www.google.com");

		return Response.status(Response.Status.FORBIDDEN).entity(message).build();
	}

}
