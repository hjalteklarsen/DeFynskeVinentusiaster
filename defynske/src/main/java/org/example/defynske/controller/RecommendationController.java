package org.example.defynske.controller;

import lombok.RequiredArgsConstructor;
import org.example.defynske.service.WineRecommenderService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/internal/recommendations")
@RequiredArgsConstructor
public class RecommendationController {

    private final WineRecommenderService recommenderService;

    @PostMapping
    public Map<String, Object> recommend(@RequestBody RecommendationRequest req) {
        if ("INTERNAL".equalsIgnoreCase(req.mode())) {
            return recommenderService.recommendInternal(req.memberId(), req.prompt());
        } else {
            return recommenderService.recommendExternal(req.memberId(), req.prompt());
        }
    }

    public record RecommendationRequest(Long memberId, String mode, String prompt) {}
}
