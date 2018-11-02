package com.example.anonymous.domain;

import com.example.anonymous.status.BoardStatus;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(of = "boardId")

@Entity
@Table(name = "BOARD")
public class Board {
    // 게시판 ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long  boardId;

    @Column(length = 500, nullable = false)
    private String boardSubject;

    // 게시판 내용
    @Column(columnDefinition = "TEXT", length=2000)
    private String boardContents;

    @Column(length = 100, nullable = false)
    private String writer;

    // 파일 주소
    @Column(nullable = false, unique = true, length=2000)
    private String filePath;

    @Column(name = "boardRegDate", updatable=false)
    @CreationTimestamp
    private Date boardRegDate ;

    @Column(name = "boardModDate")
    private Date boardModDate ;

    @Column(length=20, columnDefinition = "int default 0")
    private int likeCnt;

    @Enumerated(EnumType.STRING)
    private BoardStatus boardStatus;

    @Column(length=20, columnDefinition = "int default 0")
    private int viewCnt;

    @Column(length=20, columnDefinition = "int default 0")
    private int commentCnt;

    @ManyToOne
    @JoinColumn(name = "memberId")
    private Member member;

    @OneToMany(mappedBy ="board")
    private List<Reply> replyList = new ArrayList<>();

    @OneToMany(mappedBy = "board")
    private List<LikeTable> likeTableList = new ArrayList<>();


    public void setMember(Member member){
        if(this.member!=null){
            this.member.getBoardList().remove(this);
        }
        this.member = member;
        member.getBoardList().add(this);
    }
}
