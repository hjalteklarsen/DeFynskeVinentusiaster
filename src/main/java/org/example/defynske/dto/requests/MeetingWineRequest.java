package org.example.defynske.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MeetingWineRequest {
    private Long wineId;          // optional existing wine
    private String wineName;      // optional new wine
    private String country;       // new
    private String category;      // new (Rødvin / Hvidvin / Rosé)
    private Integer sequenceNo;
    private String notes;
    private String vivinoUrl;
    private Double vivinoScore;
    private Long addedById;
}
