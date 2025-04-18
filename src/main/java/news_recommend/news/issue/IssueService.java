package news_recommend.news.issue;

import news_recommend.news.issue.repository.IssueRepository;


import java.util.List;
import java.util.Optional;


public class IssueService {

    private final IssueRepository issueRepository;
    public IssueService(final IssueRepository issueRepository){
        this.issueRepository = issueRepository;
    }



    public List<Issue> findIssues() {
        return issueRepository.findAll();
    }

    public Issue save(Issue issue) {
        return issueRepository.save(issue);
    }

    public Optional<Issue> findById(Long id) {
        return issueRepository.findById(id);
    }

    public int update(Issue issue){ return issueRepository.update(issue); }

    public int delete(Long id) {
        return issueRepository.delete(id);
    }


}
