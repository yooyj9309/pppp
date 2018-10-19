package com.example.anonymous.repository;

import com.example.anonymous.domain.Board;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;


public interface BoardRepository extends PagingAndSortingRepository<Board, Long> {
    Board findBoardByBoardId(Long boardId);

    List<Board> findAllByBoardStatusLessThan(int boardStatus, Pageable request);

    List<Board> findAllByBoardStatusLessThanAndBoardIdLessThan(int boardStatus, long boardId, Pageable request);

    List<Board> findAllByBoardStatusLessThanAndBoardIdGreaterThan(int boardStatus, long boardId, Pageable request);
}