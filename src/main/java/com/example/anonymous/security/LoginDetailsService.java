package com.example.anonymous.security;

import com.example.anonymous.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public class LoginDetailsService implements UserDetailsService {
    @Autowired
    MemberRepository memberRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return
                Optional.ofNullable(memberRepository.findMemberByMemberEmail(username))
                        .filter(m -> m!= null)
                        .map(m -> new SecurityMember(m)).get();
    }
}
