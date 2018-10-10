package com.example.anonymous.controller;

import com.example.anonymous.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class viewController {
    @Autowired
    private MemberService memberService;

    @GetMapping("/login")
    public String getLoginView(){
        return "login";
    }

    @GetMapping (value = "/emailConfirm")
    public String confirmEmail(String userEmail, Model model) {
        memberService.updateAuth(userEmail);
        return "login";
    }
}
