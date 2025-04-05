package com.docconnect.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthTestController {

    @GetMapping("/public/hello")
    public ResponseEntity<Map<String, String>> publicHello() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "This is a public endpoint - no authentication required!");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/protected/user-info")
    public ResponseEntity<Map<String, Object>> userInfo() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        Map<String, Object> response = new HashMap<>();
        response.put("userId", auth.getPrincipal());
        response.put("authenticated", auth.isAuthenticated());
        response.put("authorities", auth.getAuthorities());
        
        return ResponseEntity.ok(response);
    }
}
