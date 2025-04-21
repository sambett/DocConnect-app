package com.docconnect.backend.model;

import com.docconnect.backend.model.enums.Status;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "professors")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Professor {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(nullable = false)
    private String department;
    
    @Column(name = "office_location")
    private String officeLocation;
    
    @Column(name = "working_hours")
    private String workingHours;
    
    @Column(name = "email_verified")
    private Boolean emailVerified;
    
    @Column(name = "status", nullable = false)
    private String status = Status.AWAY.name();
    
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    @Transient
    @JsonInclude
    private List<Announcement> announcements;
    
    @Column(name = "email_contact")
    private String emailContact;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}