package com.example.BapZip.repository;

import com.example.BapZip.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface MenuRepository extends JpaRepository<Menu, Long> {
}
