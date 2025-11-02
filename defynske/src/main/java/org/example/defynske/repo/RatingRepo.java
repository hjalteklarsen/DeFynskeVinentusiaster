package org.example.defynske.repo;

import org.example.defynske.model.Member;
import org.example.defynske.model.Rating;
import org.example.defynske.model.Wine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RatingRepo extends JpaRepository<Rating,Long> {
    List<Rating> findByMember(Member member);
    List<Rating> findByWine(Wine wine);
}
