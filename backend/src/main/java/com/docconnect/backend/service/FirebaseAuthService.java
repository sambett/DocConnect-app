package com.docconnect.backend.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FirebaseAuthService {

    private static final Logger logger = LoggerFactory.getLogger(FirebaseAuthService.class);

    @Value("${firebase.enabled:false}")
    private boolean firebaseEnabled;

    public String verifyToken(String idToken) throws FirebaseAuthException {
        if (!firebaseEnabled) {
            logger.warn("Firebase authentication is disabled. Skipping token verification.");
            return "fake-uid-for-development";
        }
        
        // Verify the ID token
        FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
        
        // Return the UID
        return decodedToken.getUid();
    }
    
    public String getUserEmail(String idToken) throws FirebaseAuthException {
        if (!firebaseEnabled) {
            logger.warn("Firebase authentication is disabled. Returning test email.");
            return "test@example.com";
        }
        
        FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
        return decodedToken.getEmail();
    }
}
