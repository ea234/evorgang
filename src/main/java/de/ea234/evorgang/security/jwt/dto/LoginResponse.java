package de.ea234.evorgang.security.jwt.dto;

public record LoginResponse(
    String token,
    String tokenType
) {}