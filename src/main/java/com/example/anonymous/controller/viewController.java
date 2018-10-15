package com.example.anonymous.controller;

import com.example.anonymous.service.MemberService;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;



@Controller
public class viewController {
    @Autowired
    private MemberService memberService;


    @GetMapping("/login")
    public String getLoginView(HttpServletRequest req) {
        //String referer = req.getHeader("Referer");
        // req.getSession().setAttribute("prevPage", referer);

        return "login";
    }

    @GetMapping("/board")
    public String getMainView() {

        return "main";
    }
    @GetMapping(value = "/emailConfirm")
    public String confirmEmail(String userEmail, String key, Model model) {
        memberService.updateAuth(userEmail, key);
        model.addAttribute("msg","이메일 인증에 성공 하셨습니다.");
        return "login";
    }

}

