package org.example.defynske.repo;

import org.example.defynske.model.Meeting;
import org.example.defynske.model.Member;
import org.example.defynske.model.Rating;
import org.example.defynske.model.Wine;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface RatingRepo extends JpaRepository<Rating,Long> {
    List<Rating> findByMeetingId(Long meetingId);
    Optional<Rating> findByMeetingAndWineAndMember(Meeting meeting, Wine wine, Member member);
    List<Rating> findByMeetingIdAndWineId(Long meetingId, Long wineId);
    Optional<Rating> findByMeetingIdAndWineIdAndMemberId(Long meetingId, Long wineId, Long memberId);
    List<Rating> findByMemberId(Long id);
}
