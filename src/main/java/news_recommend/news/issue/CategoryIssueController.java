package news_recommend.news.issue;

import lombok.RequiredArgsConstructor;
import news_recommend.news.issue.dto.IssueDetailResponse;
import news_recommend.news.issue.dto.RawNews;
import news_recommend.news.issue.dto.IssueDetailResponse.NewsWithScore;
import news_recommend.news.llm.LLMService;
import news_recommend.news.issue.service.NewsFetchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/issues")
@RequiredArgsConstructor
public class CategoryIssueController {

    private final NewsFetchService newsFetchService;
    private final LLMService llmService;

    // 회차별 키워드 변경을 위한 카테고리 키워드 맵
    private static final Map<String, List<String>> CATEGORY_KEYWORDS_MAP = Map.ofEntries(
            Map.entry("정치", Arrays.asList("정치", "국회", "대통령", "총리", "외교")),
            Map.entry("경제", Arrays.asList("경제", "주식", "환율", "물가", "무역")),
            Map.entry("사회", Arrays.asList("사회", "복지", "노동", "청년", "고용")),
            Map.entry("사건/사고", Arrays.asList("사건사고", "범죄", "교통사고", "화재", "실종")),
            Map.entry("IT/과학", Arrays.asList("AI", "인공지능", "로봇", "반도체", "스타트업")),
            Map.entry("자동차", Arrays.asList("자동차", "전기차", "자율주행", "현대차", "테슬라")),
            Map.entry("국제", Arrays.asList("국제", "미국", "중국", "일본", "전쟁")),
            Map.entry("교육", Arrays.asList("교육", "수능", "대학입시", "학교", "교사")),
            Map.entry("문화", Arrays.asList("문화", "예술", "전시회", "문학", "박물관")),
            Map.entry("여행/레저", Arrays.asList("여행", "관광지", "해외여행", "캠핑", "레저")),
            Map.entry("연예", Arrays.asList("연예인", "드라마", "아이돌", "예능", "가수")),
            Map.entry("환경", Arrays.asList("환경", "기후변화", "탄소중립", "미세먼지", "재활용")),
            Map.entry("부동산", Arrays.asList("부동산", "아파트", "전세", "청약", "재건축")),
            Map.entry("스포츠", Arrays.asList("스포츠", "축구", "야구", "올림픽", "선수")),
            Map.entry("생활/건강", Arrays.asList("생활", "건강", "운동", "건강식", "질병"))
    );

    @GetMapping("/category")
    public ResponseEntity<List<IssueDetailResponse>> getIssuesByCategoryRealtime(@RequestParam String name) {
        Map<String, List<NewsWithScore>> groupedNews = new LinkedHashMap<>();
        Set<String> seenTitles = new HashSet<>();

        int maxIssues = 5;
        int maxAttempts = 5;
        int attempt = 0;

        System.out.println("\uD83D\uDFE2 요청 시작 - 카테고리: " + name);

        List<String> keywordList = CATEGORY_KEYWORDS_MAP.getOrDefault(name, List.of(name));

        while (groupedNews.size() < maxIssues && attempt < maxAttempts) {
            String query = keywordList.get(attempt % keywordList.size());
            List<RawNews> fetched = newsFetchService.fetch(query);
            System.out.println("\uD83D\uDCE6 [" + attempt + "회차] 키워드: " + query + ", 뉴스 개수: " + fetched.size());

            for (RawNews news : fetched) {
                if (seenTitles.contains(news.getTitle())) continue;

                String fullText = news.getTitle() + " " + news.getDescription();
                String predictedCategory = llmService.classifyCategory(List.of(fullText));

                String normalizedPredicted = predictedCategory.replaceAll("\\s", "").toLowerCase();
                String normalizedTarget = name.replaceAll("\\s", "").toLowerCase();

                boolean isMatchStrict = normalizedPredicted.equals(normalizedTarget);
                boolean isMatchSoft = normalizedPredicted.contains(normalizedTarget) || normalizedTarget.contains(normalizedPredicted);

                if (attempt < 2 && !isMatchStrict) continue;
                if (attempt >= 2 && !isMatchSoft) continue;

                int score = llmService.analyzeSentimentScore(news.getTitle(), news.getDescription());

                NewsWithScore newsWithScore = new NewsWithScore(
                        news.getTitle(), news.getLink(), news.getDescription(), news.getPubDate(), score);

                String issueKey = news.getTitle();
                groupedNews.putIfAbsent(issueKey, new ArrayList<>());
                groupedNews.get(issueKey).add(newsWithScore);
                seenTitles.add(news.getTitle());

                if (groupedNews.size() >= maxIssues) break;
            }

            attempt++;
        }

        List<IssueDetailResponse> result = groupedNews.entrySet().stream()
                .filter(e -> e.getValue().size() >= 1)
                .map(e -> new IssueDetailResponse(e.getKey(), name, e.getValue(), false))
                .limit(maxIssues)
                .toList();

        return ResponseEntity.ok(result);
    }

    @GetMapping("/category/grouped")
    public ResponseEntity<List<IssueDetailResponse>> getGroupedIssuesByCategory(@RequestParam String name) {
        // 1. 키워드 기반 뉴스 수집
        List<String> keywordList = CATEGORY_KEYWORDS_MAP.getOrDefault(name, List.of(name));
        List<RawNews> allNews = new ArrayList<>();
        Set<String> seenTitles = new HashSet<>();

        for (String keyword : keywordList) {
            List<RawNews> fetched = newsFetchService.fetch(keyword);
            for (RawNews news : fetched) {
                if (seenTitles.add(news.getTitle())) {
                    allNews.add(news);
                }
            }
            if (allNews.size() >= 25) break; // 적당한 양만 수집
        }

        // 2. GPT로 이슈 그룹핑
        Map<String, List<RawNews>> grouped = llmService.groupByIssue(allNews);
        List<IssueDetailResponse> responseList = new ArrayList<>();

        // 3. 그룹별 대표 제목 생성 + 감정 점수 부여
        for (List<RawNews> group : grouped.values()) {
            if (group.isEmpty()) continue;

            String repTitle = llmService.generateRepresentativeTitle(group);

            List<NewsWithScore> newsWithScores = group.stream()
                    .map(n -> new NewsWithScore(
                            n.getTitle(),
                            n.getLink(),
                            n.getDescription(),
                            n.getPubDate(),
                            llmService.analyzeSentimentScore(n.getTitle(), n.getDescription())
                    )).toList();

            responseList.add(new IssueDetailResponse(repTitle, name, newsWithScores, false));
        }

        return ResponseEntity.ok(responseList);
    }

}