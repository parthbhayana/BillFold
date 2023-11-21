package com.nineleaps.expensemanagementproject.firebase;

import lombok.Getter;

@Getter
public class PushNotificationRequest {
	private String title;
	private String topic;
	private String message;
	private String token;

	public PushNotificationRequest() {
	}

	public PushNotificationRequest(String title, String topic, String message, String token) {
		this.title = title;
		this.topic = topic;
		this.message = message;
		this.token = token;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setToken(String token) {
		this.token = token;
	}
}