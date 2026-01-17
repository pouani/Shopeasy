package com.shopeasy.shopeasy.service;

import com.shopeasy.shopeasy.exception.BadRequestException;
import com.shopeasy.shopeasy.model.RefreshToken;
import com.shopeasy.shopeasy.model.User;
import com.shopeasy.shopeasy.repository.RefreshTokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;

    @Value("${jwt.refresh-token.expiration:604800000}")
    private long refreshTokenExpiration;

    @Value("${jwt.refresh-token.max-per-user:5}") // Max 5 tokens actifs par utilisateur
    private int maxTokensPerUser;

    @Transactional
    public RefreshToken createRefreshToken(User user, HttpServletRequest request) {
        cleanupUserTokensIfNeeded(user);

        String tokenString = jwtService.generateRefreshToken(user);
        LocalDateTime expiresAt = LocalDateTime.now().plusSeconds(refreshTokenExpiration / 1000);

        RefreshToken refreshToken = new RefreshToken(tokenString, user, expiresAt);
        refreshToken.setIpAddress(getClientIp(request));
        refreshToken.setUserAgent(request.getHeader("User-Agent"));

        refreshTokenRepository.save(refreshToken);
        log.info("Refresh token created for user={}", user.getEmail());

        return refreshToken;
    }


    /**
     * Valide et récupère un refresh token
     */
    @Transactional(readOnly = true)
    public RefreshToken validateRefreshToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new BadRequestException("Refresh token invalide"));

        if (refreshToken.isRevoked()) {
            log.warn("Attempted use of revoked token for user={}", refreshToken.getUser().getEmail());
            throw new BadRequestException("Token révoqué");
        }

        if (refreshToken.isExpired()) {
            log.warn("Attempted use of expired token for user={}", refreshToken.getUser().getEmail());
            throw new BadRequestException("Token expiré");
        }

        return refreshToken;
    }

    /**
     * Révoque un refresh token spécifique
     */
    @Transactional
    public void revokeToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new BadRequestException("Token introuvable"));

        refreshToken.setRevoked(true);
        refreshTokenRepository.save(refreshToken);

        log.info("Token revoked for user={}", refreshToken.getUser().getEmail());
    }


    /**
     * Nettoie les tokens de l'utilisateur si la limite est atteinte
     */
    private void cleanupUserTokensIfNeeded(User user) {
        List<RefreshToken> activeTokens = refreshTokenRepository
                .findActiveTokensByUser(user, LocalDateTime.now());

        if (activeTokens.size() >= maxTokensPerUser) {
            // Suppression du plus ancien
            RefreshToken oldest = activeTokens.stream()
                    .min((a, b) -> a.getCreatedAt().compareTo(b.getCreatedAt()))
                    .orElse(null);

            if (oldest != null) {
                refreshTokenRepository.delete(oldest);
                log.info("Deleted oldest token for user={}", user.getEmail());
            }
        }
    }

    /**
     * Révoque tous les tokens d'un utilisateur
     * @param user
     */
    @Transactional
    public void revokeAllUserTokens(User user) {
        refreshTokenRepository.revokeAllUserTokens(user);
        log.info("All tokens revoked for user={}", user.getEmail());
    }

    /**
     * Tâche planifiée pour nettoyer les tokens expirés (tous les jours à 3h)
     */
    @Scheduled(cron = "0 0 3 * * ?")
    @Transactional
    public void cleanupExpiredTokens() {
        log.info("Starting cleanup of expired tokens");
        refreshTokenRepository.deleteExpiredTokens(LocalDateTime.now());
        log.info("Expired tokens cleanup completed");
    }

    /**
     * Récupère l'IP du client
     * @param request
     * @return
     */
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if(ip == null || ip.isEmpty()) {
            ip = request.getRemoteAddr();
        }

        return ip;
    }
}
