package com.shopeasy.shopeasy.service;

import com.shopeasy.shopeasy.dto.request.CreateUserRequest;
import com.shopeasy.shopeasy.dto.response.UserResponse;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserService {

    UserResponse create(CreateUserRequest dto);

    UserResponse update(String publicId, CreateUserRequest dto);

    List<UserResponse> findAll();

    UserResponse findById(String publicId);

    @Transactional(readOnly = true)
    UserResponse findByEmail(String email);

    UserDetails loadUserByUsername(String email);

    void delete(String publicId);
}
