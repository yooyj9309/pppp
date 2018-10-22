package com.example.anonymous.domain;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@EqualsAndHashCode(of = "boardId")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Board {
    // 게시판 ID
    @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long  boardId;

    @Column(length = 500, nullable = false)
    private String boardSubject;

    @Column(columnDefinition = "TEXT", length=2000)
    // 게시판 내용
    private String boardContents;

    @Column(nullable = false, unique = true, length=2000)
    // 파일 주소
    private String filePath;

    @Column(nullable = false, length=100)
    // 게시자(닉네임)
    private String memberNick;

    @Column(name = "boardRegDate", updatable=false)
    @CreationTimestamp
    // 등록 시간
    private Date boardRegDate ;

    @Column(name = "boardModDate")
    // 수정 시간
    private Date boardModDate ;

    @Column( length=20, columnDefinition = "int default 0")
    // 좋아요 수
    private Integer likeCnt;

    @Column(length=2, columnDefinition = "int default 0")
    // 게시판 상태 코드
    private Integer boardStatus;

    @Column( length=20, columnDefinition = "int default 0")
    // 조회 수
    private Integer viewCnt;

    @Transient
    private MultipartFile imgFile;

    @Transient
    private String memberEmail;

    @Builder
    public Board(Long boardId, String boardSubject, String boardContents, String filePath, String memberNick, Date boardRegDate, Date boardModDate, Integer likeCnt, Integer boardStatus, Integer viewCnt) {
        this.boardId = boardId;
        this.boardSubject = boardSubject;
        this.boardContents = boardContents;
        this.filePath = filePath;
        this.memberNick = memberNick;
        this.boardRegDate = boardRegDate;
        this.boardModDate = boardModDate;
        this.likeCnt = likeCnt;
        this.boardStatus = boardStatus;
        this.viewCnt = viewCnt;
    }
}
