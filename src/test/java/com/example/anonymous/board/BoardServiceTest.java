package com.example.anonymous.board;

import com.example.anonymous.domain.Board;
import com.example.anonymous.member.MemberServiceTest;
import com.example.anonymous.repository.BoardRepository;
import com.example.anonymous.service.BoardService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BoardServiceTest {
    @Autowired
    BoardService boardService;

    @Autowired
    BoardRepository boardRepository;

    private static final Logger log = LoggerFactory.getLogger(BoardServiceTest.class);

    /**
     * 스크롤 다운할 경우,
     * BoardId 18번,23보다 먼저 생긴 게시글 5개를 가져오는 테스트 함수
     * 18번의 경우, 번호 이하의 게시글이 총 3개, 하지만 삭제된 상태의 게시글이 있어 총 2개
     */
    @Test
    public void scrollDownTest(){
        List<Board> boardList = boardService.getBoardList(18,"down","da9793cb05a15ffcea4f8262e99844180384faebcb76907a9a018ef0922e988f");
        assertEquals(boardList.size(),2);

        for(Board board:boardList){
            log.info(board.getBoardId() + " 번 게시물");
        }

        boardList = boardService.getBoardList(23,"down","da9793cb05a15ffcea4f8262e99844180384faebcb76907a9a018ef0922e988f");
        assertEquals(boardList.size(),5);

        for(Board board:boardList){
            log.info(board.getBoardId() + " 번 게시물");
        }
    }

    /**
     * 스크롤 업 할 경우,
     * BoardId 16번보다 나중에 생긴 5개의 게시글 가져오기 테스트 함수
     */
    @Test
    public void scrollUpTest(){
        List<Board> boardList = boardService.getBoardList(20,"up","da9793cb05a15ffcea4f8262e99844180384faebcb76907a9a018ef0922e988f");
        assertEquals(boardList.size(),5);
        for(Board board:boardList){
            log.info(board.getBoardId() + " 번 게시물");
        }

    }
}
