package com.example.anonymous.jpa;

import com.example.anonymous.domain.Board;
import com.example.anonymous.domain.Member;
import com.example.anonymous.domain.MemberRole;
import com.example.anonymous.repository.BoardRepository;
import com.example.anonymous.repository.MemberRepository;

import com.example.anonymous.restcontroller.BoardController;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;


@RunWith(SpringRunner.class)
@SpringBootTest

public class JPATest {
    private static final Logger LOGGER = LoggerFactory.getLogger(JPATest.class);
    @Autowired
    MemberRepository memberRepository;

    @Autowired
    BoardRepository boardRepository;

    @Test
    public void insertTest() {
        for(int i=0; i<100; i++) {
            MemberRole role = new MemberRole();
            if(i <= 80) {
                role.setRoleName("BASIC");
            }else if(i <= 90) {
                role.setRoleName("MANAGER");
            }else {
                role.setRoleName("ADMIN");
            }
    //given
            Member member = Member.builder()
                    .memberCheck(i)
                    .memberEmail("Email"+i)
                    .memberRegDate(new Date())
                    .memberModDate(new Date())
                    .memberNick("Nick"+i)
                    .memberPw("Pw"+i)
                    .memberPwCheck("pwCheck")
                    .emailKey("key")
                    .memberStatus(Arrays.asList(role))
                    .build();

            memberRepository.save(member);


        }
    }

    @Test
    public void queryTest(){
        Pageable request = new PageRequest(0,10, Sort.Direction.DESC,"boardRegDate");

        Collection<Board> boards = boardRepository.findAllByBoardStatusLessThanAndBoardIdLessThan(2,76L,request);
        for(Board board : boards){
            LOGGER.info(board.toString());
        }
    }

}
