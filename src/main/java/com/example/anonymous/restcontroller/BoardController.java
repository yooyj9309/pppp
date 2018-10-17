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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
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

    @GetMapping("/main")
    public ModelAndView getMainView() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("main");
        return mav;
    }
    @GetMapping(value = "/boardList")
    public List<Board> getBoardList() {
        List<Board> boardList = boardService.getBoardList();
        return boardList;
    }

    @PostMapping(value = "/main")
    public ResponseEntity<String> postContent(Board board, Principal principal, HttpSession session) {
        board.setMemberEmail(principal.getName());
        boardService.registerBoardService(board);

        return new ResponseEntity<String>("게시글을 등록하였습니다.",HttpStatus.OK);
    }
}
