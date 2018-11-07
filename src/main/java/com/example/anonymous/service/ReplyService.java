package com.example.anonymous.service;

import com.example.anonymous.dto.ReplyDTO;
import com.example.anonymous.domain.Board;
import com.example.anonymous.domain.Member;
import com.example.anonymous.domain.Reply;
import com.example.anonymous.exception.ServerException;
import com.example.anonymous.repository.BoardRepository;
import com.example.anonymous.repository.MemberRepository;
import com.example.anonymous.repository.ReplyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;


import java.security.Principal;


@Service
public class ReplyService {
    private static final int ONE_REPLY_SIZE = 10;
    private static final int ONE_COMMENT_SIZE = 3;

    private static final long ROOT_REPLY = 0;
    private static final int DELETED_REPLY = 2;

    @Autowired
    private ReplyRepository replyRepository;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private MemberRepository memberRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(ReplyService.class);

    public void insertReply(ReplyDTO replyDTO, long boardId, Principal principal) {
        Board board = boardRepository.findByBoardId(boardId);
        Member authorMember = memberRepository.findMemberByMemberEmail(principal.getName());

        Reply reply = replyDTO.toEntity(board,authorMember);
        try {
            replyRepository.save(reply);
        } catch (DataAccessException e) {
            throw new ServerException("댓글 저장 중 문제가 발생하였습니다.");
        }

    }

/*

    public void updateReplyByReplyId(long replyId, String content) {
        Reply reply = replyRepository.findByReplyId(replyId);
        if (reply.getReplyStatus() == DELETED_REPLY) {
            throw new InvalidInputException("이미 지워진 댓글 입니다.");
        }
        reply.setReplyContents(content);
        reply.setReplyModDate(new Date());

        if (content.equals("해당 댓글은 삭제되었습니다.")) {
            reply.setReplyStatus(DELETED_REPLY);
        }
        replyRepository.save(reply);
    }

    public void insertCommentByReplyId(long replyId, String content, Principal principal) {
        Reply parentReply = replyRepository.findByReplyId(replyId);
        Member authorMember = memberRepository.findMemberByMemberEmail(principal.getName());
        Reply comment = new Reply();

        if (StringUtils.isEmpty(content)) {
            throw new InvalidInputException("답글을 입력해주세요.");
        }

        comment.setBoard(parentReply.getBoard());
        comment.setSessionEmail(authorMember.getMemberEmail());
        comment.setReplyContents(content);
        comment.setReplyParentId(replyId);
        comment.setReplyModDate(new Date());
        comment.setMemberNick(authorMember.getMemberNick());

        replyRepository.save(comment);
    }

    public List<Reply> getCommentListByReplyId(long replyId) {
        Pageable request = new PageRequest(0, ONE_COMMENT_SIZE, Sort.Direction.DESC, "replyRegDate");
        List<Reply> commentList = replyRepository.findAllByReplyParentId(replyId, request);
        return commentList;
    }

    public List<Reply> getAllCommentListByReplyId(long replyId){
        Pageable request = new PageRequest(0,Integer.MAX_VALUE,Sort.Direction.DESC, "replyRegDate");
        List<Reply> allCommentList = replyRepository.findAllByReplyParentId(replyId,request);
        return allCommentList;
    }

    public List<Reply> getReplyListByMemberEmail(String memberEmail){
        List<Reply> replyListByMemberEmail = replyRepository.findAllBySessionEmail(memberEmail);
        for(Reply reply:replyListByMemberEmail){
            reply.setBoardId(reply.getBoard().getBoardId());
        }
        return replyListByMemberEmail;
    }

    */
}
