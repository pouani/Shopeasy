package com.shopeasy.shopeasy.dto.response;

import com.shopeasy.shopeasy.util.RoleCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    private String accessToken;
    private String refreshToken;
    private String tokenType = "Bearer";
    private long expiresIn; // en secondes
    private UserInfo user;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserInfo {
        private String publicId;
        private String name;
        private String email;
        private String roleName;
        private RoleCode roleCode;
    }

}
