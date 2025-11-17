package org.example.defynske.repo;

import org.example.defynske.model.Wine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WineRepo extends JpaRepository<Wine, Long> {
}
