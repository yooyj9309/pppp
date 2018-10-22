package com.example.anonymous.service;

import com.example.anonymous.domain.Board;
import com.example.anonymous.domain.Member;
import com.example.anonymous.domain.Reply;
import com.example.anonymous.repository.BoardRepository;
import com.example.anonymous.repository.MemberRepository;
import com.example.anonymous.repository.ReplyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Date;


@Service
public class ReplyService {
    @Autowired
    private ReplyRepository replyRepository;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private MemberRepository memberRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(ReplyService.class);

    public void  insertReply(Reply reply){
       Board board = boardRepository.findBoardByBoardId(reply.getBoardId());
       Member member = memberRepository.findMemberByMemberEmail(reply.getMemberEmail());
       reply.setMemberNick(member.getMemberNick());
       reply.setBoard(board);
       reply.setReplyModDate(new Date());

       replyRepository.save(reply);
    }

}
