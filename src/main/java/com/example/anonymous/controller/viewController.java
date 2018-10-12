package com.example.anonymous.controller;

import com.example.anonymous.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class viewController {
    @Autowired
    private MemberService memberService;

    @GetMapping("/")
    public String getLogin() {
        return "login";
    }

    @GetMapping("/login")
    public String getLoginView() {
        return "login";
    }

    @GetMapping(value = "/emailConfirm")
    public String confirmEmail(String userEmail, String key, Model model) {
        memberService.updateAuth(userEmail, key);
        model.addAttribute("msg","이메일 인증에 성공 하셨습니다.");
        return "login";
    }

}



