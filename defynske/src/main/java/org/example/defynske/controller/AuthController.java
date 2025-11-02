package org.example.defynske.controller;


import lombok.RequiredArgsConstructor;
import org.example.defynske.dto.MemberResponse;
import org.example.defynske.dto.RegisterRequest;
import org.example.defynske.model.Member;
import org.example.defynske.service.MemberService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final MemberService memberService;

    @PostMapping("/register")
    public MemberResponse register(@RequestBody RegisterRequest request) {
        var saved = memberService.register(
                request.getUsername(),
                request.getDisplayName(),
                request.getPassword()
        );
        return MemberResponse.fromEntity(saved);
    }

    @GetMapping("/login-success")
    public String loginSuccess() {
        return "Login successful!";
    }
}
