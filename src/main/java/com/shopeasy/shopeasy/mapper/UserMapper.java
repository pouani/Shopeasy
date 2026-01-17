package com.shopeasy.shopeasy.mapper;

import com.shopeasy.shopeasy.dto.request.CreateUserRequest;
import com.shopeasy.shopeasy.dto.response.UserResponse;
import com.shopeasy.shopeasy.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    /**
     * Convertit un DTO de création en entité User
     * Note: Le rôle et l'encodage du mot de passe sont gérés dans le service
     */
    public User toEntity(CreateUserRequest dto) {
        User user = new User();

        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());

        return user;
    }

    /**
     * Convertit une entité User en DTO de réponse
     */
    public UserResponse toResponse(User user) {

        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setPublicId(user.getPublicId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());

        if(user.getRole() != null) {
            response.setRoleCode(String.valueOf(user.getRole().getCode()));
        }

        response.setCreatedAt(user.getCreatedAt());
        response.setEnabled(user.isEnabled());

        return response;
    }
}
