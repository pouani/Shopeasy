package com.shopeasy.shopeasy.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CreateProductRequest {

    @NotBlank(message = "Le nom du produit est obligatoire")
    private String name;

    @Positive(message = "Le prix du produit ne peux pas etre negatif ou < 0")
    private BigDecimal price;

    private Long stock;
}
