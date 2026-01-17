package com.shopeasy.shopeasy.mapper;

import com.shopeasy.shopeasy.dto.request.RoleRequest;
import com.shopeasy.shopeasy.dto.response.RoleResponse;
import com.shopeasy.shopeasy.model.Role;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper {

    public Role toEntity(RoleRequest dto) {
        Role role = new Role();
        role.setName(dto.getName());
        role.setDescription(String.valueOf(dto.getCode()));
        role.setDescription(dto.getDescription());

        return role;
    }

    public RoleResponse toResponse(Role role) {
        RoleResponse response = new RoleResponse();

        response.setId(role.getId());
        response.setName(role.getName());
        response.setDescription(role.getDescription());
        response.setCreatedAt(role.getCreatedAt());
        response.setUpdatedAt(role.getUpdateAt());

        return response;
    }
}
