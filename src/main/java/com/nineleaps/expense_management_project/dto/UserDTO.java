package com.nineleaps.expense_management_project.dto;

// A Data Transfer Object (DTO) class for representing user information
public class UserDTO {
    private String employeeEmail; // User's email address
    private String imageUrl; // URL to user's profile image
    private String firstName; // User's first name
    private String middleName; // User's middle name
    private String lastName; // User's last name
    private String fcmToken; // Firebase Cloud Messaging (FCM) token

    // Default constructor
    public UserDTO() {
    }

    // Parameterized constructor to initialize user information
    public UserDTO(String employeeEmail, String imageUrl, String firstName, String middleName, String lastName, String fcmToken) {
        this.employeeEmail = employeeEmail;
        this.imageUrl = imageUrl;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.fcmToken = fcmToken;
    }

    // Getter method to retrieve the user's email address
    public String getEmployeeEmail() {
        return employeeEmail;
    }

    // Setter method to set the user's email address
    public void setEmployeeEmail(String employeeEmail) {
        this.employeeEmail = employeeEmail;
    }

    // Getter method to retrieve the URL of the user's profile image
    public String getImageUrl() {
        return imageUrl;
    }

    // Setter method to set the URL of the user's profile image
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    // Getter method to retrieve the user's first name
    public String getFirstName() {
        return firstName;
    }

    // Setter method to set the user's first name
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    // Getter method to retrieve the user's middle name
    public String getMiddleName() {
        return middleName;
    }

    // Setter method to set the user's middle name
    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    // Getter method to retrieve the user's last name
    public String getLastName() {
        return lastName;
    }

    // Setter method to set the user's last name
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    // Getter method to retrieve the user's FCM token
    public String getFcmToken() {
        return fcmToken;
    }

    // Setter method to set the user's FCM token
    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }
}
