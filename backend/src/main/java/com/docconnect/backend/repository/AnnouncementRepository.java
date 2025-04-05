package com.docconnect.backend.repository;

import com.docconnect.backend.model.Announcement;
import com.docconnect.backend.model.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {
    List<Announcement> findByProfessorOrderByPostedAtDesc(Professor professor);
}
