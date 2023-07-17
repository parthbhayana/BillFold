package com.nineleaps.expensemanagementproject.DTO;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class CategoryDTOTest {

    @Test
    void testCategoryDTOConstructor() {
        String categoryDescription = "Test Category";
        CategoryDTO categoryDTO = new CategoryDTO(categoryDescription);
        assertEquals(categoryDescription, categoryDTO.getCategoryDescription());
    }

    @Test
    void testGetSetCategoryDescription() {
        String categoryDescription = "Test Category";
        CategoryDTO categoryDTO = new CategoryDTO();
        assertNull(categoryDTO.getCategoryDescription());

        categoryDTO.setCategoryDescription(categoryDescription);
        assertEquals(categoryDescription, categoryDTO.getCategoryDescription());
    }
}
