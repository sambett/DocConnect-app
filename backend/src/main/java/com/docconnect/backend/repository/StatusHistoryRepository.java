package com.docconnect.backend.repository;

import com.docconnect.backend.model.Professor;
import com.docconnect.backend.model.StatusHistory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StatusHistoryRepository extends JpaRepository<StatusHistory, Long> {
    List<StatusHistory> findByProfessorOrderByTimestampDesc(Professor professor);
    Optional<StatusHistory> findTopByProfessorOrderByTimestampDesc(Professor professor);
    Optional<StatusHistory> findFirstByProfessorOrderByTimestampDesc(Professor professor);
}
