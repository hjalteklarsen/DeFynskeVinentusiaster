package org.example.defynske.dto.responds;

import lombok.*;
import org.example.defynske.model.Meeting;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MeetingDetailsResponse {
    private Long id;
    private LocalDate date;
    private String description;
    private String hostDisplayName;
    private List<String> members;
    private List<MeetingWineResponse> wines;

    public static MeetingDetailsResponse fromEntity(Meeting meeting,
                                                    List<String> members,
                                                    List<MeetingWineResponse> wines) {
        return MeetingDetailsResponse.builder()
                .id(meeting.getId())
                .date(meeting.getDate())
                .description(meeting.getDescription())
                .hostDisplayName(meeting.getHost().getDisplayName())
                .members(members)
                .wines(wines)
                .build();
    }
}
