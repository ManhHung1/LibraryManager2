package org.example.interfaces;

import org.example.dto.CategoryDTO;

import java.util.List;

public interface CategoryService {
    List<CategoryDTO> getAllCategories();
    CategoryDTO getCategoryById(int id);
    CategoryDTO saveCategory(CategoryDTO categoryDTO);
    void deleteCategory(int id);
}
