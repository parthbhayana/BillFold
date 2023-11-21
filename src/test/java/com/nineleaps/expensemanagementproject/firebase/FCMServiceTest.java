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


    @Test
    @Disabled("TODO: Complete this test")
    void testSendMessageToToken() throws InterruptedException, ExecutionException {

        fCMService.sendMessageToToken(new PushNotificationRequest("Dr", "Topic", "Not all who wander are lost", "ABC123"));
    }
}

