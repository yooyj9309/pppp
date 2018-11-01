package com.example.anonymous.member;

import com.example.anonymous.domain.Member;
import com.example.anonymous.repository.MemberRepository;
import com.example.anonymous.security.CustomAuthenticationProvider;
import com.example.anonymous.service.MemberService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


@RunWith(SpringRunner.class)
@SpringBootTest
public class MemberServiceTest {
    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MemberService memberService;

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    private static final Logger log = LoggerFactory.getLogger(MemberServiceTest.class);
/*
    @Test
    public void signUpTest() {
        Member member = new Member();

        member.setMemberEmail("rlgus2028@gmail.com");
        member.setMemberNick("yooyj");
        member.setMemberPw("12");
        member.setMemberPwCheck("12");

        Member testMember = null;
        try {
           memberService.insertMember(member);
           testMember = memberRepository.findMemberByMemberEmail(member.getMemberEmail());

        }catch (DataAccessException e){
            log.error("회원 가입 테스트 실패");
        }
        assertNotNull(testMember);
        assertEquals(testMember.getMemberCheck(),0); // 맞는 코드
        assertEquals(testMember.getMemberCheck(),1); // 에러 코드
    }

    //무작위 닉네임 10만번 생성 후
    @Test
    public void getLandomNickNameDuplicateTest(){
        Set<String> set = new HashSet<String>();

        for(int i=0;i<100000;i++) {
            String currentNick = memberService.getRandomName();
            if(set.contains(currentNick)){
                log.error(currentNick + " 중복 발생 " + i + " 번째에서");
                break;
            }
            set.add(currentNick);
        }
        log.info("Not Duplicate");
    }
*/
}
