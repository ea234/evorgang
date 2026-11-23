
package de.ea234.evorgang;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import de.ea234.evorgang.security.jwt.service.JwtService;

import io.jsonwebtoken.Jwts;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles ( "h2" )
class JwtSecurityTest
{
  private static final String TEST_URL = "/api/security/jwt/test/ping";

  @Autowired
  private MockMvc             mockMvc;

  @Autowired
  private JwtService          jwtService;

  /*
   * Gültiges JWT
   */
  @Test
  void accessWithValidTokenShouldReturn200() throws Exception
  {

    String token = jwtService.generateToken( "admin" );

    mockMvc.perform( get( TEST_URL ).header( "Authorization", "Bearer " + token ) ).andExpect( status().isOk() );
  }

  /*
   * Kein JWT vorhanden
   */
  @Test
  void accessWithoutTokenShouldReturn401() throws Exception
  {

    mockMvc.perform( get( TEST_URL ) ).andExpect( status().isUnauthorized() );
  }

  /*
   * Manipulierte JWT-Signatur
   */
  @Test
  void accessWithInvalidSignatureShouldReturn401() throws Exception
  {

    String token = jwtService.generateToken( "admin" );

    String[] parts = token.split( "\\." );

    String tamperedToken = parts[ 0 ] + "." + parts[ 1 ] + ".AAAA";

    mockMvc.perform( get( TEST_URL ).header( "Authorization", "Bearer " + tamperedToken ) ).andExpect( status().isUnauthorized() );
  }

  /*
   * Abgelaufenes JWT
   */
  @Test
  void accessWithExpiredTokenShouldReturn401() throws Exception
  {

    long now = System.currentTimeMillis();

    String expiredToken = Jwts.builder().subject( "admin" ).issuedAt( new Date( now - 7200000 ) ).expiration( new Date( now - 3600000 ) ).signWith( jwtService.getKey() ).compact();

    mockMvc.perform( get( TEST_URL ).header( "Authorization", "Bearer " + expiredToken ) ).andExpect( status().isUnauthorized() );
  }

  /*
   * Login mit gültigen Zugangsdaten
   */
  @Test
  void loginWithValidCredentialsShouldReturn200() throws Exception
  {

    String loginJson = """
        {
            "username": "admin",
            "password": "geheim"
        }
        """;

    mockMvc.perform( post( "/api/security/jwt/login" ).contentType( MediaType.APPLICATION_JSON ).content( loginJson ) ).andExpect( status().isOk() ).andExpect( jsonPath( "$.token" ).isNotEmpty() ).andExpect( jsonPath( "$.tokenType" ).value( "Bearer" ) );
  }
  
  
  @Test
  void accessWithAdminAuthorityShouldReturn200() throws Exception
  {
      String token = jwtService.generateToken(
          "admin",
          List.of("EVG_ADMIN")
      );

      mockMvc.perform(
          get("/api/security/jwt/test/admin")
              .header("Authorization", "Bearer " + token)
      ).andExpect(status().isOk());
  }  
  
  @Test
  void accessWithoutAdminAuthorityShouldReturn403() throws Exception
  {
      String token = jwtService.generateToken(
          "admin",
          List.of("EVG_USER")
      );

      mockMvc.perform(
          get("/api/security/jwt/test/admin")
              .header("Authorization", "Bearer " + token)
      ).andExpect(status().isForbidden());
  }  
}
