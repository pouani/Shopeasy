package com.shopeasy.shopeasy.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCategoryRequest {

    @NotBlank(message = "Le nom de la catégorie ne peux pas etre vide")
    private String name;

    private String description;
}
