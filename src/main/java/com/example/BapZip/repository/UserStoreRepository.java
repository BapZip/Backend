package com.example.BapZip.repository;

import com.example.BapZip.domain.mapping.UserStore;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserStoreRepository extends JpaRepository<UserStore, Long> {
}
