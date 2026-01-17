package com.shopeasy.shopeasy.controller;

import com.shopeasy.shopeasy.dto.request.CreateProductRequest;
import com.shopeasy.shopeasy.dto.request.UpdateProductRequest;
import com.shopeasy.shopeasy.dto.response.ProductResponse;
import com.shopeasy.shopeasy.service.ProductService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private static final Logger log = LoggerFactory.getLogger(ProductController.class);

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAll() {
        log.info("Fetching all products");
        return ResponseEntity.ok(productService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getById(@PathVariable Long id) {
        log.info("Fetching product id={}", id);
        return ResponseEntity.ok(productService.getById(id));
    }

    @PostMapping
    public ResponseEntity<ProductResponse> create(@Valid @RequestBody CreateProductRequest request) {
        log.info("Creating product name={}", request.getName());

        ProductResponse response = productService.create(request);

        return ResponseEntity.status(201).body(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProductResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateProductRequest request
    ) {
        log.info("Updating product id={}", id);
        ProductResponse response = productService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("Deleting product id={}", id);
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
