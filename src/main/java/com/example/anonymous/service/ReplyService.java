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

    public void insertReply(Reply reply,long boardId) {
        String replyContents = reply.getReplyContents();
        if (StringUtils.isEmpty(replyContents)) {
            throw new InvalidInputException("댓글을 입력해주세요.");
        }
        Board board = boardRepository.findBoardByBoardId(boardId);

        LOGGER.info(board.toString());
        LOGGER.info(board.getMember().getMemberNick());

        reply.setMemberNick(board.getMember().getMemberNick());
        reply.setBoard(board);
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
}
