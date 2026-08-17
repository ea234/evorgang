package de.ea234.evorgang.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.stereotype.Controller;



@RestController
@RequestMapping("/api/test")
public class ControllerTest1 {
	
 private static final Logger log = LoggerFactory.getLogger( ControllerTest1.class );

	
	@GetMapping( "/t1" )
	public String hallo()
	{
		return "hallo " + System.currentTimeMillis();
	}

	  /*
	   * ****************************************************************************
	   * PING
	   * ****************************************************************************
	   */
	  @GetMapping(path = "/ping", produces = MediaType.TEXT_PLAIN_VALUE)
	  public String ping()
	  {
	    // http://localhost:8080/ping

	    log.info( "RestControllerVorgang ping " + System.currentTimeMillis() );

	    return "RestControllerVorgang " + System.currentTimeMillis();
	  }

}
