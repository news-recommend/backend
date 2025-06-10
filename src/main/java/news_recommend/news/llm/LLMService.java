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

    // 뉴스 제목 기반 카테고리 분류
    public String classifyCategory(List<String> titles) {
        String prompt = "다음 뉴스 제목을 보고 15개 카테고리 중 하나로 분류해줘. " +
                "카테고리는 정치, 경제, 사회, 사건/사고, IT/과학, 자동차, 국제, 교육, 문화, 여행/레저, 연예, 환경, 부동산, 스포츠, 생활/건강이야.\n\n";

        for (String title : titles) {
            prompt += "- " + title + "\n";
        }
        prompt += "\n이 뉴스들의 공통 카테고리는? (정확한 하나만 반환해줘)";

        return callOpenAI(prompt);
    }

    // 뉴스 본문 기반 카테고리 분류
    public String classifyCategory(String query, List<RawNews> newsList) {
        String prompt = "다음 뉴스들을 참고하여 '" + query + "' 이슈의 카테고리를 다음 중 하나로 분류해줘:\n" +
                "정치, 경제, 사회, 사건/사고, IT/과학, 자동차, 국제, 교육, 문화, 여행/레저, 연예, 환경, 부동산, 스포츠, 생활/건강\n\n" +
                newsList.stream()
                        .map(n -> "제목: " + n.getTitle() + "\n내용: " + n.getDescription())
                        .collect(Collectors.joining("\n\n")) +
                "\n\n위 뉴스들의 공통 카테고리는? 정확하게 하나만 답해줘.";

        return callOpenAI(prompt);
    }

    // 뉴스 감정 점수 분석 (1 ~ 100)
    public int analyzeSentimentScore(String title, String description) {
        String prompt = "다음 뉴스의 제목과 내용을 바탕으로 감정을 분석해서 1~100 사이의 점수로 나타내줘.\n" +
                "1에 가까우면 부정, 50은 중립, 100에 가까우면 긍정이야.\n숫자만 반환해줘.\n\n" +
                "제목: " + title + "\n내용: " + description;

        String result = callOpenAI(prompt).replaceAll("[^\\d]", "");
        try {
            return Integer.parseInt(result);
        } catch (NumberFormatException e) {
            System.err.println("⚠️ 감정 점수 파싱 실패: " + result);
            return 50; // 실패 시 중립 처리
        }
    }

    // GPT 호출 공통 메서드
    private String callOpenAI(String prompt) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("OpenAI API 키가 설정되지 않았습니다. 환경변수 또는 application.properties 확인 필요");
        }

        Map<String, Object> body = new HashMap<>();
        body.put("model", "gpt-3.5-turbo");
        body.put("messages", List.of(Map.of("role", "user", "content", prompt)));
        body.put("temperature", 0.2);
        body.put("max_tokens", 30); // 응답을 간결하게 제한

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(API_URL, request, Map.class);

            if (response.getBody() == null || !response.getBody().containsKey("choices")) {
                throw new RuntimeException("OpenAI 응답에 'choices'가 없습니다.");
            }

            List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
            if (choices == null || choices.isEmpty()) {
                throw new RuntimeException("OpenAI 응답에서 결과를 찾을 수 없습니다.");
            }

            Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
            return message.get("content").toString().trim();

        } catch (Exception e) {
            throw new RuntimeException("OpenAI 호출 중 오류 발생: " + e.getMessage());
        }
    }
}
