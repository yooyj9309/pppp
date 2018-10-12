package com.example.anonymous.service;

import com.example.anonymous.domain.Member;
import com.example.anonymous.domain.MemberRole;
import com.example.anonymous.exception.InvalidInputException;
import com.example.anonymous.exception.NoAuthException;
import com.example.anonymous.exception.ServerException;
import com.example.anonymous.repository.MemberRepository;
import com.example.anonymous.utils.MailHandler;
import com.example.anonymous.utils.SecurityUtil;
import com.example.anonymous.utils.TempKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

@Service
public class MemberService {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private JavaMailSender mailSender;

    private static final Logger LOGGER = LoggerFactory.getLogger(MemberService.class);

    @Transactional
    public void insertMember(Member member) {
        String email = member.getMemberEmail();
        String pw = member.getMemberPw();
        String pwCheck = member.getMemberPwCheck();
        String nick = member.getMemberNick();
        String encryptEmail = SecurityUtil.encryptSHA256(email);

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        //사용자 입력 값이 null일 때
        if (StringUtils.isEmpty(email) || StringUtils.isEmpty(pw) || StringUtils.isEmpty(pwCheck) || StringUtils.isEmpty(nick)) {
            throw new NullPointerException();
        }

        //수정 필요
        //비밀번호와 체크가 다를 때
        if (!pw.equals(pwCheck)) {
            throw new InvalidInputException("비밀 번호가 다릅니다.");
        }

        //이미 존재하는 이메일인 경우
        //matches를 사용하는 경우 list iterator를 사용해야 하기 때문에 단일 hash로 구현하기
        if (memberRepository.findMemberByMemberEmail(encryptEmail) != null) {
            throw new InvalidInputException("이미 가입한 메일입니다.");
        }

        //이미 닉네임이 있다면
        if (memberRepository.findMemberByMemberNick(nick) != null) {
            LOGGER.error(memberRepository.findMemberByMemberNick(nick).getMemberNick() + " 이 존재 합니다.");
            throw new InvalidInputException("이미 존재하는 닉네임입니다.");
        }

        MemberRole role = new MemberRole();
        role.setRoleName("BASIC");
        String key = new TempKey().getKey(50, false);

        member = member.builder()
                .memberEmail(encryptEmail)
                .memberPw(encoder.encode(member.getMemberPw()))
                .memberPwCheck(encoder.encode(member.getMemberPw()))
                .memberCheck(0)
                .emailKey(key)
                .memberNick(nick)
                .build();

        LOGGER.info(member.toString());

        memberRepository.save(member);
        LOGGER.info("Create");

        sendMail(email, key);
    }

    private void sendMail(String email, String key) {
        try {
            MailHandler mailHandler = new MailHandler(mailSender);
            mailHandler.setSubject("[익명 게시판 이메일 인증입니다.]");
            mailHandler.setText(new StringBuffer()
                    .append("<h1>메일인증</h1>")
                    .append("<a href='http://localhost:8080/emailConfirm?userEmail=")
                    .append(email)
                    .append("&key=")
                    .append(key)
                    .append("' target='_blenk'>이메일 인증 확인</a>")
                    .toString());

            mailHandler.setFrom("yooyj9219@gmail.com", "yooyj9309");
            mailHandler.setTo(email);
            mailHandler.send();
        } catch (UnsupportedEncodingException e) {
            throw new ServerException("메일 인증 중 오류");
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new ServerException("메일 인증 중 오류");
        }
    }

    public void updateAuth(String email, String key) {
        if(StringUtils.isEmpty(email))
            throw new InvalidInputException("잘못된 입력 값입니다.");
        email = SecurityUtil.encryptSHA256(email);

        Member member = memberRepository.findMemberByMemberEmail(email);
        if(member == null) {
            throw new NoAuthException("잘못된 접근 입니다.");
        }
        if (!member.getEmailKey().equals(key)) {
            throw new NoAuthException("잘못된 접근입니다.");
        }

        member.setMemberCheck(1);
        memberRepository.save(member);
    }
}
