package com.example.anonymous.domain;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@EqualsAndHashCode(of = "memberEmail")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Member {

    // 이메일
    @Id
    @Column(nullable = false, unique = true, length=100)
    private String memberEmail;

    // 비밀 번호
    @Column(nullable = false, length=100)
    private String memberPw;

    // 비밀번호 확인
    @Column(nullable = false, length=100)
    private String memberPwCheck;
    // 닉네임
    @Column(nullable = false, unique = true, length=100)
    private String memberNick;

    // 이메일 인증
    @Column(length=2)
    private Integer memberCheck = 0;

    // 회원 등록일
    // 날짜 수정시 등록 시간 null로 수정되는 버그 fix
    @Column(name = "memberRegDate", updatable=false)
    @CreationTimestamp
    private Date memberRegDate;

    // 정보 수정일
    @Column(name = "memberModDate")
    @UpdateTimestamp
    private Date memberModDate;

    //이메일 인증 키
    @Column(nullable = false, unique = true, length=100)
    private String emailKey;

    // 회원 상태 코드
    @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
    @JoinColumn(name="memberEmail")
    private List<MemberRole> memberStatus;

    @Builder
    public Member(String memberEmail, String memberPw, String memberPwCheck, String memberNick, Integer memberCheck, Date memberRegDate, Date memberModDate, String emailKey, List<MemberRole> memberStatus) {
        this.memberEmail = memberEmail;
        this.memberPw = memberPw;
        this.memberPwCheck = memberPwCheck;
        this.memberNick = memberNick;
        this.memberCheck = memberCheck;
        this.memberRegDate = memberRegDate;
        this.memberModDate = memberModDate;
        this.emailKey = emailKey;
        this.memberStatus = memberStatus;
    }

}
