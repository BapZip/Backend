package com.example.BapZip.repository;

import com.example.BapZip.domain.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface HashtagRepository extends JpaRepository<Hashtag, Long> {
}
