package com.circlesHealth.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//@Data
//@NoArgsConstructor
//@AllArgsConstructor
public class LoginDto {

    private String accessToken;
    private String refreshToken;
    private String sessionId;
    private UserDto userDto;
	
    public LoginDto() {
		super();
	}

	public LoginDto(String accessToken, String refreshToken, String sessionId, UserDto userDto) {
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
		this.sessionId = sessionId;
		this.userDto = userDto;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public UserDto getUserDto() {
		return userDto;
	}

	public void setUserDto(UserDto userDto) {
		this.userDto = userDto;
	}
}
