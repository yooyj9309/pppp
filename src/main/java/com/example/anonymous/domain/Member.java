package com.example.anonymous.domain;

import com.example.anonymous.status.MailCheck;
import com.example.anonymous.status.MemberStatus;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(of = "memberId")

@Entity
@Table(name = "MEMBER")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long memberId;

    @Column(nullable = false, unique = true, length=100)
    private String memberEmail;

    @Column(nullable = false, length=100)
    private String memberPw;

    @Column(nullable = false, unique = true, length=100)
    private String memberNick;

    @Enumerated(EnumType.STRING)
    private MemberStatus memberStatus;

    @Enumerated(EnumType.STRING)
    private MailCheck mailCheck;

    @Column(name = "memberRegDate", updatable=false)
    @CreationTimestamp
    private Date memberRegDate;

    @Column(name = "memberModDate")
    private Date memberModDate;

    @OneToMany(mappedBy = "member")
    private List<Board> boardList = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Reply> replyList = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<LikeTable> likeTableList = new ArrayList<>();

    @OneToMany
    @JoinColumn(name="memberId")
    private List<MemberRole> memberRoles;

    @Column(nullable = false, unique = true, length=100)
    private String emailKey;

    public void recoverMember(String nickName, String key){
        this.setMemberNick(nickName);
        this.setMemberModDate(new Date());
        this.setMemberStatus(MemberStatus.CREATED);
        this.setEmailKey(key);
        this.setMailCheck(MailCheck.NO);
    }
}
