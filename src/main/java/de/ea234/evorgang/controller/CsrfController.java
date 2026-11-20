
package de.ea234.evorgang.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/*
 * Angular ruft diesen Endpunkt vor der Anmeldung auf. Dadurch wird sichergestellt, dass das CSRF-Cookie vorhanden ist.
 */

@RestController
public class CsrfController
{
  @GetMapping("/csrf")
  public ResponseEntity<Void> csrf(CsrfToken csrfToken)
  {
    // Die Übergabe erzwingt das Laden des CSRF-Tokens.
    return ResponseEntity.noContent().build();
  }
}
