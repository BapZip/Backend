package com.example.BapZip.repository;

import com.example.BapZip.domain.Major;
import com.example.BapZip.domain.School;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface MajorRepository extends JpaRepository<Major, Long> {
    List<Major> findBySchoolAndNameContains(School school, String majorName);
}
