package com.docconnect.backend.repository;

import com.docconnect.backend.model.Notification;
import com.docconnect.backend.model.Professor;
import com.docconnect.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByProfessorAndNotifiedFalse(Professor professor);
    List<Notification> findByStudentOrderByNotificationSetAtDesc(User student);
    int countByProfessorAndNotifiedFalse(Professor professor);
    boolean existsByStudentAndProfessorAndNotifiedFalse(User student, Professor professor);
    
    // Added methods to match those used in StudentController
    List<Notification> findByStudentId(Long studentId);
    
    // Using Query annotation to properly find by student ID and professor ID
    @Query("SELECT n FROM Notification n WHERE n.student.id = :studentId AND n.professor.id = :professorId")
    Optional<Notification> findByStudentIdAndProfessorId(@Param("studentId") Long studentId, @Param("professorId") Long professorId);
    
    // Additional method that might be needed
    List<Notification> findByStudentAndNotifiedFalse(User student);
}
