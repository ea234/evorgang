package de.ea234.evorgang.security.jwt.service;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private final SecretKey key = Keys.hmacShaKeyFor( "MeinGeheimerSchluessel12345678901234567890".getBytes() );

    public String generateToken( String username ) 
    {
       return generateToken( username, List.of() );
    }

    public String generateToken( String username, Collection<String> authorities ) 
    {
        long now = System.currentTimeMillis();

        return Jwts.builder()
            .subject(username)
            .claim("authorities", authorities)
            .issuedAt(new Date(now))
            .expiration(new Date(now + 1000 * 60 * 60))
            .signWith(key)
            .compact();
    }

    public SecretKey getKey() {
        return key;
    }
}