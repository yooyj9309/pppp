package com.example.anonymous.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@EqualsAndHashCode(of = "boardId")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = "replies")
public class Board {
    // 게시판 ID
    @Id
    @Column(name = "boardId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long  boardId;

    @Column(length = 500, nullable = false)
    private String boardSubject;

    @Column(columnDefinition = "TEXT", length=2000)
    // 게시판 내용
    private String boardContents;

    @Column(nullable = false, unique = true, length=2000)
    // 파일 주소
    private String filePath;

    @Column(name = "boardRegDate", updatable=false)
    @CreationTimestamp
    // 등록 시간
    private Date boardRegDate ;

    @Column(name = "boardModDate")
    // 수정 시간
    private Date boardModDate ;

    @Column( length=20, columnDefinition = "int default 0")
    // 좋아요 수
    private int likeCnt;

    @Column(length=2, columnDefinition = "int default 0")
    // 게시판 상태 코드
    private int boardStatus;

    @Column( length=20, columnDefinition = "int default 0")
    // 조회 수
    private int viewCnt;

    @JsonIgnore
    @ManyToOne
    private Member member;

    @Column(nullable = false, length=100)
    private String memberNick;

    @Column(nullable = false,  length=100)
    private String sessionEmail;

    @OneToMany(mappedBy = "board")
    private List<Reply> replies;

    @Transient
    private MultipartFile imgFile;

}
