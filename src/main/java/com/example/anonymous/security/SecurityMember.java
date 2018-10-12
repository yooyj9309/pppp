package com.example.anonymous.security;

import com.example.anonymous.domain.Member;
import com.example.anonymous.domain.MemberRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.List;

public class SecurityMember extends User {
    private static final String ROLE_PREFIX = "ROLE_";
    private static final long serialVersionUID = 1L;

    public SecurityMember(Member member) {
        super(member.getMemberEmail(), member.getMemberPw(), makeGrantedAuthority(member.getMemberStatus()));
    }

    private static List<GrantedAuthority> makeGrantedAuthority(List<MemberRole> roles){
        List<GrantedAuthority> list = new ArrayList<>();
        roles.forEach(role -> list.add(new SimpleGrantedAuthority(ROLE_PREFIX + role.getRoleName())));
        return list;
    }
}
