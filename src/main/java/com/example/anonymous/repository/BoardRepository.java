package com.example.anonymous.repository;

import com.example.anonymous.domain.Board;
import com.example.anonymous.status.BoardStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.security.core.parameters.P;

import java.util.List;


public interface BoardRepository extends PagingAndSortingRepository<Board, Long> {
    List<Board> findAllByBoardStatusIsNot(BoardStatus boardStatus, Pageable request);

    List<Board> findAllByBoardStatusIsNotAndBoardIdLessThan(BoardStatus boardStatus, long boardId, Pageable request);

    List<Board> findAllByBoardStatusIsNotAndBoardIdGreaterThan(BoardStatus boardStatus, long boardId, Pageable request);
}