package com.example.anonymous.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@EqualsAndHashCode(of = "memberEmail")
@ToString

public class Member {

    // 이메일
    @Id
    @Column(nullable = false, unique = true, length=50)
    private String memberEmail;

    // 비밀 번호
    @Column(nullable = false, length=50)
    private String memberPw;

    // 닉네임
    @Column(nullable = false, unique = true, length=50)
    private String memberNick;

    // 이메일 인증
    @Column(nullable = false, unique = true, length=2)
    private Integer memberCheck;

    // 회원 등록일
    @CreationTimestamp
    private Date memberRegDate;

    // 정보 수정일
    @UpdateTimestamp
    private Date memberModDate;

    // 회원 상태 코드
    @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
    @JoinColumn(name="uid")
    private List<MemberRole> memberStatus;


}
