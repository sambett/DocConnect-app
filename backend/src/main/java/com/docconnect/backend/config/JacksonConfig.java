package com.docconnect.backend.config;

import com.docconnect.backend.model.User.Role;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Configuration
public class JacksonConfig {
    
    @Bean
    @Primary
    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
        ObjectMapper objectMapper = builder.build();
        
        // Create a module for custom deserializers
        SimpleModule module = new SimpleModule();
        
        // Register the Role deserializer
        module.addDeserializer(Role.class, new RoleDeserializer());
        
        // Register the module with the ObjectMapper
        objectMapper.registerModule(module);
        
        // Configure to prevent circular reference issues
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        
        return objectMapper;
    }
}