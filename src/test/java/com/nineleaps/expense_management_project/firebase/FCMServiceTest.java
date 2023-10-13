package com.nineleaps.expense_management_project.firebase;

import static java.util.concurrent.CompletableFuture.completedFuture;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.concurrent.ExecutionException;
import java.time.Duration;

import com.google.api.core.ApiFuture;
import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.ApnsConfig;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class FCMServiceTest {

    @InjectMocks
    private FCMService fcmService;

    @Mock
    private FirebaseMessaging firebaseMessaging;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

//    @Test
//    public void testSendMessageToToken() throws InterruptedException, ExecutionException {
//        // Prepare test data
//        PushNotificationRequest request = new PushNotificationRequest();
//        request.setToken("device-token");
//        request.setTitle("Test Title");
//        request.setMessage("Test Message");
//
//        // Mock the behavior of FirebaseMessaging
//        Message mockedMessage = Message.builder().build();
//        when(firebaseMessaging.sendAsync(any(Message.class))).thenReturn((ApiFuture<String>) completedFuture("success"));
//
//        // Call the method
//        fcmService.sendMessageToToken(request);
//
//        // Verify that FirebaseMessaging's sendAsync is called with the correct message
//        verify(firebaseMessaging, times(1)).sendAsync(any(Message.class));
//    }

//    @Test
//    public void testSendAndGetResponse() throws InterruptedException, ExecutionException {
//        // Prepare test data
//        PushNotificationRequest request = new PushNotificationRequest();
//        request.setToken("device-token");
//        request.setTitle("Test Title");
//        request.setMessage("Test Message");
//
//        // Mock the behavior of FirebaseMessaging
//        when(firebaseMessaging.sendAsync(any(Message.class))).thenReturn((ApiFuture<String>) completedFuture("success"));
//
//        // Call the method with a valid token
//        Message message = Message.builder()
//                .setToken("device-token") // Specify a valid token
//                .putData("title", request.getTitle())
//                .putData("message", request.getMessage())
//                .build();
//
//        // Call the method
//        String response = fcmService.sendAndGetResponse(message);
//
//        // Verify that the response is not null and equals "success"
//        assertNotNull(response);
//        assertEquals("success", response);
//    }


    @Test
    public void testGetAndroidConfig() {
        String topic = "test-topic";

        // Call the method
        AndroidConfig androidConfig = fcmService.getAndroidConfig(topic);

        // Verify that the returned AndroidConfig is not null
        assertNotNull(androidConfig);
    }

    @Test
    public void testGetApnsConfig() {
        String topic = "test-topic";

        // Call the method
        ApnsConfig apnsConfig = fcmService.getApnsConfig(topic);

        // Verify that the returned ApnsConfig is not null
        assertNotNull(apnsConfig);
    }

    @Test
    public void testGetPreconfiguredMessageToToken() {
        // Prepare test data
        PushNotificationRequest request = new PushNotificationRequest();
        request.setToken("device-token");
        request.setTitle("Test Title");
        request.setMessage("Test Message");

        // Call the method
        Message message = fcmService.getPreconfiguredMessageToToken(request);

        // Verify that the returned Message is not null
        assertNotNull(message);
    }
}
