package com.shopeasy.shopeasy.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserResponse {
    private Long id;
    private String publicId;
    private String name;
    private String email;
    private String roleCode;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean enabled;
}
