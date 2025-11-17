package org.example.defynske.dto.responds;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.defynske.model.MeetingWine;

import java.util.Map;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MeetingWineResponse {
    private Long id;
    private Long wineId;
    private String wineName;
    private String category;
    private Integer sequenceNo;
    private Long broughtById;
    private String broughtByName;
    private String imageUrl;
    private String vivinoUrl;
    private Double vivinoScore;
    private Map<String, Double> ratings;
    private Double dfv;

    public static MeetingWineResponse fromEntity(MeetingWine mW) {
        System.out.println("DEBUG → MW ID: " + mW.getId());
        System.out.println("DEBUG → AddedBy: " + mW.getAddedBy());
        if (mW.getAddedBy() != null) {
            System.out.println("DEBUG → AddedBy.displayName: " + mW.getAddedBy().getDisplayName());
            System.out.println("DEBUG → AddedBy.username: " + mW.getAddedBy().getUsername());
        } else {
            System.out.println("DEBUG → AddedBy is NULL");
        }

        return MeetingWineResponse.builder()
                .id(mW.getId())
                .wineId(mW.getWine().getId())
                .wineName(mW.getWine().getName())
                .category(mW.getWine().getCategory())
                .sequenceNo(mW.getSequenceNo())
                .broughtById(mW.getAddedBy() != null ? mW.getAddedBy().getId() : null)
                .broughtByName(mW.getAddedBy().getDisplayName())
                .imageUrl(mW.getWine().getImageUrl())
                .vivinoUrl(mW.getVivinoUrl())
                .vivinoScore(mW.getVivinoScore())
                .build();
    }

}
