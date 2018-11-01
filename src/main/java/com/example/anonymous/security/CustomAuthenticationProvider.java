package com.example.anonymous.security;

import com.example.anonymous.domain.Member;
import com.example.anonymous.exception.InvalidInputException;
import com.example.anonymous.exception.NoAuthException;
import com.example.anonymous.repository.MemberRepository;
import com.example.anonymous.status.MailCheck;
import com.example.anonymous.status.MemberStatus;
import com.example.anonymous.utils.SecurityUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private MemberRepository memberRepository;

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        Collection<? extends GrantedAuthority> authorities;

        Member member = memberRepository.findMemberByMemberEmail(SecurityUtil.encryptSHA256(username));

        if (member == null)
            throw new InvalidInputException("이메일이 존재하지 않습니다.");

        if (member.getMailCheck() == MailCheck.NO) {
            throw new NoAuthException("이메일 인증을 해주십쇼.");
        }
        if (member.getMemberStatus() == MemberStatus.DELETED) {
            throw new NoAuthException("이메일이 존재하지 않습니다.");
        }
        if (!encoder.matches(password, member.getMemberPw())) {
            throw new InvalidInputException("비밀번호가 다릅니다.");
        }

        SecurityMember user = new SecurityMember(member);
        authorities = user.getAuthorities();

        return new UsernamePasswordAuthenticationToken(user, password, authorities);
    }
}
