package com.shopeasy.shopeasy.service;

import com.shopeasy.shopeasy.dto.request.RoleRequest;
import com.shopeasy.shopeasy.dto.response.RoleResponse;

import java.util.List;

public interface RoleService {

    RoleResponse create(RoleRequest dto);

    RoleResponse update(Long id, RoleRequest dto);

    List<RoleResponse> findAll();

    RoleResponse findById(Long id);

    void delete(Long id);
}
