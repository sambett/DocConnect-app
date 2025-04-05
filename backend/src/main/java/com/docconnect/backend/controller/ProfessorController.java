package com.docconnect.backend.controller;

import com.docconnect.backend.model.Professor;
import com.docconnect.backend.model.StatusHistory;
import com.docconnect.backend.service.ProfessorService;
import com.docconnect.backend.service.StatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/professors")
@RequiredArgsConstructor
public class ProfessorController {

    private final ProfessorService professorService;
    private final StatusService statusService;

    @GetMapping
    public ResponseEntity<List<Professor>> getAllProfessors() {
        return ResponseEntity.ok(professorService.getAllProfessors());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Professor> getProfessorById(@PathVariable Long id) {
        return ResponseEntity.ok(professorService.getProfessorById(id));
    }

    @GetMapping("/{id}/status")
    public ResponseEntity<StatusHistory> getProfessorCurrentStatus(@PathVariable Long id) {
        return ResponseEntity.ok(statusService.getCurrentStatusByProfessorId(id));
    }

    @PostMapping("/{id}/status")
    public ResponseEntity<?> updateProfessorStatus(
            @PathVariable Long id, 
            @RequestBody StatusHistory.Status status) {
        
        StatusHistory updatedStatus = statusService.updateStatus(id, status);
        return ResponseEntity.ok(updatedStatus);
    }

    @GetMapping("/{id}/waiting-students")
    public ResponseEntity<?> getWaitingStudentsCount(@PathVariable Long id) {
        int count = statusService.getWaitingStudentsCount(id);
        Map<String, Integer> response = new HashMap<>();
        response.put("waitingStudents", count);
        return ResponseEntity.ok(response);
    }
}
