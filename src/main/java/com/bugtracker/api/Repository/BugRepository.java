package com.bugtracker.api.Repository;

import com.bugtracker.api.Model.Bug;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BugRepository extends JpaRepository<Bug, Long> {
    Optional<List<Bug>> findByAssigneeId(Long id);

    Optional<List<Bug>> findByReportedById(Long id);
    // Basic JPA methods are enough; custom queries can be added later if needed.
}
