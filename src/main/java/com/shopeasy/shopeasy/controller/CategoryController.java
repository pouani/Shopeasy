package com.shopeasy.shopeasy.controller;

import com.shopeasy.shopeasy.dto.request.CreateCategoryRequest;
import com.shopeasy.shopeasy.dto.request.UpdateCategoryRequest;
import com.shopeasy.shopeasy.dto.response.CategoryResponse;
import com.shopeasy.shopeasy.service.CategoryService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    private final static Logger log = LoggerFactory.getLogger(CategoryController.class);

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAll() {
        log.info("Fetching categories");
        return ResponseEntity.ok(categoryService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getById(id));
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> create(@Valid @RequestBody CreateCategoryRequest request) {
        log.info("Creating category by name={}", request.getName());
        CategoryResponse category = categoryService.create(request);

        return ResponseEntity.ok(category);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CategoryResponse> update(
            @PathVariable Long  id,
            @Valid @RequestBody UpdateCategoryRequest request
    ) {

        CategoryResponse response = categoryService.update(id, request);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        categoryService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
