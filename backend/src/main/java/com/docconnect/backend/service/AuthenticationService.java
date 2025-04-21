package com.docconnect.backend.service;

import com.docconnect.backend.model.User;
import com.docconnect.backend.repository.UserRepository;
import com.docconnect.backend.security.JwtTokenService;
import com.docconnect.backend.security.PasswordEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService jwtTokenService;

    public AuthenticationService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtTokenService jwtTokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenService = jwtTokenService;
    }

    /**
     * Authenticate a user with email and password
     */
    public Optional<String> authenticate(String email, String password) {
        logger.info("Authenticating user: {}", email);
        
        return userRepository.findByEmail(email)
                .filter(user -> passwordEncoder.matches(password, user.getPasswordHash()))
                .map(user -> {
                    logger.info("User authenticated successfully: {}", email);
                    return jwtTokenService.generateToken(email, user.getId(), user.getRole().toString());
                });
    }

    /**
     * Register a new user
     */
    public User register(User user, String rawPassword) {
        logger.info("Registering new user: {}", user.getEmail());
        
        if (userRepository.existsByEmail(user.getEmail())) {
            logger.error("Email already exists: {}", user.getEmail());
            throw new IllegalArgumentException("Email already exists");
        }
        
        // Hash the password
        String encodedPassword = passwordEncoder.encode(rawPassword);
        user.setPasswordHash(encodedPassword);
        
        // Save the user
        return userRepository.save(user);
    }

    /**
     * Validate a JWT token and get the user associated with it
     */
    public Optional<User> validateTokenAndGetUser(String token) {
        if (token == null || !jwtTokenService.validateToken(token)) {
            logger.warn("Invalid token");
            return Optional.empty();
        }

        String email = jwtTokenService.getEmailFromToken(token);
        logger.info("Token validated for user: {}", email);
        
        return userRepository.findByEmail(email);
    }
}