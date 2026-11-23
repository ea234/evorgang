package de.ea234.evorgang.security.jwt.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.ea234.evorgang.security.jwt.dto.LoginRequest;
import de.ea234.evorgang.security.jwt.dto.LoginResponse;
import de.ea234.evorgang.security.jwt.service.JwtService;

@RestController
@RequestMapping("/api/security/jwt")
public class JwtLoginController {

    private final AuthenticationManager authenticationManager;
    
    private final JwtService jwtService;

    public JwtLoginController( AuthenticationManager authenticationManager, JwtService jwtService ) 
    {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login( @RequestBody LoginRequest request ) 
    {
        try {

            Authentication authentication =
                authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                    )
                );

            /*
             * Die Authorities aus dem Authentication-Objekt uebernehmen.
             */
            List<String> authorities =
                authentication.getAuthorities()
                    .stream()
                    .map( GrantedAuthority::getAuthority )
                    .toList();

            String token = jwtService.generateToken(
                authentication.getName(),
                authorities
            );

            return ResponseEntity.ok(
                new LoginResponse(token, "Bearer")
            );

        } catch (AuthenticationException exception) {

            return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body("Benutzername oder Passwort falsch");
        }
    }
}