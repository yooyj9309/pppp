package com.example.anonymous.service;

import com.example.anonymous.dto.MemberDTO;
import com.example.anonymous.domain.Member;
import com.example.anonymous.exception.InvalidInputException;
import com.example.anonymous.exception.NoAuthException;
import com.example.anonymous.exception.ServerException;
import com.example.anonymous.repository.MemberRepository;
import com.example.anonymous.status.MailCheck;
import com.example.anonymous.status.MemberStatus;
import com.example.anonymous.utils.MailHandler;
import com.example.anonymous.utils.SecurityUtil;
import com.example.anonymous.utils.TempKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.mail.javamail.JavaMailSender;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(MemberService.class);
    private static final int WEEK = 7;

    @Transactional
    public void insertMember(MemberDTO memberDTO) {
        String encryptEmail = SecurityUtil.encryptSHA256(memberDTO.getMemberEmail());
        String nickName = memberDTO.getMemberNick();

        Member newMember = memberRepository.findMemberByMemberEmail(encryptEmail);

        if (!memberDTO.isCheckPassword()) {
            throw new InvalidInputException("비밀 번호가 다릅니다.");
        }

        if (newMember != null && newMember.getMemberStatus() != MemberStatus.DELETED) {
            throw new InvalidInputException("이미 가입한 메일입니다.");
        }

        if (memberRepository.findMemberNickByMemberNick(nickName) != null) {
            throw new InvalidInputException("이미 존재하는 닉네임입니다.");
        }

        String key = new TempKey().getKey(50, false);

        if(newMember.getMemberStatus() == MemberStatus.DELETED){
            newMember.recoverMember(nickName,key);
        }else {
            newMember = memberDTO.toEntity(key);
        }
        memberRepository.save(newMember);

        LOGGER.info("데이터 베이스 저장 및 이메일 인증 요청");
        sendMail(memberDTO.getMemberEmail(), key);
    }

    private void sendMail(String email, String key) {
        try {
            MailHandler mailHandler = new MailHandler(mailSender);
            mailHandler.setSubject("[익명 게시판 이메일 인증입니다.]");
            mailHandler.setText(new StringBuffer()
                    .append("<h1>메일인증</h1>")
                    .append("<a href='http://localhost:8080/email_confirm?userEmail=")
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

    /**
     * URL 파라미터 값으로 접근되기에 이메일 확인 요청에 대한 파라미터의
     * 타당성을 판단하는 예외처리가 필요.
     *
     * @param email
     * @param key
     */
    public void updateAuth(String email, String key) {
        if(StringUtils.isEmpty(email))
            throw new InvalidInputException("잘못된 입력 값입니다.");
        email = SecurityUtil.encryptSHA256(email);

        Member member = memberRepository.findMemberByMemberEmail(email);

        if(member == null) {
            throw new NoAuthException("해당 멤버가 없습니다.");
        }
        if (!member.getEmailKey().equals(key)) {
            throw new NoAuthException("잘못된 키로 접근하였습니다.");
        }

        member.setMailCheck(MailCheck.OK);
        memberRepository.save(member);
    }

    public void changNickName(String nickName,String sessionEmail){
        String memberNickName = memberRepository.findMemberNickByMemberNick(nickName);

        if(!StringUtils.isEmpty(memberNickName)){
            throw new InvalidInputException("이미 존재하는 닉네임 입니다.");
        }

        Member member =  memberRepository.findMemberByMemberEmail(sessionEmail);;
        if(member.getMemberModDate() == null || getDiffDate(member.getMemberModDate()) >= WEEK){
            member.setMemberNick(nickName);
            member.setMemberModDate(new Date());

            memberRepository.save(member);
        } else{
            throw new InvalidInputException("닉네임을 바꾸기에 너무 이릅니다.");
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
        member.setMemberStatus(MemberStatus.DELETED);
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
}
