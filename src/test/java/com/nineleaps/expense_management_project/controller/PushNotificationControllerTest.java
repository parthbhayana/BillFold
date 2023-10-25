package com.nineleaps.expense_management_project.controller;

import static org.mockito.Mockito.doNothing;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nineleaps.expense_management_project.firebase.PushNotificationRequest;
import com.nineleaps.expense_management_project.firebase.PushNotificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {PushNotificationController.class})
@ExtendWith(SpringExtension.class)
class PushNotificationControllerTest {
    @Autowired
    private PushNotificationController pushNotificationController;

    @MockBean
    private PushNotificationService pushNotificationService;

    @Test
    void testSendTokenNotification() throws Exception {
        // Arrange
        doNothing().when(pushNotificationService).sendPushNotificationToToken(Mockito.<PushNotificationRequest>any());

        PushNotificationRequest pushNotificationRequest = new PushNotificationRequest();
        pushNotificationRequest.setMessage("Not all who wander are lost");
        pushNotificationRequest.setTitle("Dr");
        pushNotificationRequest.setToken("ABC123");
        pushNotificationRequest.setTopic("Topic");
        String content = (new ObjectMapper()).writeValueAsString(pushNotificationRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/notification/token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        // Act and Assert
        MockMvcBuilders.standaloneSetup(pushNotificationController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"status\":200,\"message\":\"Notification sent.\"}"));
    }
}

