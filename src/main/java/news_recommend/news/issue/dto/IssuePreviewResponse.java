package news_recommend.news.issue.dto;

import java.util.List;

public class IssuePreviewResponse {
    private Long issueId;
    private String issueName;
    private String category;

    private List<Integer> sentimentTrend;
    private List<RawNews> newsList;
    private String thumbnail;
    private boolean isBookmarked;

    // ✅ 통합 생성자
    public IssuePreviewResponse(Long issueId, String issueName, String category,
                                 List<Integer> sentimentTrend, List<RawNews> newsList,
                                 String thumbnail, boolean isBookmarked) {
        this.issueId = issueId;
        this.issueName = issueName;
        this.category = category;
        this.sentimentTrend = sentimentTrend;
        this.newsList = newsList;
        this.thumbnail = thumbnail;
        this.isBookmarked = isBookmarked;
    }

    public Long getIssueId() {
        return issueId;
    }

    public String getIssueName() {
        return issueName;
    }

    public String getCategory() {
        return category;
    }


    public List<Integer> getSentimentTrend() {
        return sentimentTrend;
    }

    public List<RawNews> getNewsList() {
        return newsList;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public boolean isBookmarked() {
        return isBookmarked;
    }
}