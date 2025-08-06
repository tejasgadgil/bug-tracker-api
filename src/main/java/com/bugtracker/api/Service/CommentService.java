package com.bugtracker.api.Service;


import com.bugtracker.api.Model.Bug;
import com.bugtracker.api.Model.Comment;
import com.bugtracker.api.Model.User;
import com.bugtracker.api.Payload.CommentRequest;
import com.bugtracker.api.Payload.CommentResponse;
import com.bugtracker.api.Repository.BugRepository;
import com.bugtracker.api.Repository.CommentRepository;
import com.bugtracker.api.Repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final BugRepository bugRepository;
    private final UserRepository userRepository;


    public CommentService(CommentRepository commentRepository, BugRepository bugRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.bugRepository = bugRepository;
        this.userRepository = userRepository;
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('DEVELOPER')")
    public List<Comment> getCommentsByBugId(Long bugId) {
        if (!bugRepository.existsById(bugId)) {
            throw new EntityNotFoundException("Bug with ID " + bugId + " does not exist.");
        }
        return commentRepository.findByBugId(bugId);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('DEVELOPER')")
    public Comment createCommentFromRequest(CommentRequest request, String username) {
        Bug bug = bugRepository.findById(request.getBugId())
                .orElseThrow(() -> new EntityNotFoundException("Bug with ID " + request.getBugId() + " does not exist."));
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User with username " + username + " does not exist."));

        Comment comment = new Comment();
        comment.setBug(bug);
        comment.setUser(user);
        comment.setContent(request.getContent());
        comment.setCreatedAt(OffsetDateTime.now());
        return commentRepository.save(comment);
    }

    @PreAuthorize("hasRole('ADMIN') or (hasRole('DEVELOPER') and @commentRepository.findById(#id).get().user.username == authentication.name)")
    public Comment updateComment(Long id, String content, String username) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comment with ID " + id + " does not exist."));
        // Additional manual check if desired instead of SpEL security on method
//         if (!comment.getUser().getUsername().equals(username)) { throw new AccessDeniedException("Forbidden"); }
        comment.setContent(content);
        return commentRepository.save(comment);
    }

    @PreAuthorize("hasRole('ADMIN') or (hasRole('DEVELOPER') and @commentRepository.findById(#id).get().user.username == authentication.name)")
    public void deleteComment(Long id, String username) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comment with ID " + id + " does not exist."));
        commentRepository.delete(comment);
    }

    public CommentResponse convertToResponse(Comment comment) {
        CommentResponse response = new CommentResponse();
        response.setId(comment.getId());
        response.setContent(comment.getContent());
        response.setCreatedAt(comment.getCreatedAt());
        response.setBugId(comment.getBug().getId());
        response.setUserId(comment.getUser().getId());
        response.setUsername(comment.getUser().getUsername());
        return response;
    }


}
