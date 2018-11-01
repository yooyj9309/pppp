package com.example.anonymous.domain;

import com.example.anonymous.status.ReplyStatus;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;


@Getter
@Setter
@EqualsAndHashCode(of = "replyId")

@Entity
public class Reply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long replyId;

    @Column(columnDefinition = "TEXT", length=1000)
    private String replyContents;

    @Column(name = "replyRegDate", updatable=false)
    @CreationTimestamp
    private Date replyRegDate;

    @Column(name = "replyModDate")
    private Date replyModDate;

    @ManyToOne
    @JoinColumn(name = "boardId")
    private Board board;

    @ManyToOne
    @JoinColumn(name = "memberId")
    private Member member;

    @Column(nullable = false,length=100)
    private String memberNick;

    @Column
    private long replyParentId;

    @Enumerated(EnumType.STRING)
    private ReplyStatus replyStatus;

    public void setBoard(Board board){
        if(this.board!=null){
            this.board.getReplyList().remove(this);
        }
        this.board = board;
        board.getReplyList().add(this);
    }

    public void setMember(Member member){
        if(this.member!=null){
            this.member.getReplyList().remove(this);
        }
        this.member = member;
        member.getReplyList().add(this);
    }
}
