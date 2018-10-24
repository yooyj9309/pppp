package com.example.anonymous.security;

import com.example.anonymous.domain.Member;
import com.example.anonymous.exception.InvalidInputException;
import com.example.anonymous.exception.NoAuthException;
import com.example.anonymous.repository.MemberRepository;
import com.example.anonymous.utils.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationProvider.class);

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
        Member member = null;
        SecurityMember user = null;
        Collection<? extends GrantedAuthority> authorities;

        member = memberRepository.findMemberByMemberEmail(SecurityUtil.encryptSHA256(username));
        if(member == null)
            throw new InvalidInputException("이메일이 존재하지 않습니다.");

        if (member.getMemberCheck() == 0) {
            throw new NoAuthException("이메일 인증을 해주십쇼.");
        }
        if (member.getMemberCheck() == 2) {
            throw new NoAuthException("삭제된 계정입니다.");
        }
        if (!encoder.matches(password, member.getMemberPw())) {
            logger.info("비밀번호가 다르다.");
            throw new InvalidInputException("비밀번호가 다릅니다.");
        }
        user = new SecurityMember(member);
        authorities = user.getAuthorities();
        logger.info(user.toString());
        logger.info("로그인 성공");
        return new UsernamePasswordAuthenticationToken(user, password, authorities);
    }
}
