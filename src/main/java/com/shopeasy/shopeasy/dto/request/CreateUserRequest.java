package com.shopeasy.shopeasy.dto.request;

import com.shopeasy.shopeasy.util.RoleCode;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateUserRequest {

    @NotBlank(message = "Le nom est requis")
    @Size(min = 2, max = 100, message = "Le nom doit contenir entre 2 et 100 caractères")
    private String name;

    @NotBlank(message = "L'email est réquis")
    @Email(message = "Email invalid")
    private String email;

    @NotBlank(message = "Le mot de passe ne doit pas etre vide")
    @Size(min = 8, message = "Le mot de passe doit contenir au moins 8 caractères")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$",
            message = "Le mot de passe doit contenir au moins une majuscule, une minuscule et un chiffre"
    )
    private String password;

    private String roleCode;

    /**
     * Récupère le RoleCode ou retourne CUSTOMER par défaut
     * @return
     */
    public RoleCode getRoleCodeEnum() {
        if (roleCode == null || roleCode.isEmpty()) {
            return RoleCode.CUSTOMER; // Rôle par défaut
        }
        return RoleCode.fromString(roleCode);
    }
}
