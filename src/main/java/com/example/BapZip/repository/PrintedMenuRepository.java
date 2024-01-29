package com.example.BapZip.repository;

import com.example.BapZip.domain.PrintedMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface PrintedMenuRepository extends JpaRepository<PrintedMenu, Long> {
}
