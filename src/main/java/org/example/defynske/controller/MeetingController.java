package org.example.defynske.controller;

import lombok.RequiredArgsConstructor;
import org.example.defynske.dto.requests.MeetingRequest;
import org.example.defynske.dto.requests.MeetingWineRequest;
import org.example.defynske.dto.requests.RatingRequest;
import org.example.defynske.dto.responds.MeetingDetailsResponse;
import org.example.defynske.dto.responds.MeetingResponse;
import org.example.defynske.dto.responds.MeetingWineResponse;
import org.example.defynske.model.Meeting;
import org.example.defynske.repo.MeetingWineRepo;
import org.example.defynske.service.MeetingService;
import org.example.defynske.service.RatingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/internal/meeting")
@RequiredArgsConstructor
public class MeetingController {

    private final MeetingService meetingService;
    private final MeetingWineRepo meetingWineRepo;
    private final RatingService ratingService;


    @PostMapping
    public MeetingResponse createMeeting(@RequestBody MeetingRequest req) {
        Meeting meeting = meetingService.createMeeting(req.getDate(), req.getDescription(), req.getHostUsername());
        return MeetingResponse.fromEntity(meeting);
    }

    // List view
    @GetMapping
    public List<MeetingResponse> listMeetings() {
        return meetingService.getAllMeetings().stream()
                .map(MeetingResponse::fromEntity)
                .toList();
    }

    // Details view
    @GetMapping("/{id}")
    public MeetingDetailsResponse getMeetingDetails(@PathVariable Long id) {
        return meetingService.getMeetingDetails(id);
    }


    @GetMapping("/{id}/wines")
    public List<MeetingWineResponse> listMeetingWines(@PathVariable Long id) {
        return meetingWineRepo.findByMeetingIdOrderBySequenceNoAsc(id).stream()
                .map(MeetingWineResponse::fromEntity)
                .toList();
    }

    @PostMapping("/{id}/wines")
    public MeetingWineResponse addMeetingWine(@PathVariable Long id, @RequestBody MeetingWineRequest req) {
        return meetingService.addMeetingWine(id, req);
    }

    @DeleteMapping("/wines/{meetingWineId}")
    public void removeMeetingWine(@PathVariable Long meetingWineId) {
        meetingWineRepo.deleteById(meetingWineId);
    }

    @PostMapping("/{meetingId}/rating")
    public ResponseEntity<Void> saveRating(
            @PathVariable Long meetingId,
            @RequestBody RatingRequest req) {
        ratingService.saveRating(req);
        return ResponseEntity.ok().build();
    }

}
