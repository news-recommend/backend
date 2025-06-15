package news_recommend.news.issue.dto;

import java.util.List;

public class IssuePreviewResponse {
    private Long issueId;
    private String issueName;
    private String category;
    private List<String> newsList;
    private boolean isBookmarked;

    public IssuePreviewResponse(Long issueId, String issueName, String category,
                                List<String> newsList, boolean isBookmarked) {
        this.issueId = issueId;
        this.issueName = issueName;
        this.category = category;
        this.newsList = newsList;
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

    public List<String> getNewsList() {
        return newsList;
    }

    public boolean isBookmarked() {
        return isBookmarked;
    }
}