package com.example.anonymous.service;

import com.example.anonymous.domain.Member;
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
import org.springframework.dao.DataAccessException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.Date;

@Service
public class MemberService {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private JavaMailSender mailSender;

    private static final int DELETED_MEMBER_STATUS = 2;

    private static final int EMAIL_CHECK = 1;
    private static final Logger LOGGER = LoggerFactory.getLogger(MemberService.class);
/*
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
            throw new InvalidInputException("입력 값을 제대로 입력해주세요");
        }

        //비밀번호와 체크가 다를 때
        if (!pw.equals(pwCheck)) {
            throw new InvalidInputException("비밀 번호가 다릅니다.");
        }

        //이미 존재하는 이메일인 경우
        //matches를 사용하는 경우 list iterator를 사용해야 하기 때문에 단일 hash로 구현하기
        if (memberRepository.findMemberByMemberEmail(encryptEmail) != null && memberRepository.findMemberByMemberEmail(encryptEmail).getMemberCheck()!= DELETED_MEMBER_STATUS) {
            throw new InvalidInputException("이미 가입한 메일입니다.");
        }

        //이미 닉네임이 있다면
        if (memberRepository.findMemberByMemberNick(nick) != null) {
            LOGGER.error(memberRepository.findMemberByMemberNick(nick).getMemberNick() + " 이 존재 합니다.");
            throw new InvalidInputException("이미 존재하는 닉네임입니다.");
        }

        String key = new TempKey().getKey(50, false);

        member.setMemberEmail(encryptEmail);
        member.setMemberPw(encoder.encode(member.getMemberPw()));
        member.setEmailKey(key);
        member.setMemberNick(nick);

        LOGGER.info(member.toString());

        memberRepository.save(member);
        LOGGER.info("Sign UP Member");

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
            LOGGER.info(email+" - 이메일 전송 완료");

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

        member.setMemberCheck(EMAIL_CHECK);
        memberRepository.save(member);
    }

    public void changNickName(String nickName,String sessionEmail){
        if(StringUtils.isEmpty(nickName)){
            throw new InvalidInputException("닉네임을 입력해주세요");
        }
        Member member = null;
        try{
            member = memberRepository.findMemberByMemberNick(nickName);

            if(member!=null) {
                throw new InvalidInputException("이미 존재하는 닉네임 입니다.");
            }else {
                member = memberRepository.findMemberByMemberEmail(sessionEmail);
                LOGGER.info(member.getMemberRegDate()+" "+member.getMemberModDate());

                if(member.getMemberModDate() == null || getDiffDate(member.getMemberModDate())>=7) {
                    member.setMemberNick(nickName);
                    member.setMemberModDate(new Date());
                    memberRepository.save(member);
                }else{
                    throw new InvalidInputException("닉네임을 바꾸기에 너무 이릅니다.");
                }

            }
        }catch (DataAccessException e){
            throw  new ServerException("닉네임을 바꾸던 도중 서버 에러");
        }
    }

    private long getDiffDate(Date mod){
        long diffDate = new Date().getTime() - mod.getTime();

        diffDate = diffDate/(24*60*60*1000);
        LOGGER.info(diffDate+" ");
        return diffDate;
    }
    public void deleteMemberByMemberEmail(String memberEmail){
        if(StringUtils.isEmpty(memberEmail)){
            throw new NoAuthException("삭제할 권한이 없습니다.");
        }
        Member member = memberRepository.findMemberByMemberEmail(memberEmail);
        member.setMemberCheck(DELETED_MEMBER_STATUS);
        memberRepository.save(member);
    }

    public String getRandomName(){
        final int leastNameSize = 4;
        final int MAX_NAME_SIZE = 12;

        StringBuffer randomName = new StringBuffer();
        int nameSize = (int)((Math.random()*100)%MAX_NAME_SIZE+leastNameSize);

        for(int i = 0; i <nameSize; i++){
            char ch = (char)((Math.random() * 11172) + 0xAC00);
            randomName.append(ch);
        }
        return randomName.toString();
    }
*/}
