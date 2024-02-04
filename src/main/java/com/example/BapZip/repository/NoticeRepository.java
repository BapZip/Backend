package com.example.BapZip.repository;

import com.example.BapZip.domain.Notice;
import com.example.BapZip.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository

public interface NoticeRepository extends JpaRepository<Notice, Long> {
    List<Notice> findByStoreAndCreatedAtBetween(Store store, LocalDateTime startOfDay, LocalDateTime endOfDay);
}
