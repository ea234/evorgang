package de.ea234.evorgang.security.jwt.service;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private final SecretKey key = Keys.hmacShaKeyFor( "MeinGeheimerSchluessel12345678901234567890".getBytes()  );

    public String generateToken(String username) {

        long now = System.currentTimeMillis();

        return Jwts.builder()
            .subject(username)
            .issuedAt(new Date(now))
            .expiration(new Date(now + 1000 * 60 * 60))
            .signWith(key)
            .compact();
    }

    public SecretKey getKey() {
        return key;
    }
}