package com.bugtracker.api.Repository;

import com.bugtracker.api.Model.Bug;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BugRepository extends JpaRepository<Bug, Long> {
    // Basic JPA methods are enough; custom queries can be added later if needed.
}
