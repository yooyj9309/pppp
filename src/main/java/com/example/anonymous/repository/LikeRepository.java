package com.example.anonymous.repository;

import com.example.anonymous.domain.Board;
import com.example.anonymous.domain.LikeTable;
import com.example.anonymous.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<LikeTable, Long> {
   LikeTable findByBoardAndMember(Board board, Member member);
}

