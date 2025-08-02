package com.docconnect.backend.controller;

import com.docconnect.backend.model.Announcement;
import com.docconnect.backend.model.Professor;
import com.docconnect.backend.model.StatusHistory;
import com.docconnect.backend.model.User;
import com.docconnect.backend.model.enums.Status;
import com.docconnect.backend.service.AnnouncementService;
import com.docconnect.backend.service.ProfessorService;
import com.docconnect.backend.service.StatusHistoryService;
import com.docconnect.backend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/professors")
public class ProfessorController {

    private static final Logger logger = LoggerFactory.getLogger(ProfessorController.class);
    private final ProfessorService professorService;
    private final StatusHistoryService statusHistoryService;
    private final AnnouncementService announcementService;
    private final UserService userService;

    public ProfessorController(
            ProfessorService professorService, 
            StatusHistoryService statusHistoryService,
            AnnouncementService announcementService,
            UserService userService) {
        this.professorService = professorService;
        this.statusHistoryService = statusHistoryService;
        this.announcementService = announcementService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<Professor>> getAllProfessors() {
        logger.info("Getting all professors");
        List<Professor> professors = professorService.getAllProfessors();
        
        // For each professor, get their announcements
        for (Professor professor : professors) {
            List<Announcement> announcements = announcementService.getAnnouncementsByProfessor(professor);
            professor.setAnnouncements(announcements);
        }
        
        return ResponseEntity.ok(professors);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Professor> getProfessorById(@PathVariable Long id) {
        logger.info("Getting professor with id: {}", id);
        try {
            Professor professor = professorService.getProfessorById(id);
            
            // Get announcements for this professor
            List<Announcement> announcements = announcementService.getAnnouncementsByProfessor(professor);
            professor.setAnnouncements(announcements);
            
            return ResponseEntity.ok(professor);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> statusUpdate) {
        String newStatus = statusUpdate.get("status");
        
        if (newStatus == null || newStatus.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Status cannot be empty"));
        }
        
        logger.info("Updating status for professor {}: {}", id, newStatus);
        
        Professor professor;
        try {
            professor = professorService.getProfessorById(id);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
        professor.setStatus(newStatus);
        professor.setUpdatedAt(LocalDateTime.now());
        
        Professor updatedProfessor = professorService.saveProfessor(professor);
        
        // Create status history entry
        StatusHistory statusHistory = new StatusHistory();
        statusHistory.setProfessor(professor);
        statusHistory.setStatus(Status.valueOf(newStatus));
        statusHistory.setTimestamp(LocalDateTime.now());
        statusHistoryService.saveStatusHistory(statusHistory);
        
        return ResponseEntity.ok(updatedProfessor);
    }

    @GetMapping("/{id}/status-history")
    public ResponseEntity<List<StatusHistory>> getStatusHistory(@PathVariable Long id) {
        logger.info("Getting status history for professor: {}", id);
        List<StatusHistory> history = statusHistoryService.getStatusHistoryByProfessorId(id);
        return ResponseEntity.ok(history);
    }
    
    @DeleteMapping("/{id}/status-history")
    public ResponseEntity<?> clearStatusHistory(@PathVariable Long id) {
        logger.info("Clearing status history for professor: {}", id);
        try {
            statusHistoryService.clearStatusHistoryByProfessorId(id);
            return ResponseEntity.ok(Map.of("message", "Status history cleared successfully"));
        } catch (Exception e) {
            logger.error("Error clearing status history: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to clear status history"));
        }
    }

    @GetMapping("/by-user/{userId}")
    public ResponseEntity<Professor> getProfessorByUserId(@PathVariable Long userId) {
        logger.info("Getting professor by user id: {}", userId);
        Optional<Professor> professorOpt = professorService.getProfessorByUserId(userId);
        return professorOpt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Professor> updateProfessor(@PathVariable Long id, @RequestBody Professor professorUpdate) {
        logger.info("Updating professor with id: {}", id);
        try {
            Professor existingProfessor = professorService.getProfessorById(id);
            
            // Update fields if provided in the request (make sure to sanitize where needed)
            if (professorUpdate.getDepartment() != null && !professorUpdate.getDepartment().trim().isEmpty()) {
                existingProfessor.setDepartment(professorUpdate.getDepartment().trim());
            }
            
            if (professorUpdate.getOfficeLocation() != null) {
                existingProfessor.setOfficeLocation(professorUpdate.getOfficeLocation().trim());
                logger.info("Updated office location to: {}", professorUpdate.getOfficeLocation());
            }
            
            if (professorUpdate.getWorkingHours() != null) {
                existingProfessor.setWorkingHours(professorUpdate.getWorkingHours().trim());
                logger.info("Updated working hours to: {}", professorUpdate.getWorkingHours());
            }
            
            if (professorUpdate.getEmailContact() != null && !professorUpdate.getEmailContact().trim().isEmpty()) {
                existingProfessor.setEmailContact(professorUpdate.getEmailContact().trim());
            }
            
            // Handle user updates if provided
            if (professorUpdate.getUser() != null) {
                User user = existingProfessor.getUser();
                
                // Update user fields if provided
                if (professorUpdate.getUser().getFullName() != null && !professorUpdate.getUser().getFullName().trim().isEmpty()) {
                    user.setFullName(professorUpdate.getUser().getFullName().trim());
                    logger.info("Updated user name to: {}", professorUpdate.getUser().getFullName());
                }
                
                if (professorUpdate.getUser().getEmail() != null && !professorUpdate.getUser().getEmail().trim().isEmpty()) {
                    user.setEmail(professorUpdate.getUser().getEmail().trim());
                    logger.info("Updated user email to: {}", professorUpdate.getUser().getEmail());
                }
                
                user.setUpdatedAt(LocalDateTime.now());
                userService.saveUser(user); // Save the user updates
            }
            
            existingProfessor.setUpdatedAt(LocalDateTime.now());
            
            Professor updatedProfessor = professorService.saveProfessor(existingProfessor);
            logger.info("Professor updated successfully. New values: department={}, location={}, hours={}, email={}", 
                    updatedProfessor.getDepartment(), 
                    updatedProfessor.getOfficeLocation(), 
                    updatedProfessor.getWorkingHours(),
                    updatedProfessor.getEmailContact());
            
            return ResponseEntity.ok(updatedProfessor);
        } catch (IllegalArgumentException e) {
            logger.error("Professor not found with id: {}", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Error updating professor: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{id}/account")
    public ResponseEntity<?> deleteProfessorAccount(@PathVariable Long id) {
        logger.info("Deleting professor account with id: {}", id);
        try {
            // Get the professor to find associated user
            Professor professor = professorService.getProfessorById(id);
            User user = professor.getUser();
            Long userId = user.getId();
            
            // Delete professor first (to maintain referential integrity)
            professorService.deleteProfessor(id);
            logger.info("Deleted professor with id: {}", id);
            
            // Then delete the user account
            userService.deleteUser(userId);
            logger.info("Deleted user with id: {}", userId);
            
            return ResponseEntity.ok(Map.of("message", "Account deleted successfully"));
        } catch (IllegalArgumentException e) {
            logger.error("Professor not found with id: {}", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Error deleting professor account: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body(Map.of("error", "Failed to delete account: " + e.getMessage()));
        }
    }
}