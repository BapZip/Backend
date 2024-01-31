package com.example.BapZip.repository;

import com.example.BapZip.domain.User;
import com.example.BapZip.domain.mapping.UserStore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserStoreRepository extends JpaRepository<UserStore, Long> {

    List<UserStore> findByUser_Id(Long id);
}
