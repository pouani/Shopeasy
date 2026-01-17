package com.shopeasy.shopeasy.dto.response;

import com.shopeasy.shopeasy.util.RoleCode;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RoleResponse {
    private Long id;
    private String name;
    private RoleCode code;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
