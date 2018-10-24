package com.example.anonymous.jpa;

import com.example.anonymous.domain.Board;
import com.example.anonymous.domain.Member;
import com.example.anonymous.domain.Reply;
import com.example.anonymous.repository.BoardRepository;
import com.example.anonymous.repository.MemberRepository;

import com.example.anonymous.repository.ReplyRepository;

import com.example.anonymous.service.ReplyService;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    @Autowired
    ReplyService replyService;

    @Test
    public void insertTest() {
       Member member = new Member();
       member.setMemberNick("yooyj9209");
       member.setMemberEmail("f0cc5c6b73a3b45028df06d3a57de2cfc7321f40c28b8a1c39056fe1e43963d3");
       member.setMemberRegDate(new Date());
       member.setMemberCheck(1);
       member.setMemberPw("$2a$10$NTKEwsw0VfKA5tuWmtWgh.uV6umdyix9Fc508gwbu92RzRNwdYngy");
       member.setEmailKey("BuvfUxAgkkDfOIfgvYHkU0G5HICET61fT9JexGmUrQOapEaVXJ");

       memberRepository.save(member);
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
        List<Reply> replyList = replyService.getReplyListByBoardId(16,0);
        LOGGER.info(replyList.size()+" ");
        for(Reply reply : replyList){
            LOGGER.info(reply.toString());
            if(reply.getCommentList()!=null) {
                for (Reply comment : reply.getCommentList()) {
                    LOGGER.info("답글 : " + comment.toString());
                }
            }
        }
        
        Pageable request = new PageRequest(1, 10, Sort.Direction.DESC, "replyRegDate");
        replyList =   replyRepository.findAllByBoardBoardIdAndReplyParentId(16,0,request);
        LOGGER.info(replyList.size()+" ");
        for(Reply reply : replyList){
            LOGGER.info(reply.toString());
        }
    }

    @Test
    public void commentTest(){
        Pageable request = new PageRequest(0, 3, Sort.Direction.DESC, "replyRegDate");
        List<Reply> replyList = replyRepository.findAllByReplyParentId(44,request);
        LOGGER.info(replyList.size()+" ");
        for(Reply reply : replyList){
            LOGGER.info(reply.toString());
        }
        request = new PageRequest(1, 10, Sort.Direction.DESC, "replyRegDate");
        replyList =  replyRepository.findAllByReplyParentId(44,request);
        LOGGER.info(replyList.size()+" ");
        for(Reply reply : replyList){
            LOGGER.info(reply.toString());
        }
    }
    @Test
    public void Test(){
       List<Member> list = memberRepository.findAll();
       for(Member m : list){
           m.setMemberModDate(null);
           memberRepository.save(m);
       }

    }
}
