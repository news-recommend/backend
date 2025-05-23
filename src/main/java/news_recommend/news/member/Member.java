package news_recommend.news.member;

import java.math.BigDecimal;

public class Member {
    private Long userId;
    private String  name;
    private String email;
    private String interestCategory;
    private String password;


    public Member() {}

    // 회원가입용 생성자 (비밀번호 포함)
    public Member(String name, String email, String interestCategory, String password) {
        this.name = name;
        this.email = email;
        this.interestCategory = interestCategory;
        this.password = password;
    }

    public Member( String name, String email,String interestCategory) {

        this.email = email;
        this.name = name;
        this.interestCategory = interestCategory;
    }
    public Member(Long userId, String name, String email,String interestCategory, String password) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.interestCategory = interestCategory;
        this.password = password;

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

    public String getPassword() { return password; }

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


    public void setPassword(String password) { this.password = password; }
}
