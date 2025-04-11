package news_recommend.news.member;

import java.math.BigDecimal;

public class Member {
    private Long userId;
    private String  name;
    private String email;
    private String interestCategory;


    public Member() {}

    public Member( String name, String email,String interestCategory) {

        this.email = email;
        this.name = name;
        this.interestCategory = interestCategory;
    }
    public Member(Long userId, String name, String email,String interestCategory) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.interestCategory = interestCategory;
    }

    public Long getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getInterestCategory() {
        return interestCategory;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setInterestCategory(String interestCategory) {
        this.interestCategory = interestCategory;
    }
}
