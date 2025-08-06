package com.bugtracker.api.Controller;


import com.bugtracker.api.Model.Comment;
import com.bugtracker.api.Payload.CommentRequest;
import com.bugtracker.api.Payload.CommentResponse;
import com.bugtracker.api.Payload.MessageResponse;
import com.bugtracker.api.Service.CommentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/bug/{bugId}")
    public ResponseEntity<List<CommentResponse>> getCommentsByBugId(@PathVariable Long bugId){
        List<CommentResponse> comments = commentService.getCommentsByBugId(bugId).stream()
                .map(commentService::convertToResponse)
                .toList();
        return ResponseEntity.ok(comments);
    }

    // Create comment for a bug - authenticated user
    @PostMapping
    public ResponseEntity<CommentResponse> createComment(@Valid @RequestBody CommentRequest request, Authentication authentication) {
        String username = authentication.getName();
        var createdComment = commentService.createCommentFromRequest(request, username);
        return ResponseEntity.status(201).body(commentService.convertToResponse(createdComment));
    }
    // Update comment content - only owner or admin can update
    @PutMapping("/{id}")
    public ResponseEntity<CommentResponse> updateComment(@PathVariable Long id,
                                                         @Valid @RequestBody CommentRequest request,
                                                         Authentication authentication) {
        String username = authentication.getName();
        var updatedComment = commentService.updateComment(id, request.getContent(), username);
        return ResponseEntity.ok(commentService.convertToResponse(updatedComment));
    }

    // Delete comment - only owner or admin
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteComment(@PathVariable Long id, Authentication authentication) {
        String username = authentication.getName();
        commentService.deleteComment(id, username);
        return ResponseEntity.ok(new MessageResponse("Comment deleted successfully"));
    }
}
