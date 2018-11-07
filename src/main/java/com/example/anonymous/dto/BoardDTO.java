package com.example.anonymous.dto;


import com.example.anonymous.domain.Board;
import com.example.anonymous.domain.Member;
import com.example.anonymous.status.BoardStatus;
import com.example.anonymous.status.LikeStatus;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;


@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "boardId")
@ToString
public class BoardDTO {
    private static final Logger LOGGER = LoggerFactory.getLogger(BoardDTO.class);

    private long boardId;

    @NotBlank(message = "제목을 입력해주세요.")
    @Size(max = 500, min = 1, message = "제목 길이를 확인 해주세요.")
    private String boardSubject;

    @NotBlank(message = "내용을 입력해주세요.")
    @Size(max = 2000, min = 1, message = "내용 길이를 확인 해주세요.")
    private String boardContents;

    private String filePath;
    private String writer;
    private String writerEmail;
    private String boardDate;

    private int likeCnt;
    private int viewCnt;
    private int commentCnt;

    private BoardStatus boardStatus;
    private LikeStatus likeStatus;

    //프론트에 보여야하는 데이터들
    public BoardDTO(Board board) {
        this.boardId = board.getBoardId();
        this.boardSubject = board.getBoardSubject();
        this.boardContents = board.getBoardContents();
        this.filePath = board.getFilePath();
        this.boardStatus = board.getBoardStatus();
        this.likeCnt = board.getLikeCnt();
        this.viewCnt = board.getViewCnt();
        this.writer = board.getWriter();
        this.writerEmail = board.getMember().getMemberEmail();

        this.boardDate = formatDate(board);
    }

    public Board toEntity(String filePath, Member member) {
        Board board = new Board();

        board.setBoardSubject(this.boardSubject);
        board.setBoardContents(this.boardContents);
        board.setMember(member);
        board.setWriter(member.getMemberNick());
        board.setFilePath(filePath);
        board.setBoardStatus(BoardStatus.CREATED);

        return board;
    }

    public Board toUpdate(Board board) {
        board.setBoardStatus(BoardStatus.UPDATED);
        board.setBoardModDate(new Date());
        board.setBoardSubject(this.boardSubject);
        board.setBoardContents(this.boardContents);

        return board;
    }

    private String formatDate(Board board) {
        String dateResult = "";

        switch (this.boardStatus) {
            case CREATED:
                dateResult = board.getBoardRegDate() + "(작성됨)";
                break;
            case UPDATED:
                dateResult = board.getBoardModDate() + "(수정됨)";
                break;
            case DELETED:
                dateResult = "삭제 된 게시글 입니다.";
                break;
            default:
                dateResult =  board.getBoardRegDate()+"";
        }
        return dateResult;
    }
}
