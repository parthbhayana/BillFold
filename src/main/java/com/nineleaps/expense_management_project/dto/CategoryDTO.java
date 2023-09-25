package com.nineleaps.expense_management_project.dto;

public class CategoryDTO {
    private String categoryDescription;

    public CategoryDTO() {
    }

    public CategoryDTO(String categoryDescription) {
        this.categoryDescription = categoryDescription;
    }

    public String getCategoryDescription() {
        return categoryDescription;
    }

    public void setCategoryDescription(String categoryDescription) {
        this.categoryDescription = categoryDescription;
    }



}