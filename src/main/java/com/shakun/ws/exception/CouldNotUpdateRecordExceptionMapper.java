package com.shakun.ws.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.shakun.ws.ui.model.ErrorMessage;
import com.shakun.ws.ui.model.ErrorMessages;
@Provider
public class CouldNotUpdateRecordExceptionMapper implements ExceptionMapper<CouldNotUpdateRecordException> {

	@Override
	public Response toResponse(CouldNotUpdateRecordException exception) {
		ErrorMessage message = new ErrorMessage(exception.getMessage(), ErrorMessages.COULD_NOT_UPDATE_RECORD.name(),
				"www.google.com");
		
		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(message).build();
	}

}
