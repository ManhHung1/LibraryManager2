package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.config.ResponseConfig;
import org.example.dto.CategoryDTO;
import org.example.dto.ResponseDto;
import org.example.interfaces.CategoryService;
import org.example.model.Category;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
@Tag(name = "Categories", description = "Operations related to managing categories")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    @Operation(summary = "Retrieve all categories", description = "Fetches a list of all categories")
    public ResponseEntity<ResponseDto<List<CategoryDTO>>> getAllCategories() {
        List<CategoryDTO> categories = categoryService.getAllCategories();
        return ResponseConfig.success(categories);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Retrieve a category by ID", description = "Fetches details of a category by its ID")
    public ResponseEntity<ResponseDto<CategoryDTO>> getCategoryById(
            @Parameter(description = "ID of the category to retrieve", required = true) @PathVariable int id
    ) {
        CategoryDTO category = categoryService.getCategoryById(id);
        if (category != null) {
            return ResponseConfig.success(category);
        } else {
            return ResponseConfig.error(HttpStatus.NOT_FOUND, "01", "Category not found");
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(summary = "Create a new category", description = "Adds a new category")
    public ResponseEntity<ResponseDto<Category>> createCategory(
            @Parameter(description = "Details of the category to create", required = true) @RequestBody Category category
    ) {
        Category createdCategory = categoryService.saveCategory(category);
        return ResponseConfig.success(createdCategory);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(summary = "Update an existing category", description = "Updates the details of an existing category by its ID")
    public ResponseEntity<ResponseDto<Category>> updateCategory(
            @Parameter(description = "ID of the category to update", required = true) @PathVariable int id,
            @Parameter(description = "Updated details of the category", required = true) @RequestBody Category category
    ) {
        category.setId(id);
        Category updatedCategory = categoryService.saveCategory(category);
        return ResponseConfig.success(updatedCategory);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(summary = "Delete a category", description = "Deletes a category by its ID")
    public ResponseEntity<ResponseDto<Void>> deleteCategory(
            @Parameter(description = "ID of the category to delete", required = true) @PathVariable int id
    ) {
        boolean success = categoryService.deleteCategory(id);
        return ResponseConfig.successDelete(null, success);
    }
}
