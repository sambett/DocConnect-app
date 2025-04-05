package com.docconnect.backend.service;

import com.docconnect.backend.model.Favorite;
import com.docconnect.backend.model.Professor;
import com.docconnect.backend.model.User;
import com.docconnect.backend.repository.FavoriteRepository;
import com.docconnect.backend.repository.ProfessorRepository;
import com.docconnect.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final ProfessorRepository professorRepository;

    public List<Professor> getFavoriteProfessors(Long studentId) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));
        
        return favoriteRepository.findByStudent(student).stream()
                .map(Favorite::getProfessor)
                .collect(Collectors.toList());
    }

    public Favorite addFavorite(Long studentId, Long professorId) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));
        
        Professor professor = professorRepository.findById(professorId)
                .orElseThrow(() -> new IllegalArgumentException("Professor not found"));
        
        if (favoriteRepository.existsByStudentAndProfessor(student, professor)) {
            throw new IllegalArgumentException("Professor already in favorites");
        }
        
        Favorite favorite = new Favorite();
        favorite.setStudent(student);
        favorite.setProfessor(professor);
        favorite.setCreatedAt(LocalDateTime.now());
        
        return favoriteRepository.save(favorite);
    }

    public void removeFavorite(Long studentId, Long professorId) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));
        
        Professor professor = professorRepository.findById(professorId)
                .orElseThrow(() -> new IllegalArgumentException("Professor not found"));
        
        favoriteRepository.findByStudentAndProfessor(student, professor)
                .ifPresent(favoriteRepository::delete);
    }
}
