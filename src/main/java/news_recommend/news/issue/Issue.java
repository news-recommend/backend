package news_recommend.news.issue;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "issue")
public class Issue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "issue_id")
    private Long issueId;

    @Column(name = "issue_name", nullable = false)
    private String issueName;

    @Column(nullable = false)
    private String category;

    @Column(columnDefinition = "TEXT")
    private String emotion;

    private String title;

    private String thumbnail;

    @Column(name = "news_list", columnDefinition = "TEXT")
    private String newsList;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public Issue() {}

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

    public String getThumbnail() {
        return thumbnail;
    }

    public String getNewsList() {
        return newsList;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
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

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void setNewsList(String newsList) {
        this.newsList = newsList;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
