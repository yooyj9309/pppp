package com.example.anonymous.restcontroller;

import com.example.anonymous.dto.MemberDTO;
import com.example.anonymous.exception.InvalidInputException;
import com.example.anonymous.service.MemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
public class MemberController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MemberController.class);
    @Autowired
    private MemberService memberService;

    @PostMapping(value = "/signup")
    public void join(@Valid MemberDTO member) {
        LOGGER.info(member.toString());
        memberService.insertMember(member);
    }

    @PutMapping(value = "/nick_name")
    public void changeNickName(@RequestParam String nickName, Principal principal) {
        if(StringUtils.isEmpty(nickName)){
            throw new InvalidInputException("닉네임을 입력해주세요");
        }
        memberService.changNickName(nickName, principal.getName());
    }

    @DeleteMapping(value = "/member_out")
    public void withdrawMember(Principal principal) {
        memberService.deleteMemberByMemberEmail(principal.getName());
    }
}
