package com.docconnect.backend.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class StartupConfigChecker implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(StartupConfigChecker.class);
    
    @Autowired
    private Environment environment;
    
    @Value("${spring.datasource.url}")
    private String databaseUrl;
    
    @Value("${server.servlet.context-path}")
    private String contextPath;
    
    @Value("${server.port}")
    private int serverPort;
    
    @Value("${jwt.secret:default-secret}")
    private String jwtSecret;

    @Override
    public void run(String... args) {
        logger.info("=== DocConnect Configuration Check ===");
        logger.info("Active profiles: {}", String.join(", ", environment.getActiveProfiles()));
        logger.info("Database URL: {}", databaseUrl);
        logger.info("Context Path: {}", contextPath);
        logger.info("Server Port: {}", serverPort);
        logger.info("JWT Authentication: Enabled");
        
        // URL paths to check
        logger.info("API Base URL: http://localhost:{}{}", serverPort, contextPath);
        logger.info("Auth Endpoints:");
        logger.info(" - Register: POST http://localhost:{}{}/auth/register", serverPort, contextPath);
        logger.info(" - Login: POST http://localhost:{}{}/auth/login", serverPort, contextPath);
        logger.info(" - Get User: GET http://localhost:{}{}/auth/user", serverPort, contextPath);
        
        logger.info("Test Endpoints:");
        logger.info(" - Ping: GET http://localhost:{}{}/test/ping", serverPort, contextPath);
        logger.info(" - Config: GET http://localhost:{}{}/test/config", serverPort, contextPath);
        
        logger.info("=== Configuration Check Complete ===");
    }
}