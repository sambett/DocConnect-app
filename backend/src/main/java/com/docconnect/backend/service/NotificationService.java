package com.docconnect.backend.service;

import com.docconnect.backend.model.Notification;
import com.docconnect.backend.model.Professor;
import com.docconnect.backend.model.User;
import com.docconnect.backend.model.enums.Status;
import com.docconnect.backend.repository.NotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public Notification createNotification(User student, Professor professor) {
        // Check if professor is already available
        if (Status.AVAILABLE.name().equals(professor.getStatus())) {
            throw new IllegalArgumentException("Professor is already available");
        }
        
        Notification notification = new Notification();
        notification.setStudent(student);
        notification.setProfessor(professor);
        notification.setNotificationSetAt(LocalDateTime.now());
        notification.setNotified(false);
        
        return notificationRepository.save(notification);
    }
    
    public boolean notificationExists(User student, Professor professor) {
        return notificationRepository.existsByStudentAndProfessorAndNotifiedFalse(student, professor);
    }
    
    public int getWaitingStudentsCount(Professor professor) {
        return notificationRepository.countByProfessorAndNotifiedFalse(professor);
    }
    
    public List<Notification> getNotificationsByStudent(User student) {
        return notificationRepository.findByStudentOrderByNotificationSetAtDesc(student);
    }
    
    public void deleteNotification(Long id) {
        if (!notificationRepository.existsById(id)) {
            throw new IllegalArgumentException("Notification not found");
        }
        notificationRepository.deleteById(id);
    }
    
    @Transactional
    public int markAllAsNotified(Professor professor) {
        List<Notification> notifications = notificationRepository.findByProfessorAndNotifiedFalse(professor);
        
        for (Notification notification : notifications) {
            notification.setNotified(true);
        }
        
        notificationRepository.saveAll(notifications);
        return notifications.size();
    }
}
