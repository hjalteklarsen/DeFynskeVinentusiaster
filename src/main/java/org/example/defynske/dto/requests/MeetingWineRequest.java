package org.example.defynske.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MeetingWineRequest {
    private Long wineId;
    private String wineName;
    private String country;
    private String category;
    private Integer sequenceNo;
    private String notes;
    private String vivinoUrl;
    private Double vivinoScore;
    private Long addedById;
}
