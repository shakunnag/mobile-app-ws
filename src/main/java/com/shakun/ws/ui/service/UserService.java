package com.shakun.ws.ui.service;

import java.util.List;

import com.shakun.ws.shared.dto.UserDTO;

public interface UserService {
	UserDTO createUser(UserDTO userDto);
	UserDTO getUser(String userId);
	UserDTO getUserByUserName(String userName);
	List<UserDTO> getUsers(int start, int limit);
	void updateUserDetails(UserDTO storedUser);
	void deleteUser(UserDTO storedUser);
	boolean verifyEmail(String emailVerificationToken);
		
}
