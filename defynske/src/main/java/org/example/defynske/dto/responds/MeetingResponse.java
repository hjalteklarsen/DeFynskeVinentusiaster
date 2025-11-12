package org.example.defynske.dto.responds;


import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.example.defynske.model.Meeting;

@Getter
@Builder
@AllArgsConstructor
public class MeetingResponse {
    private Long id;
    private LocalDate date;
    private String description;
    private String hostUsername;
    private String hostDisplayName;

    public static MeetingResponse fromEntity(Meeting m) {
        return new MeetingResponse(
                m.getId(),
                m.getDate(),
                m.getDescription(),
                m.getHost().getUsername(),
                m.getHost().getDisplayName()
        );
    }
}
