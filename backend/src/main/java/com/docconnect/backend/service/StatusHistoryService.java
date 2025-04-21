package com.docconnect.backend.service;

import com.docconnect.backend.model.StatusHistory;
import com.docconnect.backend.repository.StatusHistoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatusHistoryService {

    private final StatusHistoryRepository statusHistoryRepository;

    public StatusHistoryService(StatusHistoryRepository statusHistoryRepository) {
        this.statusHistoryRepository = statusHistoryRepository;
    }

    public StatusHistory saveStatusHistory(StatusHistory statusHistory) {
        return statusHistoryRepository.save(statusHistory);
    }

    public List<StatusHistory> getStatusHistoryByProfessorId(Long professorId) {
        return statusHistoryRepository.findByProfessorIdOrderByTimestampDesc(professorId);
    }

    public void deleteStatusHistory(Long id) {
        statusHistoryRepository.deleteById(id);
    }
}
