package com.example.anonymous.restcontroller;

import com.example.anonymous.domain.Reply;
import com.example.anonymous.service.ReplyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.security.Principal;
import java.util.List;


@RestController
@RequestMapping("reply")
public class ReplyController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReplyController.class);
    @Autowired
    ReplyService replyService;
/*
    @GetMapping(value="/list")
    public List<Reply> sendReplyList(@RequestParam("boardId") long boardId, @RequestParam("page") int page) {
        List<Reply> replyList = replyService.getReplyListByBoardId(boardId, page);

        return replyList;
    }

    @PostMapping()
    public ResponseEntity<String> insertReply(@RequestParam("boardId") long boardId, Reply reply, Principal principal){
        LOGGER.info(reply.toString());
        replyService.insertReply(reply,boardId,principal);
        return new ResponseEntity<String>("댓글 작성에 성공하였습니다.", HttpStatus.OK);
    }

    @PutMapping()
    public ResponseEntity<String> updateReply(@RequestParam long replyId, @RequestParam String content) {
        LOGGER.info(replyId+" "+content);
        replyService.updateReplyByReplyId(replyId,content);

        return new ResponseEntity<String>("댓글 수정에 성공하였습니다.", HttpStatus.OK);
    }

    @DeleteMapping()
    public ResponseEntity<String> deleteReply(@RequestParam("replyId") long replyId) {
        LOGGER.info(replyId+"번 댓글을 삭제합니다.");

        replyService.updateReplyByReplyId(replyId,"해당 댓글은 삭제되었습니다.");

        return new ResponseEntity<String>("댓글 삭제에 성공하였습니다.", HttpStatus.OK);
    }

    @PostMapping(value = "/comment")
    public ResponseEntity<String> insertComment(@RequestParam long replyId, @RequestParam String content,Principal principal) {
        LOGGER.info(replyId+"를 부모로 삼는 "+content +" 답글");
        replyService.insertCommentByReplyId(replyId,content,principal);

        return new ResponseEntity<String>("답글 입력에 성공하였습니다.", HttpStatus.OK);
    }

    @GetMapping(value = "/comment")
    public List<Reply> sendCommentList(@RequestParam long replyId){
        return replyService.getAllCommentListByReplyId(replyId);
    }*/
}
