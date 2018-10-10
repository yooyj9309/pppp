package com.example.anonymous.jpa;

import com.example.anonymous.domain.Member;
import com.example.anonymous.domain.MemberRole;
import com.example.anonymous.repository.MemberRepository;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Date;


@RunWith(SpringRunner.class)
@SpringBootTest

public class JPATest {

    @Autowired
    MemberRepository memberRepository;
    @After
    public void cleanup(){
        memberRepository.deleteAll();
    }
    @Test
    public void insertTest() {
        for(int i=0; i<100; i++) {
            MemberRole role = new MemberRole();
            if(i <= 80) {
                role.setRoleName("BASIC");
            }else if(i <= 90) {
                role.setRoleName("MANAGER");
            }else {
                role.setRoleName("ADMIN");
            }
    //given
            Member member = Member.builder()
                    .memberCheck(i)
                    .memberEmail("Email"+i)
                    .memberRegDate(new Date())
                    .memberModDate(new Date())
                    .memberNick("Nick"+i)
                    .memberPw("Pw"+i)
                    .memberPwCheck("pwCheck")
                    .emailKey("key")
                    .memberStatus(Arrays.asList(role))
                    .build();

            memberRepository.save(member);


        }
    }


}
