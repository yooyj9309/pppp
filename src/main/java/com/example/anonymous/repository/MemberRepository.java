package com.example.anonymous.repository;

import com.example.anonymous.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MemberRepository extends JpaRepository<Member, Long> {

}
