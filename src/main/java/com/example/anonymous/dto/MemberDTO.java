package com.example.anonymous.dto;

import com.example.anonymous.domain.*;
import com.example.anonymous.status.MailCheck;
import com.example.anonymous.status.MemberStatus;
import com.example.anonymous.utils.SecurityUtil;
import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

//Validation check,
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "memberId")
@ToString
public class MemberDTO {

    private long memberId;
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @NotBlank(message = "이메일을 작성해주세요.")
    @Pattern(regexp = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$", message = "이메일 형식이 아닙니다.")
    @Size(max = 100, min = 3, message = "이메일 길이를 확인해주세요.")
    private String memberEmail;

    @NotBlank(message = "비밀 번호를 작성해주세요.")
    @Size(max = 100, min = 1, message = "비밀번호 길이를 확인해주세요.")
    private String memberPw;

    @NotBlank(message = "비밀 번호 확인을 해주세요.")
    private String memberPwCheck;

    @NotBlank(message = "닉네임을 작성해주세요.")
    @Size(max = 100, min = 1, message = "닉네임 길이를 확인해주세요.")
    private String memberNick;

    private String emailKey;

    private MemberStatus memberStatus;

    private Date memberRegDate;
    private Date memberModDate;

    //프론트에서 봐야할 데이터들
    public MemberDTO(Member member){
        this.memberId = member.getMemberId();
        this.memberEmail = member.getMemberEmail();
        this.memberPw = member.getMemberPw();
        this.memberNick = member.getMemberNick();
        this.memberStatus = member.getMemberStatus();
        this.memberRegDate = member.getMemberRegDate();
        this.memberModDate = member.getMemberModDate();
    }

    //디비에 저장할 데이터들
    public Member toEntity(String emailKey){
        Member member = new Member();
        member.setMemberEmail(SecurityUtil.encryptSHA256(this.memberEmail));
        member.setMemberPw(encoder.encode(this.memberPw));
        member.setMemberNick(this.memberNick);
        member.setEmailKey(emailKey);
        member.setMemberStatus(MemberStatus.CREATED);
        member.setMailCheck(MailCheck.NO);

        return member;
    }

    public boolean isCheckPassword(){
        if(this.memberPw !=null && this.memberPwCheck !=null){
            if(memberPw.equals(memberPwCheck)){
                return true;
            }
        }
        return false;
    }

}
