package com.shakun.ws.ui.service;

import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;

import com.shakun.ws.dao.UserDAO;
import com.shakun.ws.dao.UserDAOImpl;
import com.shakun.ws.exception.AuthenticationException;
import com.shakun.ws.exception.EmailVerificationException;
import com.shakun.ws.shared.dto.UserDTO;
import com.shakun.ws.ui.model.ErrorMessages;
import com.shakun.ws.utils.UserProfileUtils;

public class AuthenticationServiceImpl implements AuthenticationService {
	UserDAO userDao;
	@Autowired
	UserService userService;
	@Override
	public UserDTO authenticate(String userName, String userPassword) throws AuthenticationException {
		UserDTO storedUser = null;
		
		storedUser = userService.getUserByUserName(userName);
		if (null == storedUser) {
			throw new AuthenticationException(ErrorMessages.INVALID_CREDENTIALS.getErrorMessage());
		}
		if(null!=storedUser && !storedUser.getEmailVerificationStatus()) {
			throw new EmailVerificationException(ErrorMessages.EMAIL_ADDRESS_NOT_VERIFIED.getErrorMessage());
		}
		String encryptedPassword = null;
		boolean authenticated = false;
		encryptedPassword = new UserProfileUtils().generateSecurePassword(userPassword, storedUser.getSalt());
		if (null != encryptedPassword && encryptedPassword.equalsIgnoreCase(storedUser.getEncryptedPassword())) {
			if (null != userName && userName.equalsIgnoreCase(storedUser.getEmail())) {
				authenticated = true;
			}
		}
		if (!authenticated) {
			throw new AuthenticationException(ErrorMessages.INVALID_CREDENTIALS.getErrorMessage());
		}
		return storedUser;
	}

	@Override
	public String issueAccessToken(UserDTO userProfile) throws AuthenticationException {
		String returnValue = null;
		String newSalt = userProfile.getSalt();
		String accessTokenMaterial = userProfile.getUserId() + newSalt;
		byte[] encryptedAccessToken = null;
		try {
			encryptedAccessToken = new UserProfileUtils().encrypt(userProfile.getEncryptedPassword(),
					accessTokenMaterial);
		} catch (InvalidKeySpecException e) {
			Logger.getLogger(AuthenticationServiceImpl.class.getName()).log(Level.SEVERE, null, e);
			throw new AuthenticationException("Failed to issue secire access token");
		}
		String encryptedAccessTokenBase64 = Base64.getEncoder().encodeToString(encryptedAccessToken);
		// Split string to 2 parts
		int tokenLength = encryptedAccessTokenBase64.length();
		String tokenValueToSaveToDB = encryptedAccessTokenBase64.substring(0, tokenLength / 2);
		returnValue = encryptedAccessTokenBase64.substring(tokenLength / 2, tokenLength);
		userProfile.setToken(tokenValueToSaveToDB);
		updateUserProfile(userProfile);
		return returnValue;
	}

	private void updateUserProfile(UserDTO userProfile) {
		this.userDao = new UserDAOImpl();
		try {
			userDao.openConnection();
			userDao.updateUser(userProfile);
		} finally {
			userDao.closeConnection();
		}
	}

	@Override
	public void resetSecurityCredentials(String password, UserDTO userProfile) {
		// Generate new salt
		UserProfileUtils userProfileUtils = new UserProfileUtils();
		String updatedSalt = userProfileUtils.getSalt(30);
		// Generate new password	
		String updatedPassword = userProfileUtils.generateSecurePassword(password, updatedSalt);
		userProfile.setSalt(updatedSalt);
		userProfile.setEncryptedPassword(updatedPassword);
		updateUserProfile(userProfile);
	}
}
