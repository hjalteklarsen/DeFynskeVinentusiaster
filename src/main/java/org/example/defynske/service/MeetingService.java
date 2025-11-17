package org.example.defynske.service;

import lombok.RequiredArgsConstructor;
import org.example.defynske.dto.requests.MeetingWineRequest;
import org.example.defynske.dto.responds.MeetingDetailsResponse;
import org.example.defynske.dto.responds.MeetingWineResponse;
import org.example.defynske.dto.responds.MeetingResponse;
import org.example.defynske.model.*;
import org.example.defynske.repo.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MeetingService {

    private final MeetingRepo meetingRepo;
    private final MemberRepo memberRepo;
    private final WineRepo wineRepo;
    private final RatingRepo ratingRepo;
    private final MeetingWineRepo meetingWineRepo;

    public List<Meeting> getAllMeetings() {
        return meetingRepo.findAll();
    }

    public Meeting getMeetingById(Long id) {
        return meetingRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No meeting found with ID: " + id));
    }

    public Meeting createMeeting(LocalDate date, String description, String hostUsername) {
        Member host = memberRepo.findByUsername(hostUsername)
                .orElseThrow(() -> new IllegalArgumentException("No member found with username: " + hostUsername));

        Meeting meeting = Meeting.builder()
                .date(date)
                .description(description)
                .host(host)
                .build();

        return meetingRepo.save(meeting);
    }

    public MeetingWineResponse addMeetingWine(Long meetingId, MeetingWineRequest req) {
        Meeting meeting = meetingRepo.findById(meetingId)
                .orElseThrow(() -> new IllegalArgumentException("Meeting not found"));

        Member member = memberRepo.findById(req.getAddedById())
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        Wine wine;

        // 👇 If wineId exists, use it — otherwise create a new wine
        if (req.getWineId() != null) {
            wine = wineRepo.findById(req.getWineId())
                    .orElseThrow(() -> new IllegalArgumentException("Wine not found"));
        } else {
            wine = Wine.builder()
                    .name(req.getWineName())
                    .country(req.getCountry())
                    .category(req.getCategory())
                    .vivinoScore(req.getVivinoScore())
                    .build();
            wine = wineRepo.save(wine);
        }

        MeetingWine mw = meetingWineRepo.save(MeetingWine.builder()
                .meeting(meeting)
                .wine(wine)
                .addedBy(member)
                .sequenceNo(req.getSequenceNo())
                .vivinoScore(req.getVivinoScore())
                .vivinoUrl(req.getVivinoUrl())
                .build());

        return MeetingWineResponse.fromEntity(mw);
    }


    public MeetingDetailsResponse getMeetingDetails(Long meetingId) {
        Meeting meeting = meetingRepo.findById(meetingId)
                .orElseThrow(() -> new IllegalArgumentException("Meeting not found"));

        List<MeetingWine> meetingWines = meetingWineRepo.findByMeetingIdOrderBySequenceNoAsc(meetingId);

        // Map wines -> DTOs
        List<MeetingWineResponse> wineResponses = meetingWines.stream()
                .map(mw -> {
                    // Get all ratings for this wine + meeting
                    List<Rating> ratings = ratingRepo.findByMeetingIdAndWineId(meetingId, mw.getWine().getId());

                    Map<String, Double> ratingMap = ratings.stream()
                            .collect(Collectors.toMap(
                                    r -> r.getMember().getDisplayName(),
                                    Rating::getDfvScore,
                                    (a, b) -> b,
                                    LinkedHashMap::new
                            ));

                    // Calculate DFV average
                    double dfv = ratingMap.values().stream()
                            .filter(v -> v != null && v > 0)
                            .mapToDouble(Double::doubleValue)
                            .average()
                            .orElse(0.0);

                    // Build the DTO with ratings + dfv
                    return MeetingWineResponse.builder()
                            .id(mw.getId())
                            .wineId(mw.getWine().getId())
                            .wineName(mw.getWine().getName())
                            .category(mw.getWine().getCategory())
                            .sequenceNo(mw.getSequenceNo())
                            .broughtById(mw.getAddedBy().getId())
                            .broughtByName(mw.getAddedBy().getDisplayName())
                            .imageUrl(mw.getWine().getImageUrl())
                            .vivinoUrl(mw.getVivinoUrl())
                            .vivinoScore(mw.getVivinoScore())
                            .ratings(ratingMap)
                            .dfv(dfv)
                            .build();
                })
                .toList();

        List<String> members = memberRepo.findAll().stream()
                .map(Member::getDisplayName)
                .toList();

        return MeetingDetailsResponse.fromEntity(meeting, members, wineResponses);
    }
}
