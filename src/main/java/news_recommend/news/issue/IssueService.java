package news_recommend.news.issue;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import news_recommend.news.issue.dto.IssueDetailResponse;
import news_recommend.news.issue.dto.IssuePreviewResponse;
import news_recommend.news.issue.dto.RawNews;
import news_recommend.news.issue.repository.IssueRepository;
import news_recommend.news.issue.repository.NewsRepository;
import news_recommend.news.llm.LLMService;
import news_recommend.news.issue.service.NewsFetchService;

import news_recommend.news.utils.PagedResponse;
import news_recommend.news.utils.Pagination;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class IssueService {

    private final IssueRepository issueRepository;
    private final NewsRepository newsRepository;
    private final NewsFetchService newsFetchService;
    private final LLMService llmService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public IssueService(IssueRepository issueRepository,
                        NewsRepository newsRepository,
                        NewsFetchService newsFetchService,
                        LLMService llmService) {
        this.issueRepository = issueRepository;
        this.newsRepository = newsRepository;
        this.newsFetchService = newsFetchService;
        this.llmService = llmService;
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

    public int update(Issue issue) {
        return issueRepository.update(issue);
    }

    public int delete(Long id) {
        return issueRepository.delete(id);
    }

    // 카테고리별 이슈 리스트 반환 (뉴스 리스트 포함)
    public List<IssuePreviewResponse> getIssuesByCategory(String category, int page, int size) {
        int offset = (page - 1) * size;
        List<Issue> issues = issueRepository.findByCategory(category, size, offset);

        return issues.stream().map(issue -> {
            List<String> newsTitles = new ArrayList<>();
            if (issue.getNewsList() != null && !issue.getNewsList().isBlank()) {
                try {
                   newsTitles = Collections.singletonList(issue.getNewsList());
                    
                } catch (Exception e) {
                    System.err.println("뉴스 파싱 실패: " + e.getMessage());
                    newsTitles = List.of("뉴스 제목 파싱 오류");
                }
            }

            return new IssuePreviewResponse(
                    issue.getIssueId(),
                    issue.getIssueName(),
                    issue.getCategory(),
                    null,       // sentimentTrend
                    null,       // thumbnail
                    false,      // isBookmarked
                    newsTitles  // ✅ title만 추출한 뉴스 리스트
            );
        }).collect(Collectors.toList());
    }

    public int getTotalIssuesByCategory(String category) {
        return issueRepository.countByCategory(category);
    }

    // 이슈 + 뉴스 저장 (스케줄러에서는 감정분석 없이 사용됨)
    public void saveIssueOnly(String issueName, String category, List<RawNews> newsList) {
        Issue issue = new Issue();
        issue.setIssueName(issueName);
        issue.setCategory(category);
        issue.setEmotion(null); // 감정점수 없이 저장
        issue.setThumbnail(null);

        Issue saved = issueRepository.save(issue);

        for (RawNews news : newsList) {
            newsRepository.save(news, saved.getIssueId());
        }
    }

    // 감정 분석이 포함된 이슈 저장 (상세조회 시 활용 가능)
    public void saveIssueWithSentiment(String issueName, String category, List<RawNews> enrichedNews) {
        System.out.println("✔ 뉴스 개수: " + (enrichedNews == null ? 0 : enrichedNews.size()));
        System.out.println("✔ 뉴스 내용: " + toJson(enrichedNews));

        List<Integer> sentimentTrend = extractSentimentTrend(enrichedNews);
        String emotionJson = toJson(sentimentTrend);

        Issue issue = new Issue();
        issue.setIssueName(issueName);
        issue.setCategory(category);
        issue.setEmotion(emotionJson);
        issue.setThumbnail(null);

        // 피드백 반영: 제목만 뽑아서 콤마로 join
        List<String> titleList = enrichedNews.stream()
                .map(RawNews::getTitle)
                .collect(Collectors.toList());

        String joinedTitles = String.join("|", titleList);  // 안전한 구분자 사용
        issue.setNewsList(joinedTitles);

        Issue saved = issueRepository.save(issue);

    }

    // 감정 추세 분석
    private List<Integer> extractSentimentTrend(List<RawNews> newsList) {
        List<Integer> scores = newsList.stream()
                .map(RawNews::getSentimentScore)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (scores.isEmpty()) return List.of(50, 50, 50, 50, 50);

        int groupSize = Math.max(1, scores.size() / 5);
        List<Integer> trend = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            int from = i * groupSize;
            int to = Math.min(from + groupSize, scores.size());
            if (from >= to) {
                trend.add(50);
            } else {
                int avg = (int) scores.subList(from, to).stream().mapToInt(Integer::intValue).average().orElse(50);
                trend.add(avg);
            }
        }
        return trend;
    }

    // 객체 → JSON 문자열
    private String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException("JSON 변환 실패", e);
        }
    }

    // 스케줄러: 매일 4시 22분 실행 - 감정 분석 없이 이슈만 저장
    @Scheduled(cron = "0 22 4 * * *", zone = "Asia/Seoul")
    public void updateDailyIssues() {
        List<String> categories = List.of(
                "정치",
                "경제",
                "사회",
                "사건/사고",
                "IT/과학",
                "자동차",
                "국제",
                "교육",
                "문화",
                "여행/레저",
                "연예",
                "환경",
                "부동산",
                "스포츠",
                "생활/건강"
        );
        System.out.println("스케줄러 실행");
        for (String category : categories) {
            List<RawNews> newsList = newsFetchService.fetch(category);   // category 키워드 기반 수집
            if (newsList.isEmpty()) continue;

            Map<String, List<RawNews>> grouped = llmService.groupByIssue(newsList);

            for (Map.Entry<String, List<RawNews>> entry : grouped.entrySet()) {
                String issueName = llmService.generateRepresentativeTitle(entry.getValue());
                saveIssueWithSentiment(issueName, category, entry.getValue());
            }
        }
    }


    // 상세 조회 기능 (피드백 반영: 실시간 뉴스 API 호출)
    public IssueDetailResponse getIssueDetail(Long id) {
        return issueRepository.findById(id)
                .map(issue -> {
                    List<IssueDetailResponse.NewsWithScore> newsList = new ArrayList<>();

                    try {
                        // ✅ 실시간 뉴스 수집 (이슈 이름으로 검색)
                        List<RawNews> fetched = newsFetchService.searchByQuery(issue.getIssueName());

                        // 각 뉴스에 대해 실시간 감정 분석 적용
                        newsList = fetched.stream()
                                .map(news -> {
                                    int score = llmService.analyzeSentimentScore(news.getTitle(), news.getDescription());
                                    return new IssueDetailResponse.NewsWithScore(
                                            news.getTitle(),
                                            news.getLink(),
                                            news.getDescription(),
                                            news.getPubDate(),
                                            score
                                    );
                                })
                                .collect(Collectors.toList());

                    } catch (Exception e) {
                        System.err.println("뉴스 API 호출 실패: " + e.getMessage());
                    }

                    return new IssueDetailResponse(
                            issue.getIssueName(),
                            issue.getCategory(),
                            newsList,
                            false // 북마크 여부는 아직 미구현
                    );
                })
                .orElse(null);
    }

    public Optional<Issue> findByName(String name) {
        return issueRepository.findByName(name);
    }

    public List<String> getIssueTitlesByCategory(String category) {
        List<Issue> issues = issueRepository.findByCategory(category, 100, 0); // 최대 100개 조회
        return issues.stream()
                .map(Issue::getIssueName)
                .collect(Collectors.toList());
    }

    public PagedResponse<IssuePreviewResponse> searchIssues(String keyword, String sort, int page, int size) {
        int offset =  (page - 1) * size;
        List<IssuePreviewResponse> issues = issueRepository.findByKeywordAndSort(keyword, sort, size, offset);
        System.out.println("searchIssues: " + issues);
        for (IssuePreviewResponse res : issues) {
            System.out.println("ID: " + res.getIssueId());
            System.out.println("Name: " + res.getIssueName());
            System.out.println("Category: " + res.getCategory());
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

