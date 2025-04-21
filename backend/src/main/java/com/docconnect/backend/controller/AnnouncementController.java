package com.docconnect.backend.controller;

import com.docconnect.backend.model.Announcement;
import com.docconnect.backend.model.Professor;
import com.docconnect.backend.service.AnnouncementService;
import com.docconnect.backend.service.ProfessorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/announcements")
public class AnnouncementController {

    private static final Logger logger = LoggerFactory.getLogger(AnnouncementController.class);
    private final AnnouncementService announcementService;
    private final ProfessorService professorService;

    public AnnouncementController(AnnouncementService announcementService, ProfessorService professorService) {
        this.announcementService = announcementService;
        this.professorService = professorService;
    }

    @GetMapping("/professor/{professorId}")
    public ResponseEntity<List<Announcement>> getAnnouncementsByProfessorId(@PathVariable Long professorId) {
        logger.info("Getting announcements for professor with id: {}", professorId);
        
        try {
            Professor professor = professorService.getProfessorById(professorId);
            List<Announcement> announcements = announcementService.getAnnouncementsByProfessor(professor);
            return ResponseEntity.ok(announcements);
        } catch (IllegalArgumentException e) {
            logger.error("Professor not found with id: {}", professorId);
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/professor/{professorId}")
    public ResponseEntity<?> createAnnouncement(@PathVariable Long professorId, @RequestBody Map<String, String> announcementData) {
        String content = announcementData.get("content");
        
        logger.info("Received announcement creation request for professor {}: {}", professorId, announcementData);
        
        if (content == null || content.trim().isEmpty()) {
            logger.error("Announcement content is empty");
            return ResponseEntity.badRequest().body(Map.of("error", "Announcement content cannot be empty"));
        }
        
        logger.info("Creating announcement for professor {}: {}", professorId, content);
        
        try {
            Professor professor = professorService.getProfessorById(professorId);
            logger.info("Found professor: {}", professor.getId());
            
            Announcement announcement = new Announcement();
            announcement.setProfessor(professor);
            announcement.setContent(content);
            announcement.setPostedAt(LocalDateTime.now());
            
            logger.info("Saving announcement: {}", announcement);
            Announcement savedAnnouncement = announcementService.saveAnnouncement(announcement);
            logger.info("Successfully saved announcement with ID: {}", savedAnnouncement.getId());
            return ResponseEntity.ok(savedAnnouncement);
        } catch (IllegalArgumentException e) {
            logger.error("Professor not found with id: {}", professorId);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Error creating announcement: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(Map.of("error", "Error creating announcement: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAnnouncement(@PathVariable Long id, @RequestBody Map<String, String> announcementData) {
        String content = announcementData.get("content");
        
        if (content == null || content.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Announcement content cannot be empty"));
        }
        
        logger.info("Updating announcement with id {}: {}", id, content);
        
        try {
            Announcement announcement = announcementService.getAnnouncementById(id);
            announcement.setContent(content);
            // No need to update postedAt as we're just editing the content
            
            Announcement updatedAnnouncement = announcementService.saveAnnouncement(announcement);
            return ResponseEntity.ok(updatedAnnouncement);
        } catch (IllegalArgumentException e) {
            logger.error("Announcement not found with id: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAnnouncement(@PathVariable Long id) {
        logger.info("Deleting announcement with id: {}", id);
        
        try {
            announcementService.deleteAnnouncement(id);
            return ResponseEntity.ok(Map.of("message", "Announcement deleted successfully"));
        } catch (IllegalArgumentException e) {
            logger.error("Announcement not found with id: {}", id);
            return ResponseEntity.notFound().build();
        }
    }
}
