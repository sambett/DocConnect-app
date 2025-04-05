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
    List<Notification> findByStudentAndNotifiedFalse(User student);
    List<Notification> findByProfessorAndNotifiedFalse(Professor professor);
    Optional<Notification> findByStudentAndProfessorAndNotifiedFalse(User student, Professor professor);
    int countByProfessorAndNotifiedFalse(Professor professor);
}
