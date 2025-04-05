package com.docconnect.backend.controller;

import com.docconnect.backend.model.Announcement;
import com.docconnect.backend.model.Professor;
import com.docconnect.backend.model.StatusHistory;
import com.docconnect.backend.model.User;
import com.docconnect.backend.model.StatusHistory.Status;
import com.docconnect.backend.model.User.Role;
import com.docconnect.backend.repository.AnnouncementRepository;
import com.docconnect.backend.repository.ProfessorRepository;
import com.docconnect.backend.repository.StatusHistoryRepository;
import com.docconnect.backend.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/api/test-data")
public class TestDataController {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ProfessorRepository professorRepository;
    
    @Autowired
    private StatusHistoryRepository statusHistoryRepository;
    
    @Autowired
    private AnnouncementRepository announcementRepository;
    
    private final Random random = new Random();

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createTestData() {
        // Clear existing data
        announcementRepository.deleteAll();
        statusHistoryRepository.deleteAll();
        professorRepository.deleteAll();
        userRepository.deleteAll();
        
        Map<String, Object> response = new HashMap<>();
        
        // Create Professors
        List<User> professors = createProfessors();
        response.put("professors", professors.size());
        
        // Create Students
        List<User> students = createStudents();
        response.put("students", students.size());
        
        return ResponseEntity.ok(response);
    }
    
    private List<User> createProfessors() {
        // Create professor users
        List<User> professorUsers = Arrays.asList(
            createUser("Mohamed Kharrat", "mohamed.kharrat@fss.tn", "password123", Role.PROFESSOR),
            createUser("Hala Bezine", "hala.bezine@fss.tn", "password123", Role.PROFESSOR),
            createUser("Imene Ouali", "imene.ouali@fss.tn", "password123", Role.PROFESSOR),
            createUser("Mondher Ben Jemaa", "mondher.benjemaa@fss.tn", "password123", Role.PROFESSOR),
            createUser("Badr Siala", "badr.siala@fss.tn", "password123", Role.PROFESSOR),
            createUser("Mohamed Ali Hadj Taieb", "mohamedali.hadjtaieb@fss.tn", "password123", Role.PROFESSOR)
        );
        
        // Create professors with details
        createProfessorDetails(professorUsers.get(0), "Computer Science", "Building B, Room 305", "Tue: 10:30 - 12:30, 13:30 - 15:30");
        createProfessorDetails(professorUsers.get(1), "Computer Science", "Building A, Room 210", "Mon: 10:00 - 12:00, Tue: 13:30 - 15:30");
        createProfessorDetails(professorUsers.get(2), "Computer Science", "Building C, Room 112", "Mon: 15:45 - 16:45, Fri: 14:00 - 16:00");
        createProfessorDetails(professorUsers.get(3), "Computer Science", "Building A, Room 401", "Fri: 8:15 - 10:15, 14:00 - 16:00");
        createProfessorDetails(professorUsers.get(4), "Computer Science", "Building D, Room 205", "Sat: 8:15 - 9:45, 10:00 - 11:30, 11:45 - 13:15");
        createProfessorDetails(professorUsers.get(5), "Computer Science", "Building B, Room 410", "Mon: 14:00 - 15:30, Sat: 11:45 - 13:15");
        
        return professorUsers;
    }
    
    private List<User> createStudents() {
        // Create student users
        return Arrays.asList(
            createUser("Ahmed Triki", "ahmed.triki@fss.tn", "password123", Role.STUDENT),
            createUser("Sami Gharbi", "sami.gharbi@fss.tn", "password123", Role.STUDENT),
            createUser("Sara Jouini", "sara.jouini@fss.tn", "password123", Role.STUDENT)
        );
    }
    
    private User createUser(String fullName, String email, String password, Role role) {
        User user = new User();
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPasswordHash(password); // In a real app, you would hash this
        user.setRole(role);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        
        return userRepository.save(user);
    }
    
    private Professor createProfessorDetails(User user, String department, String officeLocation, String workingHours) {
        Professor professor = new Professor();
        professor.setUser(user);
        professor.setDepartment(department);
        professor.setOfficeLocation(officeLocation);
        professor.setWorkingHours(workingHours);
        professor.setEmailVerified(true);
        professor.setCreatedAt(LocalDateTime.now());
        professor.setUpdatedAt(LocalDateTime.now());
        
        Professor savedProfessor = professorRepository.save(professor);
        
        // Create initial status
        createInitialStatus(savedProfessor);
        
        // Create announcements
        createAnnouncements(savedProfessor);
        
        return savedProfessor;
    }
    
    private void createInitialStatus(Professor professor) {
        StatusHistory statusHistory = new StatusHistory();
        statusHistory.setProfessor(professor);
        
        // Random status
        Status[] statuses = Status.values();
        Status randomStatus = statuses[random.nextInt(statuses.length)];
        statusHistory.setStatus(randomStatus);
        
        statusHistory.setTimestamp(LocalDateTime.now());
        
        statusHistoryRepository.save(statusHistory);
    }
    
    private void createAnnouncements(Professor professor) {
        // Create 1-3 announcements per professor
        int announcementCount = random.nextInt(3) + 1;
        
        String[] announcementTemplates = {
            "Office hours for %s are changed to %s for this week only.",
            "I will be holding an additional Q&A session on %s at %s in room %s.",
            "The deadline for the %s assignment has been extended to %s.",
            "The exam for %s will be held on %s. Please prepare chapters 1-7.",
            "I will be unavailable during my regular office hours on %s. Please email for appointments."
        };
        
        String[] courses = {"Cloud Computing", "Apprentissage automatique", "DevOps", "Développement Mobile",
                           "Algorithmes numérique", "Initiation Big Data"};
        
        String[] dates = {"Monday, April 8", "Tuesday, April 9", "Wednesday, April 10", 
                         "Thursday, April 11", "Friday, April 12", "Saturday, April 13"};
        
        String[] times = {"9:00 AM", "10:30 AM", "1:00 PM", "2:30 PM", "4:00 PM"};
        
        String[] rooms = {"S1201", "S1209", "Labo1", "Labo4", "Labo6"};
        
        for (int i = 0; i < announcementCount; i++) {
            Announcement announcement = new Announcement();
            announcement.setProfessor(professor);
            
            // Generate random announcement text
            String template = announcementTemplates[random.nextInt(announcementTemplates.length)];
            String course = courses[random.nextInt(courses.length)];
            String date = dates[random.nextInt(dates.length)];
            String time = times[random.nextInt(times.length)];
            String room = rooms[random.nextInt(rooms.length)];
            
            String content = String.format(template, 
                                          course, date, time, room, 
                                          course, date,
                                          course, date,
                                          date);
            
            announcement.setContent(content);
            announcement.setPostedAt(LocalDateTime.now().minusDays(random.nextInt(7)));
            
            announcementRepository.save(announcement);
        }
    }
}
