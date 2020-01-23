package com.shakun.ws.utils;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Random;
import java.util.UUID;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import com.shakun.ws.exception.MissingRequiredFieldException;
import com.shakun.ws.shared.dto.UserDTO;
import com.shakun.ws.ui.model.ErrorMessages;

public class UserProfileUtils {

	private final Random RANDOM = new SecureRandom();
	private final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvxxyz";
	private final int ITERATIONS = 10000;
	private final int KEY_LENGTH = 256;

	public void validateRequiredFields(UserDTO userDto) throws MissingRequiredFieldException {
		if (userDto.getFirstName() == null || userDto.getFirstName().isEmpty() || userDto.getLastName() == null
				|| userDto.getLastName().isEmpty() || userDto.getEmail() == null || userDto.getEmail().isEmpty()
				|| userDto.getPassword() == null || userDto.getPassword().isEmpty()) {
			throw new MissingRequiredFieldException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
		}
	}

	public String generateUUID() {
		String returnValue = UUID.randomUUID().toString().replaceAll("-", "");
		return returnValue;
	}

	private String generateRandomString(int length) {
		StringBuilder sb = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			sb.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));

		}
		return new String(sb);
	}

	public String generateUserId(int length) {
		return generateRandomString(length);
	}
	public String generateEmailVerificationToken(int length) {
		return generateRandomString(length);
	}
	public String getSalt(int length) {
		return generateRandomString(length);
	}

	public String generateSecurePassword(String password, String salt) {
		String returnValue = null;
		byte[] securePassword = hash(password.toCharArray(), salt.getBytes());
		returnValue = Base64.getEncoder().encodeToString(securePassword);
		return returnValue;
	}

	public byte[] hash(char[] password, byte[] salt) {
		PBEKeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, KEY_LENGTH);
		Arrays.fill(password, Character.MIN_VALUE);
		try {
			SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			return skf.generateSecret(spec).getEncoded();
		} catch (NoSuchAlgorithmException e) {
			throw new AssertionError("Error while hashing a password: " + e.getMessage(), e);
		} catch (InvalidKeySpecException e) {
			throw new AssertionError("Error while hashing a password: " + e.getMessage(), e);
		} finally {
			spec.clearPassword();
		}
	}

	public byte[] encrypt(String securePassword, String accessTokenMaterial) throws InvalidKeySpecException {
		return hash(securePassword.toCharArray(), accessTokenMaterial.getBytes());
	}
}
