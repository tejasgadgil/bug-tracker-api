package com.bugtracker.api.Controller;

import com.bugtracker.api.Model.Bug;
import com.bugtracker.api.Model.PriorityEnum;
import com.bugtracker.api.Model.StatusEnum;
import com.bugtracker.api.Model.User;
import com.bugtracker.api.Payload.BugRequest;
import com.bugtracker.api.Payload.BugResponse;
import com.bugtracker.api.Payload.MessageResponse;
import com.bugtracker.api.Service.BugService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bugs")
public class BugController {
    @Autowired
    private BugService bugService;

    @GetMapping
    public ResponseEntity<List<BugResponse>> getAllBugs(
            @RequestParam(value = "assigneeId", required = false) Long assigneeId,
            @RequestParam(value = "status", required = false) StatusEnum status,
            @RequestParam(value = "priority", required = false) PriorityEnum priority
    ) {
        List<Bug> bugs = bugService.filterBugs(assigneeId, status, priority);
        List<BugResponse> response = bugs.stream()
                .map(bugService::convertToResponse)
                .toList();

        return ResponseEntity.ok(response);
    }


    @GetMapping("/{id}")
    public ResponseEntity<BugResponse> getBugById(@PathVariable Long id) {
        var bug = bugService.getBugById(id);
        return ResponseEntity.ok(bugService.convertToResponse(bug));
    }

    @PostMapping
    public ResponseEntity<BugResponse> createBug(@RequestBody @Valid BugRequest bugRequest, Authentication authentication) {
        String username = authentication.getName();
        var createdBug = bugService.createBugFromRequest(bugRequest, username);
        return ResponseEntity.status(201).body(bugService.convertToResponse(createdBug));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BugResponse> updateBug(@PathVariable Long id, @RequestBody @Valid BugRequest bugRequest) {
        var updatedBug = bugService.updateBugFromRequest(id, bugRequest);
        return ResponseEntity.ok(bugService.convertToResponse(updatedBug));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteBug(@PathVariable Long id) {
        bugService.deleteBug(id);
        return ResponseEntity.ok(new MessageResponse("Bug deleted successfully"));
    }

    @GetMapping("/{id}/assigned")
    public ResponseEntity<List<BugResponse>> getBugsAssignedToUser(Authentication auth) {
        String username = auth.getName();
        List<Bug> bugs = bugService.findBugsByAssigneeUsername(username);
        List<BugResponse> responses = bugs.stream()
                .map(bugService::convertToResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}/reported")
    public ResponseEntity<List<BugResponse>> getBugsReportedByUser(Authentication auth) {
        String username = auth.getName();
        List<Bug> bugs = bugService.findBugsByReporterUsername(username);
        List<BugResponse> responses = bugs.stream()
                .map(bugService::convertToResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }


}
