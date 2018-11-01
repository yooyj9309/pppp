package com.example.anonymous.repository;

import com.example.anonymous.domain.Reply;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ReplyRepository extends PagingAndSortingRepository<Reply, Long> {

 
}
