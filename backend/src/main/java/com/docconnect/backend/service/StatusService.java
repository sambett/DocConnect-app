package com.docconnect.backend.service;

import com.docconnect.backend.model.Notification;
import com.docconnect.backend.model.Professor;
import com.docconnect.backend.model.StatusHistory;
import com.docconnect.backend.repository.NotificationRepository;
import com.docconnect.backend.repository.ProfessorRepository;
import com.docconnect.backend.repository.StatusHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatusService {

    private final StatusHistoryRepository statusHistoryRepository;
    private final ProfessorRepository professorRepository;
    private final NotificationRepository notificationRepository;

    public StatusHistory getCurrentStatusByProfessorId(Long professorId) {
        Professor professor = professorRepository.findById(professorId)
                .orElseThrow(() -> new IllegalArgumentException("Professor not found"));
        
        return statusHistoryRepository.findFirstByProfessorOrderByTimestampDesc(professor);
    }

    public List<StatusHistory> getStatusHistoryByProfessorId(Long professorId) {
        Professor professor = professorRepository.findById(professorId)
                .orElseThrow(() -> new IllegalArgumentException("Professor not found"));
        
        return statusHistoryRepository.findByProfessorOrderByTimestampDesc(professor);
    }

    @Transactional
    public StatusHistory updateStatus(Long professorId, StatusHistory.Status status) {
        Professor professor = professorRepository.findById(professorId)
                .orElseThrow(() -> new IllegalArgumentException("Professor not found"));
        
        StatusHistory statusHistory = new StatusHistory();
        statusHistory.setProfessor(professor);
        statusHistory.setStatus(status);
        statusHistory.setTimestamp(LocalDateTime.now());
        
        statusHistory = statusHistoryRepository.save(statusHistory);
        
        // If status is AVAILABLE, notify all waiting students
        if (status == StatusHistory.Status.AVAILABLE) {
            List<Notification> notifications = notificationRepository.findByProfessorAndNotifiedFalse(professor);
            notifications.forEach(notification -> {
                notification.setNotified(true);
                notificationRepository.save(notification);
                
                // In a real implementation, you would send a notification to the student here
                // (e.g., via WebSocket, email, or push notification)
            });
        }
        
        return statusHistory;
    }

    public int getWaitingStudentsCount(Long professorId) {
        Professor professor = professorRepository.findById(professorId)
                .orElseThrow(() -> new IllegalArgumentException("Professor not found"));
        
        return notificationRepository.countByProfessorAndNotifiedFalse(professor);
    }
}
