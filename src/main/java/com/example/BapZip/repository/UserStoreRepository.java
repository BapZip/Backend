package com.example.BapZip.repository;

import com.example.BapZip.domain.Store;
import com.example.BapZip.domain.User;
import com.example.BapZip.domain.mapping.UserStore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

import java.util.Optional;

@Repository
public interface UserStoreRepository extends JpaRepository<UserStore, Long> {
    Optional<UserStore> findByStoreAndUser(Store store, User user);
    List<UserStore> findByUser_Id(Long id);

}
