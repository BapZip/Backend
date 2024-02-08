package com.example.BapZip.repository;

import com.example.BapZip.domain.Menu;
import com.example.BapZip.domain.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface MenuRepository extends JpaRepository<Menu, Long> {
    List<Menu> findByMenuGroupId(Long menuGroupId);
}
