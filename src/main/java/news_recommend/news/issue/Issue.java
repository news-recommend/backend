package news_recommend.news.issue;



public class Issue {
    private Long issueId;
    private String issueName;
    private String category;
    private String emotion;
    private String title;


    public Issue() {}

    public Issue(String issueName, String category, String emotion) {

        this.issueName = issueName;
        this.category = category;
        this.emotion = emotion;
    }
    public Issue(Long issueId, String issueName, String category, String emotion) {
        this.issueId = issueId;
        this.issueName = issueName;
        this.category = category;
        this.emotion = emotion;
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

    public String getEmotion() {
        return emotion;
    }

    public String getTitle() {
        return title;
    }

    public void setIssueId(Long issueId) {
        this.issueId = issueId;
    }

    public void setIssueName(String issueName) {
        this.issueName = issueName;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setEmotion(String emotion) {
        this.emotion = emotion;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
