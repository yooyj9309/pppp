package com.example.anonymous.jpa;

import com.example.anonymous.domain.Board;
import com.example.anonymous.domain.Member;
import com.example.anonymous.domain.MemberRole;
import com.example.anonymous.domain.Reply;
import com.example.anonymous.repository.BoardRepository;
import com.example.anonymous.repository.MemberRepository;

import com.example.anonymous.repository.ReplyRepository;

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

    @Test
    public void insertTest() {

    }

    @Test
    public void queryTest(){
        Pageable request = new PageRequest(0,10, Sort.Direction.DESC,"boardRegDate");

        Collection<Board> boards = boardRepository.findAllByBoardStatusLessThanAndBoardIdLessThan(2,76L,request);
        for(Board board : boards){
            LOGGER.info(board.toString());
        }
    }

    @Test
    public void replyTest(){
        Pageable request = new PageRequest(0, 10, Sort.Direction.DESC, "replyRegDate");
        List<Reply> replyList = replyRepository.findAllByBoardBoardIdAndReplyStatusLessThan(86,2,request);
        LOGGER.info(replyList.size()+" ");
        for(Reply reply : replyList){
            LOGGER.info(reply.toString());
        }
    }

}
