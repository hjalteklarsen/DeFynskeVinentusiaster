package org.example.defynske.controller;


import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.defynske.dto.requests.LoginRequest;
import org.example.defynske.dto.responds.MemberResponse;
import org.example.defynske.dto.requests.RegisterRequest;
import org.example.defynske.model.Member;
import org.example.defynske.repo.MemberRepo;
import org.example.defynske.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final MemberService memberService;
    private final AuthenticationManager authenticationManager;
    private final MemberRepo memberRepo;

    @PostMapping("/register")
    public MemberResponse register(@RequestBody RegisterRequest request) {
        var saved = memberService.register(
                request.getUsername(),
                request.getDisplayName(),
                request.getPassword()
        );
        return MemberResponse.fromEntity(saved);
    }

    @PostMapping("/login")
    @CrossOrigin(origins = "http://localhost:63342", allowCredentials = "true")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpServletRequest httpRequest) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            // ✅ Store authentication in session
            SecurityContextHolder.getContext().setAuthentication(auth);
            httpRequest.getSession(true).setAttribute(
                    HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                    SecurityContextHolder.getContext()
            );

            Member member = memberRepo.findByUsername(request.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            return ResponseEntity.ok(Map.of(
                    "username", member.getUsername(),
                    "role", member.getRole()
            ));
        } catch (AuthenticationException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }


    @GetMapping("/login-success")
    public String loginSuccess() {
        return "Login successful!";
    }
}
