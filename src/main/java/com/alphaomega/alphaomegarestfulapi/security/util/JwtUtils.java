package com.alphaomega.alphaomegarestfulapi.security.util;

import com.alphaomega.alphaomegarestfulapi.entity.User;
import com.alphaomega.alphaomegarestfulapi.payload.request.SignupRequest;
import com.alphaomega.alphaomegarestfulapi.security.service.UserDetailsImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.TimeUnit;

@Component
public class JwtUtils {

    private final static Logger log = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${firebox.jwt.secret-key}")
    private String secretKey;

    @Value("${firebox.jwt.expiration-day}")
    private Integer expirationDay;

    @Getter
    @Value("${firebox.jwt.token-type}")
    private String tokenType;


    public String generateRegisterJwtToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(Date.from(LocalDateTime.now().toInstant(ZoneOffset.ofHours(+7))))
                .setExpiration(Date.from(LocalDateTime.now().toInstant(ZoneOffset.ofHours(+7)).plusSeconds(TimeUnit.DAYS.toSeconds(this.expirationDay))))
                .signWith(SignatureAlgorithm.HS512, this.secretKey)
                .compact();
    }

    public String generateJwtToken(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject(userDetails.getEmail())
                .setIssuedAt(Date.from(LocalDateTime.now().toInstant(ZoneOffset.ofHours(+7))))
                .setExpiration(Date.from(LocalDateTime.now().toInstant(ZoneOffset.ofHours(+7)).plusSeconds(TimeUnit.DAYS.toSeconds(this.expirationDay))))
                .signWith(SignatureAlgorithm.HS512, this.secretKey)
                .compact();
    }


    public String getEmailFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(this.secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    public Boolean validateJwtToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            return true;
        } catch (SignatureException exception) {
            log.error("Invalid JWT Token signature: {}", exception.getMessage());
        } catch (MalformedJwtException exception) {
            log.error("Invalid JWT Token: {}", exception.getMessage());
        } catch (ExpiredJwtException exception) {
            log.error("JWT Token is expired: {} ", exception.getMessage());
        } catch (UnsupportedJwtException exception) {
            log.error("JWT Token is unsupported: {}", exception.getMessage());
        } catch (IllegalArgumentException exception) {
            log.error("Jwt claims string is empty: {}", exception.getMessage());
        }
        return false;
    }

}
