package com.shakun.ws.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.shakun.ws.ui.model.ErrorMessage;
import com.shakun.ws.ui.model.ErrorMessages;
@Provider
public class UserServiceExceptionMapper implements ExceptionMapper<UserServiceException> {

	public Response toResponse(UserServiceException exception) {
		ErrorMessage error = new ErrorMessage(exception.getMessage(), ErrorMessages.RECORD_ALREADY_EXISTS.name(),
				"https://www.google.com");
		return Response.status(Response.Status.BAD_REQUEST).entity(error).build();

	}

}
