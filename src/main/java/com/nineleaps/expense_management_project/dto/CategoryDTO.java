package com.nineleaps.expense_management_project.dto;

import lombok.Getter;

@Getter
public class CategoryDTO {
    private String categoryDescription;

    public CategoryDTO() {
    }

    public CategoryDTO(String categoryDescription) {
        this.categoryDescription = categoryDescription;
    }

    public void setCategoryDescription(String categoryDescription) {
        this.categoryDescription = categoryDescription;
    }



}