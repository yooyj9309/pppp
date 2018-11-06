package com.example.anonymous.restcontroller;

import com.example.anonymous.DTO.BoardDTO;
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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping(value = "/board")
public class BoardController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BoardController.class);

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    BoardService boardService;

    @Autowired
    ReplyService replyService;


    @PostMapping()
    public void insertBoardContent(@Valid BoardDTO boardDTO, MultipartFile postFile, Principal principal) {
       boardService.registerBoardService(boardDTO, postFile, principal);
    }

    @GetMapping(value = "/articles")
    public List<BoardDTO> getBoardList(@RequestParam("boardId") long boardId, @RequestParam("type") String type, Principal principal) {
        List<BoardDTO> articleList = boardService.getBoardList(boardId,type,principal);

        for(BoardDTO boardDTO:articleList){
            LOGGER.info(boardDTO.toString());
        }
        return articleList;
    }

    @PutMapping(value = "/{boardId}")
    public void updateBoard(@PathVariable("boardId") long boardId, @Valid BoardDTO inputBoard, MultipartFile updateFile, Principal principal) {
        LOGGER.info(inputBoard.toString());
        inputBoard.setBoardId(boardId);
        boardService.updateBoardById(inputBoard,updateFile,principal);
    }

    @DeleteMapping(value = "/{boardId}")
    public void deleteBoard(@PathVariable("boardId") long boardId, Principal principal) {
        LOGGER.info(boardId + "번 게시판 삭제하기");
        boardService.deleteBoardById(boardId, principal);

    }

/*
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
*/
}
