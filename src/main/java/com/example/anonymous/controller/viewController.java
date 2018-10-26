package com.example.anonymous.controller;

import com.example.anonymous.repository.MemberRepository;
import com.example.anonymous.service.MemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpSession;
import java.security.Principal;


@Controller
public class viewController {
    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(viewController.class);

    @GetMapping(value = "login")
    public String getLoginView(Model model) {
        String randomNick = memberService.getRandomName();
        model.addAttribute("nick",randomNick);
        return "login";
    }

    @GetMapping("main")
    public ModelAndView getMainView(Principal principal, HttpSession session) {
        ModelAndView mav = new ModelAndView();
        String nick = memberRepository.findMemberByMemberEmail(principal.getName()).getMemberNick();

        mav.addObject("nick",nick);
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

