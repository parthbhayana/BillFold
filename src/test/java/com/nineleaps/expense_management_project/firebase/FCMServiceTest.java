package com.nineleaps.expense_management_project.firebase;


import static java.util.concurrent.CompletableFuture.completedFuture;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.firebase.messaging.Message;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.concurrent.ExecutionException;

@ContextConfiguration(classes = {FCMService.class})
@ExtendWith(SpringExtension.class)
class FCMServiceTest {
    @Autowired
    private FCMService fCMService;

    private FCMService fcmService;

    @Test
    void testGetAndroidConfig() {

        fCMService.getAndroidConfig("Topic");
    }


    @Test
    void testGetApnsConfig() {

        fCMService.getApnsConfig("Topic");
    }


    @Test
    void testGetPreconfiguredMessageToToken() {
        // Arrange
        PushNotificationRequest request = mock(PushNotificationRequest.class);
        when(request.getMessage()).thenReturn("Not all who wander are lost");
        when(request.getTitle()).thenReturn("Dr");
        when(request.getToken()).thenReturn("ABC123");
        when(request.getTopic()).thenReturn("Topic");

        // Act
        fCMService.getPreconfiguredMessageToToken(request);

        // Assert
        verify(request).getMessage();
        verify(request).getTitle();
        verify(request).getToken();
        verify(request, atLeast(1)).getTopic();
    }


}

