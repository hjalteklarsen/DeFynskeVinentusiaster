package org.example.defynske.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RatingRequest {
    private Long wineId;
    private double dfvScore;
    private double vivinoScore;
    private String comment;
}
