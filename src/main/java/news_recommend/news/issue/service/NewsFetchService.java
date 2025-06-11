package news_recommend.news.issue.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import news_recommend.news.issue.dto.RawNews;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

@Service
public class NewsFetchService {

    @Value("${naver.client.id}")
    private String clientId;

    @Value("${naver.client.secret}")
    private String clientSecret;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 키워드 기반 뉴스 검색
     */
    public List<RawNews> fetch(String keyword) {
        System.out.println("\uD83D\uDFE2 fetch() 호출됨 - 키워드: " + keyword);

        String url = "https://openapi.naver.com/v1/search/news.json";

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("query", keyword)
                .queryParam("display", 5)
                .queryParam("sort", "date");

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Naver-Client-Id", clientId);
        headers.set("X-Naver-Client-Secret", clientSecret);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    builder.toUriString(), HttpMethod.GET, entity, String.class);

            List<RawNews> results = new ArrayList<>();
            JsonNode root = objectMapper.readTree(response.getBody());

            for (JsonNode item : root.path("items")) {
                RawNews news = new RawNews();
                news.setTitle(item.path("title").asText().replaceAll("<[^>]*>", ""));
                news.setLink(item.path("originallink").asText());
                news.setDescription(item.path("description").asText().replaceAll("<[^>]*>", ""));
                news.setPubDate(item.path("pubDate").asText());
                results.add(news);
            }

            return results;

        } catch (org.springframework.web.client.HttpClientErrorException.TooManyRequests e) {
            System.err.println("\u2757 네이버 뉴스 API 호출 속도 제한 초과 (429): " + e.getMessage());
            return Collections.emptyList();
        } catch (Exception e) {
            throw new RuntimeException("네이버 뉴스 API 응답 파싱 실패", e);
        }
    }
}
