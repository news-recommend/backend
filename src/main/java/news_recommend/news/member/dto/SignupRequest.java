package news_recommend.news.member.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class SignupRequest {
    private String email;
    private String password;
    private String passwordCheck;
    private String name;
    private List<String> preferredTags;
}