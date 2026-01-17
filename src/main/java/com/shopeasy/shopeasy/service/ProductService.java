package com.shopeasy.shopeasy.service;

import com.shopeasy.shopeasy.dto.request.CreateProductRequest;
import com.shopeasy.shopeasy.dto.request.UpdateProductRequest;
import com.shopeasy.shopeasy.dto.response.ProductResponse;

import java.util.List;

public interface ProductService {

    ProductResponse create(CreateProductRequest request);

    ProductResponse update(Long id, UpdateProductRequest request);

    ProductResponse getById(Long id);

    List<ProductResponse> getAll();

    void delete(Long id);
}
