package com.docconnect.backend.controller;

import com.docconnect.backend.model.Professor;
import com.docconnect.backend.model.StatusHistory;
import com.docconnect.backend.model.enums.Status;
import com.docconnect.backend.service.ProfessorService;
import com.docconnect.backend.service.StatusHistoryService;
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

    public ProfessorController(ProfessorService professorService, StatusHistoryService statusHistoryService) {
        this.professorService = professorService;
        this.statusHistoryService = statusHistoryService;
    }

    @GetMapping
    public ResponseEntity<List<Professor>> getAllProfessors() {
        logger.info("Getting all professors");
        List<Professor> professors = professorService.getAllProfessors();
        return ResponseEntity.ok(professors);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Professor> getProfessorById(@PathVariable Long id) {
        logger.info("Getting professor with id: {}", id);
        try {
            Professor professor = professorService.getProfessorById(id);
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

    @GetMapping("/by-user/{userId}")
    public ResponseEntity<Professor> getProfessorByUserId(@PathVariable Long userId) {
        logger.info("Getting professor by user id: {}", userId);
        Optional<Professor> professorOpt = professorService.getProfessorByUserId(userId);
        return professorOpt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
