package com.example.anonymous.restcontroller;

import com.example.anonymous.domain.Board;
import com.example.anonymous.domain.Reply;
import com.example.anonymous.repository.BoardRepository;
import com.example.anonymous.service.BoardService;
import com.example.anonymous.service.ReplyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.List;

@RestController
public class BoardController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BoardController.class);

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    BoardService boardService;

    @Autowired
    ReplyService replyService;

    @GetMapping(value = "/boardList")
    public List<Board> getBoardList(@RequestParam("boardId") long boardId, @RequestParam("type") String type, Principal principal) {
        LOGGER.info(boardId + " 요청");

        List<Board> boardList = boardService.getBoardList(boardId, type, principal.getName());
        return boardList;
    }

    @PostMapping(value = "/main")
    public ResponseEntity<String> insertBoardContent(Board board, Principal principal) {

        boardService.registerBoardService(board, principal);

        return new ResponseEntity<String>("게시글을 등록하였습니다.", HttpStatus.OK);
    }

    @GetMapping(value = "main/{boardId}")
    public ModelAndView getDetailView(@PathVariable("boardId") long boardId, HttpSession session) {
        LOGGER.info(boardId + "번 게시판 상세보기");

        ModelAndView mav = new ModelAndView();
        Board board = boardService.getBoardById(boardId);

        board.setViewCnt(board.getViewCnt() + 1);
        boardRepository.save(board);
        LOGGER.info(board.toString());
        mav.setViewName("view");
        mav.addObject("board", board);
        return mav;
    }

    @PostMapping(value = "main/{boardId}")
    public ResponseEntity<String> updateBoard(@PathVariable("boardId") long boardId, Board inputBoard, Principal principal) {
        LOGGER.info(boardId + "번 게시판 수정하기");
        LOGGER.info(inputBoard.toString());

        boardService.updateBoardById(boardId, inputBoard, principal);
        return new ResponseEntity<String>("게시글을 수정하였습니다.", HttpStatus.OK);
    }

    @DeleteMapping(value = "main/{boardId}")
    public ResponseEntity<String> deleteBoard(@PathVariable("boardId") long boardId, Principal principal) {
        LOGGER.info(boardId + "번 게시판 삭제하기");
        boardService.deleteBoardById(boardId, principal);

        return new ResponseEntity<String>("게시글을 삭제하였습니다.", HttpStatus.OK);
    }


    @GetMapping(value = "dashboard")
    public ModelAndView getDashBoardView(ModelAndView mav, Principal principal) {
        List<Board> boardListByMemberEmail = boardService.getBoardListByMemberEmail(principal.getName());
        List<Reply> replyListByMemberEmail = replyService.getReplyListByMemberEmail(principal.getName());

        mav.addObject("boardList", boardListByMemberEmail);
        mav.addObject("replyList", replyListByMemberEmail);

        mav.setViewName("dashboard");

        return mav;
    }

    @PostMapping(value = "main/like")
    public int processLike(@RequestParam long boardId,Principal principal) {

        return boardService.processLikeByBoardIdAndMemberEmail(boardId,principal.getName());
    }

}
