package org.example.defynske.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.defynske.model.Rating;
import org.example.defynske.model.Wine;
import org.example.defynske.repo.RatingRepo;
import org.example.defynske.repo.WineRepo;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WineRecommenderService {
    private final RatingRepo ratingRepo;
    private final WineRepo wineRepo;
    private final OpenAiService openAiService;
    private static final ObjectMapper mapper = new ObjectMapper();

    public Map<String, Object> recommendInternal(Long memberId, String userPrompt) {
        List<Rating> allRatings = ratingRepo.findAll();

        Map<Long, Map<Long, Double>> memberRatings = allRatings.stream()
                .collect(Collectors.groupingBy(r -> r.getMember().getId(),
                        Collectors.toMap(r -> r.getWine().getId(), Rating::getDfvScore)));

        Map<Long, Double> targetRatings = memberRatings.getOrDefault(memberId, new HashMap<>());
        if (targetRatings.isEmpty()) {
            return Map.of("message", "Ingen ratings fundet. Prøv at bedømme et par vine først.");
        }

        Map<Long, Double> recs = new HashMap<>();
        for (var entry : memberRatings.entrySet()) {
            if (entry.getKey().equals(memberId)) continue;
            double sim = SimilarityUtils.cosineSimilarity(targetRatings, entry.getValue());
            if (sim <= 0) continue;
            for (var wineEntry : entry.getValue().entrySet()) {
                if (targetRatings.containsKey(wineEntry.getKey())) continue;
                recs.merge(wineEntry.getKey(), sim * wineEntry.getValue(), Double::sum);
            }
        }

        List<Long> topWineIds = recs.entrySet().stream()
                .sorted(Map.Entry.<Long, Double>comparingByValue().reversed())
                .limit(10)
                .map(Map.Entry::getKey)
                .toList();

        List<Wine> candidates = wineRepo.findAllById(topWineIds);

        String system = """
        You are a poetic sommelier for a Danish wine club 'De Fynske Vinentusiaster'.
        Every answer must begin with: "I druens rige er vi alle lige."
        Your tone is warm, elegant, and Danish in spirit.

        You must recommend 1–3 wines from the provided list of candidate wines,
        based on the member's past preferences and any additional mood or prompt given.
        For each, include:
        - A short poetic reason why it fits the member
        - A few words about taste, notes, grape, or vineyard if possible
        Return strict JSON:
        { "recommendations": [ { "wineId": 123, "name": "...", "reason": "..." } ] }
        """;

        String user = """
        The member has rated these wines highly: """ + targetRatings.keySet() + """
        
        Candidate wines (from club collection):
        """ + candidates.stream()
                .map(w -> w.getId() + ": " + w.getName() + " (" + w.getCountry() + ")")
                .collect(Collectors.joining("\n")) + """

        Member's current request or mood: """ + userPrompt;

        String json = openAiService.getRecommendationJson(system, user);
        try {
            return mapper.readValue(json, Map.class);
        } catch (Exception e) {
            return Map.of("error", "Failed to parse AI response", "raw", json);
        }
    }

    public Map<String, Object> recommendExternal(Long memberId, String userPrompt) {
        List<Rating> ratings = ratingRepo.findByMemberId(memberId);
        List<String> likedWineNames = ratings.stream()
                .filter(r -> r.getDfvScore() >= 7)
                .map(r -> r.getWine().getName())
                .distinct()
                .toList();

        String system = """
        You are a global sommelier with deep knowledge of wines worldwide.
        Every message must begin with: "I druens rige er vi alle lige."
        Your tone is warm, poetic, and passionate about wine.
        It has to be in Danish.
        Announce which wines (from the users liked wines) you base the recommendations on.
        Suggest 3 wines (not from the provided list) that would delight the member based on their preferences and prompt.
        For each, include:
        - Name
        - Region or country
        - Grape variety (if known)
        - Short poetic reasoning connecting to the member’s taste or prompt.
        Return strict JSON:
        {
          "recommendations": [
            {"name":"...", "region":"...", "grape":"...", "reason":"..."}
          ]
        }
        """;

        String user = """
        The member has enjoyed wines like:
        """ + String.join(", ", likedWineNames) + """

        The member describes their mood or wish as:
        """ + userPrompt;

        String json = openAiService.getRecommendationJson(system, user);
        try {
            return mapper.readValue(json, Map.class);
        } catch (Exception e) {
            return Map.of("error", "Failed to parse AI response", "raw", json);
        }
    }
}
