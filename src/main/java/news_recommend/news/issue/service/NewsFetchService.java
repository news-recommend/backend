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

    public List<RawNews> fetch(String keyword) {
        String url = "https://openapi.naver.com/v1/search/news.json";

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("query", keyword)
                .queryParam("display", 5)
                .queryParam("sort", "date");

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Naver-Client-Id", clientId);
        headers.set("X-Naver-Client-Secret", clientSecret);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                builder.toUriString(), HttpMethod.GET, entity, String.class);

        List<RawNews> results = new ArrayList<>();

        try {
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
        } catch (Exception e) {
            throw new RuntimeException("네이버 뉴스 API 응답 파싱 실패", e);
        }
    }
}
