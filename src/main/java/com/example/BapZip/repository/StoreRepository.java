package com.example.BapZip.repository;

import com.example.BapZip.domain.School;
import com.example.BapZip.domain.Store;
import com.example.BapZip.domain.enums.InOrOut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import com.example.BapZip.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository

public interface StoreRepository extends JpaRepository<Store, Long> {


    List<Store> findBySchool(School school);


    List<Store> findBySchoolAndOutin(School school, InOrOut inOrOut);

    Optional<Store> findByName(String name); // 리뷰 작성 위해 추가된 부분

    Optional<Store> findById(Long storeId);

    @Query("SELECT s FROM Store s JOIN s.reviewList rl GROUP BY s ORDER BY COUNT(rl) DESC")
    List<Store> findAllStoresOrderByReviewCountDesc();

    List<Store> findAll();

    List<Store> findByNameContains(String storeName);

    // storeId, school에 부합하는 store 찾기
    Optional<Store> findByIdAndSchool(Long storeId, School school);
}
