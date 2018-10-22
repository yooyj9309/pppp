package com.example.anonymous.domain;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;
@Getter
@Setter
@Entity
@EqualsAndHashCode(of = "replyId")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = "board")
public class Reply {
    @Id
    @Column(nullable = false, insertable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // 댓글 ID
    private long replyId;

    @Column(nullable = false, length=100)
    // 댓글 작성자 닉네임
    private String memberNick;

    @Column(columnDefinition = "TEXT", length=1000)
    // 댓글 내용
    private String replyContents;

    @Column(name = "replyRegDate", updatable=false)
    @CreationTimestamp
    // 댓글 등록일
    private Date replyRegDate;

    @Column(name = "replyModDate")
    // 댓글 수정일
    private Date replyModDate;

    @ManyToOne
    private Board board;

    @Column
    // 댓글 부모 ID
    private long replyParentId;

    @Column
    @JoinColumn(name="boardId")
    // 게시판 ID
    private long boardId;

    @Column(length=2)
    // 댓글 상태
    private int replyStatus;

    @Column(length=20)
    // 댓글 순서
    private long seq;

    @Transient
    private String memberEmail;

}
