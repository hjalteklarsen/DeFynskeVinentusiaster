package org.example.defynske.dto.responds;

import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RatingResponse {
    private Long wineId;
    private String wineName;
    private String category;
    private String country;
    private Double dfvAvg;
    private Double vivinoScore;
    private String imageUrl;
    private LocalDate meetingDate;
    private String hostName;
    private List<MemberRatingDTO> ratings;
}
