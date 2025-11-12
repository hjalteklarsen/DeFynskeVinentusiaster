package org.example.defynske.service;

import lombok.RequiredArgsConstructor;
import org.example.defynske.dto.requests.RatingRequest;
import org.example.defynske.dto.responds.MemberRatingDTO;
import org.example.defynske.dto.responds.RatingResponse;
import org.example.defynske.model.Meeting;
import org.example.defynske.model.Member;
import org.example.defynske.model.Rating;
import org.example.defynske.model.Wine;
import org.example.defynske.repo.MeetingRepo;
import org.example.defynske.repo.MemberRepo;
import org.example.defynske.repo.RatingRepo;
import org.example.defynske.repo.WineRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RatingService {
    private final RatingRepo ratingRepo;
    private final MemberRepo memberRepo;
    private final WineRepo wineRepo;
    private final MeetingRepo meetingRepo;

    public void saveRating(RatingRequest req) {
        Member member;
        if (req.getMemberId() != null) {
            member = memberRepo.findById(req.getMemberId())
                    .orElseThrow(() -> new IllegalArgumentException("Member not found"));
        } else if (req.getMemberName() != null) {
            member = memberRepo.findByDisplayName(req.getMemberName())
                    .orElseThrow(() -> new IllegalArgumentException("Member not found by name"));
        } else {
            throw new IllegalArgumentException("Member information missing");
        }

        Wine wine = wineRepo.findById(req.getWineId())
                .orElseThrow(() -> new IllegalArgumentException("Wine not found"));
        Meeting meeting = meetingRepo.findById(req.getMeetingId())
                .orElseThrow(() -> new IllegalArgumentException("Meeting not found"));

        Rating rating = ratingRepo.findByMeetingIdAndWineIdAndMemberId(
                        req.getMeetingId(), req.getWineId(), member.getId())
                .orElse(new Rating());

        rating.setMeeting(meeting);
        rating.setWine(wine);
        rating.setMember(member);
        rating.setDfvScore(req.getDfvScore());

        ratingRepo.save(rating);
    }

    public List<RatingResponse> getRatingsOverview() {
        List<Rating> allRatings = ratingRepo.findAll();

        // Group all ratings by wine ID
        Map<Long, List<Rating>> grouped = allRatings.stream()
                .collect(Collectors.groupingBy(r -> r.getWine().getId()));

        return grouped.values().stream()
                .map(group -> {
                    Rating first = group.get(0);

                    // Collect only members who rated this wine
                    List<MemberRatingDTO> memberRatings = group.stream()
                            .filter(r -> r.getDfvScore() != null && r.getDfvScore() > 0)
                            .map(r -> new MemberRatingDTO(
                                    r.getMember().getDisplayName(),
                                    r.getDfvScore()
                            ))
                            .toList();

                    // Calculate average DFV
                    double avg = memberRatings.stream()
                            .mapToDouble(MemberRatingDTO::score)
                            .average()
                            .orElse(0.0);

                    return RatingResponse.builder()
                            .wineId(first.getWine().getId())
                            .wineName(first.getWine().getName())
                            .category(first.getWine().getCategory())
                            .imageUrl(first.getWine().getImageUrl())
                            .vivinoScore(first.getWine().getVivinoScore())
                            .country(first.getWine().getCountry())
                            .meetingDate(first.getMeeting().getDate())
                            .hostName(first.getMeeting().getHost().getDisplayName())
                            .dfvAvg(avg)
                            .ratings(memberRatings)
                            .build();
                })
                .toList();
    }

    public List<RatingResponse> getAllRatings() {
        return ratingRepo.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<RatingResponse> getRatingsForMeeting(Long meetingId) {
        return ratingRepo.findByMeetingId(meetingId).stream()
                .map(this::mapToResponse)
                .toList();
    }

    private RatingResponse mapToResponse(Rating rating) {
        return RatingResponse.builder()
                .wineId(rating.getWine().getId())
                .wineName(rating.getWine().getName())
                .category(rating.getWine().getCategory())
                .country(rating.getWine().getCountry())
                .dfvAvg(rating.getDfvScore()) // this is per-member, not average
                .vivinoScore(rating.getWine().getVivinoScore())
                .imageUrl(rating.getWine().getImageUrl())
                .meetingDate(rating.getMeeting() != null ? rating.getMeeting().getDate() : null)
                .hostName(rating.getMeeting() != null ? rating.getMeeting().getHost().getDisplayName() : null)
                .ratings(null) // optional, not used here
                .build();
    }
}
