package org.example.defynske.controller;

import lombok.RequiredArgsConstructor;
import org.example.defynske.dto.requests.RatingRequest;
import org.example.defynske.dto.responds.RatingResponse;
import org.example.defynske.model.Rating;
import org.example.defynske.repo.RatingRepo;
import org.example.defynske.service.RatingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.List;


@RestController
@RequestMapping("/api/ratings")
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;
    private final RatingRepo ratingRepo;

    @PostMapping
    public ResponseEntity<Void> saveRating(@RequestBody RatingRequest req) {
        ratingService.saveRating(req);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public List<RatingResponse> getRatingsOverview() {
        return ratingService.getRatingsOverview();
    }


    @GetMapping("/{id}/ratings")
    public List<RatingResponse> ratingsForMeeting(@PathVariable Long id) {
        return ratingService.getRatingsForMeeting(id);
    }
}
