package org.example.defynske.service;

import lombok.RequiredArgsConstructor;
import org.example.defynske.model.Member;
import org.example.defynske.model.Rating;
import org.example.defynske.model.Wine;
import org.example.defynske.repo.MemberRepo;
import org.example.defynske.repo.RatingRepo;
import org.example.defynske.repo.WineRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RatingService {
    private final RatingRepo ratingRepo;
    private final MemberRepo memberRepo;
    private final WineRepo wineRepo;

    public Rating addRating(String username, Long wineId, double dfvScore, double vivinoScore, String comment){
        Member member = memberRepo.findByUsername(username)
                .orElseThrow(()-> new IllegalArgumentException("Member not found: " + username));
        Wine wine = wineRepo.findById(wineId)
                .orElseThrow(()-> new IllegalArgumentException("Wine not found with id: " + wineId));

        Rating rating = Rating.builder()
                .member(member)
                .wine(wine)
                .dfvScore(dfvScore)
                .vivinoScore(vivinoScore)
                .comment(comment)
                .build();

        return ratingRepo.save(rating);
    }
}
