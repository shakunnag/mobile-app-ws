package com.shakun.ws.ui.service;

import java.util.List;

import com.shakun.ws.dao.UserDAO;
import com.shakun.ws.exception.CouldNotUpdateRecordException;
import com.shakun.ws.exception.EmailVerificationException;
import com.shakun.ws.exception.UserNotFoundException;
import com.shakun.ws.exception.UserServiceException;
import com.shakun.ws.shared.dto.UserDTO;
import com.shakun.ws.ui.model.ErrorMessages;
import com.shakun.ws.utils.AmazonSES;
import com.shakun.ws.utils.UserProfileUtils;

public class UserServiceImpl implements UserService {
	UserProfileUtils userProfileUtils = new UserProfileUtils();
	UserDAO userDao;

	public UserServiceImpl(UserDAO userDao) {
		this.userDao = userDao;
	}

	public UserDTO createUser(UserDTO user) {
		UserDTO returnValue = null;
		// Validate the required fields
		userProfileUtils.validateRequiredFields(user);
		// check if user already exists
		UserDTO existingUser = this.getUserByUserName(user.getEmail());
		if (null != existingUser) {
			throw new UserServiceException(ErrorMessages.RECORD_ALREADY_EXISTS.name());
		}
		// Generate secure Public user id
		String userId = userProfileUtils.generateUserId(30);
		user.setUserId(userId);

		// Generate Salt
		String salt = userProfileUtils.getSalt(30);

		// generate secure password
		String encryptedPassword = userProfileUtils.generateSecurePassword(user.getPassword(), salt);
		user.setSalt(salt);
		user.setEncryptedPassword(encryptedPassword);
		user.setEmailVerificationStatus(false);
		user.setEmailVerificationToken(userProfileUtils.generateEmailVerificationToken(30));
		returnValue = this.saveUser(user);
		AmazonSES ses = new AmazonSES();
		ses.verifyEmail(user);
		// persist data in to database
		return returnValue;
	}

	@Override
	public UserDTO getUserByUserName(String userName) {
		UserDTO user = null;
		if (null == userName || userName.isEmpty()) {
			return null;
		}
		try {
			this.userDao.openConnection();
			user = this.userDao.getUserByUserName(userName);
		} finally {
			this.userDao.closeConnection();
		}
		return user;
	}

	private UserDTO saveUser(UserDTO user) {
		UserDTO returnValue = null;
		try {
			this.userDao.openConnection();
			returnValue = this.userDao.saveUser(user);
		} finally {
			this.userDao.closeConnection();
		}
		return returnValue;
	}

	@Override
	public UserDTO getUser(String userId) {
		UserDTO returnValue = null;
		try {
			this.userDao.openConnection();
			returnValue = this.userDao.getUser(userId);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new UserNotFoundException(ex.getMessage());
		} finally {
			this.userDao.closeConnection();
		}
		return returnValue;
	}

	@Override
	public List<UserDTO> getUsers(int start, int limit) {
		List<UserDTO> returnValue = null;
		try {
			userDao.openConnection();
			returnValue = userDao.getUsers(start, limit);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new UserNotFoundException(ex.getMessage());
		} finally {
			this.userDao.closeConnection();
		}
		return returnValue;
	}

	@Override
	public void updateUserDetails(UserDTO storedUser) {
		try {
			userDao.openConnection();
			userDao.updateUser(storedUser);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new CouldNotUpdateRecordException(ex.getMessage());
		} finally {
			this.userDao.closeConnection();
		}
	}

	@Override
	public void deleteUser(UserDTO storedUser) {
		try {
			userDao.openConnection();
			userDao.deleteUser(storedUser);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new com.shakun.ws.exception.CouldNotDeleteRecordException(ex.getMessage());
		} finally {
			this.userDao.closeConnection();
		}
	}

	@Override
	public boolean verifyEmail(String emailVerificationToken) {
		boolean returnValue = false;
		if (null == emailVerificationToken || emailVerificationToken.isEmpty()) {
			throw new EmailVerificationException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
		}
		try {
			UserDTO storedUser = getUserByEmailToken(emailVerificationToken);
			if (null == storedUser) {
				throw new UserNotFoundException(ErrorMessages.USER_NOT_FOUND.getErrorMessage());
			}
			storedUser.setEmailVerificationStatus(true);
			storedUser.setEmailVerificationToken(null);
			updateUserDetails(storedUser);
		} catch (Exception e) {
			throw new EmailVerificationException(e.getMessage());
		}
		return returnValue;
	}

	private UserDTO getUserByEmailToken(String emailVerificationToken) {
		UserDTO returnValue = null;
		try {
			userDao.openConnection();
			returnValue = userDao.getUserByEmailToken(emailVerificationToken);
		} finally {
			this.userDao.closeConnection();
		}
		return returnValue;
	}
}
