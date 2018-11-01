package com.example.anonymous.repository;

import com.example.anonymous.domain.Board;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;


public interface BoardRepository extends PagingAndSortingRepository<Board, Long> {

}