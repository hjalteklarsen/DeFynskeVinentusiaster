package org.example.defynske.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RatingRequest {
    private Long wineId;
    private Long meetingId;
    private Long memberId; // optional
    private String memberName; // optional
    private Double dfvScore;
}
