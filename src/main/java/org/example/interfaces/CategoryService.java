package org.example.interfaces;

import org.example.dto.CategoryDTO;
import org.example.model.Category;

import java.util.List;

public interface CategoryService {
    List<CategoryDTO> getAllCategories();
    CategoryDTO getCategoryById(int id);
    Category saveCategory(Category category);
    boolean deleteCategory(int id);
}
