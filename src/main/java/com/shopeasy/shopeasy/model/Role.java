package com.shopeasy.shopeasy.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.shopeasy.shopeasy.util.RoleCode;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "roles", indexes = {
        @Index(name = "idx_code", columnList = "code", unique = true)
})
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class Role extends BaseEntity {

    @Column(nullable = false, length = 50)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(unique = true, nullable = false, length = 20)
    private RoleCode code;

    @Column(length = 255)
    private String description;

    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<User> users = new ArrayList<>();

    // Constructeur utilitaire
    public Role(String name, RoleCode code, String description) {
        this.name = name;
        this.code = code;
        this.description = description;
    }
}
