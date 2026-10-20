package com.circlesHealth.admin.security;

import com.circlesHealth.admin.entity.Permission;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class JwtProvider {

	private static final Logger LOGGER = LoggerFactory.getLogger(JwtProvider.class);
	
	private static final String PERMISSIONS_KEY = "permissions";
    private static final String BEARER = "Bearer";

    @Value("${security.jwt.token.secret-key}")
    private String secretKey;

    @Value("${security.jwt.temp.token.secret-key}")
    private String tempTokenSecretKey;

    @Value("${security.jwt.refresh.token.secret-key}")
    private String refreshTokenSecretKey;

    @Value("${security.jwt.token.expiration}")
    private long validityInMilliseconds;

    @Value("${security.jwt.temp.token.expiration}")
    private long tempTokenValidity;

    @Value("${security.jwt.refresh.token.expiration}")
    private long refreshTokenValidity;

    public String createToken(String subject, List<Permission> permissions) {
        return createJwtToken(subject, permissions, validityInMilliseconds, secretKey);
    }

    public String createTempToken(String subject, List<Permission> permissions) {
        return createJwtToken(subject, permissions, tempTokenValidity, tempTokenSecretKey);
    }

    public String createRefreshToken(String subject, List<Permission> permissions) {
        return createJwtToken(subject, permissions, refreshTokenValidity, refreshTokenSecretKey);
    }

    private String createJwtToken(String subject, List<Permission> permissions, long validityInMilliseconds, String secretKey) {
        Claims claims = Jwts.claims().setSubject(subject);
        claims.put(PERMISSIONS_KEY, permissions.stream().map(permission -> new SimpleGrantedAuthority(permission.getAuthority()))
                .collect(Collectors.toList()));
        Date now = new Date();
        Date expiresAt = new Date(now.getTime() + validityInMilliseconds);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiresAt)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public boolean isValidToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public boolean isTempTokenValid(String token) {
        try {
            Jwts.parser().setSigningKey(tempTokenSecretKey).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public boolean isRefreshTokenValid(String token) {
        try {
            Jwts.parser().setSigningKey(refreshTokenSecretKey).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String getUserSubject(String token) {
        return Jwts.parser().setSigningKey(secretKey)
                .parseClaimsJws(token).getBody().getSubject();
    }

    public Optional<String> getBearerToken(String headerVal) {
    	LOGGER.info("Authorization in header value of getBearerToken method : "+headerVal);
        if (headerVal != null && headerVal.startsWith(BEARER)) {
        	LOGGER.info("Bearer token value in Authorization : "+Optional.of(headerVal.replace(BEARER, "").trim()).get());
            return Optional.of(headerVal.replace(BEARER, "").trim());
        }
        return Optional.empty();
    }

    public List<GrantedAuthority> getPermissions(String token) {
        List<Map<String, String>> permissionClaims = Jwts.parser().setSigningKey(secretKey)
                .parseClaimsJws(token).getBody().get(PERMISSIONS_KEY, List.class);
        return permissionClaims.stream().map(permissionClaim ->
                new SimpleGrantedAuthority(permissionClaim.get("authority")))
                .collect(Collectors.toList());
    }
}
