package news_recommend.news.issue.dto;

import java.util.List;

public class IssuePreviewResponse {
    private String issueName;
    private String category;
    private List<RawNews> newsList;

    public IssuePreviewResponse(String issueName, String category, List<RawNews> newsList) {
        this.issueName = issueName;
        this.category = category;
        this.newsList = newsList;
    }

    public String getIssueName() {
        return issueName;
    }

    public String getCategory() {
        return category;
    }


    public List<RawNews> getNewsList() {
        return newsList;
    }
}
