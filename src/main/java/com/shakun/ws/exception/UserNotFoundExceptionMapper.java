package com.shakun.ws.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.shakun.ws.ui.model.ErrorMessage;
import com.shakun.ws.ui.model.ErrorMessages;
@Provider
public class UserNotFoundExceptionMapper implements ExceptionMapper<UserNotFoundException>{

	@Override
	public Response toResponse(UserNotFoundException exception) {
		ErrorMessage message = new ErrorMessage(exception.getMessage(),ErrorMessages.USER_NOT_FOUND.name(),"www.google.com");
		return Response.status(Response.Status.BAD_REQUEST).entity(message).build();
	}

}
