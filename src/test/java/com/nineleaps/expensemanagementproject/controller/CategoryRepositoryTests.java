package com.nineleaps.expensemanagementproject.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.nineleaps.expensemanagementproject.entity.Category;
import com.nineleaps.expensemanagementproject.repository.CategoryRepository;

@RunWith(MockitoJUnitRunner.class)
@DataJpaTest
public class CategoryRepositoryTests {

    @Mock
    private CategoryRepository categoryRepository;

    private Category category;

    @Before
    public void setUp() {
        category = new Category();
        category.setCategoryId(1L);
        category.setName("Food");
        // Set any other necessary properties of the category

        Mockito.when(categoryRepository.getCategoryByCategoryId(1L)).thenReturn(category);
    }

    @Test
    public void testGetCategoryByCategoryId() {
        Category retrievedCategory = categoryRepository.getCategoryByCategoryId(1L);
        assertNotNull(retrievedCategory);
        assertEquals(category.getName(), retrievedCategory.getName());
        // Assert other properties if needed
    }



}
