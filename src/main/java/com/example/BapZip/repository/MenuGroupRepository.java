package com.example.BapZip.repository;

import com.example.BapZip.domain.Menu_Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface MenuGroupRepository extends JpaRepository<Menu_Group, Long> {
}
