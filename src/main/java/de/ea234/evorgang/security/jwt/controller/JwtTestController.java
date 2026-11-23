package de.ea234.evorgang.security.jwt.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/security/jwt/test")
public class JwtTestController
{
  private static final Logger log =
      LoggerFactory.getLogger(JwtTestController.class);

  /*
   * http://localhost:8080/api/security/jwt/test/ping
   */
  
  /*
   * ****************************************************************************
   * PING
   * ****************************************************************************
   */
  @GetMapping(path = "/ping", produces = MediaType.TEXT_PLAIN_VALUE)
  public String ping()
  {
    log.info("JwtTestController ping " + System.currentTimeMillis());

    return "RestControllerVorgang " + System.currentTimeMillis();
  }
}