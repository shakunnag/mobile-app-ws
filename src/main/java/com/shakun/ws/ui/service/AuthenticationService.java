package com.shakun.ws.ui.service;

import com.shakun.ws.exception.AuthenticationException;
import com.shakun.ws.shared.dto.UserDTO;

public interface AuthenticationService {
	UserDTO authenticate(String userName, String userPassword) throws AuthenticationException;
	String issueAccessToken (UserDTO userProfile) throws AuthenticationException;
	void resetSecurityCredentials(String password, UserDTO userProfile);
}
