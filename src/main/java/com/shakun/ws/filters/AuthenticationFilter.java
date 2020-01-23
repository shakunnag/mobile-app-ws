package com.shakun.ws.filters;

import java.io.IOException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.ext.Provider;

import org.springframework.beans.factory.annotation.Autowired;

import com.shakun.ws.annotations.Secured;
import com.shakun.ws.exception.AuthenticationException;
import com.shakun.ws.shared.dto.UserDTO;
import com.shakun.ws.ui.service.UserService;
import com.shakun.ws.utils.UserProfileUtils;

@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {
	@Autowired
	UserService userService;
	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
		if (null == authorizationHeader || !authorizationHeader.startsWith("Bearer")) {
			throw new AuthenticationException("Authorization header must be provided");
		}
		String token = authorizationHeader.substring("Bearer".length()).trim();
		String userId = requestContext.getUriInfo().getPathParameters().getFirst("userId");
		validateToken(token, userId);
	}

	private void validateToken(String token, String userId) {
		UserDTO userProfile = userService.getUser(userId);
		String completeToken = userProfile.getToken() + token;
		String salt = userProfile.getSalt();
		String encryptedPassword = userProfile.getEncryptedPassword();
		String accessTokenMaterial = userId + salt;
		byte[] encryptedAccessToken = null;
		try {
			encryptedAccessToken = new UserProfileUtils().encrypt(encryptedPassword, accessTokenMaterial);
		}catch(InvalidKeySpecException e) {
			throw new AuthenticationException("Failed to issue secure access token");
		}
		String encodedToken = Base64.getEncoder().encodeToString(encryptedAccessToken);
		if(!encodedToken.equals(completeToken)) {
			throw new AuthenticationException("Authorization token does not match");
		}
	}

}
