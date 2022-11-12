package com.loofi.backoffice.request;

public class ResponseToken {

	private String accessToken;

	private String role;
	private int userId;

	public ResponseToken() {
	}

	public ResponseToken(String accessToken, int userId, String role) {
		this.accessToken = accessToken;
		this.userId = userId;
		this.role = role;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
}
