package com.docconnect.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Test controller for basic API connectivity testing.
 * This controller can be used in development environments to verify
 * that the API is responding correctly.
 */
@RestController
public class TestController {

    /**
     * Simple health check endpoint that returns a success message.
     * This can be used to verify that the application is running correctly.
     *
     * @return A simple success message
     */
    @GetMapping("/test")
    public String test() {
        return "DocConnect API is running";
    }
}
