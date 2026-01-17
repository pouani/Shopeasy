package com.shopeasy.shopeasy.service;

import com.shopeasy.shopeasy.dto.request.CreateCategoryRequest;
import com.shopeasy.shopeasy.dto.request.UpdateCategoryRequest;
import com.shopeasy.shopeasy.dto.response.CategoryResponse;

import java.util.List;

public interface CategoryService {

    CategoryResponse create(CreateCategoryRequest request);
    CategoryResponse update(Long id, UpdateCategoryRequest request);

    CategoryResponse getById(Long id);

    List<CategoryResponse> getAll();

    void delete(Long id);
}
