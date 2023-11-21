package com.nineleaps.expensemanagementproject.firebase;

import lombok.Getter;

@Getter
public class PushNotificationResponse {
	private int status;
	private String message;

	public PushNotificationResponse() {
	}

	public PushNotificationResponse(int status, String message) {
		this.status = status;
		this.message = message;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}