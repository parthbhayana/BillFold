package com.nineleaps.expense_management_project.entity;

import java.time.LocalDateTime;

public class Timestamp {

    public static LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now();
    }
}