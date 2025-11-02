package org.example.defynske.repo;

import org.example.defynske.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepo extends JpaRepository<Member,Long> {
    Optional<Member> findByUsername(String username);
}
