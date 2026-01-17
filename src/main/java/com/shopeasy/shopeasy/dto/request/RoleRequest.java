package com.shopeasy.shopeasy.dto.request;

import com.shopeasy.shopeasy.util.RoleCode;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;

@Data
public class RoleRequest {

    @NotBlank(message = "Le nom du role est obligatoire")
    private String name;

    private String description;

    private RoleCode code;
}
