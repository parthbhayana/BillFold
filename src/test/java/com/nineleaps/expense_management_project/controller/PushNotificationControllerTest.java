package com.nineleaps.expense_management_project.controller;

import com.nineleaps.expense_management_project.firebase.PushNotificationRequest;
import com.nineleaps.expense_management_project.firebase.PushNotificationResponse;
import com.nineleaps.expense_management_project.firebase.PushNotificationService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.http.MediaType;


public class PushNotificationControllerTest {

    @Mock
    private PushNotificationService pushNotificationService;

    @InjectMocks
    private PushNotificationController pushNotificationController;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSendTokenNotification() {
        // Create a sample PushNotificationRequest
        PushNotificationRequest request = new PushNotificationRequest();
        request.setToken("sampleToken");
        request.setTitle("Test Notification");
        request.setMessage("This is a test notification");

        // Mock the behavior of the pushNotificationService
        doNothing().when(pushNotificationService).sendPushNotificationToToken(request);

        // Call the controller method
        ResponseEntity<PushNotificationResponse> responseEntity = pushNotificationController.sendTokenNotification(request);

        // Verify that the service method was called
        verify(pushNotificationService, times(1)).sendPushNotificationToToken(request);

        // Check the response entity
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Notification sent.", Objects.requireNonNull(responseEntity.getBody()).getMessage());
        assertEquals(HttpStatus.OK.value(), responseEntity.getBody().getStatus());
    }






}