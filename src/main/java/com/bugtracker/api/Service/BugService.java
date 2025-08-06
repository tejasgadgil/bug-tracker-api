package com.bugtracker.api.Service;

import com.bugtracker.api.Model.Bug;
import com.bugtracker.api.Model.PriorityEnum;
import com.bugtracker.api.Model.StatusEnum;
import com.bugtracker.api.Model.User;
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

    @PreAuthorize("hasRole('ADMIN') or hasRole('DEVELOPER')")
    public List<Bug> getAllBugs() {
        return bugRepository.findAll();
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('DEVELOPER')")
    public Bug getBugById(Long id) {
        return bugRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bug not found with id: " + id));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('DEVELOPER')")
    public Bug createBug(Bug bug, String creatorUsername) {
//        Optional<User> reporter = userService.findByUsername(creatorUsername);
        bug.setCreatedAt(OffsetDateTime.now());
//        bug.setReportedBy(reporter);       // add this field to Bug if not present already
//        bug.setStatus(Bug.StatusEnum.OPEN);
        return bugRepository.save(bug);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('DEVELOPER')")
    public Bug updateBug(Long id, Bug updatedBug) {
        Bug bug = getBugById(id);
        bug.setTitle(updatedBug.getTitle());
        bug.setDescription(updatedBug.getDescription());
        bug.setPriority(updatedBug.getPriority());
        bug.setStatus(updatedBug.getStatus());
        bug.setAssignee(updatedBug.getAssignee());
        bug.setUpdatedAt(OffsetDateTime.now());
        return bugRepository.save(bug);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteBug(Long id) {
        bugRepository.deleteById(id);
    }

//    public List<Bug> findBugsByAssignee(String assignee) {
//        return bugRepository.findByAssignee(userService.findByUsername(assignee))
//                .orElseThrow(() -> new RuntimeException("Bug not found with assignee: " + assignee));
//    }

    public List<Bug> findBugsByAssigneeId(Long assigneeId) {
//        User assignee = (User) userService.findById(assigneeId)
//                .orElseThrow(() -> new RuntimeException("Assignee user not found"));
        return bugRepository.findByAssigneeId(assigneeId)
                .orElseThrow(() -> new RuntimeException("No bugs found for assignee"));
    }


    public List<Bug> findBugsByStatus(StatusEnum status) {
        return bugRepository.findByStatus(status)
                .orElseThrow(() -> new RuntimeException("Bug not found with status: " + status));
    }

    public List<Bug> findBugsByPriority(PriorityEnum priority) {
        return bugRepository.findByPriority(priority)
                .orElseThrow(() -> new RuntimeException("Bug not found with priority: " + priority));
    }
}
