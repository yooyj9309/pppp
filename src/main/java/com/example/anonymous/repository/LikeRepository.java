package com.example.anonymous.repository;

import com.example.anonymous.domain.LikeTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<LikeTable, Long> {


}

