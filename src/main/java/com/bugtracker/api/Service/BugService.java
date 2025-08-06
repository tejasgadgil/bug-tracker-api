package com.bugtracker.api.Service;

import com.bugtracker.api.Model.Bug;
import com.bugtracker.api.Model.PriorityEnum;
import com.bugtracker.api.Model.StatusEnum;
import com.bugtracker.api.Model.User;
import com.bugtracker.api.Payload.BugRequest;
import com.bugtracker.api.Payload.BugResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.access.prepost.PreAuthorize;
import java.time.OffsetDateTime;
import java.util.List;

import com.bugtracker.api.Repository.BugRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BugService {

    @Autowired
    private BugRepository bugRepository;

    @Autowired
    private UserService userService;

//    @PreAuthorize("hasRole('ADMIN') or hasRole('DEVELOPER')")
//    public List<Bug> getAllBugs() {
//        return bugRepository.findAll();
//    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('DEVELOPER')")
    public List<Bug> filterBugs(Long assigneeId, StatusEnum status, PriorityEnum priority) {
        return bugRepository.findAll().stream()
                .filter(bug -> assigneeId == null || (bug.getAssignee() != null && assigneeId.equals(bug.getAssignee().getId())))
                .filter(bug -> status == null || status == bug.getStatus())
                .filter(bug -> priority == null || priority == bug.getPriority())
                .toList();
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('DEVELOPER')")
    public Bug getBugById(Long id) {
        return bugRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Bug not found with id: " + id));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('DEVELOPER')")
    public Bug createBugFromRequest(BugRequest request, String creatorUsername) {
        Bug bug = new Bug();
        bug.setTitle(request.getTitle());
        bug.setDescription(request.getDescription());
        bug.setPriority(request.getPriority());
        bug.setStatus(request.getStatus() != null ? request.getStatus() : StatusEnum.OPEN);

        if (request.getAssigneeId() != null) {
            User assignee = userService.findById(request.getAssigneeId())
                    .orElseThrow(() -> new EntityNotFoundException("Assignee not found"));
            bug.setAssignee(assignee);
        }

        bug.setCreatedAt(OffsetDateTime.now());

        return createBug(bug, creatorUsername);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('DEVELOPER')")
    public Bug createBug(Bug bug, String creatorUsername) {
        bug.setCreatedAt(OffsetDateTime.now());
        return bugRepository.save(bug);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('DEVELOPER')")
    public Bug updateBugFromRequest(Long bugId, BugRequest request) {
        Bug bug = getBugById(bugId);
        bug.setTitle(request.getTitle());
        bug.setDescription(request.getDescription());
        bug.setPriority(request.getPriority());
        bug.setStatus(request.getStatus());

        if (request.getAssigneeId() != null) {
            User assignee = userService.findById(request.getAssigneeId())
                    .orElseThrow(() -> new EntityNotFoundException("Assignee not found"));
            bug.setAssignee(assignee);
        } else {
            bug.setAssignee(null);
        }

        bug.setUpdatedAt(OffsetDateTime.now());

        return bugRepository.save(bug);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteBug(Long id) {
        if (!bugRepository.existsById(id)) {
            throw new EntityNotFoundException("Bug not found with id: " + id);
        }
        bugRepository.deleteById(id);
    }
    public BugResponse convertToResponse(Bug bug) {
        BugResponse response = new BugResponse();
        response.setId(bug.getId());
        response.setTitle(bug.getTitle());
        response.setDescription(bug.getDescription());
        response.setPriority(bug.getPriority());
        response.setStatus(bug.getStatus());
        response.setCreatedAt(bug.getCreatedAt());
        response.setUpdatedAt(bug.getUpdatedAt());
        if (bug.getAssignee() != null) {
            response.setAssigneeId(bug.getAssignee().getId());
        }
        return response;
    }
}
