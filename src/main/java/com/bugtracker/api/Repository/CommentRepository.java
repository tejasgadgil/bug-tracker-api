package com.bugtracker.api.Repository;

import com.bugtracker.api.Model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {}
