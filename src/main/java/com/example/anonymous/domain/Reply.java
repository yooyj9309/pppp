package com.example.anonymous.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @JsonIgnore
    @ManyToOne
    private Board board;

    @Column(nullable = false,length=100)
    private String memberNick;

    @Column
    // 댓글 부모 ID
    private long replyParentId;

    @Column(length=2)
    // 댓글 상태
    private int replyStatus;

    @Column(length=20)
    // 댓글 순서
    private long seq;

    @Column(length=100)
    private String sessionEmail;
}
