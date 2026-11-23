package de.ea234.evorgang.security.jwt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class JwtSecurityConfig {

  /*
   * spring security muss den jwt-claim auch als authorities interpretieren.
   * ohne diese änderung wäre der claim zwar im token vorhanden, würde aber nicht automatisch als berechtigung verwendet.
   */
  
    @Bean
    @Order(2)
    SecurityFilterChain jwtFilterChain(
        HttpSecurity http
    ) throws Exception {

        http
            .securityMatcher("/api/security/jwt/**")

            .csrf(csrf -> csrf.disable())

            .sessionManagement(session ->
                session.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS
                )
            )

            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    HttpMethod.POST,
                    "/api/security/jwt/login"
                ).permitAll()
                .requestMatchers("/api/security/jwt/test/admin").hasAuthority("EVG_ADMIN")
                .requestMatchers("/api/security/jwt/test/user").hasAuthority("EVG_USER")
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth ->
                oauth.jwt(jwt ->
                    jwt.jwtAuthenticationConverter(
                        jwtAuthenticationConverter()
                    )
                )
            );

        return http.build();
    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() 
    {
        JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();

        authoritiesConverter.setAuthoritiesClaimName("authorities");

        // Authorities ohne automatisches ROLE_-Präfix übernehmen
        authoritiesConverter.setAuthorityPrefix("");

        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();

        converter.setJwtGrantedAuthoritiesConverter( authoritiesConverter );

        return converter;
    }
}