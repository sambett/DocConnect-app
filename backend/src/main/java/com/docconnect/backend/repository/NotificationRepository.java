package com.docconnect.backend.repository;

import com.docconnect.backend.model.Notification;
import com.docconnect.backend.model.Professor;
import com.docconnect.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
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
    Optional<Notification> findByStudentIdAndProfessorId(Long studentId, Long professorId);
}

