package news_recommend.news.issue.dto;

import java.util.List;

public class IssuePreviewResponse {
    private String issueName;
    private String category;
    private List<IssueDetailResponse.NewsWithScore> newsList;
    private boolean isBookmarked;

    public IssuePreviewResponse(String issueName, String category, List<IssueDetailResponse.NewsWithScore> newsList, boolean isBookmarked) {
        this.issueName = issueName;
        this.category = category;
        this.newsList = newsList;
        this.isBookmarked = isBookmarked;
    }

    public String getIssueName() {
        return issueName;
    }

    public String getCategory() {
        return category;
    }

    public List<IssueDetailResponse.NewsWithScore> getNewsList() {
        return newsList;
    }

    public boolean isBookmarked() {
        return isBookmarked;
    }
}
