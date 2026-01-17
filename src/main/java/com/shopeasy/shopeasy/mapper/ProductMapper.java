package com.shopeasy.shopeasy.mapper;

import com.shopeasy.shopeasy.dto.request.CreateProductRequest;
import com.shopeasy.shopeasy.dto.response.ProductResponse;
import com.shopeasy.shopeasy.model.Product;

public class ProductMapper {

    public static Product toEntity(CreateProductRequest dto) {

        Product product = new Product();
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());

        return product;
    }

    public static ProductResponse toResponse(Product product) {
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setPrice(product.getPrice());
        response.setStock(product.getStock());

        if(product.getCategory() != null) {
            response.setCategoryName(product.getCategory().getName());
        }

        return response;
    }
}
