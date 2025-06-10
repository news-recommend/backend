package news_recommend.news.issue;

import news_recommend.news.issue.dto.IssueDetailResponse;
import news_recommend.news.issue.dto.RawNews;
import news_recommend.news.issue.service.NewsFetchService;
import news_recommend.news.llm.LLMService;
import news_recommend.news.utils.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/issues")
public class IssueController {

    private final IssueService issueService;
    private final NewsFetchService newsFetchService;
    private final LLMService llmService;

    public IssueController(
            final IssueService issueService,
            final NewsFetchService newsFetchService,
            final LLMService llmService
    ) {
        this.issueService = issueService;
        this.newsFetchService = newsFetchService;
        this.llmService = llmService;
    }

    @GetMapping
    public ResponseEntity<List<Issue>> getIssues() {
        return ResponseEntity.ok(issueService.findIssues());
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
//    @GetMapping("/category")
//    public ResponseEntity<?> getIssuesByCategory(
//            @RequestParam String category,
//            @RequestParam(defaultValue = "1") int page,
//            @RequestParam(defaultValue = "5") int size
//    ) {
//        return ResponseEntity.ok(ApiResponse.success(issueService.getIssuesByCategory(category, page, size)));
//    }
    // 카테고리별 이슈 리스트 코드 추가 끝

    // 네이버 뉴스 API로 이슈명에 해당하는 뉴스 조회
    @GetMapping("/{issueName}/news")
    public ResponseEntity<List<RawNews>> getIssueNews(@PathVariable String issueName) {
        List<RawNews> news = newsFetchService.fetch(issueName);
        return ResponseEntity.ok(news);
    }

    // 북마크를 위한 db저장용?
    @GetMapping("/{id}/detail")
    public ResponseEntity<IssueDetailResponse> getIssueDetail(@PathVariable Long id) {
        Optional<Issue> issueOpt = issueService.findById(id);
        if (issueOpt.isEmpty()) return ResponseEntity.notFound().build();

        Issue issue = issueOpt.get();
        List<RawNews> newsList = newsFetchService.fetch(issue.getTitle());

        List<IssueDetailResponse.NewsWithScore> newsWithScores = new ArrayList<>();
        for (RawNews news : newsList) {
            int score = llmService.analyzeSentimentScore(news.getTitle(), news.getDescription());
            newsWithScores.add(new IssueDetailResponse.NewsWithScore(
                    news.getTitle(),
                    news.getLink(),
                    news.getDescription(),
                    news.getPubDate(),
                    score
            ));
        }

        String category = llmService.classifyCategory(issue.getTitle(), newsList);
        boolean bookmarked = false;

        IssueDetailResponse response = new IssueDetailResponse(
                issue.getTitle(), category, newsWithScores, bookmarked
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/analyze")
    public ResponseEntity<IssueDetailResponse> analyzeIssueByQuery(@RequestParam String query) {
        List<RawNews> newsList = newsFetchService.fetch(query);

        List<IssueDetailResponse.NewsWithScore> newsWithScores = new ArrayList<>();
        for (RawNews news : newsList) {
            int score = llmService.analyzeSentimentScore(news.getTitle(), news.getDescription());
            newsWithScores.add(new IssueDetailResponse.NewsWithScore(
                    news.getTitle(),
                    news.getLink(),
                    news.getDescription(),
                    news.getPubDate(),
                    score
            ));
        }

        String category = llmService.classifyCategory(query, newsList);

        IssueDetailResponse response = new IssueDetailResponse(
                query,
                category,
                newsWithScores,
                false // 북마크 여부 (임시)
        );

        return ResponseEntity.ok(response);
    }
}
