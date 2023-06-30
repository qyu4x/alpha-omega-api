package com.alphaomega.alphaomegarestfulapi.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/alpha/v1/test")
public class TestAuthController {

    private final static Logger log = LoggerFactory.getLogger(TestAuthController.class);


    @PreAuthorize("hasRole('USER') or hasRole('INSTRUCTOR')")
    @GetMapping("/user")
    public String user() {
        log.info("test from user");
        return "Test from user";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public String admin() {
        log.info("test from admin");
        return "Test from admin";
    }

}
