package com.example.anonymous.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@EqualsAndHashCode(of = "likeId")
@NoArgsConstructor()
@Entity
public class LikeTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long likeId;

    @ManyToOne
    @JoinColumn(name = "memberId")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "boardId")
    private Board board;

    //양방향 연관 관계를 위한 편의 메소드
    public void setMember(Member member){
        if(this.member!=null){
            this.member.getLikeTableList().remove(this);
        }
        this.member = member;
        member.getLikeTableList().add(this);
    }

    public void setBoard(Board board){
        if(this.board!=null){
            this.board.getLikeTableList().remove(this);
        }
        this.board = board;
        board.getLikeTableList().add(this);
    }

    public LikeTable(Board board,Member member){
        this.board = board;
        this.member = member;
    }
}
