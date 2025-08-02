package com.docconnect.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Controller for testing authentication and authorization.
 * This controller provides endpoints for verifying that security
 * configurations are working correctly.
 */
@RestController
@RequestMapping("/auth-test")
public class AuthTestController {

    /**
     * Public endpoint that doesn't require authentication.
     * Can be used to verify the API is accessible.
     *
     * @return A simple response with authentication status
     */
    @GetMapping("/public")
    public ResponseEntity<Map<String, String>> publicEndpoint() {
        return ResponseEntity.ok(Map.of(
            "message", "This is a public endpoint",
            "authenticated", "false",
            "authorization", "none required"
        ));
    }
    
    /**
     * Protected endpoint that requires authentication.
     * Can be used to verify that JWT authentication is working.
     *
     * @return A simple response confirming authentication
     */
    @GetMapping("/protected")
    public ResponseEntity<Map<String, String>> protectedEndpoint() {
        return ResponseEntity.ok(Map.of(
            "message", "This is a protected endpoint",
            "authenticated", "true",
            "authorization", "JWT required"
        ));
    }
    
    /**
     * Role-specific endpoint that requires PROFESSOR role.
     * Can be used to verify that role-based authorization is working.
     *
     * @return A simple response confirming professor authorization
     */
    @GetMapping("/professor-only")
    @PreAuthorize("hasRole('PROFESSOR')")
    public ResponseEntity<Map<String, String>> professorOnlyEndpoint() {
        return ResponseEntity.ok(Map.of(
            "message", "This endpoint is only accessible to professors",
            "authenticated", "true",
            "authorization", "PROFESSOR role required"
        ));
    }
}
