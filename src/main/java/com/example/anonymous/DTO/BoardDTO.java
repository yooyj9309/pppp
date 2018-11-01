package com.example.anonymous.DTO;


import com.example.anonymous.domain.Board;
import com.example.anonymous.status.BoardStatus;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;


@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "boardId")

public class BoardDTO {
    private long  boardId;

    private String boardSubject;
    private String boardContents;
    private String filePath;

    private String boardDate;

    private int likeCnt;
    private int viewCnt;
    private int commentCnt;

    private BoardStatus boardStatus;

    public BoardDTO(Board board){
        this.boardId = board.getBoardId();
        this.boardSubject = board.getBoardSubject();
        this.filePath = board.getFilePath();
        this.boardDate = formatDate(board.getBoardRegDate());
        this.likeCnt = board.getLikeCnt();
        this.viewCnt = board.getViewCnt();
        this.boardStatus = board.getBoardStatus();
    }

    private String formatDate(Date date){
        String dateResult = "";

        switch (this.boardStatus){
            case CREATED:
                dateResult =  "작성 일 : "+date.toString();
                break;
            case UPDATED:
                dateResult = "수정 일 : "+date.toString();
                break;
            case DELETED:
                dateResult = "삭제 된 게시글 입니다.";
                break;
        }
        return dateResult;
    }
}
