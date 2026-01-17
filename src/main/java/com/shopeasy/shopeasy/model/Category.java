package com.shopeasy.shopeasy.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Category extends BaseEntity {

    @Column(unique = true)
    private String name;

    @Column(nullable = true)
    private String description;

    @OneToMany(mappedBy = "category")
    @JsonManagedReference
    private List<Product> products = new ArrayList<>();
}
