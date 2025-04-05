package com.docconnect.backend.controller;

import com.docconnect.backend.model.Favorite;
import com.docconnect.backend.model.Notification;
import com.docconnect.backend.model.Professor;
import com.docconnect.backend.service.FavoriteService;
import com.docconnect.backend.service.NotificationService;
import com.docconnect.backend.service.ProfessorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final NotificationService notificationService;
    private final FavoriteService favoriteService;
    private final ProfessorService professorService;

    @GetMapping("/{studentId}/notifications")
    public ResponseEntity<List<Notification>> getNotifications(@PathVariable Long studentId) {
        return ResponseEntity.ok(notificationService.getNotificationsForStudent(studentId));
    }

    @PostMapping("/{studentId}/notifications/{professorId}")
    public ResponseEntity<?> createNotification(
            @PathVariable Long studentId, 
            @PathVariable Long professorId) {
        
        try {
            Notification notification = notificationService.createNotification(studentId, professorId);
            return ResponseEntity.ok(notification);
        } catch (IllegalArgumentException e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/{studentId}/notifications/{professorId}")
    public ResponseEntity<?> cancelNotification(
            @PathVariable Long studentId, 
            @PathVariable Long professorId) {
        
        notificationService.cancelNotification(studentId, professorId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{studentId}/favorites")
    public ResponseEntity<List<Professor>> getFavorites(@PathVariable Long studentId) {
        return ResponseEntity.ok(favoriteService.getFavoriteProfessors(studentId));
    }

    @PostMapping("/{studentId}/favorites/{professorId}")
    public ResponseEntity<?> addFavorite(
            @PathVariable Long studentId, 
            @PathVariable Long professorId) {
        
        try {
            Favorite favorite = favoriteService.addFavorite(studentId, professorId);
            return ResponseEntity.ok(favorite);
        } catch (IllegalArgumentException e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/{studentId}/favorites/{professorId}")
    public ResponseEntity<?> removeFavorite(
            @PathVariable Long studentId, 
            @PathVariable Long professorId) {
        
        favoriteService.removeFavorite(studentId, professorId);
        return ResponseEntity.ok().build();
    }
}
