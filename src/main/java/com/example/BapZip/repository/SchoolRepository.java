package com.example.BapZip.repository;

import com.example.BapZip.domain.School;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface SchoolRepository extends JpaRepository<School, Long> {
}
