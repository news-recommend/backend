package news_recommend.news.issue;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/issues")
public class IssueController {

    private final IssueService issueService;

    public IssueController(final IssueService issueService){
        this.issueService = issueService;
    }


    @GetMapping
    public ResponseEntity<List<Issue>> getIssues() { return ResponseEntity.ok(issueService.findIssues());
    }

    @PostMapping
    public ResponseEntity<Issue> createIssue(@RequestBody Issue issue) {

        return ResponseEntity.ok(issueService.save(issue));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Issue> getIssue(@PathVariable Long id) {
        return issueService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Issue> updateIssue(@PathVariable Long id, @RequestBody Issue updatedIssue) {
        updatedIssue.setIssueId(id);
        issueService.update(updatedIssue);
        return ResponseEntity.ok(updatedIssue);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIssue(@PathVariable Long id) {
        issueService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // 카테고리별 이슈 리스트 코드 추가
    @GetMapping("/category")
    public ResponseEntity<?> getIssuesByCategory(
            @RequestParam String category,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        return ResponseEntity.ok(issueService.getIssuesByCategory(category, page, size));
    }
    // 카테고리별 이슈 리스트 코드 추가 끝


}