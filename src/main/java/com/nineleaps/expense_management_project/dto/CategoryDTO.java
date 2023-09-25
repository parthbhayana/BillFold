package com.nineleaps.expense_management_project.dto;

// A Data Transfer Object (DTO) class for representing category information
public class CategoryDTO {
    private String categoryDescription;

    // Default constructor
    public CategoryDTO() {
    }

    // Parameterized constructor to initialize the categoryDescription
    public CategoryDTO(String categoryDescription) {
        this.categoryDescription = categoryDescription;
    }

    // Getter method to retrieve the category description
    public String getCategoryDescription() {
        return categoryDescription;
    }

    // Setter method to set the category description
    public void setCategoryDescription(String categoryDescription) {
        this.categoryDescription = categoryDescription;
    }
}
