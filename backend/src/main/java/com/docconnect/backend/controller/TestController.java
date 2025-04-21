package com.docconnect.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/ping")
    public ResponseEntity<?> ping() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "API is up and running");
        response.put("timestamp", System.currentTimeMillis());
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/config")
    public ResponseEntity<?> config() {
        Map<String, Object> response = new HashMap<>();
        response.put("applicationName", "DocConnect");
        response.put("apiVersion", "1.0");
        response.put("serverTime", System.currentTimeMillis());
        response.put("contextPath", "/api");
        
        return ResponseEntity.ok(response);
    }
}