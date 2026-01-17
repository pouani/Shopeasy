package com.shopeasy.shopeasy.service.impl;

import com.shopeasy.shopeasy.dto.request.CreateCategoryRequest;
import com.shopeasy.shopeasy.dto.request.UpdateCategoryRequest;
import com.shopeasy.shopeasy.dto.response.CategoryResponse;
import com.shopeasy.shopeasy.exception.BadRequestException;
import com.shopeasy.shopeasy.exception.ResourceNotFoundException;
import com.shopeasy.shopeasy.mapper.CategoryMapper;
import com.shopeasy.shopeasy.model.Category;
import com.shopeasy.shopeasy.repository.CategoryRepository;
import com.shopeasy.shopeasy.service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final static Logger log = LoggerFactory.getLogger(CategoryServiceImpl.class);

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public CategoryResponse create(CreateCategoryRequest request) {

        log.info("Creating category name={}", request.getName());

        if(categoryRepository.existsByName(request.getName())) {
            throw new BadRequestException("La catégorie existe déjà");
        }

        Category category = CategoryMapper.toEntity(request);
        Category saved = categoryRepository.save(category);

        return CategoryMapper.toResponse(saved);
    }

    @Override
    public CategoryResponse update(Long id, UpdateCategoryRequest request) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categorie introuvable"));

        if(request.getName() != null) {
            category.setName(request.getName());
        }

        Category updated = categoryRepository.save(category);

        return CategoryMapper.toResponse(updated);
    }

    @Override
    public CategoryResponse getById(Long id) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categorie introuvable"));

        return CategoryMapper.toResponse(category);
    }

    @Override
    public List<CategoryResponse> getAll() {
        return categoryRepository.findAll()
                .stream()
                .map(CategoryMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        if(categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Category non trouvé");
        }

        categoryRepository.deleteById(id);
    }
}
