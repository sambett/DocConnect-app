package com.docconnect.backend.service;

import com.docconnect.backend.model.Notification;
import com.docconnect.backend.model.Professor;
import com.docconnect.backend.model.StatusHistory;
import com.docconnect.backend.model.User;
import com.docconnect.backend.repository.NotificationRepository;
import com.docconnect.backend.repository.ProfessorRepository;
import com.docconnect.backend.repository.StatusHistoryRepository;
import com.docconnect.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final ProfessorRepository professorRepository;
    private final StatusHistoryRepository statusHistoryRepository;

    public Notification createNotification(Long studentId, Long professorId) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));
        
        Professor professor = professorRepository.findById(professorId)
                .orElseThrow(() -> new IllegalArgumentException("Professor not found"));
        
        // Check if the professor is already available
        StatusHistory currentStatus = statusHistoryRepository.findFirstByProfessorOrderByTimestampDesc(professor);
        if (currentStatus != null && currentStatus.getStatus() == StatusHistory.Status.AVAILABLE) {
            throw new IllegalArgumentException("Professor is already available");
        }
        
        // Check if a notification already exists
        Optional<Notification> existingNotification = notificationRepository
                .findByStudentAndProfessorAndNotifiedFalse(student, professor);
        
        if (existingNotification.isPresent()) {
            throw new IllegalArgumentException("Notification already set for this professor");
        }
        
        Notification notification = new Notification();
        notification.setStudent(student);
        notification.setProfessor(professor);
        notification.setNotificationSetAt(LocalDateTime.now());
        notification.setNotified(false);
        
        return notificationRepository.save(notification);
    }

    public List<Notification> getNotificationsForStudent(Long studentId) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));
        
        return notificationRepository.findByStudentAndNotifiedFalse(student);
    }

    public void deleteNotification(Long notificationId) {
        notificationRepository.deleteById(notificationId);
    }

    public void cancelNotification(Long studentId, Long professorId) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));
        
        Professor professor = professorRepository.findById(professorId)
                .orElseThrow(() -> new IllegalArgumentException("Professor not found"));
        
        Optional<Notification> notification = notificationRepository
                .findByStudentAndProfessorAndNotifiedFalse(student, professor);
        
        notification.ifPresent(notificationRepository::delete);
    }
}
