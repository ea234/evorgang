package de.ea234.evorgang;

import static org.hamcrest.Matchers.startsWith;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.springframework.mock.web.MockHttpSession;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("h2")
class PingSecurityTest
{
  @Autowired
  private MockMvc mockMvc;

  @Test
  void ping_with_real_login() throws Exception
  {
    // ---------------------------------------------------------
    // 1. Login mit echtem Benutzer und echtem Passwort
    // ---------------------------------------------------------

    var loginResult = mockMvc.perform( post( "/login" )
        .with( csrf() ).param( "username", "admin" ).param( "password", "geheim" ) )
        .andExpect( status().is3xxRedirection() )
        .andReturn();

    // Session nach dem Login übernehmen
    MockHttpSession session = (MockHttpSession) loginResult.getRequest().getSession();

    // ---------------------------------------------------------
    // 2. Geschützten REST-Endpunkt aufrufen
    // ---------------------------------------------------------

    mockMvc.perform( get( "/api/test/ping" ).session( session ).with( csrf() ) )
    .andExpect( status().isOk() ).andExpect( content().contentTypeCompatibleWith( MediaType.TEXT_PLAIN ) )
    .andExpect( content().string( startsWith( "RestControllerVorgang" ) ) );
  }

  @Test
  void ping_without_authentication_should_redirect_to_login() throws Exception
  {
    mockMvc.perform( get( "/api/test/ping" ) ).andExpect( status().is3xxRedirection() ).andExpect( redirectedUrl( "/login" ) );
  }

  @Test
  void admin1_with_user_role_should_be_forbidden() throws Exception
  {
    mockMvc.perform( get( "/admin1/test" ).with( user( "user" ).roles( "USER" ) ).with( csrf() ) ).andExpect( status().isForbidden() );
  }
}
