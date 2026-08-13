package org.example.secu.controller;

import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.example.secu.domain.dto.CustomUserDetails;
import org.example.secu.domain.entity.CommentEntity;
import org.example.secu.service.CommentService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/{id}/delete")
    public String delete(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long id) {
        // 1. 관리자 인가?
        boolean isAdmin = userDetails.isAdmin();
        // 2. 소유권을 갖고 있는가?
        CommentEntity entity = commentService.findById(id);
        boolean isOwner = entity.getUser().getId()
                == userDetails.getId();
        if (!isAdmin && !isOwner) {
            // import org.springframework.security.access.AccessDeniedException;
            throw new AccessDeniedException("접근 권한이 없습니다.");
        }
        commentService.delete(id);
        return "redirect:/comment";
    }
}