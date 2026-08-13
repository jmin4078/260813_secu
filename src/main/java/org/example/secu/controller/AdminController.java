package org.example.secu.controller;

import lombok.RequiredArgsConstructor;
import org.example.secu.service.CommentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final CommentService commentService;

    @GetMapping
    public String page(Model model) {
        model.addAttribute("data", commentService.findAll());
        return "comment/page";
    }
}
