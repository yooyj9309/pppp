package com.example.anonymous.jpa;

import com.example.anonymous.domain.Board;
import com.example.anonymous.domain.LikeTable;
import com.example.anonymous.domain.Member;
import com.example.anonymous.domain.Reply;
import com.example.anonymous.repository.BoardRepository;
import com.example.anonymous.repository.LikeRepository;
import com.example.anonymous.repository.MemberRepository;

import com.example.anonymous.repository.ReplyRepository;

import com.example.anonymous.service.BoardService;
import com.example.anonymous.service.MemberService;
import com.example.anonymous.service.ReplyService;
import com.example.anonymous.status.BoardStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertNotNull;


@RunWith(SpringRunner.class)
@SpringBootTest

public class JPATest {
    private static final Logger LOGGER = LoggerFactory.getLogger(JPATest.class);
    @Autowired
    MemberRepository memberRepository;

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    ReplyRepository replyRepository;

    @Autowired
    LikeRepository likeRepository;

    @Autowired
    ReplyService replyService;

    @Autowired
    MemberService memberService;

    @Autowired
    BoardService boardService;

    @Test
    public void insertTest() {
        Pageable request = PageRequest.of(0, 5, Sort.Direction.DESC, "boardRegDate");
        List<Board> list = boardRepository.findAllByBoardStatusIsNot(BoardStatus.DELETED, request);
        for(Board b:list){
            LOGGER.info(b.toString());
        }
    }


}
