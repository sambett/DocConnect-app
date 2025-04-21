package com.docconnect.backend.service;

import com.docconnect.backend.model.Professor;
import com.docconnect.backend.model.User;
import com.docconnect.backend.repository.ProfessorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfessorService {

    private final ProfessorRepository professorRepository;

    public Professor createProfessor(Professor professor) {
        return professorRepository.save(professor);
    }

    public Professor getProfessorById(Long id) {
        return professorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Professor not found"));
    }

    public Optional<Professor> getProfessorByUser(User user) {
        return professorRepository.findByUser(user);
    }

    public Optional<Professor> getProfessorByUserId(Long userId) {
        return professorRepository.findByUser_Id(userId);
    }

    public List<Professor> getAllProfessors() {
        return professorRepository.findAll();
    }

    public Professor updateProfessor(Professor professor) {
        if (!professorRepository.existsById(professor.getId())) {
            throw new IllegalArgumentException("Professor not found");
        }
        return professorRepository.save(professor);
    }
    
    public Professor saveProfessor(Professor professor) {
        return professorRepository.save(professor);
    }

    public void deleteProfessor(Long id) {
        professorRepository.deleteById(id);
    }
}
