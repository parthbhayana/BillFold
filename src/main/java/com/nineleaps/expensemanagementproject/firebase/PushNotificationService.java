package com.nineleaps.expensemanagementproject.firebase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PushNotificationService {

	private final Logger logger = LoggerFactory.getLogger(PushNotificationService.class);

	private final FCMService fcmService;

	public PushNotificationService(FCMService fcmService) {
		this.fcmService = fcmService;
	}

	public void sendPushNotificationToToken(PushNotificationRequest request) {
		try {
			fcmService.sendMessageToToken(request);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

}