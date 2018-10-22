package com.example.anonymous.repository;

import com.example.anonymous.domain.Reply;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ReplyRepository extends PagingAndSortingRepository<Reply, Long> {

}
