package com.bugtracker.api.Payload;

import com.bugtracker.api.Model.PriorityEnum;
import com.bugtracker.api.Model.StatusEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;

// Request DTO for creating/updating a bug
public class BugRequest {
    @NotBlank
    private String title;

    private String description;

    @NotNull
    private StatusEnum status;

    @NotNull
    private PriorityEnum priority;

    private Long assigneeId;  // Just the user's ID, not the full User object

    // getters/setters


    public Long getAssigneeId() {
        return assigneeId;
    }

    public void setAssigneeId(Long assigneeId) {
        this.assigneeId = assigneeId;
    }

    public @NotNull PriorityEnum getPriority() {
        return priority;
    }

    public void setPriority(@NotNull PriorityEnum priority) {
        this.priority = priority;
    }

    public @NotNull StatusEnum getStatus() {
        return status;
    }

    public void setStatus(@NotNull StatusEnum status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public @NotBlank String getTitle() {
        return title;
    }

    public void setTitle(@NotBlank String title) {
        this.title = title;
    }
}


