package com.nineleaps.expense_management_project.firebase;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {FCMInitializer.class})
@ExtendWith(SpringExtension.class)
class FCMInitializerTest {
    @Autowired
    private FCMInitializer fCMInitializer;


    @Test
    void testInitialize() {

        fCMInitializer.initialize();
    }
}

