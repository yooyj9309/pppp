package com.example.anonymous.controller;

import com.example.anonymous.dto.BoardDTO;
import com.example.anonymous.domain.Board;
import com.example.anonymous.repository.BoardRepository;
import com.example.anonymous.repository.MemberRepository;
import com.example.anonymous.service.BoardService;
import com.example.anonymous.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;


@Controller
public class viewController {
    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private BoardService boardService;

    @GetMapping(value = "login")
    public String getLoginView(Model model) {
        String randomNick = memberService.getRandomName();
        model.addAttribute("nick",randomNick);
        return "login";
    }

    @GetMapping("main")
    public ModelAndView getMainView(Principal principal) {
        ModelAndView mav = new ModelAndView();
        String nick = memberRepository.findMemberByMemberEmail(principal.getName()).getMemberNick();

        mav.addObject("nick",nick);
        mav.setViewName("main");
        return mav;
    }

    @GetMapping(value = "email_confirm")
    public String confirmEmail(String userEmail, String key, Model model) {
        memberService.updateAuth(userEmail, key);
        model.addAttribute("msg","이메일 인증에 성공 하셨습니다.");
        return "login";
    }

    @GetMapping(value = "board/{boardId}")
    public String getDetailView(@PathVariable("boardId") long boardId, Model model, Principal principal) {
        Board board = boardRepository.findByBoardId(boardId);
        boardService.processViewCnt(board,principal);
        BoardDTO boardDTO = new BoardDTO(board);


        model.addAttribute("board",boardDTO);

        return "view";
    }
}

