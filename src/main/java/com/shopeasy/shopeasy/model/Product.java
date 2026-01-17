package com.shopeasy.shopeasy.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
public class Product extends BaseEntity {

    @Column(nullable = true)
    private String name;

    private BigDecimal price;

    private Long stock;

    @ManyToOne(fetch = FetchType.LAZY) // Plusieurs produits pour une catégory
    @JoinColumn(name = "category_id")
    @JsonBackReference
    private Category category;

}
