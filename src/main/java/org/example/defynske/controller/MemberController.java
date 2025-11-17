package org.example.defynske.controller;

import lombok.RequiredArgsConstructor;
import org.example.defynske.dto.responds.MemberResponse;
import org.example.defynske.model.Member;
import org.example.defynske.repo.MemberRepo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/internal/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepo memberRepo;

    @GetMapping
    public List<MemberResponse> getAllMembers() {
         return memberRepo.findAll().stream()
                 .map(MemberResponse::fromEntity)
                 .toList();
    }
}
