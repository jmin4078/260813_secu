package org.example.secu.controller;

import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.example.secu.service.CommentService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {
    private final CommentService commentService;

    @GetMapping
    public String page(Model model) {
        model.addAttribute("data", commentService.findAll());
        return "comment/page";
    }

    @PostMapping
    public String create(
            @RequestParam @Validated @NotBlank String content,
            @AuthenticationPrincipal(expression = "id") Long userId) {
        commentService.create(userId, content);
        return "redirect:/comment";
    }
}