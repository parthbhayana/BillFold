package com.nineleaps.expense_management_project.firebase;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import java.util.concurrent.ExecutionException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {PushNotificationService.class})
@ExtendWith(SpringExtension.class)
class PushNotificationServiceTest {
    @MockBean
    private FCMService fCMService;

    @Autowired
    private PushNotificationService pushNotificationService;


    @Test
    void testSendPushNotificationToToken() throws InterruptedException, ExecutionException {
        // Arrange
        doNothing().when(fCMService).sendMessageToToken(Mockito.<PushNotificationRequest>any());

        // Act
        pushNotificationService.sendPushNotificationToToken(
                new PushNotificationRequest("Dr", "Topic", "Not all who wander are lost", "ABC123"));

        // Assert
        verify(fCMService).sendMessageToToken(Mockito.<PushNotificationRequest>any());
    }
}

