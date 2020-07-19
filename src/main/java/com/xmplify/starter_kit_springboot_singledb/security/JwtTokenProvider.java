package com.xmplify.starter_kit_springboot_singledb.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xmplify.starter_kit_springboot_singledb.payload.JwtCustomPayload;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpirationInMs}")
    private int jwtExpirationInMs;

    @Value("${secureKey}")
    private String key;

    @Value("${secureInitVector}")
    private String initVector;

    @Autowired
    private ObjectMapper objectMapper;

    public String generateToken(Authentication authentication, String signInAs) throws JsonProcessingException {

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        JwtCustomPayload jwtCustomPayload = new JwtCustomPayload(userPrincipal.getId(), signInAs);
        return EncrypterDecrypter.encrypt(key, initVector, Jwts.builder()
                .setSubject(objectMapper.writeValueAsString(jwtCustomPayload))
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact());
    }

    public String getUserIdFromJWT(String token) throws IOException {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();

        JwtCustomPayload jwtCustomPayload = objectMapper.readValue(claims.getSubject(), JwtCustomPayload.class);
        return jwtCustomPayload.getUserId();
    }

    public String getRoleFromJWT(String token) throws IOException {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();

        JwtCustomPayload jwtCustomPayload = objectMapper.readValue(claims.getSubject(), JwtCustomPayload.class);
        return jwtCustomPayload.getRoleType();
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            logger.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty.");
        }
        return false;
    }
}
