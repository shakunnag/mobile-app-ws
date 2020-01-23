package com.shakun.ws.ui.entrypoint;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;

import com.shakun.ws.shared.dto.UserDTO;
import com.shakun.ws.ui.model.AuthenticationDetails;
import com.shakun.ws.ui.model.LoginCredentials;
import com.shakun.ws.ui.service.AuthenticationService;

@Path("/authentication")
public class AuthenticationEntryPoint {
	@Autowired
	AuthenticationService authService;
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public AuthenticationDetails userLogin(LoginCredentials credentials) {
		AuthenticationDetails returnValue = new AuthenticationDetails();
		
		UserDTO authenticatedUser = authService.authenticate(credentials.getUserName(), credentials.getUserPassword());
		// Reset Access Token
		authService.resetSecurityCredentials(credentials.getUserPassword(), authenticatedUser);
		String secureAccessToken = authService.issueAccessToken(authenticatedUser);
		returnValue.setId(authenticatedUser.getUserId());
		returnValue.setToken(secureAccessToken);
		return returnValue;
	}
}
