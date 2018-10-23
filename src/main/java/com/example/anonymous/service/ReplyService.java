package com.example.anonymous.service;

import com.example.anonymous.domain.Board;
import com.example.anonymous.domain.Member;
import com.example.anonymous.domain.Reply;
import com.example.anonymous.exception.InvalidInputException;
import com.example.anonymous.exception.ServerException;
import com.example.anonymous.repository.BoardRepository;
import com.example.anonymous.repository.MemberRepository;
import com.example.anonymous.repository.ReplyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


import java.security.Principal;
import java.util.Date;
import java.util.List;


@Service
public class ReplyService {
    private static final int ONE_REPLY_SIZE = 10;
    private static final int DELETED_REPLY = 2;

    @Autowired
    private ReplyRepository replyRepository;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private MemberRepository memberRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(ReplyService.class);

    public void insertReply(Reply reply, long boardId, Principal principal) {
        String replyContents = reply.getReplyContents();
        if (StringUtils.isEmpty(replyContents)) {
            throw new InvalidInputException("댓글을 입력해주세요.");
        }
        Board board = boardRepository.findBoardByBoardId(boardId);
        Member authorMember = memberRepository.findMemberByMemberEmail(principal.getName());

        reply.setMemberNick(authorMember.getMemberNick());
        reply.setBoard(board);
        reply.setReplyModDate(new Date());
        reply.setSessionEmail(principal.getName());

        try{
            replyRepository.save(reply);
        }catch(DataAccessException e){
            throw new ServerException("댓글 저장 중 문제가 발생하였습니다.");
        }

    }

    public List<Reply> getReplyListByBoardId(long boardId) {
        Pageable request = new PageRequest(0, ONE_REPLY_SIZE, Sort.Direction.DESC, "replyRegDate");
        List<Reply> replyList = replyRepository.findAllByBoardBoardIdAndReplyStatusLessThan(boardId, DELETED_REPLY, request);
        return replyList;
    }

    public void updateReplyByReplyId(long replyId, String content){
        Reply reply = replyRepository.findByReplyId(replyId);
        reply.setReplyContents(content);
        reply.setReplyModDate(new Date());

        replyRepository.save(reply);
    }
}
