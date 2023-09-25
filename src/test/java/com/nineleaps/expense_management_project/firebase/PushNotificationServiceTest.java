package com.nineleaps.expense_management_project.firebase;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.ExecutionException;

import static org.mockito.Mockito.*;

@SpringBootTest
public class PushNotificationServiceTest {

    @Mock
    private FCMService mockFcmService;

    @Mock
    private Logger mockLogger;

    private PushNotificationService pushNotificationService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        pushNotificationService = new PushNotificationService(mockFcmService);
        pushNotificationService.logger = mockLogger;
    }

    @Test
    public void testSendPushNotificationToToken_Success() throws ExecutionException, InterruptedException {
        // Arrange
        PushNotificationRequest request = new PushNotificationRequest("Title", "Topic", "Message", "Token");

        // Act
        pushNotificationService.sendPushNotificationToToken(request);

        // Assert
        verify(mockFcmService, times(1)).sendMessageToToken(request);
        verifyNoInteractions(mockLogger); // Ensure logger is not called in case of success
    }
}

