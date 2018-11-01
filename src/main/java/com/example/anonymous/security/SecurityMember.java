package com.example.anonymous.security;

import com.example.anonymous.domain.Member;
import com.example.anonymous.domain.MemberRole;
import com.example.anonymous.status.MailCheck;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.List;

public class SecurityMember extends User {
    private static final String ROLE_PREFIX = "ROLE_";

    private MailCheck emailCheck;

    public MailCheck getEmailCheck() {
        return emailCheck;
    }

    public SecurityMember(Member member) {
        super(member.getMemberEmail(), member.getMemberPw(), makeGrantedAuthority(member.getMemberRoles()));
        this.emailCheck=member.getMailCheck();
    }

    //TODO: ADMIN 계정처럼 권한이 필요한 기능 구현할 때 리스트에 권한 값을 넣어주기
    //fetchMode는 lazy이므로 객체 그래프 탐색을 통해 프록시 객체를 얻고 getXX를 통해 사용하자
    private static List<GrantedAuthority> makeGrantedAuthority(List<MemberRole> roles){
        List<GrantedAuthority> list = new ArrayList<>();
        return list;
    }
}
