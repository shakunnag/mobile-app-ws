package com.shakun.ws.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import com.shakun.ws.ui.model.ErrorMessage;
import com.shakun.ws.ui.model.ErrorMessages;

public class CouldNotDeleteRecordExceptionMapper implements ExceptionMapper<CouldNotDeleteRecordException> {

	@Override
	public Response toResponse(CouldNotDeleteRecordException exception) {
		ErrorMessage message = new ErrorMessage(exception.getMessage(), ErrorMessages.COULD_NOT_DELETE_RECORD.name(),
				"www.google.com");
		
		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(message).build();
	}
}
