package com.docconnect.backend.controller;

import com.docconnect.backend.model.User;
import com.docconnect.backend.service.FirebaseAuthService;
import com.docconnect.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    
    private final UserService userService;
    private final FirebaseAuthService firebaseAuthService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user, @RequestHeader("Authorization") String tokenHeader) {
        logger.info("Received registration request for user: {}", user.getEmail());
        
        try {
            // Extract token from Authorization header (Bearer token)
            String token = null;
            if (tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
                token = tokenHeader.substring(7);
            }
            
            if (token != null) {
                // If token is provided, verify it and use the UID from it
                String uid = firebaseAuthService.verifyToken(token);
                user.setUid(uid);
                logger.info("Firebase UID verified: {}", uid);
            } else {
                logger.warn("No Firebase token provided for user registration");
            }
            
            User savedUser = userService.createUser(user);
            logger.info("User successfully registered: {}", savedUser.getEmail());
            
            Map<String, Object> response = new HashMap<>();
            response.put("user", savedUser);
            response.put("message", "User registered successfully");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error registering user: {}", e.getMessage(), e);
            Map<String, String> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest, @RequestHeader("Authorization") String tokenHeader) {
        logger.info("Processing login request for: {}", loginRequest.get("email"));
        
        try {
            String email = loginRequest.get("email");
            String token = null;
            
            // Extract token from Authorization header
            if (tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
                token = tokenHeader.substring(7);
            }
            
            if (token == null) {
                throw new IllegalArgumentException("No valid authorization token provided");
            }
            
            // Verify the Firebase token
            String uid = firebaseAuthService.verifyToken(token);
            logger.info("Firebase token verified for UID: {}", uid);
            
            // Get user from database
            Optional<User> userOpt = userService.findByEmail(email);
            
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                
                // Update the Firebase UID if it's missing
                if (user.getUid() == null || user.getUid().isEmpty()) {
                    user.setUid(uid);
                    userService.updateUser(user);
                    logger.info("Updated missing UID for user: {}", email);
                }
                
                Map<String, Object> response = new HashMap<>();
                response.put("user", user);
                response.put("message", "Login successful");
                
                return ResponseEntity.ok(response);
            } else {
                logger.warn("User not found in database: {}", email);
                return ResponseEntity.status(404).body(Map.of("error", "User not found"));
            }
            
        } catch (Exception e) {
            logger.error("Login error: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/user")
    public ResponseEntity<?> getCurrentUser(@RequestHeader("Authorization") String tokenHeader) {
        logger.info("Getting current user details");
        
        try {
            // Extract token
            String token = null;
            if (tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
                token = tokenHeader.substring(7);
            }
            
            if (token == null) {
                throw new IllegalArgumentException("No valid authorization token provided");
            }
            
            // Verify token and get user ID
            String uid = firebaseAuthService.verifyToken(token);
            String email = firebaseAuthService.getUserEmail(token);
            
            logger.info("Getting user details for UID: {} and email: {}", uid, email);
            
            // Find user by UID or email
            Optional<User> userOpt = userService.findByUid(uid);
            if (userOpt.isEmpty()) {
                userOpt = userService.findByEmail(email);
            }
            
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                return ResponseEntity.ok(user);
            } else {
                logger.warn("User not found for UID: {} and email: {}", uid, email);
                return ResponseEntity.status(404).body(Map.of("error", "User not found"));
            }
            
        } catch (Exception e) {
            logger.error("Error getting current user: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/verify-token")
    public ResponseEntity<?> verifyToken(@RequestHeader("Authorization") String tokenHeader) {
        logger.info("Verifying token");
        
        try {
            // Extract token
            String token = null;
            if (tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
                token = tokenHeader.substring(7);
            }
            
            if (token == null) {
                throw new IllegalArgumentException("No valid authorization token provided");
            }
            
            // Verify token
            String uid = firebaseAuthService.verifyToken(token);
            logger.info("Token verified for UID: {}", uid);
            
            return ResponseEntity.ok(Map.of("valid", true, "uid", uid));
        } catch (Exception e) {
            logger.error("Token verification failed: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(Map.of("valid", false, "error", e.getMessage()));
        }
    }
    
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        // The actual logout happens on the client side with Firebase
        // Here we just return a success message
        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
    }
}
