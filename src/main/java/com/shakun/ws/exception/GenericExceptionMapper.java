package com.shakun.ws.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.shakun.ws.ui.model.ErrorMessage;
import com.shakun.ws.ui.model.ErrorMessages;

@Provider
public class GenericExceptionMapper implements ExceptionMapper<Throwable> {

	public Response toResponse(Throwable exception) {
		ErrorMessage errorMessage = new ErrorMessage(exception.getMessage(), ErrorMessages.INTERNAL_SERVER_ERROR.name(),
				"http://www.google.com");
		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorMessage).build();
	}

}
