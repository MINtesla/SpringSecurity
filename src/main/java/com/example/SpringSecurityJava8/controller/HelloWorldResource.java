package com.example.SpringSecurityJava8.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldResource {
 // by default everything is authenticated even you have url at that end point or not
    @GetMapping("/hello")
    public String hello(){
        return "Hello world !";
    }
}
