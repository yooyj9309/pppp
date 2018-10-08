package com.example.anonymous.jpa;

import com.example.anonymous.domain.Member;
import com.example.anonymous.domain.MemberRole;
import com.example.anonymous.repository.MemberRepository;
import lombok.extern.java.Log;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Date;


@RunWith(SpringRunner.class)
@SpringBootTest
@Log
public class JPATest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    public void insertTest() {
        for(int i=0; i<100; i++) {
            Member member = new Member();
            member.setMemberEmail("dd"+i);
            member.setMemberPw("pw"+i);
            member.setMemberModDate(new Date());
            member.setMemberRegDate(new Date());
            member.setMemberNick("nick"+i);
            member.setMemberCheck(i);
            MemberRole role = new MemberRole();
            if(i <= 80) {
                role.setRoleName("BASIC");
            }else if(i <= 90) {
                role.setRoleName("MANAGER");
            }else {
                role.setRoleName("ADMIN");
            }
            member.setMemberStatus(Arrays.asList(role));
            memberRepository.save(member);
        }
    }


}
