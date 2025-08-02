package com.docconnect.backend.config;

import com.docconnect.backend.model.Professor;
import com.docconnect.backend.model.StatusHistory;
import com.docconnect.backend.model.User;
import com.docconnect.backend.model.enums.Status;
import com.docconnect.backend.repository.ProfessorRepository;
import com.docconnect.backend.repository.StatusHistoryRepository;
import com.docconnect.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import com.docconnect.backend.security.PasswordEncoder;

import java.time.LocalDateTime;

/**
 * Responsible for initializing the database with required data when the application starts.
 * This class runs on application startup and can initialize admin users or required configuration.
 */
@Configuration
public class DatabaseInitializer {

    @Autowired
    private Environment env;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Creates a CommandLineRunner bean that initializes the database with required data.
     * This runs on application startup and will be skipped if the database already contains data.
     *
     * @param userRepository Repository for user data access
     * @param professorRepository Repository for professor data access
     * @param statusHistoryRepository Repository for status history data access
     * @return CommandLineRunner for database initialization
     */
    @Bean
    public CommandLineRunner initDatabase(
            UserRepository userRepository,
            ProfessorRepository professorRepository,
            StatusHistoryRepository statusHistoryRepository) {
        
        return args -> {
            // Skip if already initialized
            if (userRepository.count() > 0) {
                System.out.println("Database already contains data, skipping initialization");
                return;
            }
            
            System.out.println("Database initialization check complete.");
            // No mock data initialization - production environment should use proper data loading
        };
    }
    
    /**
     * Creates a new user with the specified details.
     *
     * @param repository Repository for user data access
     * @param fullName Full name of the user
     * @param email Email address of the user
     * @param passwordHash Hashed password for the user
     * @param role Role of the user (PROFESSOR or STUDENT)
     * @return The saved User entity
     */
    private User createUser(UserRepository repository, String fullName, String email, String passwordHash, User.Role role) {
        User user = new User();
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPasswordHash(passwordHash);
        user.setRole(role);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        
        return repository.save(user);
    }
    
    /**
     * Creates a new professor profile associated with a user.
     *
     * @param repository Repository for professor data access
     * @param user The user entity to associate with this professor
     * @param department Academic department of the professor
     * @param officeLocation Office location (building, room number)
     * @param workingHours Office hours schedule as a string
     * @return The saved Professor entity
     */
    private Professor createProfessor(ProfessorRepository repository, User user, String department, 
                                      String officeLocation, String workingHours) {
        Professor professor = new Professor();
        professor.setUser(user);
        professor.setDepartment(department);
        professor.setOfficeLocation(officeLocation);
        professor.setWorkingHours(workingHours);
        professor.setEmailVerified(true);
        professor.setCreatedAt(LocalDateTime.now());
        professor.setUpdatedAt(LocalDateTime.now());
        
        return repository.save(professor);
    }
    
    /**
     * Creates a new status history entry for a professor.
     *
     * @param repository Repository for status history data access
     * @param professor The professor whose status is being set
     * @param status The new status (AVAILABLE, BUSY, AWAY, or IN_MEETING)
     * @return The saved StatusHistory entity
     */
    private StatusHistory createStatus(StatusHistoryRepository repository, Professor professor, Status status) {
        StatusHistory statusHistory = new StatusHistory();
        statusHistory.setProfessor(professor);
        statusHistory.setStatus(status);
        statusHistory.setTimestamp(LocalDateTime.now());
        
        return repository.save(statusHistory);
    }
}
