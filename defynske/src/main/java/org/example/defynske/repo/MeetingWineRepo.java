package org.example.defynske.repo;

import org.example.defynske.model.MeetingWine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MeetingWineRepo extends JpaRepository<MeetingWine, Long>{
    @Query("SELECT mw FROM MeetingWine mw " +
            "JOIN FETCH mw.addedBy " +
            "JOIN FETCH mw.wine " +
            "WHERE mw.meeting.id = :meetingId " +
            "ORDER BY mw.sequenceNo ASC")
    List<MeetingWine> findByMeetingIdOrderBySequenceNoAsc(@Param("meetingId") Long meetingId);

}
