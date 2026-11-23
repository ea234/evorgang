package de.ea234.evorgang.security.jwt.dto;

public record LoginRequest(
    String username,
    String password
) {}
