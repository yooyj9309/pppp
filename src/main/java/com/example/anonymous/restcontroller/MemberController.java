package com.example.anonymous.restcontroller;

import com.example.anonymous.domain.Member;
import com.example.anonymous.service.MemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MemberController.class);
    @Autowired
    private MemberService memberService;

    @PostMapping(value = "/signup")
    public ResponseEntity<String> join(Member member) {
        LOGGER.info(member.toString());
        memberService.insertMember(member);

        LOGGER.info("Signup SERVICE 로직 성공");
        return new ResponseEntity<String>("해당 메일로 인증 요청을 보냈습니다.", HttpStatus.OK);
    }


}
