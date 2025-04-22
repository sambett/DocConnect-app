package com.docconnect.backend.repository;

import com.docconnect.backend.model.Favorite;
import com.docconnect.backend.model.Professor;
import com.docconnect.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findByStudentId(Long studentId);
    
    @Query("SELECT f FROM Favorite f WHERE f.student.id = :studentId AND f.professor.id = :professorId")
    Optional<Favorite> findByStudentIdAndProfessorId(@Param("studentId") Long studentId, @Param("professorId") Long professorId);
    
    List<Favorite> findByStudent(User student);
    boolean existsByStudentAndProfessor(User student, Professor professor);
    Optional<Favorite> findByStudentAndProfessor(User student, Professor professor);
}
