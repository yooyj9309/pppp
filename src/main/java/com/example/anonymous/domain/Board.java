package com.example.anonymous.domain;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
    @Column(nullable = false, unique = true, length=100)
    private Integer boardId;

    @Column(nullable = false, length=2000)
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
    private Date boardRegDate;

    @Column(name = "boardModDate")
    @UpdateTimestamp
    // 수정 시간
    private Date boardModDate;

    @Column(nullable = false, length=20)
    // 좋아요 수
    private Integer likeCnt;

    @Column(nullable = false, length=2)
    // 게시판 상태 코드
    private Integer boardStatus;

    @Column(nullable = false, length=20)
    // 조회 수
    private Integer viewCnt;


    private MultipartFile imgFile;

    @Builder
    public Board(Integer boardId, String boardContents, String filePath, String memberNick, Date boardRegDate, Date boardModDate, Integer likeCnt, Integer boardStatus, Integer viewCnt, MultipartFile imgFile) {
        this.boardId = boardId;
        this.boardContents = boardContents;
        this.filePath = filePath;
        this.memberNick = memberNick;
        this.boardRegDate = boardRegDate;
        this.boardModDate = boardModDate;
        this.likeCnt = likeCnt;
        this.boardStatus = boardStatus;
        this.viewCnt = viewCnt;
        this.imgFile = imgFile;
    }
}
