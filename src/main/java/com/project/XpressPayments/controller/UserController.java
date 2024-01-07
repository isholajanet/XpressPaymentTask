package com.project.XpressPayments.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @PostMapping("/")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> register(String username){
        return ResponseEntity.ok("Registered successfully");
    }
    


}
