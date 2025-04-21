package com.docconnect.backend.controller;

import com.docconnect.backend.model.Notification;
import com.docconnect.backend.model.Professor;
import com.docconnect.backend.model.User;
import com.docconnect.backend.service.NotificationService;
import com.docconnect.backend.service.ProfessorService;
import com.docconnect.backend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private static final Logger logger = LoggerFactory.getLogger(NotificationController.class);
    private final NotificationService notificationService;
    private final UserService userService;
    private final ProfessorService professorService;

    public NotificationController(
            NotificationService notificationService,
            UserService userService,
            ProfessorService professorService) {
        this.notificationService = notificationService;
        this.userService = userService;
        this.professorService = professorService;
    }

    @PostMapping
    public ResponseEntity<?> createNotification(@RequestBody Map<String, Long> notificationData) {
        Long studentId = notificationData.get("studentId");
        Long professorId = notificationData.get("professorId");
        
        if (studentId == null || professorId == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Student ID and Professor ID are required"));
        }
        
        logger.info("Creating notification for student {} and professor {}", studentId, professorId);
        
        try {
            User student = userService.getUserById(studentId);
            Professor professor = professorService.getProfessorById(professorId);
            
            // Check if notification already exists
            if (notificationService.notificationExists(student, professor)) {
                return ResponseEntity.badRequest().body(Map.of(
                    "error", "Notification already exists for this student and professor"
                ));
            }
            
            Notification notification = notificationService.createNotification(student, professor);
            return ResponseEntity.ok(notification);
        } catch (IllegalArgumentException e) {
            logger.error("Error creating notification: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @GetMapping("/professor/{professorId}")
    public ResponseEntity<?> getWaitingStudentsCount(@PathVariable Long professorId) {
        logger.info("Getting waiting students count for professor {}", professorId);
        
        try {
            Professor professor = professorService.getProfessorById(professorId);
            int count = notificationService.getWaitingStudentsCount(professor);
            return ResponseEntity.ok(Map.of("count", count));
        } catch (IllegalArgumentException e) {
            logger.error("Professor not found with id: {}", professorId);
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Notification>> getStudentNotifications(@PathVariable Long studentId) {
        logger.info("Getting notifications for student {}", studentId);
        
        try {
            User student = userService.getUserById(studentId);
            List<Notification> notifications = notificationService.getNotificationsByStudent(student);
            return ResponseEntity.ok(notifications);
        } catch (IllegalArgumentException e) {
            logger.error("Student not found with id: {}", studentId);
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNotification(@PathVariable Long id) {
        logger.info("Deleting notification with id: {}", id);
        
        try {
            notificationService.deleteNotification(id);
            return ResponseEntity.ok(Map.of("message", "Notification deleted successfully"));
        } catch (IllegalArgumentException e) {
            logger.error("Notification not found with id: {}", id);
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/professor/{professorId}")
    public ResponseEntity<?> markAllAsNotified(@PathVariable Long professorId) {
        logger.info("Marking all notifications as notified for professor {}", professorId);
        
        try {
            Professor professor = professorService.getProfessorById(professorId);
            int count = notificationService.markAllAsNotified(professor);
            return ResponseEntity.ok(Map.of(
                "message", "All notifications marked as notified",
                "count", count
            ));
        } catch (IllegalArgumentException e) {
            logger.error("Professor not found with id: {}", professorId);
            return ResponseEntity.notFound().build();
        }
    }
}
