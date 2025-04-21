package com.docconnect.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    @com.fasterxml.jackson.annotation.JsonBackReference("student-notification")
    private User student;
    
    @ManyToOne
    @JoinColumn(name = "professor_id", nullable = false)
    @com.fasterxml.jackson.annotation.JsonBackReference("professor-notification")
    private Professor professor;
    
    @Column(name = "notification_set_at", nullable = false)
    private LocalDateTime notificationSetAt;
    
    @Column(nullable = false)
    private Boolean notified;
    
    @PrePersist
    protected void onCreate() {
        notificationSetAt = LocalDateTime.now();
        notified = false;
    }
}
