package org.example.defynske.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class MeetingRequest {
    private LocalDate date;
    private String description;
    private String hostUsername;
}