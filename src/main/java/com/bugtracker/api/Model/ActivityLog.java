package com.bugtracker.api.Model;

import jakarta.persistence.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "activity_log")
public class ActivityLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String details;

    @Column(name = "timestamp", nullable = false, updatable = false)
    private OffsetDateTime timestamp = OffsetDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bug_id", nullable = false)
    private Bug bug;
}
