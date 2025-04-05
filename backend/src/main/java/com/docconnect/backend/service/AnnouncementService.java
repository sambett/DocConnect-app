package com.docconnect.backend.service;

import com.docconnect.backend.model.Announcement;
import com.docconnect.backend.model.Professor;
import com.docconnect.backend.repository.AnnouncementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AnnouncementService {

    @Autowired
    private AnnouncementRepository announcementRepository;

    public List<Announcement> getAnnouncementsForProfessor(Professor professor) {
        return announcementRepository.findByProfessorOrderByPostedAtDesc(professor);
    }
    
    public Optional<Announcement> getAnnouncementById(Long id) {
        return announcementRepository.findById(id);
    }
    
    @Transactional
    public Announcement createAnnouncement(Announcement announcement) {
        return announcementRepository.save(announcement);
    }
    
    @Transactional
    public void deleteAnnouncement(Long id) {
        announcementRepository.deleteById(id);
    }
    
    @Transactional
    public Announcement updateAnnouncement(Announcement announcement) {
        return announcementRepository.save(announcement);
    }
}
