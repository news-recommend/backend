package news_recommend.news.issue;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class SchedulerTestController {

    private final IssueService issueService;

    @PostMapping("/run-scheduler")
    public ResponseEntity<String> runScheduler() {
        issueService.updateDailyIssues();  // 수동 실행
        return ResponseEntity.ok("✅ 스케줄러 수동 실행 완료");
    }
}
