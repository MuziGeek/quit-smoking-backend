package com.muzi.quitsmoking.service.impl;

import com.muzi.quitsmoking.exception.BusinessException;
import com.muzi.quitsmoking.exception.ErrorCode;
import com.muzi.quitsmoking.service.TokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Token服务实现类
 * 基于JWT实现
 */
@Slf4j
@Service
public class TokenServiceImpl implements TokenService {

    /**
     * 用户ID在Token中的键名
     */
    private static final String CLAIM_KEY_USER_ID = "userId";

    /**
     * Token密钥
     */
    @Value("${jwt.secret:defaultSecretKeyMustBeAtLeast32BytesLongForHS256}")
    private String secret;

    /**
     * Token有效期（毫秒）
     */
    @Value("${jwt.expiration:86400000}")
    private Long expiration;

    /**
     * 生成密钥
     */
    private Key getSigningKey() {
        byte[] keyBytes = secret.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public String generateToken(Long userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USER_ID, userId);
        return generateToken(claims);
    }

    @Override
    public Long getUserIdFromToken(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            return Long.valueOf(claims.get(CLAIM_KEY_USER_ID).toString());
        } catch (Exception e) {
            log.error("从Token中获取用户ID失败: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public boolean validateToken(String token) {
        try {
            return !isTokenExpired(token);
        } catch (Exception e) {
            log.error("验证Token失败: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public String refreshToken(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            claims.setIssuedAt(new Date());
            return generateToken(claims);
        } catch (Exception e) {
            log.error("刷新Token失败: {}", e.getMessage());
            throw new BusinessException(ErrorCode.USER_TOKEN_INVALID, "刷新Token失败");
        }
    }

    @Override
    public void invalidateToken(String token) {
        // JWT是无状态的，不能主动使其失效
        // 在实际应用中，可以通过维护一个token黑名单（可以存储在Redis中）来实现
        log.warn("JWT是无状态的，不能主动使其失效");
    }

    @Override
    public boolean isTokenExpired(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            Date expiration = claims.getExpiration();
            return expiration.before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        } catch (Exception e) {
            log.error("检查Token是否过期失败: {}", e.getMessage());
            return true;
        }
    }

    /**
     * 从Token中获取Claims
     * @param token Token
     * @return Claims
     */
    private Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 根据Claims生成Token
     * @param claims Claims
     * @return Token
     */
    private String generateToken(Map<String, Object> claims) {
        Date createdDate = new Date();
        Date expirationDate = new Date(createdDate.getTime() + expiration);
        
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }
} 