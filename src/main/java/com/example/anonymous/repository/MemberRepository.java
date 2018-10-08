package com.example.anonymous.repository;

import com.example.anonymous.domain.Member;
import org.springframework.data.repository.CrudRepository;

public interface MemberRepository extends CrudRepository<Member, Long> {
}
