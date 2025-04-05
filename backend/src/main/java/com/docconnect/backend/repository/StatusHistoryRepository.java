package com.docconnect.backend.repository;

import com.docconnect.backend.model.Professor;
import com.docconnect.backend.model.StatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface StatusHistoryRepository extends JpaRepository<StatusHistory, Long> {
    List<StatusHistory> findByProfessorOrderByTimestampDesc(Professor professor);
    StatusHistory findFirstByProfessorOrderByTimestampDesc(Professor professor);
}
