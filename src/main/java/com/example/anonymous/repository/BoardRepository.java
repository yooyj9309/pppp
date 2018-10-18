package com.example.anonymous.repository;

import com.example.anonymous.domain.Board;


import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
    Board findBoardByBoardId(Long boardId);
}