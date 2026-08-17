package de.ea234.evorgang.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class HelloController {
    
    @GetMapping("/hello")
    public String hello() {
        return "Hello World from Spring Boot!";
    }


/*
 * http://localhost:8080/admin1
 * http://localhost:8080/admin2
 */
    @GetMapping("/admin1")
    public String hello1() {
        return "Hello World from Spring admin1!";
    }
    @GetMapping("/admin2")
    public String hello2() {
        return "Hello World from Spring admin2!";
    }
}