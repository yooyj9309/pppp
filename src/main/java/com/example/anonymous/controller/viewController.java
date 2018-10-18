package com.example.anonymous.controller;

import com.example.anonymous.restcontroller.BoardController;
import com.example.anonymous.service.MemberService;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@Controller
public class viewController {
    @Autowired
    private MemberService memberService;
    private static final Logger LOGGER = LoggerFactory.getLogger(viewController.class);
    @GetMapping(value = "login")
    public String getLoginView(HttpSession session) {

        LOGGER.info("Session 경로 : " + session.getServletContext().getRealPath(""));
        return "login";
    }

    @GetMapping("main")
    public ModelAndView getMainView() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("main");
        return mav;
    }
    @GetMapping(value = "emailConfirm")
    public String confirmEmail(String userEmail, String key, Model model) {
        memberService.updateAuth(userEmail, key);
        model.addAttribute("msg","이메일 인증에 성공 하셨습니다.");
        return "login";
    }
}

