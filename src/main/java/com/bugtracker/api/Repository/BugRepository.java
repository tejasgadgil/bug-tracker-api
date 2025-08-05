package com.bugtracker.api.Repository;

import com.bugtracker.api.Model.Bug;
import com.bugtracker.api.Model.PriorityEnum;
import com.bugtracker.api.Model.StatusEnum;
import com.bugtracker.api.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BugRepository extends JpaRepository<Bug, Long> {
    Optional<List<Bug>> findByAssignee(User assignee);
    Optional<List<Bug>> findByStatus(StatusEnum status);
    Optional<List<Bug>> findByPriority(PriorityEnum priority);

}
