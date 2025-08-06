package com.bugtracker.api.Payload;

import jakarta.validation.constraints.NotBlank;

// Request DTO for creating or updating comments
public class CommentRequest {

    @NotBlank
    private String content;

    private Long bugId;

    // getters/setters
    public Long getBugId() {
        return bugId;
    }

    public void setBugId(Long bugId) {
        this.bugId = bugId;
    }

    public @NotBlank String getContent() {
        return content;
    }

    public void setContent(@NotBlank String content) {
        this.content = content;
    }
}
