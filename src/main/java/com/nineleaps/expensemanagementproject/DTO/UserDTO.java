package com.nineleaps.expensemanagementproject.DTO;

public class UserDTO {
    private String employeeEmail;
    private String imageUrl;
    private String firstName;
    private String middleName;
    private String lastName;

    public UserDTO() {
    }

    public UserDTO(String employeeEmail, String imageUrl, String firstName, String middleName, String lastName) {
        this.employeeEmail = employeeEmail;
        this.imageUrl = imageUrl;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
    }

    public String getEmployeeEmail() {
        return employeeEmail;
    }

    public void setEmployeeEmail(String employeeEmail) {
        this.employeeEmail = employeeEmail;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}