package org.example.defynske.controller;

import lombok.RequiredArgsConstructor;
import org.example.defynske.dto.RatingRequest;
import org.example.defynske.model.Rating;
import org.example.defynske.repo.RatingRepo;
import org.example.defynske.service.RatingService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;


@RestController
@RequestMapping("/ratings")
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;
    private final RatingRepo ratingRepo;

    @PostMapping
    public Rating addRating(@RequestBody RatingRequest req, Authentication auth){
        String username = auth.getName();
        System.out.println("🟣 Logged in as: " + username);
        return ratingService.addRating(
                username,
                req.getWineId(),
                req.getDfvScore(),
                req.getVivinoScore(),
                req.getComment());
    }
}
