package com.example.BapZip.repository;

import com.example.BapZip.domain.Region;
import com.example.BapZip.domain.School;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SchoolRepository extends JpaRepository<School, Long> {

    List<School> findByNameContains(String schoolName);

    List<School> findByRegion(Region region);

    List<School> findAllByOrderByName();
}
