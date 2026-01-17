package com.shopeasy.shopeasy.mapper;

import com.shopeasy.shopeasy.dto.request.CreateCategoryRequest;
import com.shopeasy.shopeasy.dto.response.CategoryResponse;
import com.shopeasy.shopeasy.model.Category;

public class CategoryMapper {

    public static Category toEntity(CreateCategoryRequest dto) {

        Category category = new Category();
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());

        return category;
    }

    public static CategoryResponse toResponse(Category category) {

        CategoryResponse response = new CategoryResponse();
        response.setId(category.getId());
        response.setName(category.getName());
        response.setDescription(category.getDescription());

        return response;
    }
}
