package com.nineleaps.expense_management_project.controller;

import com.nineleaps.expense_management_project.firebase.PushNotificationRequest;
import com.nineleaps.expense_management_project.firebase.PushNotificationResponse;
import com.nineleaps.expense_management_project.firebase.PushNotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PushNotificationController {

    private PushNotificationService pushNotificationService;

    // Constructor to inject the PushNotificationService
    public PushNotificationController(PushNotificationService pushNotificationService) {
        this.pushNotificationService = pushNotificationService;
    }

    // Endpoint to send a push notification to a specific token
    @SuppressWarnings("rawtypes")
    @PostMapping("/notification/token")
    public ResponseEntity sendTokenNotification(@RequestBody PushNotificationRequest request) {
        // Call the PushNotificationService to send the push notification
        pushNotificationService.sendPushNotificationToToken(request);

        // Return a response entity with a success message and HTTP status OK
        return new ResponseEntity<>(new PushNotificationResponse(HttpStatus.OK.value(), "Notification sent."), HttpStatus.OK);
    }
}
