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
import java.util.Arrays;
import java.util.List;

@Configuration
public class DatabaseInitializer {

    @Autowired
    private Environment env;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
            
            System.out.println("Initializing database with seed data...");
            
            // Create professors
            String password = passwordEncoder.encode("docconnect2025");
            
            List<User> users = Arrays.asList(
                createUser(userRepository, "Amina Ben Ali", "amina.benali@university.edu", password, User.Role.PROFESSOR),
                createUser(userRepository, "Karim Hassan", "karim.hassan@university.edu", password, User.Role.PROFESSOR),
                createUser(userRepository, "Fatima Zahra", "fatima.zahra@university.edu", password, User.Role.PROFESSOR)
            );
            
            // Create professor details
            Professor professor1 = createProfessor(professorRepository, users.get(0), "Computer Science", 
                "Building A, Room 304", "Mon, Wed, Fri: 10:00-12:00");
            createStatus(statusHistoryRepository, professor1, Status.AVAILABLE);
            
            Professor professor2 = createProfessor(professorRepository, users.get(1), "Mathematics", 
                "Building B, Room 201", "Tue, Thu: 14:00-16:00");
            createStatus(statusHistoryRepository, professor2, Status.BUSY);
            
            Professor professor3 = createProfessor(professorRepository, users.get(2), "Physics", 
                "Building C, Room 105", "Mon, Wed: 13:00-15:00, Fri: 09:00-11:00");
            createStatus(statusHistoryRepository, professor3, Status.AWAY);
            
            System.out.println("Database initialization complete!");
        };
    }
    
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
    
    private StatusHistory createStatus(StatusHistoryRepository repository, Professor professor, Status status) {
        StatusHistory statusHistory = new StatusHistory();
        statusHistory.setProfessor(professor);
        statusHistory.setStatus(status);
        statusHistory.setTimestamp(LocalDateTime.now());
        
        return repository.save(statusHistory);
    }
}
