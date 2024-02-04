package com.example.BapZip.repository;

import com.example.BapZip.domain.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface NoticeRepository extends JpaRepository<Notice, Long> {
}
