package org.example.defynske.repo;

import org.example.defynske.model.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingRepo extends JpaRepository<Meeting,Long> {
}
