package com.shopeasy.shopeasy.service;

import com.shopeasy.shopeasy.dto.request.LoginRequest;
import com.shopeasy.shopeasy.dto.request.RefreshTokenRequest;
import com.shopeasy.shopeasy.dto.response.AuthResponse;
import com.shopeasy.shopeasy.exception.BadRequestException;
import com.shopeasy.shopeasy.model.RefreshToken;
import com.shopeasy.shopeasy.model.User;
import com.shopeasy.shopeasy.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;

    @Value("${jwt.access-token.expiration:900000}")
    private long accessTokenExpiration;

    @Transactional
    public AuthResponse login(
            LoginRequest request,
            HttpServletRequest httpRequest
    ) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur introuvable"));

        if (!user.isEnabled()) {
            throw new BadRequestException("Votre compte a été désactivé. Contactez le support.");
        }

        // Génération du tokens
        String accessToken = jwtService.generateAccessToken(user);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user, httpRequest);

        log.info("User logged in successfully: {} with role {}",
                user.getEmail(), user.getRole().getCode());

        return buildAuthResponse(accessToken, refreshToken.getToken(), user);
    }

    @Transactional
    public AuthResponse refreshToken(RefreshTokenRequest request, HttpServletRequest httpRequest) {

        RefreshToken refreshToken = refreshTokenService.validateRefreshToken(request.getRefreshToken());
        User user = refreshToken.getUser();

        if (!user.isEnabled()) {
            refreshTokenService.revokeToken(request.getRefreshToken());
            throw new BadRequestException("Votre compte a été désactivé.");
        }

        // Vérifier que le token JWT est cohérent
        String username = jwtService.extractUsername(request.getRefreshToken());
        if(!username.equals(user.getEmail())) {
            throw new BadRequestException("Token invalide");
        }

        String newAccessToken = jwtService.generateAccessToken(user);

        refreshTokenService.revokeToken(request.getRefreshToken());
        RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(user, httpRequest);

        log.info("Tokens refreshed for user: {}", user.getEmail());

        return buildAuthResponse(newAccessToken, newRefreshToken.getToken(), user);
    }

    @Transactional
    public void logout(String refreshToken) {
        try {
            refreshTokenService.revokeToken(refreshToken);
            log.info("User logged out successfully");
        } catch (Exception e) {
            log.warn("Logout failed: {}", e.getMessage());
        }
    }

    @Transactional
    public void logoutAll(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur introuvable"));

        refreshTokenService.revokeAllUserTokens(user);
        log.info("All tokens revoked for user: {}", email);
    }

    /**
     * Construction de la réponse d'authentification
     */
    private AuthResponse buildAuthResponse(
            String accessToken,
            String refreshToken,
            User user
    ) {
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(accessTokenExpiration / 1000)
                .user(AuthResponse.UserInfo.builder()
                        .publicId(user.getPublicId())
                        .name(user.getName())
                        .email(user.getEmail())
                        .roleName(user.getRole().getName())
                        .roleCode(user.getRole().getCode())
                        .build())
                .build();
    }
}
