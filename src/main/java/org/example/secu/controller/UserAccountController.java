package org.example.secu.controller;


import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.example.secu.service.UserAccountService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class UserAccountController {
    private final UserAccountService userAccountService;

    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("form", new UserForm("", ""));
        return "user/signup";
    }

    @PostMapping("/signup")
    public String signup(@Validated @ModelAttribute("form") UserForm form) {
        System.out.println("form = " + form); // 실무에서는 패스워드 포함된 폼 값은 마스킹 등 처리 없이 로깅 X
        userAccountService.create(form);
        return "redirect:/";
    }

    public record UserForm(
            @NotBlank String username, @NotBlank String password) {
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("form", new UserForm("", ""));
        return "user/login";
    }
}