package com.shopeasy.shopeasy.controller;

import com.shopeasy.shopeasy.dto.request.CreateUserRequest;
import com.shopeasy.shopeasy.dto.request.LoginRequest;
import com.shopeasy.shopeasy.dto.request.RefreshTokenRequest;
import com.shopeasy.shopeasy.dto.response.AuthResponse;
import com.shopeasy.shopeasy.dto.response.UserResponse;
import com.shopeasy.shopeasy.service.AuthService;
import com.shopeasy.shopeasy.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody CreateUserRequest request) {
        log.info("Registration attempt for email: {}", request.getEmail());
        UserResponse response = userService.create(request);
        return ResponseEntity.status(201).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletRequest httpRequest
    ) {
        log.info("Login attempt for email: {}", request.getEmail());
        AuthResponse response = authService.login(request, httpRequest);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(
            @Valid @RequestBody RefreshTokenRequest request,
            HttpServletRequest httpRequest
    ) {
        log.info("Token refresh attempt");
        AuthResponse response = authService.refreshToken(request, httpRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@Valid @RequestBody RefreshTokenRequest request) {
        log.info("Logout attempt");
        authService.logout(request.getRefreshToken());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/logout-all")
    public ResponseEntity<Void> logoutAll(Authentication authentication) {
        String email = authentication.getName();
        log.info("Logout all devices for user: {}", email);
        authService.logoutAll(email);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(Authentication authentication) {
        String email = authentication.getName();
        log.info("Fetching current user info: {}", email);

        return ResponseEntity.ok(userService.findByEmail(email));
    }
}
