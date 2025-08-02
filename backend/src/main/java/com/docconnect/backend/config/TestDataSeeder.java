package com.docconnect.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Configuration class for seeding test data in development environments.
 * This class is designed to be activated only in development profiles
 * and should not be used in production.
 */
@Configuration
@Profile("dev") // Only active in development profile
public class TestDataSeeder {
    
    // Implementation removed - production environment should use
    // proper data initialization mechanisms, not test data
}
