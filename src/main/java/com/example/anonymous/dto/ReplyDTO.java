package com.example.anonymous.dto;

import com.example.anonymous.domain.Board;
import com.example.anonymous.domain.Member;
import com.example.anonymous.domain.Reply;
import com.example.anonymous.status.ReplyStatus;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "memberId")
@ToString

public class ReplyDTO {

    @NotBlank(message = "댓글을 작성해주세요.")
    @Size(max = 1000, min = 1, message = "댓글 길이를 확인해주세요.")
    private String replyContents;

    private String replyDate;

    private String memberNick;

    private ReplyStatus replyStatus;

    public ReplyDTO(Reply reply) {
        this.replyContents = reply.getReplyContents();
        this.memberNick = reply.getMember().getMemberNick();
        this.replyStatus = reply.getReplyStatus();
        this.replyDate = formatDate(reply);
    }

    public Reply toEntity(Board board, Member member){
       Reply reply = new Reply();

       reply.setBoard(board);
       reply.setMember(member);
       reply.setReplyContents(replyContents);
       reply.setReplyStatus(ReplyStatus.CREATED);

       return reply;
    }

    private String formatDate(Reply reply) {
        switch (this.replyStatus) {
            case CREATED:
                return reply.getReplyRegDate() + "(작성)";
            case UPDATED:
                return reply.getReplyModDate() + "(수정)";
            default:
                return "삭제된 게시글 입니다.";
        }
    }
}
