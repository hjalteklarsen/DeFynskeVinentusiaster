package org.example.defynske.service;


import lombok.RequiredArgsConstructor;
import org.example.defynske.model.Member;
import org.example.defynske.repo.MemberRepo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepo memberRepo;
    private final PasswordEncoder passwordEncoder;

    public Member register(String username, String displayName, String rawPassword) {
        if (memberRepo.findByUsername(username).isPresent()) {
            throw new RuntimeException("Username already exists: " + username);
        }


        Member member = Member.builder()
                .username(username)
                .passwordHash(passwordEncoder.encode(rawPassword))
                .displayName(displayName)
                .role("USER")
                .build();

        return memberRepo.save(member);
    }
}
