package com.docconnect.backend.controller;

import com.docconnect.backend.model.Announcement;
import com.docconnect.backend.model.Professor;
import com.docconnect.backend.model.StatusHistory;
import com.docconnect.backend.model.User;
import com.docconnect.backend.model.StatusHistory.Status;
import com.docconnect.backend.repository.AnnouncementRepository;
import com.docconnect.backend.repository.ProfessorRepository;
import com.docconnect.backend.repository.StatusHistoryRepository;
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
@RequestMapping("/professors")
public class ProfessorController {

    @Autowired
    private ProfessorRepository professorRepository;
    
    @Autowired
    private StatusHistoryRepository statusHistoryRepository;
    
    @Autowired
    private AnnouncementRepository announcementRepository;
    
    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllProfessors() {
        List<Professor> professors = professorRepository.findAll();
        
        List<Map<String, Object>> result = professors.stream().map(professor -> {
            Map<String, Object> profData = new HashMap<>();
            profData.put("id", professor.getId());
            profData.put("name", professor.getUser().getFullName());
            profData.put("email", professor.getUser().getEmail());
            profData.put("department", professor.getDepartment());
            profData.put("officeLocation", professor.getOfficeLocation());
            profData.put("workingHours", professor.getWorkingHours());
            
            // Get latest status
            StatusHistory latestStatus = statusHistoryRepository.findTopByProfessorOrderByTimestampDesc(professor)
                .orElse(null);
            
            if (latestStatus != null) {
                profData.put("status", latestStatus.getStatus().name());
                profData.put("statusUpdatedAt", latestStatus.getTimestamp());
            } else {
                profData.put("status", "UNKNOWN");
                profData.put("statusUpdatedAt", null);
            }
            
            // Get announcements
            List<Map<String, Object>> announcements = announcementRepository.findByProfessorOrderByPostedAtDesc(professor)
                .stream()
                .map(announcement -> {
                    Map<String, Object> announcementData = new HashMap<>();
                    announcementData.put("id", announcement.getId());
                    announcementData.put("content", announcement.getContent());
                    announcementData.put("postedAt", announcement.getPostedAt());
                    return announcementData;
                })
                .collect(Collectors.toList());
            
            profData.put("announcements", announcements);
            
            return profData;
        }).collect(Collectors.toList());
        
        return ResponseEntity.ok(result);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getProfessorById(@PathVariable Long id) {
        Optional<Professor> professorOpt = professorRepository.findById(id);
        
        if (!professorOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        
        Professor professor = professorOpt.get();
        Map<String, Object> profData = new HashMap<>();
        profData.put("id", professor.getId());
        profData.put("name", professor.getUser().getFullName());
        profData.put("email", professor.getUser().getEmail());
        profData.put("department", professor.getDepartment());
        profData.put("officeLocation", professor.getOfficeLocation());
        profData.put("workingHours", professor.getWorkingHours());
        
        // Get latest status
        StatusHistory latestStatus = statusHistoryRepository.findTopByProfessorOrderByTimestampDesc(professor)
            .orElse(null);
        
        if (latestStatus != null) {
            profData.put("status", latestStatus.getStatus().name());
            profData.put("statusUpdatedAt", latestStatus.getTimestamp());
        } else {
            profData.put("status", "UNKNOWN");
            profData.put("statusUpdatedAt", null);
        }
        
        // Get status history
        List<Map<String, Object>> statusHistory = statusHistoryRepository.findByProfessorOrderByTimestampDesc(professor)
            .stream()
            .map(status -> {
                Map<String, Object> statusData = new HashMap<>();
                statusData.put("id", status.getId());
                statusData.put("status", status.getStatus().name());
                statusData.put("timestamp", status.getTimestamp());
                return statusData;
            })
            .collect(Collectors.toList());
        
        profData.put("statusHistory", statusHistory);
        
        // Get announcements
        List<Map<String, Object>> announcements = announcementRepository.findByProfessorOrderByPostedAtDesc(professor)
            .stream()
            .map(announcement -> {
                Map<String, Object> announcementData = new HashMap<>();
                announcementData.put("id", announcement.getId());
                announcementData.put("content", announcement.getContent());
                announcementData.put("postedAt", announcement.getPostedAt());
                return announcementData;
            })
            .collect(Collectors.toList());
        
        profData.put("announcements", announcements);
        
        return ResponseEntity.ok(profData);
    }
    
    @PostMapping("/{id}/status")
    public ResponseEntity<?> updateProfessorStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> statusUpdate) {
        
        Optional<Professor> professorOpt = professorRepository.findById(id);
        
        if (!professorOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        
        Professor professor = professorOpt.get();
        
        try {
            Status newStatus = Status.valueOf(statusUpdate.get("status").toUpperCase());
            
            StatusHistory statusHistory = new StatusHistory();
            statusHistory.setProfessor(professor);
            statusHistory.setStatus(newStatus);
            statusHistory.setTimestamp(LocalDateTime.now());
            
            statusHistoryRepository.save(statusHistory);
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", newStatus.name());
            response.put("timestamp", statusHistory.getTimestamp());
            response.put("message", "Status updated successfully");
            
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid status");
        }
    }
    
    @PostMapping("/{id}/announcements")
    public ResponseEntity<?> createAnnouncement(
            @PathVariable Long id,
            @RequestBody Map<String, String> announcementData) {
        
        Optional<Professor> professorOpt = professorRepository.findById(id);
        
        if (!professorOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        
        Professor professor = professorOpt.get();
        
        String content = announcementData.get("content");
        if (content == null || content.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Announcement content cannot be empty");
        }
        
        Announcement announcement = new Announcement();
        announcement.setProfessor(professor);
        announcement.setContent(content);
        announcement.setPostedAt(LocalDateTime.now());
        
        announcementRepository.save(announcement);
        
        Map<String, Object> response = new HashMap<>();
        response.put("id", announcement.getId());
        response.put("content", announcement.getContent());
        response.put("postedAt", announcement.getPostedAt());
        response.put("message", "Announcement created successfully");
        
        return ResponseEntity.ok(response);
    }
}
