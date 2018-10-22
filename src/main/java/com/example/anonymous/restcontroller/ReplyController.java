package com.example.anonymous.restcontroller;

import com.example.anonymous.domain.Member;
import com.example.anonymous.domain.Reply;
import com.example.anonymous.service.MemberService;
import com.example.anonymous.service.ReplyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("reply")
public class ReplyController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReplyController.class);
    @Autowired
    ReplyService replyService;

    @GetMapping(value = "/")
    public ResponseEntity<String> sendReplyList(Member member) {

        return new ResponseEntity<String>("해당 메일로 인증 요청을 보냈습니다.", HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<String> insertReply(Reply reply){
        LOGGER.info(reply.toString());
        replyService.insertReply(reply);

        return new ResponseEntity<String>("댓글 작성에 성공하였습니다.", HttpStatus.OK);
    }



}
