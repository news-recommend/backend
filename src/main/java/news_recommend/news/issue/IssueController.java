package news_recommend.news.issue;

import news_recommend.common.response.PaginationResponse;
import news_recommend.news.issue.dto.IssueDetailResponse;
import news_recommend.news.issue.dto.IssuePreviewResponse;
import news_recommend.news.issue.dto.RawNews;
import news_recommend.news.issue.service.NewsFetchService;
import news_recommend.news.llm.LLMService;
import news_recommend.news.utils.ApiResponse;
import news_recommend.news.utils.PagedResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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
    @GetMapping("/category")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getCategoryIssues(
            @RequestParam("category") String category,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        List<IssuePreviewResponse> issues = issueService.getIssuesByCategory(category, page, size);
        int totalItems = issueService.getTotalIssuesByCategory(category);
        int totalPages = (int) Math.ceil((double) totalItems / size);
        boolean hasNext = page < totalPages;

        PaginationResponse pagination = new PaginationResponse(page, size, totalItems, totalPages, hasNext);
        Map<String, Object> success = new HashMap<>();
        success.put("pagination", pagination);
        success.put("data", issues);

        return ResponseEntity.ok(ApiResponse.success(success));
    }



    // 네이버 뉴스 API로 이슈명에 해당하는 뉴스 조회
    @GetMapping("/{issueName}/news")
    public ResponseEntity<List<RawNews>> getIssueNews(@PathVariable String issueName) {
        List<RawNews> news = newsFetchService.fetch(issueName);
        return ResponseEntity.ok(news);
    }


    // DB 기반 이슈 검색
    @GetMapping("/search/{keyword}")
    public ResponseEntity<ApiResponse<PagedResponse<IssuePreviewResponse>>> searchIssues(
            @PathVariable String keyword,
            @RequestParam(defaultValue = "latest") String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        System.out.println("keyword: " + keyword + ", sort: " + sort + ", page: " + page);
        PagedResponse<IssuePreviewResponse> result = issueService.searchIssues(keyword, sort, page, size);
        return ResponseEntity.ok(ApiResponse.success(result));
    }


    // DB 기반 이슈 상세 조회
    @GetMapping("/detail")
    public ResponseEntity<ApiResponse<IssueDetailResponse>> getIssueDetail(@RequestParam("name") String issueName) {
        Optional<Issue> optional = issueService.findByName(issueName);
        if (optional.isEmpty()) {
            return ResponseEntity.status(404).body(ApiResponse.fail("해당 이슈를 찾을 수 없습니다."));
        }

        IssueDetailResponse detail = issueService.getIssueDetail(optional.get().getIssueId());
        return ResponseEntity.ok(ApiResponse.success(detail));
    }

    @GetMapping("/titles")
    public ResponseEntity<List<String>> getIssueTitles(
            @RequestParam("category") String category
    ) {
        List<String> titles = issueService.getIssueTitlesByCategory(category);
        return ResponseEntity.ok(titles);
    }




//    @GetMapping("/analyze")
//    public ResponseEntity<IssueDetailResponse> analyzeIssueByQuery(@RequestParam String query) {
//        List<RawNews> newsList = newsFetchService.fetch(query);
//
//        List<IssueDetailResponse.NewsWithScore> newsWithScores = new ArrayList<>();
//        for (RawNews news : newsList) {
//            int score = llmService.analyzeSentimentScore(news.getTitle(), news.getDescription());
//            newsWithScores.add(new IssueDetailResponse.NewsWithScore(
//                    news.getTitle(),
//                    news.getLink(),
//                    news.getDescription(),
//                    news.getPubDate(),
//                    score
//            ));
//        }
//
//        String category = llmService.classifyCategory(query, newsList);
//
//        IssueDetailResponse response = new IssueDetailResponse(
//                query,
//                category,
//                newsWithScores,
//                false // 북마크 여부 (임시)
//        );
//
//        return ResponseEntity.ok(response);
//    }
}
