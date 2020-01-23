package com.shakun.ws.ui.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class LoginCredentials {
	private String userName;
	private String userPassword;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userId) {
		this.userName = userId;
	}
	public String getUserPassword() {
		return userPassword;
	}
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}
}
