package news_recommend.news.issue;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import news_recommend.news.issue.dto.IssuePreviewResponse;
import news_recommend.news.issue.repository.IssueRepository;
import news_recommend.news.utils.PagedResponse;
import news_recommend.news.utils.Pagination;


import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.stream.Collectors;


public class IssueService {

    private final IssueRepository issueRepository;
    public IssueService(final IssueRepository issueRepository){
        this.issueRepository = issueRepository;
    }



    public List<Issue> findIssues() {
        return issueRepository.findAll();
    }

    public Issue save(Issue issue) {
        return issueRepository.save(issue);
    }

    public Optional<Issue> findById(Long id) {
        return issueRepository.findById(id);
    }

    public int update(Issue issue){ return issueRepository.update(issue); }

    public int delete(Long id) {
        return issueRepository.delete(id);
    }

    // 카테고리별 이슈 리스트 코드 추가
    public Map<String, Object> getIssuesByCategory(String category, int page, int size) {
        int offset = (page - 1) * size;
        List<Issue> issues = issueRepository.findByCategory(category, size, offset);
        int totalItems = issueRepository.countByCategory(category);
        int totalPages = (int) Math.ceil((double) totalItems / size);

        List<Map<String, Object>> data = issues.stream().map(issue -> {
            Map<String, Object> item = new HashMap<>();
            item.put("issueId", issue.getIssueId());
            item.put("issueName", issue.getIssueName());
            item.put("category", issue.getCategory());
            item.put("sentimentTrend", parseEmotion(issue.getEmotion()));  // JSON -> List<Integer>
            item.put("newsList", new ArrayList<>());  // 뉴스 연결 미구현 상태
            item.put("thumbnail", null);
            return item;
        }).collect(Collectors.toList());

        Map<String, Object> pagination = new HashMap<>();
        pagination.put("currentPage", page);
        pagination.put("pageSize", size);
        pagination.put("totalItems", totalItems);
        pagination.put("totalPages", totalPages);
        pagination.put("hasNext", page < totalPages);

        Map<String, Object> result = new HashMap<>();
        result.put("resultType", "SUCCESS");
        result.put("error", null);
        result.put("pagination", pagination);
        result.put("data", data);
        return result;
    }

    private List<Integer> parseEmotion(String emotion) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(emotion, new TypeReference<List<Integer>>() {});
        } catch (Exception e) {
            return List.of(50, 50, 50, 50, 50);
        }
    }
    // 카테고리별 이슈 리스트 코드 추가 끝


    public PagedResponse<IssuePreviewResponse> searchIssues(String keyword, String sort, int page, int size) {
        int offset =  (page - 1) * size;
        List<IssuePreviewResponse> issues = issueRepository.findByKeywordAndSort(keyword, sort, size, offset);
        System.out.println("searchIssues: " + issues);
        for (IssuePreviewResponse res : issues) {
            System.out.println("ID: " + res.getIssueId());
            System.out.println("Name: " + res.getIssueName());
            System.out.println("Category: " + res.getCategory());
            System.out.println("News List: " + String.join(", ", res.getNewsList()));
            System.out.println("Bookmarked: " + res.isBookmarked());
            System.out.println("----------");
        }
        int totalItems = issueRepository.countByKeyword(keyword);

        int totalPages = (int) Math.ceil((double) totalItems / size);
        boolean hasNext = page < totalPages;
        Pagination pagination = new Pagination(page , size, totalItems, totalPages, hasNext);

        return new PagedResponse<>(pagination, issues);
    }

}