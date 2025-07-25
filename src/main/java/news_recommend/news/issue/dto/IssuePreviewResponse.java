package news_recommend.news.issue.dto;

import java.util.List;

public class IssuePreviewResponse {

    private Long issueId;
    private String issueName;
    private String category;
    private List<Integer> sentimentTrend;
    private String thumbnail;
    private boolean isBookmarked;
    private List<String> newsList;

    public IssuePreviewResponse(Long issueId, String issueName, String category,
                                List<Integer> sentimentTrend,
                                String thumbnail, boolean isBookmarked,
                                List<String> newsList) {
        this.issueId = issueId;
        this.issueName = issueName;
        this.category = category;
        this.sentimentTrend = sentimentTrend;
        this.thumbnail = thumbnail;
        this.isBookmarked = isBookmarked;
        this.newsList = newsList;
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

    public String getThumbnail() {
        return thumbnail;
    }

    public boolean isBookmarked() {
        return isBookmarked;
    }

    public List<String> getNewsList() {
        return newsList;
    }
}
