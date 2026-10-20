package com.circles.health.patient.security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class JwtProvider {

    private static final String BEARER = "Bearer";
    
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtProvider.class);

    @Value("${security.jwt.token.secret-key}")
    private String secretKey;

    public boolean isValidToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
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
}
