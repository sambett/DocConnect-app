package com.docconnect.backend.controller;

import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Controller for development-only data operations.
 * This controller is intended only for development and testing purposes.
 * It is not meant to be used in production environments.
 */
@RestController
@RequestMapping("/test-data")
@Profile("dev") // Only active in development profile
public class TestDataController {

    /**
     * Returns confirmation that the test data controller is accessible.
     * This endpoint is only available in development environments.
     *
     * @return A simple status message
     */
    @GetMapping("/status")
    public ResponseEntity<Map<String, String>> getStatus() {
        return ResponseEntity.ok(Map.of(
            "status", "Test data controller is active",
            "environment", "development",
            "message", "This endpoint is for development purposes only"
        ));
    }
}
