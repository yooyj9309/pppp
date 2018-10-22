package com.example.anonymous.restcontroller;

import com.example.anonymous.domain.Member;
import com.example.anonymous.domain.Reply;
import com.example.anonymous.service.MemberService;
import com.example.anonymous.service.ReplyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("reply")
public class ReplyController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReplyController.class);
    @Autowired
    ReplyService replyService;

    @GetMapping()
    public List<Reply> sendReplyList(@RequestParam("boardId") long boardId) {
        return replyService.getReplyListByBoardId(boardId);
    }

    @PostMapping()
    public ResponseEntity<String> insertReply(@RequestParam("boardId") long boardId, Reply reply){
        LOGGER.info(reply.toString());
        replyService.insertReply(reply,boardId);
        return new ResponseEntity<String>("댓글 작성에 성공하였습니다.", HttpStatus.OK);
    }



}
