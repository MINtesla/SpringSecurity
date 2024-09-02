package com.example.SpringSecurityJava8.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import java.util.Arrays;


@RestController
public class TodoResource {

    private Logger logger = LoggerFactory.getLogger(TodoResource.class);

    @GetMapping("/user/{username}")
    public String retriveTodoForSingleUser(@PathVariable String username){
        return username;
    }
    @GetMapping("/users/{usernames}")
    public String retriveTodoForMultipleUsers(@PathVariable String usernames){
        return Arrays.stream(usernames.split(",")).toArray().toString();
    }

    @PostMapping("/saveuser/{username}")
//    @PreAuthorize("hasRole('USER')")
//    @PostAuthorize("returnObject.username == 'user'")
    @RolesAllowed({"ADMIN1","USER"})
    @Secured({"ADMIN","ROLE_USER"}) //  this check whole authority
    public String saveForMultipleUsers(@PathVariable String username){
        return username+" saved successfully....!";
    }

}


