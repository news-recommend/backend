package news_recommend.news.member;

import java.math.BigDecimal;

public class Member {
    private Long userId;
    private String  name;
    private String email;
    private String gender;
    private String interestCategory;

    public String getAgeGroup() {
        return ageGroup;
    }

    public void setAgeGroup(String ageGroup) {
        this.ageGroup = ageGroup;
    }

    private String ageGroup;
    private String password;


    public Member() {}

    // 회원가입용 생성자 (비밀번호 포함)
    public Member(String name, String email, String interestCategory, String agegroup, String gender, String password) {
        this.name = name;
        this.email = email;
        this.ageGroup = agegroup;
        this.gender = gender;
        this.interestCategory = interestCategory;
        this.password = password;
    }

    public Member( String name, String email,String interestCategory) {

        this.email = email;
        this.name = name;
        this.interestCategory = interestCategory;
    }
    public Member(Long userId, String name, String email,String interestCategory, String agegroup,String gender, String password) {
        System.out.println("agegroup: " + agegroup);
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.gender = gender;
        this.ageGroup = agegroup;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
