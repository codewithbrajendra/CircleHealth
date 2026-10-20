package com.circles.health.doctor.wrapper;


import java.time.LocalDateTime;
import java.util.List;


public class BaseResponse {
    private int code;
    private String status;
    private String message;
    private LocalDateTime timestamp;
    private List<String> errorMessages;
    private Object data;
	
    public BaseResponse() {
		super();
	}

	public BaseResponse(int code, String status, String message, LocalDateTime timestamp, List<String> errorMessages,
			Object data) {
		this.code = code;
		this.status = status;
		this.message = message;
		this.timestamp = timestamp;
		this.errorMessages = errorMessages;
		this.data = data;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public List<String> getErrorMessages() {
		return errorMessages;
	}

	public void setErrorMessages(List<String> errorMessages) {
		this.errorMessages = errorMessages;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}
