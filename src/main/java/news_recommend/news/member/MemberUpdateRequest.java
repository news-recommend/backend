package news_recommend.news.member;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class MemberUpdateRequest {
    private String name;
    private String email;
    private String newPassword;
    private List<String> preferredTags;
    private String ageGroup;
    private String gender;


}