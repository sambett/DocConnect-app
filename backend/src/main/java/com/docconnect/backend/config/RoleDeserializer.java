package com.docconnect.backend.config;

import com.docconnect.backend.model.User.Role;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class RoleDeserializer extends JsonDeserializer<Role> {
    
    private static final Logger logger = LoggerFactory.getLogger(RoleDeserializer.class);
    
    @Override
    public Role deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String value = p.getValueAsString();
        logger.info("Deserializing role value: '{}'", value);
        
        if (value == null || value.isEmpty()) {
            logger.error("Empty role value received");
            return null;
        }
        
        // Convert known UI values to enum values
        value = value.toUpperCase().trim();
        
        // Handle French UI values
        if (value.equals("ÉTUDIANT") || value.equals("ETUDIANT")) {
            logger.info("Converting French 'Étudiant' to STUDENT");
            return Role.STUDENT;
        } else if (value.equals("PROFESSEUR")) {
            logger.info("Converting French 'Professeur' to PROFESSOR");
            return Role.PROFESSOR;
        }
        
        // Handle English UI values
        if (value.equals("STUDENT") || value.contains("STUDENT")) {
            return Role.STUDENT;
        } else if (value.equals("PROFESSOR") || value.contains("PROFESSOR")) {
            return Role.PROFESSOR;
        }
        
        // Log error for unexpected values
        logger.error("Unknown role value: '{}', defaulting to STUDENT", value);
        return Role.STUDENT;
    }
}