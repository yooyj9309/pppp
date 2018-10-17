package com.example.anonymous.controller;

import com.example.anonymous.service.MemberService;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class viewController {
    @Autowired
    private MemberService memberService;


    @GetMapping("/login")
    public String getLoginView() {
        return "login";
    }

    @GetMapping("/main")
    public ModelAndView getMainView() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("main");
        return mav;
    }
    @GetMapping(value = "/emailConfirm")
    public String confirmEmail(String userEmail, String key, Model model) {
        memberService.updateAuth(userEmail, key);
        model.addAttribute("msg","이메일 인증에 성공 하셨습니다.");
        return "login";
    }

}

