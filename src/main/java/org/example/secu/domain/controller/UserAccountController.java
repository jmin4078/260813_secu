package org.example.secu.domain.controller;


import lombok.RequiredArgsConstructor;
import org.example.secu.domain.service.UserAccountService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class UserAccountController {
    private final UserAccountService userAccountService;
}
