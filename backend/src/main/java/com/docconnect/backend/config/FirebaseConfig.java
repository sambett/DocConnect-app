package com.docconnect.backend.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import jakarta.annotation.PostConstruct;
import java.io.IOException;

@Configuration
public class FirebaseConfig {

    @Value("${firebase.service-account.path:}")
    private Resource serviceAccountResource;

    private FirebaseApp firebaseApp;

    @PostConstruct
    public void initialize() {
        try {
            if (serviceAccountResource != null && serviceAccountResource.exists()) {
                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccountResource.getInputStream()))
                        .build();

                if (FirebaseApp.getApps().isEmpty()) {
                    firebaseApp = FirebaseApp.initializeApp(options);
                } else {
                    firebaseApp = FirebaseApp.getApps().get(0);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error initializing Firebase", e);
        }
    }

    @Bean
    public FirebaseApp getFirebaseApp() {
        return firebaseApp;
    }
}
