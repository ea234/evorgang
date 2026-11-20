
package de.ea234.evorgang.config;

import java.util.function.Supplier;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.csrf.CsrfTokenRequestHandler;
import org.springframework.security.web.csrf.XorCsrfTokenRequestAttributeHandler;
import org.springframework.util.StringUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class SpaCsrfTokenRequestHandler implements CsrfTokenRequestHandler
{
  private final CsrfTokenRequestHandler plain = new CsrfTokenRequestAttributeHandler();

  private final CsrfTokenRequestHandler xor   = new XorCsrfTokenRequestAttributeHandler();

  @Override
  public void handle( HttpServletRequest request, HttpServletResponse response, Supplier< CsrfToken > csrfToken )
  {
    // BREACH-Schutz für normale Spring-Anfragen beibehalten
    xor.handle( request, response, csrfToken );

    // Erzwingt das Laden und Setzen des CSRF-Cookies
    csrfToken.get();
  }

  @Override
  public String resolveCsrfTokenValue( HttpServletRequest request, CsrfToken csrfToken )
  {
    String headerValue = request.getHeader( csrfToken.getHeaderName() );

    if ( StringUtils.hasText( headerValue ) )
    {
      return plain.resolveCsrfTokenValue( request, csrfToken );
    }

    return xor.resolveCsrfTokenValue( request, csrfToken );
  }
}
