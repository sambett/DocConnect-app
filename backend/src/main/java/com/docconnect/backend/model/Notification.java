package com.docconnect.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnore;

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
    @JsonIgnore
    private User student;
    
    @ManyToOne
    @JoinColumn(name = "professor_id", nullable = false)
    @JsonIgnore
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
    
    // Methods to get just the IDs for JSON serialization
    @Transient
    public Long getStudentId() {
        return student != null ? student.getId() : null;
    }
    
    @Transient
    public Long getProfessorId() {
        return professor != null ? professor.getId() : null;
    }
}
