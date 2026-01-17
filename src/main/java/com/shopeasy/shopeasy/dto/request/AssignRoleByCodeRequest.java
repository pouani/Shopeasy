package com.shopeasy.shopeasy.dto.request;

import com.shopeasy.shopeasy.util.RoleCode;
import lombok.Data;

@Data
public class AssignRoleByCodeRequest {
    private RoleCode roleCode;
}
