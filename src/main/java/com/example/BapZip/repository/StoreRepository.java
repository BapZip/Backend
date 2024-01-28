package com.example.BapZip.repository;

import com.example.BapZip.domain.School;
import com.example.BapZip.domain.Store;
import com.example.BapZip.domain.enums.InOrOut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface StoreRepository extends JpaRepository<Store, Long> {


    List<Store> findBySchool(School school);


    List<Store> findBySchoolAndOutin(School school, InOrOut inOrOut);
}
