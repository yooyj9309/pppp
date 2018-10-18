package com.example.anonymous.restcontroller;

import com.example.anonymous.domain.Board;
import com.example.anonymous.domain.Member;
import com.example.anonymous.repository.BoardRepository;
import com.example.anonymous.security.SecurityMember;
import com.example.anonymous.service.BoardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
public class BoardController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BoardController.class);

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    BoardService boardService;

    @GetMapping(value = "/boardList")
    public List<Board> getBoardList() {
        List<Board> boardList = boardService.getBoardList();
        return boardList;
    }

    @PostMapping(value = "/main")
    public ResponseEntity<String> postContent(Board board, Principal principal, HttpSession session) {
        LOGGER.info("Session 경로 : " + session.getServletContext().getRealPath(""));

        board.setMemberEmail(principal.getName());
        boardService.registerBoardService(board,session);

        return new ResponseEntity<String>("게시글을 등록하였습니다.",HttpStatus.OK);
    }

    @GetMapping(value = "main/{boardId}")
    public ModelAndView getDetailView(@PathVariable("boardId") Long boardId) {
        LOGGER.info(boardId+"번 게시판 상세보기");

        ModelAndView mav = new ModelAndView();
        Board board = boardService.getBoardById(boardId);
        LOGGER.info(board.toString());
        mav.setViewName("view");
        mav.addObject("board",board);

        return mav;
    }

    @PostMapping(value = "main/{boardId}")
    public ResponseEntity<String> updateBoard(@PathVariable("boardId") Long boardId, Board updatedBoard, Principal principal, HttpSession session) {
        LOGGER.info(boardId+"번 게시판 수정하기");
        LOGGER.info(updatedBoard.toString());

        updatedBoard.setMemberEmail(principal.getName());
        boardService.updateBoardById(boardId,updatedBoard,session);
        return new ResponseEntity<String>("게시글을 수정하였습니다.",HttpStatus.OK);
    }
}
