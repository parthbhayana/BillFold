package com.nineleaps.expensemanagementproject.DTO;

import lombok.Getter;

@Getter
public class UserDTO {
    private String employeeEmail;
    private String imageUrl;

    public void setEmployeeEmail(String employeeEmail) {
        this.employeeEmail = employeeEmail;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    private String firstName;
    private String middleName;
    private String lastName;
    private String fcmToken;


}