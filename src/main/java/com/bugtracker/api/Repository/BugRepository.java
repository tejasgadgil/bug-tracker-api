package com.bugtracker.api.Repository;

import com.bugtracker.api.Model.Bug;
import com.bugtracker.api.Model.PriorityEnum;
import com.bugtracker.api.Model.StatusEnum;
import com.bugtracker.api.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BugRepository extends JpaRepository<Bug, Long> {
    List<Bug> findByAssignee(User assignee);
    List<Bug> findByStatus(StatusEnum status);
    List<Bug> findByPriority(PriorityEnum priority);

}
