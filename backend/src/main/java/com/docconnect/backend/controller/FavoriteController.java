package com.docconnect.backend.controller;

import com.docconnect.backend.model.Favorite;
import com.docconnect.backend.model.Professor;
import com.docconnect.backend.service.FavoriteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {

    private static final Logger logger = LoggerFactory.getLogger(FavoriteController.class);
    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Professor>> getFavoriteProfessors(@PathVariable Long studentId) {
        logger.info("Getting favorite professors for student with id: {}", studentId);
        
        try {
            List<Professor> favoriteProfessors = favoriteService.getFavoriteProfessors(studentId);
            return ResponseEntity.ok(favoriteProfessors);
        } catch (IllegalArgumentException e) {
            logger.error("Student not found with id: {}", studentId);
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> addFavorite(@RequestBody Map<String, Long> favoriteData) {
        Long studentId = favoriteData.get("studentId");
        Long professorId = favoriteData.get("professorId");
        
        if (studentId == null || professorId == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Student ID and Professor ID are required"));
        }
        
        logger.info("Adding professor {} to favorites for student {}", professorId, studentId);
        
        try {
            Favorite favorite = favoriteService.addFavorite(studentId, professorId);
            return ResponseEntity.ok(favorite);
        } catch (IllegalArgumentException e) {
            logger.error("Error adding favorite: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping
    public ResponseEntity<?> removeFavorite(@RequestBody Map<String, Long> favoriteData) {
        Long studentId = favoriteData.get("studentId");
        Long professorId = favoriteData.get("professorId");
        
        if (studentId == null || professorId == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Student ID and Professor ID are required"));
        }
        
        logger.info("Removing professor {} from favorites for student {}", professorId, studentId);
        
        try {
            favoriteService.removeFavorite(studentId, professorId);
            return ResponseEntity.ok(Map.of("message", "Favorite removed successfully"));
        } catch (IllegalArgumentException e) {
            logger.error("Error removing favorite: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
