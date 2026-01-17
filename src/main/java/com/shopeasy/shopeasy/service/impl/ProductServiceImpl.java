package com.shopeasy.shopeasy.service.impl;

import com.shopeasy.shopeasy.dto.request.CreateProductRequest;
import com.shopeasy.shopeasy.dto.request.UpdateProductRequest;
import com.shopeasy.shopeasy.dto.response.ProductResponse;
import com.shopeasy.shopeasy.exception.BadRequestException;
import com.shopeasy.shopeasy.exception.ResourceNotFoundException;
import com.shopeasy.shopeasy.mapper.ProductMapper;
import com.shopeasy.shopeasy.model.Product;
import com.shopeasy.shopeasy.repository.ProductRepository;
import com.shopeasy.shopeasy.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private static final Logger log  = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public ProductResponse create(CreateProductRequest request) {

        log.info("Creating product name={}", request.getName());

        if(productRepository.existsByName(request.getName())) {
            throw new BadRequestException("Produit déjà existant");
        }

        Product product = ProductMapper.toEntity(request);
        Product saved = productRepository.save(product);

        return ProductMapper.toResponse(saved);
    }

    @Override
    public ProductResponse update(Long id, UpdateProductRequest request) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Produit introuvable")
                );

        if(request.getName() != null) {
            product.setName(request.getName());
        }

        if(request.getPrice() != null) {
            product.setPrice(request.getPrice());
        }

        Product updated = productRepository.save(product);

        return ProductMapper.toResponse(updated);
    }

    @Override
    public ProductResponse getById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Produit introuvable")
                );

        return ProductMapper.toResponse(product);
    }

    @Override
    public List<ProductResponse> getAll() {
        return productRepository.findAll()
                .stream()
                .map(ProductMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        if(!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Produit intouvable");
        }

        productRepository.deleteById(id);
    }
}
