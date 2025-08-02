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
    
    // Changed method names to match JPA naming convention for nested properties
    List<Notification> findByStudent_Id(Long studentId);
    
    // Using proper naming convention for nested properties
    Optional<Notification> findByStudent_IdAndProfessor_Id(Long studentId, Long professorId);
    
    // Additional method that might be needed
    List<Notification> findByStudentAndNotifiedFalse(User student);
}
