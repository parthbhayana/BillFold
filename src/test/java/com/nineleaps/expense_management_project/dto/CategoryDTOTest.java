package com.nineleaps.expense_management_project.dto;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CategoryDTOTest {

    @Test
    public void testCategoryDTOConstructor() {
        String categoryDescription = "Test Category";
        CategoryDTO categoryDTO = new CategoryDTO(categoryDescription);

        // Verify that the constructor correctly initializes the categoryDescription
        assertEquals(categoryDescription, categoryDTO.getCategoryDescription());
    }

    @Test
    public void testCategoryDTOGetterAndSetter() {
        CategoryDTO categoryDTO = new CategoryDTO();

        // Set a category description using the setter method
        String categoryDescription = "Updated Category";
        categoryDTO.setCategoryDescription(categoryDescription);

        // Verify that the getter returns the expected category description
        assertEquals(categoryDescription, categoryDTO.getCategoryDescription());
    }
}