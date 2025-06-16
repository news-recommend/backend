package news_recommend.news.issue.dto;

import java.util.List;

public class IssueDetailResponse {

    private String issueName;
    private String category;
    private List<NewsWithScore> newsList; // RawNews -> NewsWithScore
    private boolean isBookmarked;

    public IssueDetailResponse(String issueName, String category, List<NewsWithScore> newsList, boolean isBookmarked) {
        this.issueName = issueName;
        this.category = category;
        this.newsList = newsList;
        this.isBookmarked = isBookmarked;
    }

    public IssueDetailResponse(String string, String issueName, String category, List<NewsWithScore> enriched) {
    }

    // 내부 정적 클래스 정의
    public static class NewsWithScore {
        private String title;
        private String link;
        private String description;
        private String pubDate;
        private int sentimentScore;

        public NewsWithScore() {
        }

        public NewsWithScore(String title, String link, String description, String pubDate, int sentimentScore) {
            this.title = title;
            this.link = link;
            this.description = description;
            this.pubDate = pubDate;
            this.sentimentScore = sentimentScore;
        }

        public NewsWithScore(String title, String link, int sentimentScore) {
            this.title = title;
            this.link = link;
            this.sentimentScore = sentimentScore;
            this.description = null;
            this.pubDate = null;
        }



        // Getters
        public String getTitle() { return title; }
        public String getLink() { return link; }
        public String getDescription() { return description; }
        public String getPubDate() { return pubDate; }
        public int getSentimentScore() { return sentimentScore; }
    }

    // Getters
    public String getIssueName() { return issueName; }
    public String getCategory() { return category; }
    public List<NewsWithScore> getNewsList() { return newsList; }
    public boolean isBookmarked() { return isBookmarked; }
}
