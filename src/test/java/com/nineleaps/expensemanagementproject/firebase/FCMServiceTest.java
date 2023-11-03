package com.nineleaps.expensemanagementproject.firebase;

import java.util.concurrent.ExecutionException;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {FCMService.class})
@ExtendWith(SpringExtension.class)
class FCMServiceTest {
    @Autowired
    private FCMService fCMService;

    /**
     * Method under test: {@link FCMService#sendMessageToToken(PushNotificationRequest)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testSendMessageToToken() throws InterruptedException, ExecutionException {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.IllegalStateException: FirebaseApp with name [DEFAULT] doesn't exist.
        //       at com.google.firebase.FirebaseApp.getInstance(FirebaseApp.java:165)
        //       at com.google.firebase.FirebaseApp.getInstance(FirebaseApp.java:136)
        //       at com.google.firebase.messaging.FirebaseMessaging.getInstance(FirebaseMessaging.java:64)
        //       at com.nineleaps.expensemanagementproject.firebase.FCMService.sendAndGetResponse(FCMService.java:35)
        //       at com.nineleaps.expensemanagementproject.firebase.FCMService.sendMessageToToken(FCMService.java:29)
        //   See https://diff.blue/R013 to resolve this issue.

        // Arrange and Act
        fCMService.sendMessageToToken(new PushNotificationRequest("Dr", "Topic", "Not all who wander are lost", "ABC123"));
    }
}

