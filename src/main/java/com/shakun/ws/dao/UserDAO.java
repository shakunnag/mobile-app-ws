package com.shakun.ws.dao;

import java.util.List;

import com.shakun.ws.shared.dto.UserDTO;

public interface UserDAO {
	void openConnection();
	void closeConnection();
	UserDTO getUserByUserName(String userName);
	UserDTO saveUser(UserDTO user);
	void updateUser(UserDTO user);
	UserDTO getUser(String userId);
	List<UserDTO> getUsers(int start, int limit);
	void deleteUser(UserDTO storedUser);
	UserDTO getUserByEmailToken(String emailVerificationToken);
	
}
