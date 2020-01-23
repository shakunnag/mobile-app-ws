package com.shakun.ws.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.shakun.ws.ui.model.ErrorMessage;
import com.shakun.ws.ui.model.ErrorMessages;

@Provider
public class AuthenticationExceptionMapper implements ExceptionMapper<AuthenticationException> {

	@Override
	public Response toResponse(AuthenticationException exception) {
		ErrorMessage error = new ErrorMessage(exception.getMessage(), ErrorMessages.INVALID_CREDENTIALS.name(),
				"www.google.com");

		return Response.status(Response.Status.UNAUTHORIZED).entity(error).build();
	}

}
