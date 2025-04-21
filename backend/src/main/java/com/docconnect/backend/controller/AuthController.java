package com.docconnect.backend.controller;

import com.docconnect.backend.model.User;
import com.docconnect.backend.model.Professor;
import com.docconnect.backend.service.ProfessorService;
import com.docconnect.backend.service.AuthenticationService;
import com.docconnect.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    
    private final UserService userService;
    private final AuthenticationService authService;
    private final ProfessorService professorService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody Map<String, Object> registrationData) {
        String email = (String) registrationData.get("email");
        String fullName = (String) registrationData.get("fullName");
        String password = (String) registrationData.get("password");
        String roleStr = (String) registrationData.get("role");
        
        logger.info("Received registration request for user: {}", email);
        
        // Validate required fields
        if (email == null || fullName == null || password == null || roleStr == null) {
            logger.error("Missing required registration fields");
            return ResponseEntity.badRequest().body(Map.of("error", "Email, fullName, password, and role are required"));
        }
        
        // Convert role string to enum
        User.Role role;
        try {
            role = User.Role.valueOf(roleStr.toUpperCase());
            logger.info("Role converted successfully: {}", role);
        } catch (IllegalArgumentException e) {
            logger.error("Invalid role value: {}", roleStr);
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid role: " + roleStr));
        }
        
        try {
            // Create user object
            User user = new User();
            user.setEmail(email);
            user.setFullName(fullName);
            user.setRole(role);
            
            // Register user with password
            User savedUser = authService.register(user, password);
            logger.info("User successfully registered: {}", savedUser.getEmail());
            
            // If this is a professor, create the professor record
            if (role == User.Role.PROFESSOR) {
                logger.info("Creating professor record for user: {}", savedUser.getEmail());
                
                // Extract professor-specific fields from the registration data
                String department = registrationData.containsKey("department") ? 
                    (String) registrationData.get("department") : "Not specified";
                
                String officeLocation = registrationData.containsKey("officeLocation") ?
                    (String) registrationData.get("officeLocation") : "Not specified";
                
                String workingHours = registrationData.containsKey("workingHours") ?
                    (String) registrationData.get("workingHours") : "Not specified";
                
                logger.info("Professor fields extracted - Department: {}, Office: {}, Hours: {}",
                           department, officeLocation, workingHours);
                
                Professor professor = new Professor();
                professor.setUser(savedUser);
                professor.setDepartment(department);
                professor.setOfficeLocation(officeLocation);
                professor.setWorkingHours(workingHours);
                professor.setEmailVerified(true); // For simplicity, setting as verified
                
                Professor savedProfessor = professorService.createProfessor(professor);
                logger.info("Professor record created successfully: {}", savedProfessor.getId());
            }
            
            // Generate a token for the newly registered user
            String token = authService.authenticate(email, password)
                    .orElseThrow(() -> new IllegalStateException("Failed to authenticate newly registered user"));
            
            Map<String, Object> response = new HashMap<>();
            response.put("user", savedUser);
            response.put("token", token);
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
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        logger.info("Processing login request for: {}", loginRequest.get("email"));
        
        try {
            String email = loginRequest.get("email");
            String password = loginRequest.get("password");
            
            if (email == null || password == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Email and password are required"));
            }
            
            // Authenticate the user and get a token
            Optional<String> tokenOpt = authService.authenticate(email, password);
            
            if (tokenOpt.isPresent()) {
                // Get user from database
                Optional<User> userOpt = userService.findByEmail(email);
                
                if (userOpt.isPresent()) {
                    User user = userOpt.get();
                    
                    Map<String, Object> response = new HashMap<>();
                    response.put("user", user);
                    response.put("token", tokenOpt.get());
                    response.put("message", "Login successful");
                    
                    return ResponseEntity.ok(response);
                }
            }
            
            // Authentication failed
            return ResponseEntity.status(401).body(Map.of("error", "Invalid email or password"));
            
        } catch (Exception e) {
            logger.error("Login error: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/user")
    public ResponseEntity<?> getCurrentUser(
        @RequestHeader(value = "Authorization", required = false) String tokenHeader,
        HttpServletRequest request
    ) {
        logger.info("Getting current user details");
        
        try {
            // Extract token
            String token = null;
            if (tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
                token = tokenHeader.substring(7);
            }
            
            if (token == null) {
                // For development testing
                logger.warn("No authorization token provided");
                // Use email from request parameters if available
                String email = request.getParameter("email");
                if (email != null) {
                    logger.info("Using email from request parameter: {}", email);
                    
                    // Find user by email
                    Optional<User> userOpt = userService.findByEmail(email);
                    if (userOpt.isPresent()) {
                        User user = userOpt.get();
                        return ResponseEntity.ok(user);
                    } else {
                        return ResponseEntity.status(404).body(Map.of("error", "User not found"));
                    }
                } else {
                    return ResponseEntity.status(400).body(Map.of("error", "No email provided and no token available"));
                }
            }
            
            // Validate token and get user
            Optional<User> userOpt = authService.validateTokenAndGetUser(token);
            
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                return ResponseEntity.ok(user);
            } else {
                logger.warn("User not found for token");
                return ResponseEntity.status(404).body(Map.of("error", "User not found"));
            }
            
        } catch (Exception e) {
            logger.error("Error getting current user: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/verify-token")
    public ResponseEntity<?> verifyToken(@RequestHeader(value = "Authorization", required = false) String tokenHeader) {
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
            
            // Validate token and get user
            Optional<User> userOpt = authService.validateTokenAndGetUser(token);
            
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                return ResponseEntity.ok(Map.of("valid", true, "user", user));
            } else {
                return ResponseEntity.status(401).body(Map.of("valid", false, "error", "Invalid token"));
            }
        } catch (Exception e) {
            logger.error("Token verification failed: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(Map.of("valid", false, "error", e.getMessage()));
        }
    }
    
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        // JWT is stateless, so there's no server-side logout
        // The client should simply remove the token
        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
    }
}