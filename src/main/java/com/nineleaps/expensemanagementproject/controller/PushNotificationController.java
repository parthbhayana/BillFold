package com.nineleaps.expensemanagementproject.controller;

import com.nineleaps.expensemanagementproject.firebase.PushNotificationRequest;
import com.nineleaps.expensemanagementproject.firebase.PushNotificationResponse;
import com.nineleaps.expensemanagementproject.firebase.PushNotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PushNotificationController {

    private PushNotificationService pushNotificationService;

    public PushNotificationController(PushNotificationService pushNotificationService) {
        this.pushNotificationService = pushNotificationService;
    }

    @SuppressWarnings("rawtypes")
	@PostMapping("/notification/token")
    public ResponseEntity sendTokenNotification(@RequestBody PushNotificationRequest request) {
        pushNotificationService.sendPushNotificationToToken(request);
        return new ResponseEntity<>(new PushNotificationResponse(HttpStatus.OK.value(), "Notification sent."), HttpStatus.OK);
    }

}
