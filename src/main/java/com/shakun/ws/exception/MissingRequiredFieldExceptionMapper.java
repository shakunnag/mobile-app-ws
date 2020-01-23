package com.shakun.ws.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.shakun.ws.ui.model.ErrorMessage;
import com.shakun.ws.ui.model.ErrorMessages;
@Provider
public class MissingRequiredFieldExceptionMapper implements ExceptionMapper<MissingRequiredFieldException> {

	public Response toResponse(MissingRequiredFieldException exception) {
		ErrorMessage error = new ErrorMessage(exception.getMessage(), ErrorMessages.MISSING_REQUIRED_FIELD.name(),
				"www.google.com");
		return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
	}

}
