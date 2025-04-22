package com.docconnect.backend.controller;

import com.docconnect.backend.model.Favorite;
import com.docconnect.backend.model.Notification;
import com.docconnect.backend.model.Professor;
import com.docconnect.backend.model.User;
import com.docconnect.backend.model.User.Role;
import com.docconnect.backend.repository.FavoriteRepository;
import com.docconnect.backend.repository.NotificationRepository;
import com.docconnect.backend.repository.ProfessorRepository;
import com.docconnect.backend.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private FavoriteRepository favoriteRepository;
    
    @Autowired
    private NotificationRepository notificationRepository;
    
    @Autowired
    private ProfessorRepository professorRepository;

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllStudents() {
        List<User> students = userRepository.findByRole(Role.STUDENT);
        
        List<Map<String, Object>> result = students.stream().map(student -> {
            Map<String, Object> studentData = new HashMap<>();
            studentData.put("id", student.getId());
            studentData.put("name", student.getFullName());
            studentData.put("email", student.getEmail());
            return studentData;
        }).collect(Collectors.toList());
        
        return ResponseEntity.ok(result);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getStudentById(@PathVariable Long id) {
        Optional<User> studentOpt = userRepository.findById(id);
        
        if (!studentOpt.isPresent() || studentOpt.get().getRole() != Role.STUDENT) {
            return ResponseEntity.notFound().build();
        }
        
        User student = studentOpt.get();
        Map<String, Object> studentData = new HashMap<>();
        studentData.put("id", student.getId());
        studentData.put("name", student.getFullName());
        studentData.put("email", student.getEmail());
        
        // Get favorites
        List<Long> favoriteIds = favoriteRepository.findByStudent_Id(student.getId())
            .stream()
            .map(favorite -> favorite.getProfessor().getId())
            .collect(Collectors.toList());
        
        studentData.put("favorites", favoriteIds);
        
        // Get notifications
        List<Map<String, Object>> notifications = notificationRepository.findByStudent_Id(student.getId())
            .stream()
            .map(notification -> {
                Map<String, Object> notificationData = new HashMap<>();
                notificationData.put("id", notification.getId());
                notificationData.put("professorId", notification.getProfessor().getId());
                notificationData.put("professorName", notification.getProfessor().getUser().getFullName());
                notificationData.put("setAt", notification.getNotificationSetAt());
                notificationData.put("notified", notification.getNotified());
                return notificationData;
            })
            .collect(Collectors.toList());
        
        studentData.put("notifications", notifications);
        
        return ResponseEntity.ok(studentData);
    }
    
    @PostMapping("/{studentId}/favorites/{professorId}")
    public ResponseEntity<?> toggleFavorite(
            @PathVariable Long studentId,
            @PathVariable Long professorId) {
        
        Optional<User> studentOpt = userRepository.findById(studentId);
        if (!studentOpt.isPresent() || studentOpt.get().getRole() != Role.STUDENT) {
            return ResponseEntity.badRequest().body("Invalid student ID");
        }
        
        Optional<Professor> professorOpt = professorRepository.findById(professorId);
        if (!professorOpt.isPresent()) {
            return ResponseEntity.badRequest().body("Invalid professor ID");
        }
        
        User student = studentOpt.get();
        Professor professor = professorOpt.get();
        
        // Check if favorite already exists
        Optional<Favorite> existingFavorite = favoriteRepository.findByStudent_IdAndProfessor_Id(studentId, professorId);
        
        Map<String, Object> response = new HashMap<>();
        
        if (existingFavorite.isPresent()) {
            // Remove favorite
            favoriteRepository.delete(existingFavorite.get());
            response.put("added", false);
            response.put("message", "Professor removed from favorites");
        } else {
            // Add favorite
            Favorite favorite = new Favorite();
            favorite.setStudent(student);
            favorite.setProfessor(professor);
            favorite.setCreatedAt(LocalDateTime.now());
            favoriteRepository.save(favorite);
            
            response.put("added", true);
            response.put("message", "Professor added to favorites");
        }
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/{studentId}/notifications/{professorId}")
    public ResponseEntity<?> toggleNotification(
            @PathVariable Long studentId,
            @PathVariable Long professorId) {
        
        try {
            Optional<User> studentOpt = userRepository.findById(studentId);
            if (!studentOpt.isPresent() || studentOpt.get().getRole() != Role.STUDENT) {
                return ResponseEntity.badRequest().body("Invalid student ID");
            }
            
            Optional<Professor> professorOpt = professorRepository.findById(professorId);
            if (!professorOpt.isPresent()) {
                return ResponseEntity.badRequest().body("Invalid professor ID");
            }
            
            User student = studentOpt.get();
            Professor professor = professorOpt.get();
            
            // Check if notification already exists
            Optional<Notification> existingNotification = notificationRepository.findByStudent_IdAndProfessor_Id(studentId, professorId);
            
            Map<String, Object> response = new HashMap<>();
            
            if (existingNotification.isPresent()) {
                // Remove notification
                notificationRepository.delete(existingNotification.get());
                response.put("added", false);
                response.put("message", "Notification removed");
            } else {
                // Add notification
                Notification notification = new Notification();
                notification.setStudent(student);
                notification.setProfessor(professor);
                notification.setNotificationSetAt(LocalDateTime.now());
                notification.setNotified(false);
                notificationRepository.save(notification);
                
                response.put("added", true);
                response.put("message", "Notification set");
            }
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace(); // This will help us see the error in the backend logs
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Server error: " + e.getMessage());
            errorResponse.put("cause", e.getClass().getSimpleName());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
}
