package com.bugtracker.api.Service;


import com.bugtracker.api.Model.Bug;
import com.bugtracker.api.Model.Comment;
import com.bugtracker.api.Model.User;
import com.bugtracker.api.Repository.BugRepository;
import com.bugtracker.api.Repository.CommentRepository;
import com.bugtracker.api.Repository.UserRepository;
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
    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('DEVELOPER')")
    public List<Comment> getCommentsByBugId(Long bugId) {
        if (!bugRepository.existsById(bugId)){
            throw new IllegalArgumentException("Bug with ID " + bugId + " does not exist.");
        }
        return commentRepository.findByBugId(bugId);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('DEVELOPER')")
    public Comment createComment(Long bugId, Long UserId, String content){
        Bug bug = bugRepository.findById(bugId)
                .orElseThrow(() -> new IllegalArgumentException("Bug with ID " + bugId + " does not exist."));
        User user = userRepository.findById(UserId)
                .orElseThrow(() -> new IllegalArgumentException("User with ID " + UserId + " does not exist."));
        Comment comment = new Comment();
        comment.setBug(bug);
        comment.setUser(user);
        comment.setContent(content);
        comment.setCreatedAt(OffsetDateTime.now());
        return commentRepository.save(comment);

    }

    @PreAuthorize("hasRole('ADMIN') or (hasRole('DEVELOPER') and @commentRepository.findById(#id).get().user.username == authentication.name)")
    public Comment updateComment(Long id, String content) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Comment with ID " + id + " does not exist."));
        comment.setContent(content);
        return commentRepository.save(comment);
    }

    @PreAuthorize("hasRole('ADMIN') or (hasRole('DEVELOPER') and @commentRepository.findById(#id).get().user.username == authentication.name)")
    public Comment deleteComment(Long id, String content) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Comment with ID " + id + " does not exist."));
        commentRepository.delete(comment);
        return comment;
    }

}
