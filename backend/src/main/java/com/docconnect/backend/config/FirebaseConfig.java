package com.docconnect.backend.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import jakarta.annotation.PostConstruct;
import java.io.IOException;

@Configuration
public class FirebaseConfig {

    private static final Logger logger = LoggerFactory.getLogger(FirebaseConfig.class);

    @Value("${firebase.service-account.path}")
    private String serviceAccountPath;

    @Value("${firebase.enabled:false}")
    private boolean firebaseEnabled;

    @PostConstruct
    public void initialize() {
        try {
            if (!firebaseEnabled) {
                logger.info("Firebase initialization skipped (disabled by configuration)");
                return;
            }

            if (FirebaseApp.getApps().isEmpty()) {
                logger.info("Initializing Firebase application");
                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(
                                new ClassPathResource(serviceAccountPath.replace("classpath:", "")).getInputStream()))
                        .build();
                FirebaseApp.initializeApp(options);
                logger.info("Firebase application initialized successfully");
            }
        } catch (IOException e) {
            logger.error("Error initializing Firebase", e);
        }
    }
}
