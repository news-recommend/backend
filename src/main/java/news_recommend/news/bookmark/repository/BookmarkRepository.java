package news_recommend.news.bookmark.repository;

import news_recommend.news.bookmark.Bookmark;

import java.util.List;


// JWT

public interface BookmarkRepository {
    void save(Bookmark bookmark);
    List<Bookmark> findByMemberEmail(String email);
    void deleteByMemberEmailAndIssueId(String email, Long issueId);
}