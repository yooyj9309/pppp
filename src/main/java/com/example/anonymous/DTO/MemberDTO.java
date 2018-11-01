package com.example.anonymous.DTO;

import com.example.anonymous.domain.*;
import com.example.anonymous.status.MemberStatus;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

//Validation check,
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "memberId")

public class MemberDTO {
    private long memberId;

    private String memberEmail;
    private String memberPw;
    private String memberNick;

    private MemberStatus memberCheck;

    private Date memberRegDate;
    private Date memberModDate;

    //프론트에서 봐야할 데이터들
    public MemberDTO(Member member){
        this.memberId = member.getMemberId();
        this.memberEmail = member.getMemberEmail();
        this.memberPw = member.getMemberPw();
        this.memberNick = member.getMemberNick();
        this.memberCheck = member.getMemberCheck();
        this.memberRegDate = member.getMemberRegDate();
        this.memberModDate = member.getMemberModDate();
    }

    //디비에 저장할 데이터들
    public Member toEntity(){
        Member member = new Member();
        member.setMemberEmail(this.memberEmail);
        member.setMemberPw(this.memberPw);
        member.setMemberNick(this.memberNick);

        return member;
    }
}
