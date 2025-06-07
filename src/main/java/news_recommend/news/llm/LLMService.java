package news_recommend.news.llm;

import news_recommend.news.issue.dto.RawNews;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class LLMService {

    @Value("${openai.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    // 기존 제목 기반 카테고리 분류
    public String classifyCategory(List<String> titles) {
        String prompt = "다음 뉴스 제목을 보고 15개 카테고리 중 하나로 분류해줘. 카테고리는 정치, 경제, 사회, 사건/사고, IT/과학, 자동차, 국제, 교육, 문화, 여행/레저, 연예, 환경, 부동산, 스포츠, 생활/건강이야.\n\n";
        for (String title : titles) {
            prompt += "- " + title + "\n";
        }
        prompt += "\n이 뉴스들의 공통 카테고리는? (정확한 하나만 반환해줘)";
        return callOpenAI(prompt);
    }

    // ✅ 뉴스 본문 기반 카테고리 분류 (query + 뉴스 리스트 사용)
    public String classifyCategory(String query, List<RawNews> newsList) {
        String prompt = "다음 뉴스들을 참고하여 '" + query + "' 이슈의 카테고리를 다음 중 하나로 분류해줘:\n" +
                "정치, 경제, 사회, 사건/사고, IT/과학, 자동차, 국제, 교육, 문화, 여행/레저, 연예, 환경, 부동산, 스포츠, 생활/건강\n\n" +
                newsList.stream()
                        .map(n -> "제목: " + n.getTitle() + "\n내용: " + n.getDescription())
                        .collect(Collectors.joining("\n\n")) +
                "\n\n위 뉴스들의 공통 카테고리는? 정확하게 하나만 답해줘.";

        return callOpenAI(prompt);
    }

    // ✅ 뉴스 1건에 대해 1~100 감정 점수 반환
    public int analyzeSentimentScore(String title, String description) {
        String prompt = "다음 뉴스의 제목과 내용을 바탕으로 감정을 분석해서 1~100 사이의 점수로 나타내줘.\n" +
                "1에 가까우면 부정, 50은 중립, 100에 가까우면 긍정이야.\n" +
                "숫자만 반환해줘.\n\n" +
                "제목: " + title + "\n내용: " + description;

        String result = callOpenAI(prompt);
        try {
            return Integer.parseInt(result.replaceAll("[^\\d]", ""));
        } catch (NumberFormatException e) {
            return 50; // 실패 시 중립 처리
        }
    }

    // GPT API 호출 공통 메서드
    private String callOpenAI(String prompt) {
        Map<String, Object> body = new HashMap<>();
        body.put("model", "gpt-3.5-turbo");
        body.put("messages", List.of(Map.of("role", "user", "content", prompt)));
        body.put("temperature", 0.2);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(API_URL, request, Map.class);
            Map<String, Object> choice = ((List<Map<String, Object>>) response.getBody().get("choices")).get(0);
            Map<String, Object> message = (Map<String, Object>) choice.get("message");
            return message.get("content").toString().trim();
        } catch (Exception e) {
            throw new RuntimeException("OpenAI 호출 중 오류 발생: " + e.getMessage());
        }
    }
}
