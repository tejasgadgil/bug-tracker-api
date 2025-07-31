package com.bugtracker.api.Repository;

import com.bugtracker.api.Model.ActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {}
