package com.docconnect.backend.repository;

import com.docconnect.backend.model.Favorite;
import com.docconnect.backend.model.Professor;
import com.docconnect.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    // Changed method name to match JPA naming convention for nested properties
    List<Favorite> findByStudent_Id(Long studentId);
    
    // Using proper naming convention for nested properties
    Optional<Favorite> findByStudent_IdAndProfessor_Id(Long studentId, Long professorId);
    
    List<Favorite> findByStudent(User student);
    boolean existsByStudentAndProfessor(User student, Professor professor);
    Optional<Favorite> findByStudentAndProfessor(User student, Professor professor);
}
