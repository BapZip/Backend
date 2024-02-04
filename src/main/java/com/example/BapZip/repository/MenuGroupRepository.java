package com.example.BapZip.repository;

import com.example.BapZip.domain.Menu;
import com.example.BapZip.domain.Menu_Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface MenuGroupRepository extends JpaRepository<Menu_Group, Long> {
    List<Menu_Group> findByStoreId(Long storeId);
}
