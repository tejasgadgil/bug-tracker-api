package com.bugtracker.api.Controller;

import com.bugtracker.api.Model.Bug;
import com.bugtracker.api.Model.PriorityEnum;
import com.bugtracker.api.Model.StatusEnum;
import com.bugtracker.api.Model.User;
import com.bugtracker.api.Service.BugService;
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
    public ResponseEntity<List<Bug>> getAllBugs(
            @RequestParam(value = "assigneeId", required = false) Long assigneeId,
            @RequestParam(value = "status", required = false) StatusEnum status,
            @RequestParam(value = "priority", required = false) PriorityEnum priority
    ) {
        if (assigneeId != null) {
            return ResponseEntity.ok(bugService.findBugsByAssigneeId(assigneeId));
        } else if (status != null) {
            return ResponseEntity.ok(bugService.findBugsByStatus(status));
        } else if (priority != null) {
            return ResponseEntity.ok(bugService.findBugsByPriority(priority));
        } else {
            return ResponseEntity.ok(bugService.getAllBugs());
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<Bug> getBugById(@PathVariable Long id) {
        return ResponseEntity.ok(bugService.getBugById(id));
    }

    @PostMapping
    public ResponseEntity<Bug> createBug(@RequestBody Bug bug, Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(bugService.createBug(bug, username));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Bug> updateBug(@PathVariable Long id, @RequestBody Bug bug) {
        return ResponseEntity.ok(bugService.updateBug(id, bug));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBug(@PathVariable Long id) {
        bugService.deleteBug(id);
        return ResponseEntity.ok().body("Bug deleted successfully");
    }

}
