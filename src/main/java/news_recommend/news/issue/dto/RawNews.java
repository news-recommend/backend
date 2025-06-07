package news_recommend.news.issue.dto;

public class RawNews {

    private String title;
    private String link;
    private String description;
    private String pubDate;

    // 감정 점수 추가 (1~100)
    private Integer sentimentScore;

    public RawNews() {
    }

    public RawNews(String title, String link, String description, String pubDate) {
        this.title = title;
        this.link = link;
        this.description = description;
        this.pubDate = pubDate;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getLink() { return link; }
    public void setLink(String link) { this.link = link; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getPubDate() { return pubDate; }
    public void setPubDate(String pubDate) { this.pubDate = pubDate; }

    public Integer getSentimentScore() { return sentimentScore; }
    public void setSentimentScore(Integer sentimentScore) { this.sentimentScore = sentimentScore; }
}
